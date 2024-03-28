package com.example.product.service;

import com.example.product.mapper.ProductMapper;
import com.example.product.pojo.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    @Autowired
    private ProductMapper productMapper;


    public void addProduct(Product product)
    {
        productMapper.insert(product);
    }

    public void deleteProduct(Long id)
    {
        productMapper.delete(id);
    }

    public void updateProduct(Product product){
        productMapper.update(product);
    }

    public Product queryById(Long id){
        return productMapper.queryById(id);
    }

}
