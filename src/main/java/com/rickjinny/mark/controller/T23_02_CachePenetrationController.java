package com.rickjinny.mark.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 23、缓存设计：缓存穿透问题
 * 当在缓存中查找不存在的 key 时，会直接打到 DB 中;
 * 当大量请求查找不存在的 key 时, 都打到 DB 上, 会打死 DB
 */
@RestController
@RequestMapping("/cache03")
public class T23_02_CachePenetrationController {



}
