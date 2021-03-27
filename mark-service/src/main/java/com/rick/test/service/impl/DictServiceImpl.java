package com.rick.test.service.impl;

import com.rick.common.ServerResponse;
import com.rick.service.DictService;
import com.rick.vo.AddDictRequest;
import com.rick.vo.AddDictResponse;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl implements DictService {

    @Override
    public ServerResponse<AddDictResponse> addDict(AddDictRequest request) {



        return null;
    }
}
