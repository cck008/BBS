package com.easyjava.mapper;

import org.springframework.stereotype.Repository;

@Repository
public interface ProductInfoDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfoTest record);

    int insertSelective(ProductInfoTest record);

    ProductInfoTest selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfoTest record);

    int updateByPrimaryKey(ProductInfoTest record);



}