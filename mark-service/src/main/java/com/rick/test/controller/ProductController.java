package com.rick.test.controller;

import com.rick.common.ServerResponse;
import com.rick.service.ProductService;
import com.rick.request.AddProductRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RestController
@RequestMapping(value = "/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    @ResponseBody
    @PostMapping(value = "/add")
    public ServerResponse<Void> addProduct() {
        try {
            for (int i = 0; i < 200000; i++) {
                productService.addProduct();
            }
        } catch (Exception e) {
            log.error("addProduct error: ", e);
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess();
    }

    @ResponseBody
    @PostMapping(value = "/addV2")
    public ServerResponse<Void> addProductV2() {
        long startTime = System.currentTimeMillis();
        try {
            for (int i = 0; i < 2000000; i++) {
                AddProductRequest addProductRequest = new AddProductRequest();
                ThreadLocalRandom threadLocalRandom = ThreadLocalRandom.current();
                long productId = threadLocalRandom.nextLong(1000000, 1000000000);
                addProductRequest.setProductId(productId);
                addProductRequest.setProductName(productId + "号产品");
                addProductRequest.setProductPrice(BigDecimal.valueOf(threadLocalRandom.nextDouble(100.00, 10000.00)));
                addProductRequest.setUserId(threadLocalRandom.nextInt(10000, 100000));
                productService.addProductV2(addProductRequest);
            }
        } catch (Exception e) {
            log.error("addProductV2 error: ", e);
            return ServerResponse.createByError();
        }
        log.info("addProductV2 时长 : {} ", System.currentTimeMillis() - startTime);
        return ServerResponse.createBySuccess();
    }
}
