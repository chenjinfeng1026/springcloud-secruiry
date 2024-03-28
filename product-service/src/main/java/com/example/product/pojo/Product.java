package com.example.product.pojo;

import lombok.Data;

@Data
public class Product {
    /**
     * 编号
     */
    private int id;

    /**
     * 产品名称
     */
    private String name;

    /**
     * 产品地址
     */
    private String address;

    /**
     * 产品价格
     */
    private double price;

    /**
     * 产品描述
     */
    private String description;

}
