package com.rick.test.service.impl;

import com.rick.common.ServerResponse;
import com.rick.service.DictService;
import com.rick.test.dao.dao.DictDao;
import com.rick.test.dao.model.Dict;
import com.rick.vo.AddDictRequest;
import com.rick.vo.AddDictResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictDao dictDao;

    @Override
    public ServerResponse<AddDictResponse> addDict(AddDictRequest request) {
        Dict dict = dictDao.addDict(request);
        AddDictResponse addDictResponse = new AddDictResponse();
        addDictResponse.setDictId(dict.getDictId());
        return ServerResponse.createBySuccess(addDictResponse);
    }
}
