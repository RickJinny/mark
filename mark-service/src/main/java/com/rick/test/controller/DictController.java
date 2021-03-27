package com.rick.test.controller;

import com.rick.common.ServerResponse;
import com.rick.service.DictService;
import com.rick.vo.AddDictRequest;
import com.rick.vo.AddDictResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping(value = "/dict")
public class DictController {

    @Autowired
    private DictService dictService;

    @RequestMapping(value = "/addDict")
    @ResponseBody
    public ServerResponse<AddDictResponse> addDict(@RequestBody AddDictRequest request) {
        return dictService.addDict(request);
    }
}
