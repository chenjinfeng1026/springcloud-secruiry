package com.example.product.mapper;

import com.example.product.pojo.Product;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ProductMapper {

    @Select("select * from product where id=#{id}")
    Product queryById(Long id);

    @Delete("delete from product where id=#{id}")
    void delete(Long id);

    @Update("update product set name=#{name},description=#{description},address=#{address},price=#{price} where id=#{id}")
    void update(Product product);

    @Insert("insert into product(name,description,address,price) Values(#{name},#{description},#{address},#{price})")
    void insert(Product product);
}
