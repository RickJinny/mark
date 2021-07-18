package com.rick.service;

import com.rick.request.AddProductRequest;

public interface ProductService {

    void addProduct();

    void addProductV2(AddProductRequest addProductRequest);

}
