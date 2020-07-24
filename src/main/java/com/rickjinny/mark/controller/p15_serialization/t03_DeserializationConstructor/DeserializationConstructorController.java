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

    /**
     * 得到的结果都是 false。
     * 出现这个问题的原因：默认情况下，在反序列化的时候，Jackson框架只会调用无参构造方法创建对象。
     * 如果走自定义的构造方法创建对象，需要通过 @JsonCreator 来指定构造方法，并通过 @JsonProperty 设置构造方法中参数对应的 JSON 属性名。
     */
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
