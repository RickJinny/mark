package com.rickjinny.mark.controller.p15_serialization.t04_EnumUsedInApi;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

/**
 * 4、枚举作为 api 接口参数或返回值的两个大坑。
 * 对于枚举，建议尽量在程序内部使用，而不是作为 api 接口的参数或返回值，原因是枚举涉及到序列化和反序列化时会有两个大坑。
 */
@RestController
@Slf4j
@RequestMapping(value = "/enumUsedInApi")
public class EnumUsedInAPIController {

    @Autowired
    private RestTemplate restTemplate;

    @RequestMapping(value = "/getOrderStatusClient")
    public void getOrderStatusClient() {
        StatusEnumClient result = restTemplate.getForObject("http://localhost:8080/enumusedinapi/getOrderStatus",
                StatusEnumClient.class);
        log.info("result {}", result);
    }

    /**
     * 测试一段代码，使用 RestTemplate 来发去请求，让服务端返回客户端不存在的枚举
     */
    @RequestMapping(value = "/getOrderStatus")
    public StatusEnumServer getOrderStatus() {
        return StatusEnumServer.CANCELED;
    }

    @GetMapping(value = "/queryOrderByStatusListClient")
    public void queryOrderByStatusListClient() {
        ArrayList<StatusEnumClient> request = Lists.newArrayList(StatusEnumClient.CREATE, StatusEnumClient.PAID);
        HttpEntity<List<StatusEnumClient>> entity = new HttpEntity<>(request, new HttpHeaders());
        List<StatusEnumClient> response = restTemplate.exchange("http://localhost:45678/enumusedinapi/queryOrdersByStatusList",
                HttpMethod.POST, entity, new ParameterizedTypeReference<List<StatusEnumClient>>() {}).getBody();
        log.info("result: {}", response);
    }

    @RequestMapping(value = "/queryOrdersByStatus")
    public List<StatusEnumServer> queryOrdersByStatus(@RequestBody List<StatusEnumServer> enumServers) {
        enumServers.add(StatusEnumServer.CANCELED);
        return enumServers;
    }
}