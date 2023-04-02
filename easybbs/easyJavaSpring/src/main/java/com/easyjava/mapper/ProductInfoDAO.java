package com.easyjava.mapper;

import com.easyjava.entity.po.ProductInfo;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductInfoDAO {
    int deleteByPrimaryKey(Integer id);

    int insert(ProductInfo record);

    int insertSelective(ProductInfo record);

    ProductInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(ProductInfo record);

    int updateByPrimaryKey(ProductInfo record);
}