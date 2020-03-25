package com.stephen.dao;

import com.stephen.entity.ExcelInfo;
import com.stephen.mapper.ExcelInfoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class LogDao {
    @Autowired
    ExcelInfoMapper excelInfoMapper;

    public Long saveOneItem(ExcelInfo excelInfo) {
        return excelInfoMapper.insert(excelInfo);
    }
}
