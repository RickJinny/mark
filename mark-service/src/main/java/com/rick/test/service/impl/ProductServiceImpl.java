package com.rick.test.service.impl;

import com.rick.service.ProductService;
import com.rick.test.dao.dao.ProductDao;
import com.rick.test.dao.model.Product;
import com.rick.request.AddProductRequest;
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

    @Override
    public void addProductV2(AddProductRequest addProductRequest) {
        ProductThread productThread01 = new ProductThread(addProductRequest);
        productThread01.start();
        ProductThread productThread02 = new ProductThread(addProductRequest);
        productThread02.start();
        ProductThread productThread03 = new ProductThread(addProductRequest);
        productThread03.start();
    }


    private static class ProductThread extends Thread {

        private AddProductRequest addProductRequest;

        @Autowired
        private ProductDao productDao;

        public ProductThread(AddProductRequest addProductRequest) {
            this.addProductRequest = addProductRequest;
        }

        @Override
        public void run() {
            Product product = new Product();
            product.setProductId(addProductRequest.getProductId());
            product.setProductName(addProductRequest.getProductName());
            product.setProductPrice(addProductRequest.getProductPrice());
            product.setUserId(addProductRequest.getUserId());
            product.setCreateTime(new Date());
            product.setUpdateTime(new Date());
            productDao.addProduct(product);
        }
    }
}
