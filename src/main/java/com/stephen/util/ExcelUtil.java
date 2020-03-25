package com.stephen.util;


import com.stephen.entity.User;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ExcelUtil {

    /**
     * 把数据写入到Excel文件
     *
     * @param fileName 自动生成的Excel文件的全路径文件名称
     * @param users    要写入到Excel文件中的数据
     */
    public static void writeExcel(String fileName, List<User> users) throws IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;
        Cell cell = null;

        //创建Excel文件
        File excelFile = new File(fileName.trim());

        //创建Excel工作薄
        if (excelFile.getName().endsWith("xlsx")) {
            workbook = new XSSFWorkbook();
        } else {
            workbook = new HSSFWorkbook();
        }

        //创建Excel表单
        sheet = workbook.createSheet();

        //设置列宽，宽度为256的整数倍
        sheet.setColumnWidth(1, 5120);
        sheet.setColumnWidth(2, 3840);
        sheet.setColumnWidth(3, 2560);
        sheet.setColumnWidth(4, 2560);
        sheet.setColumnWidth(5, 5120);

        //设置默认行高(默认为300)
        sheet.setDefaultRowHeight((short) 512);

        //设置合并单元格
        CellRangeAddress titleCellAddresses = new CellRangeAddress(1, 2, 1, 5);
        sheet.addMergedRegion(titleCellAddresses);

        //创建标题行
        row = sheet.createRow(1);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellStyle(getTitleCellStyle(workbook));
        cell.setCellValue("User信息表格");

        //设置合并单元格的边框，这个需要放在创建标题行之后
        setRegionBorderStyle(BorderStyle.THIN, titleCellAddresses, sheet);

        //创建表头行
        row = sheet.createRow(3);
        cell = row.createCell(1, CellType.STRING);
        cell.setCellStyle(getHeaderCellStyle(workbook));
        cell.setCellValue("USER_ID");
        cell = row.createCell(2, CellType.STRING);
        cell.setCellStyle(getHeaderCellStyle(workbook));
        cell.setCellValue("姓名");
        cell = row.createCell(3, CellType.STRING);
        cell.setCellStyle(getHeaderCellStyle(workbook));
        cell.setCellValue("性别");
        cell = row.createCell(4, CellType.STRING);
        cell.setCellStyle(getHeaderCellStyle(workbook));
        cell.setCellValue("年龄");
        cell = row.createCell(5, CellType.STRING);
        cell.setCellStyle(getHeaderCellStyle(workbook));
        cell.setCellValue("生日");

        //创建表体行
        for (int i = 0; i < users.size(); i++) {
            row = sheet.createRow(i + 4);
            cell = row.createCell(1, CellType.NUMERIC);
            cell.setCellStyle(getBodyCellStyle(workbook));
            cell.setCellValue(users.get(i).getUserid());
            cell = row.createCell(2, CellType.STRING);
            cell.setCellStyle(getBodyCellStyle(workbook));
            cell.setCellValue(users.get(i).getName());
            cell = row.createCell(3, CellType.BOOLEAN);
            cell.setCellStyle(getBodyCellStyle(workbook));
            cell.setCellValue(users.get(i).getSex());
            cell = row.createCell(4, CellType.NUMERIC);
            cell.setCellStyle(getBodyCellStyle(workbook));
            cell.setCellValue(users.get(i).getAge());
            cell = row.createCell(5, CellType.STRING);
            cell.setCellStyle(getBodyCellStyle(workbook));
            cell.setCellValue(users.get(i).getBirthday());
        }

        //把Excel工作薄写入到Excel文件
        FileOutputStream os = new FileOutputStream(excelFile);
        workbook.write(os);
        os.flush();
        os.close();
    }

    /**
     * 从Excel文件读取数据
     *
     * @param fileName 要读取的Excel文件的全路径文件名称
     * @return 从Excel文件中批量导入的用户数据
     */
    public static List<User> readExcel(String fileName) throws IOException {
        Workbook workbook = null;
        Sheet sheet = null;
        Row row = null;

        //读取Excel文件
        File excelFile = new File(fileName.trim());
        InputStream is = new FileInputStream(excelFile);

        //获取Excel工作薄
        if (excelFile.getName().endsWith("xlsx")) {
            workbook = new XSSFWorkbook(is);
        } else {
            workbook = new HSSFWorkbook(is);
        }
        if (workbook == null) {
            System.err.println("Excel文件有问题,请检查！");
            return null;
        }

        //获取Excel表单
        sheet = workbook.getSheetAt(0);

        List<User> users = new ArrayList<>();
        for (int rowNum = 4; rowNum <= sheet.getLastRowNum(); rowNum++) {
            //获取一行
            row = sheet.getRow(rowNum);
            User user = new User();
            user.setUserid(Long.valueOf(getCellValue(row.getCell(0))));
            user.setName(getCellValue(row.getCell(1)));
            user.setSex(getCellValue(row.getCell(2)));
            user.setAge(Integer.valueOf(getCellValue(row.getCell(3))));
            user.setBirthday(getCellValue(row.getCell(4)));
            users.add(user);
        }
        is.close();
        return users;
    }

    /**
     * 设置合并单元格的边框
     *
     * @param style         要设置的边框的样式
     * @param cellAddresses 要设置的合并的单元格
     * @param sheet         要设置的合并的单元格所在的表单
     */
    private static void setRegionBorderStyle(BorderStyle style, CellRangeAddress cellAddresses, Sheet sheet) {
        RegionUtil.setBorderTop(style, cellAddresses, sheet);
        RegionUtil.setBorderBottom(style, cellAddresses, sheet);
        RegionUtil.setBorderLeft(style, cellAddresses, sheet);
        RegionUtil.setBorderRight(style, cellAddresses, sheet);
    }

    /**
     * 设置普通单元格的边框
     *
     * @param style     要设置的边框的样式
     * @param cellStyle 单元格样式对象
     */
    private static void setCellBorderStyle(BorderStyle style, CellStyle cellStyle) {
        cellStyle.setBorderTop(style);
        cellStyle.setBorderBottom(style);
        cellStyle.setBorderLeft(style);
        cellStyle.setBorderRight(style);
    }

    /**
     * 设置标题单元格样式
     *
     * @param workbook 工作薄对象
     * @return 单元格样式对象
     */
    private static CellStyle getTitleCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        //设置字体
        Font font = workbook.createFont();
        font.setFontName("黑体");
        font.setFontHeightInPoints((short) 24);
        font.setColor((short) 10);
        cellStyle.setFont(font);

        //设置文字居中显示
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }


    /**
     * 设置表头单元格样式
     *
     * @param workbook 工作薄对象
     * @return 单元格样式对象
     */
    private static CellStyle getHeaderCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        //设置字体
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 20);
        font.setBold(true);
        cellStyle.setFont(font);

        //设置文字居中显示
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //设置单元格的边框
        setCellBorderStyle(BorderStyle.THIN, cellStyle);

        return cellStyle;
    }

    /**
     * 设置表体单元格样式
     *
     * @param workbook 工作薄对象
     * @return 单元格样式对象
     */
    private static CellStyle getBodyCellStyle(Workbook workbook) {
        CellStyle cellStyle = workbook.createCellStyle();

        //设置字体
        Font font = workbook.createFont();
        font.setFontName("宋体");
        font.setFontHeightInPoints((short) 16);
        cellStyle.setFont(font);

        //设置文字居中显示
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        //设置单元格的边框
        setCellBorderStyle(BorderStyle.THIN, cellStyle);

        return cellStyle;
    }

    private static String getCellValue(Cell cell) {
      if (cell == null) {
         return null;
      }
      
      String cellValue = "";
      DecimalFormat df = new DecimalFormat("#");
      switch (cell.getCellType()) {
      case HSSFCell.CELL_TYPE_STRING:
         cellValue = cell.getRichStringCellValue().getString().trim();
         break;
      case HSSFCell.CELL_TYPE_NUMERIC:
         // like12 add,20180622,支持日期格式
         if (HSSFDateUtil.isCellDateFormatted(cell)) {
            Date d = cell.getDateCellValue();
            DateFormat df2 = new SimpleDateFormat("yyyy-MM-dd");// HH:mm:ss
            cellValue = df2.format(d);
         }
         // 数字
         else{
            cellValue = df.format(cell.getNumericCellValue()).toString();
         }
         break;
      case HSSFCell.CELL_TYPE_BOOLEAN:
         cellValue = String.valueOf(cell.getBooleanCellValue()).trim();
         break;
      case HSSFCell.CELL_TYPE_FORMULA:
         cellValue = cell.getCellFormula();
         break;
      default:
         cellValue = "";
      }
      return cellValue;
   }


    /**
     * 获取单元格的值的字符串
     *
     * @param cell 单元格对象
     * @return cell单元格的值的字符串
     */
    private static String getStringValue(Cell cell) {
        if (cell == null) {
            return null;
        }
        CellType cellType = cell.getCellTypeEnum();
        switch (cellType) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                double value = cell.getNumericCellValue();
                return String.valueOf(Math.round(value));
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

}

