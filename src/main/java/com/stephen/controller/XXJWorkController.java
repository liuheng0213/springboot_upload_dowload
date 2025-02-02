package com.stephen.controller;

import com.stephen.entity.ExcelInfo;
import com.stephen.entity.User;
import com.stephen.service.ILogService;
import com.stephen.util.DateUtil;
import com.stephen.util.ExcelUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;


import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Description
 * @Author sgl
 * @Date 2018-05-15 14:04
 */
@Controller
@RequestMapping("/xxj")
public class XXJWorkController {
    private static final Logger LOGGER = LoggerFactory.getLogger(XXJWorkController.class);
    public static final String UPLOAD = "upload";
    private static final String DOWNLOAD = "download";



    @Autowired
    ILogService logService;

    @Autowired
    private HttpServletRequest request; //自动注入request

    @RequestMapping(value = "/")
    public String index() {
        return "upload";
    }


    @GetMapping("/upload")
    public String upload() {
        return "upload";
    }

    /**
     * 很奇怪 同时保存必须两个线程
     *
     * @param file
     * @return
     * @throws IOException
     */
    @PostMapping("/upload")
    @ResponseBody
    public String upload(@RequestParam("file") MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            return "上传失败，请选择文件";
        }
        try {
            String rootPath = this.getClass().getResource("/").getPath();
            String fileName = file.getOriginalFilename();
            String upPath = rootPath + "//upload//";
            String doPath = rootPath + "//download//";
            String newFileName = "xxj_" + DateUtil.formatForPah(new Date()) + "_" + fileName;
            String absoluteUploadPathName = upPath + newFileName;
            String absoluteDownloadPathName = doPath + newFileName;

            ExcelInfo excelInfoUp = new ExcelInfo();
            excelInfoUp.setFilename(fileName);
            excelInfoUp.setType(UPLOAD);
            excelInfoUp.setCreateDate(new Date());
            excelInfoUp.setPath(upPath);

            ExcelInfo excelInfoDo = new ExcelInfo();
            excelInfoDo.setFilename(newFileName);
            excelInfoDo.setType(DOWNLOAD);
            excelInfoDo.setCreateDate(new Date());
            excelInfoDo.setPath(doPath);


            ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
            CountDownLatch countDownLatch = new CountDownLatch(2);
            fixedThreadPool.execute(new MyThread(file, absoluteUploadPathName, countDownLatch,excelInfoUp));
            fixedThreadPool.execute(new MyThread(file, absoluteDownloadPathName, countDownLatch,excelInfoDo));
            countDownLatch.await();

            List<User> users = ExcelUtil.readExcel(absoluteUploadPathName);
            ExcelUtil.writeExcel(absoluteDownloadPathName, users);
            LOGGER.info("上传成功");
            return "上传成功";
        } catch (IOException e) {
            LOGGER.error(e.toString(), e);
        } catch (InterruptedException e) {
            e.printStackTrace();
            LOGGER.error(e.toString(), e);
        }
        return "上传失败！";

    }

    @GetMapping("/multiUpload")
    public String multiUpload() {
        return "multiUpload";
    }

    @PostMapping("/multiUpload")
    @ResponseBody
    public String multiUpload(HttpServletRequest request) {
        List<MultipartFile> files = ((MultipartHttpServletRequest) request).getFiles("file");
        String filePath = "/users/itinypocket/workspace/temp/";
        for (int i = 0; i < files.size(); i++) {
            MultipartFile file = files.get(i);
            if (file.isEmpty()) {
                return "上传第" + (i++) + "个文件失败";
            }
            String fileName = file.getOriginalFilename();

            File dest = new File(filePath + fileName);
            try {
                file.transferTo(dest);
                LOGGER.info("第" + (i + 1) + "个文件上传成功");
            } catch (IOException e) {
                LOGGER.error(e.toString(), e);
                return "上传第" + (i++) + "个文件失败";
            }
        }

        return "上传成功";

    }


    private class MyThread implements Runnable {
        private MultipartFile file;
        private String absolutePathNameName;
        private CountDownLatch countDownLatch;
        private ExcelInfo excelInfo;

        public MyThread(MultipartFile file, String absolutePathNameName, CountDownLatch countDownLatch,ExcelInfo excelInfo) {
            this.file = file;
            this.absolutePathNameName = absolutePathNameName;
            this.countDownLatch = countDownLatch;
            this.excelInfo = excelInfo;
        }

        @Override
        public void run() {
            try {
                //todo
                Thread.currentThread().setUncaughtExceptionHandler(new ExcedptionHandler());
                File dest = new File(absolutePathNameName);
                logService.saveOneItem(excelInfo);
                file.transferTo(dest);
            } catch (IOException e) {
                e.printStackTrace();
                LOGGER.error(e.toString(), e);
            } finally {
                countDownLatch.countDown();
            }
        }
    }

    private class ExcedptionHandler implements Thread.UncaughtExceptionHandler {
        @Override
        public void uncaughtException(Thread t, Throwable e) {
            LOGGER.error(t.getName() + "---" +e.toString(), e);
        }
    }

}
