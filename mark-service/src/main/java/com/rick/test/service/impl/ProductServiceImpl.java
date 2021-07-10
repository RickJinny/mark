package com.rick.test.service.impl;

import com.rick.service.ProductService;
import com.rick.test.dao.dao.ProductDao;
import com.rick.test.dao.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    @Override
    public void addProduct() {
        Product product = new Product();
        ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
        long productId = threadLocalRandom.nextLong(1000000, 1000000000);
        product.setProductId(productId);
        product.setProductName(productId + "号产品");
        product.setProductPrice(BigDecimal.valueOf(threadLocalRandom.nextDouble(100.00, 10000.00)));
        product.setUserId(threadLocalRandom.nextInt(10000, 100000));
        product.setCreateTime(new Date());
        product.setUpdateTime(new Date());
        productDao.addProduct(product);
    }

}
