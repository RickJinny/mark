package com.rickjinny.mark.controller.p15_serialization.t03_DeserializationConstructor;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping(value = "/deserializationConstructor")
public class DeserializationConstructorController {

    @Autowired
    private ObjectMapper objectMapper;

    @RequestMapping(value = "/wrong")
    public void wrong() throws JsonProcessingException {
        log.info("result : {}", objectMapper.readValue("{\"code\":1234}", APIResultWrong.class));
        log.info("result: {}", objectMapper.readValue("{\"code\":2000}", APIResultWrong.class));
    }

    @RequestMapping(value = "/right")
    public void right() throws JsonProcessingException {
        log.info("result : {}", objectMapper.readValue("{\"code\":1234}", APIResultRight.class));
        log.info("result :{}", objectMapper.readValue("{\"code\":2000}", APIResultRight.class));
    }
}
