package com.rick.test.controller;

import com.rick.common.ServerResponse;
import com.rick.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

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
            for (int i = 0; i < 100000; i++) {
                productService.addProduct();
            }
        } catch (Exception e) {
            log.error("addProduct error: ", e);
            return ServerResponse.createByError();
        }
        return ServerResponse.createBySuccess();
    }
}
