package com.rick.service;

import com.rick.vo.AddProductRequest;

public interface ProductService {

    void addProduct();

    void addProductV2(AddProductRequest addProductRequest);

}
