package com.rick.service;

import com.rick.common.ServerResponse;
import com.rick.request.AddDictRequest;
import com.rick.response.AddDictResponse;

public interface DictService {

    ServerResponse<AddDictResponse> addDict(AddDictRequest request);

}
