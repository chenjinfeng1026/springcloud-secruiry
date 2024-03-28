package com.example.product.controller;

import com.example.feign.pojo.UserInfo;
import com.example.product.base.ResultJson;
import com.example.product.pojo.Product;
import com.example.product.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("product")
public class ProductController {
    @Autowired
    public ProductService productService;

    /**
     * 新增产品
     * @param name
     * @param description
     * @param address
     * @param price
     * @return
     */
    @RequestMapping("add")
    @PreAuthorize("hasAnyAuthority('product_add')")//拥有权限的用户可以访问
    public ResultJson addProduct(@RequestParam(value = "name",defaultValue = "") String name,
                                  @RequestParam(value = "description",defaultValue = "") String description,
                                  @RequestParam(value = "address",defaultValue = "") String address,
                                  @RequestParam(value = "price",defaultValue = "0") Double price){

        Product product = new Product();
        product.setName(name);
        product.setDescription(description);
        product.setAddress(address);
        product.setPrice(price);
        productService.addProduct(product);

        return ResultJson.success("添加成功");
    }

    @RequestMapping("delete")
    @PreAuthorize("hasAnyAuthority('product_delete')")//拥有权限的用户可以访问
    public ResultJson deleteProduct(@RequestParam(value = "id",defaultValue = "0") Long id){
        if (id<=0){
            return ResultJson.error("请输入正确ID");
        }
        productService.deleteProduct(id);
        return ResultJson.success("删除成功");
    }

    @RequestMapping("update")
    @PreAuthorize("hasAnyAuthority('product_edit')")//拥有权限的用户可以访问
    public ResultJson updateProduct(@RequestParam(value = "id",defaultValue = "0") Long id,
                                     @RequestParam(value = "name",defaultValue = "") String name,
                                     @RequestParam(value = "description",defaultValue = "") String description,
                                     @RequestParam(value = "address",defaultValue = "") String address,
                                     @RequestParam(value = "price",defaultValue = "0") Double price
                                     ){
        if (id<=0){
            return ResultJson.error("请输入正确ID");
        }
        Product product = productService.queryById(id);
        if (product == null || product.getId() == 0){
            return ResultJson.error("请输入正确ID");
        }
        if (name!=null && name.length()>0) {
            product.setName(name);
        }
        if (description!=null && description.length()>0) {
            product.setDescription(description);
        }
        if (address!=null && address.length()>0) {
            product.setAddress(address);
        }
        if (price!=null) {
            product.setPrice(price);
        }
        productService.updateProduct(product);
        return ResultJson.success("修改成功");
    }

    @RequestMapping("id/{id}")
    @PreAuthorize("hasAnyAuthority('product_list')")//拥有权限的用户可以访问
    public ResultJson queryById(@PathVariable(value = "id") Long id){
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        System.out.println("访问了产品资源："+userInfo.toString());
        if (id<=0){
            return ResultJson.error("请输入正确ID");
        }
        System.out.println("id:"+id);
        Product product = productService.queryById(id);
        if (product == null || product.getId() == 0){
            return ResultJson.error("请输入正确ID");
        }
        return ResultJson.success(product);
    }

}
