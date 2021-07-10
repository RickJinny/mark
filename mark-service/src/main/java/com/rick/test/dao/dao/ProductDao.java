package com.rick.test.dao.dao;

import com.rick.test.dao.mapper.ProductMapper;
import com.rick.test.dao.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProductDao {

    @Autowired
    private ProductMapper productMapper;

    public void addProduct(Product product) {
        productMapper.insertSelective(product);
    }

}
