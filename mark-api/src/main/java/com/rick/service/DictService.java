package com.rick.service;

import com.rick.common.ServerResponse;
import com.rick.vo.AddDictRequest;
import com.rick.vo.AddDictResponse;

public interface DictService {

    ServerResponse<AddDictResponse> addDict(AddDictRequest request);

}
