package com.stephen.mapper;

import com.stephen.entity.ExcelInfo;


public interface ExcelInfoMapper {
    int deleteByPrimaryKey(Integer id);

    Long insert(ExcelInfo record);

    int insertSelective(ExcelInfo record);

    ExcelInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ExcelInfo record);

    int updateByPrimaryKey(ExcelInfo record);
}