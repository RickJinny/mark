package com.rickjinny.mark.controller.p15_serialization.t04_EnumUsedInApi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 4、枚举作为 api 接口参数或返回值的两个大坑。
 * 对于枚举，建议尽量在程序内部使用，而不是作为 api 接口的参数或返回值，原因是枚举涉及到序列化和反序列化时会有两个大坑。
 */
@RestController
@Slf4j
@RequestMapping(value = "/enumUsedInApi")
public class EnumUsedInAPIController {


}
