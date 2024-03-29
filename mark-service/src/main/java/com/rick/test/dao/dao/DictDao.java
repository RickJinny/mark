package com.rick.test.dao.dao;

import com.rick.test.dao.mapper.DictMapper;
import com.rick.test.dao.model.Dict;
import com.rick.request.AddDictRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Component
public class DictDao {

    @Autowired
    private DictMapper dictMapper;

    @Transactional(rollbackFor = Exception.class)
    public Dict addDict(AddDictRequest addDictRequest) {
        Dict dict = new Dict();
        dict.setDictId(addDictRequest.getDictId());
        dict.setDictType(addDictRequest.getDictType());
        dict.setDictCode(addDictRequest.getDictCode());
        dict.setDictValue(addDictRequest.getDictValue());
        dict.setCreateTime(new Date());
        dict.setUpdateTime(new Date());
        int rowCount = dictMapper.insertSelective(dict);
        if (rowCount > 0) {
            return dict;
        }
        return null;
    }

}
