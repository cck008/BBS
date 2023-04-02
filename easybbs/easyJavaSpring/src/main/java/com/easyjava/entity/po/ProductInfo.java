package com.easyjava.entity.po;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @author 
 * 商品信息
 */
public class ProductInfo implements Serializable {
    /**
     * 自增ID
     */
    private Integer id;

    /**
     * 公司ID
     */
    private String companyId;

    /**
     * 商品编号
     */
    private String code;

    /**
     * 商品名称
     */
    private String productName;

    /**
     * 价格
     */
    private BigDecimal price;

    /**
     * sku类型
     */
    private Byte skuType;

    /**
     * 颜色类型
     */
    private Byte colorType;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 创建日期
     */
    private Date createDate;

    /**
     * 库存
     */
    private Long stock;

    /**
     * 状态
     */
    private Byte status;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Byte getSkuType() {
        return skuType;
    }

    public void setSkuType(Byte skuType) {
        this.skuType = skuType;
    }

    public Byte getColorType() {
        return colorType;
    }

    public void setColorType(Byte colorType) {
        this.colorType = colorType;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public Long getStock() {
        return stock;
    }

    public void setStock(Long stock) {
        this.stock = stock;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }
}