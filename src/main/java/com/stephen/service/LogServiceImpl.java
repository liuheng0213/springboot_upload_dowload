package com.stephen.service;

import com.stephen.dao.LogDao;
import com.stephen.entity.ExcelInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl implements ILogService{
    @Autowired
    LogDao logDao;

    @Override
    public Long saveOneItem(ExcelInfo excelInfo) {
        return logDao.saveOneItem(excelInfo);
    }
}
