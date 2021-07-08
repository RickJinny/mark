package com.rick.test.service.impl;

import com.rick.service.ProductService;
import com.rick.test.dao.dao.ProductDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductDao productDao;

    

}
