package com.rickjinny.mark.controller.p15_serialization.t02_JsonIgnoreProperties;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 2、注意 Jackson JSON 反序列化对额外字段的处理。
 *
 * 通过设置 JSON 序列化工具 Jackson 的 activeDefaultTyping 方法，可以在序列化数据时写入对象类型。
 * 其实 Jackson 还有很多参数可以控制序列化和反序列化，是一个功能强大而完善的序列化工具。
 * 因此，很多框架都将 Jackson 作为 JDK 序列化工具，比如 Spring Web。但也正是这个原因，我们使用时要小心各个参数的配置。
 *
 * 比如：在开发 Spring Web 应用程序时，如果自定义了 ObjectMapper，并把它注册成了 Bean，那很可能会导致 Spring Web 使用
 * 的 ObjectMapper 也被替换，导致 Bug。
 *
 */
@RestController
@Slf4j
@RequestMapping(value = "/jsonIgnoreProperties")
public class JsonIgnorePropertiesController {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 我们来看一个案例：程序一开始是正常的，当修改一下 ObjectMapper 的行为，让枚举序列化为索引值而不是字符串，
     * 比如默认情况下序列化一个 Color 枚举中的 Color.BLUE 会得到字符串 BLUE。
     */
    public void test() throws JsonProcessingException {
        log.info("color:{}", objectMapper.writeValueAsString(Color.BLUE));
    }

    /**
     * 重新定义一个 ObjectMapper Bean，开启了 write_enums_using_index 功能特性。
     */
    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(SerializationFeature.WRITE_ENUMS_USING_INDEX, true);
        return objectMapper;
    }

    enum Color {
        RED, BLUE
    }

    /**
     * 使用 UserWrong 类型作为入参传入，其中只有一个 name 属性。
     * 从异常信息中可以看到，反序列化的时候，原始数据多了一个 version 属性。
     * 原因：自定义 ObjectMapper 启用 WRITE_ENUMS_USING_INDEX 序列化功能特性时，覆盖了 SpringBoot 自动创建的 ObjectMapper;
     * 而这个自动创建的 ObjectMapper 设置过 FAIL_ON_UNKNOWN_PROPERTIES 反序列化特性为 false ，以确保出现未知字段时不要抛出异常。
     */
    @PostMapping(value = "/wrong")
    public UserWrong wrong(@RequestBody UserWrong user) {
        return user;
    }

    @PostMapping(value = "/right")
    public Object right(@RequestBody UserRight user) {
        return user;
    }

}
