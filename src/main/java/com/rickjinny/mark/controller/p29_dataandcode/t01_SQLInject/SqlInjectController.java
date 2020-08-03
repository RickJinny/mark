package com.rickjinny.mark.controller.p29_dataandcode.t01_SQLInject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.method.HandlerMethod;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

@RequestMapping(value = "/sqlInject")
@RestController
@Slf4j
public class SqlInjectController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserDataMapper userDataMapper;

    /**
     * 实现一个 ExceptionHandler 来屏蔽异常，看看能否解决注入问题
     */
    @ExceptionHandler
    public void handle(HttpServletRequest request, HandlerMethod method, Exception e) {
        log.warn(String.format("访问 %s -> %s 出现异常!", request.getRequestURI(), method.toString(), e));
    }

    /**
     * 程序启动时，进行表结构和数据初始化
     */
    @PostConstruct
    public void init() {
        // 删除表
        jdbcTemplate.execute("drop table IF EXISTS  `userdata`;");
        // 创建表，不包含自增id、用户名、密码三列
        jdbcTemplate.execute("create TABLE `userdata` (\n" +
                "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                "  `name` varchar(255) NOT NULL,\n" +
                "  `password` varchar(255) NOT NULL,\n" +
                "  PRIMARY KEY (`id`)\n" +
                ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;");
        // 插入两条测试数据
        jdbcTemplate.execute("INSERT INTO `userdata` (name,password) VALUES ('test1','haha1'),('test2','haha2')");
    }

    /**
     * 用户模糊搜索接口
     */
    @RequestMapping(value = "/jdbcWrong")
    public void jdbcWrong(@RequestParam("name") String name) {
        // 采用拼接 SQL 的方式把姓名参数拼接到 LIKE 子句中
        log.info("{}", jdbcTemplate.queryForList("select id, name from userdata where name like '%' + name + '%'"));
    }

    /**
     * 正确的做法：在 SQL 中使用 "?" 作为参数占位符，然后提供参数值。
     */
    @PostMapping(value = "/jdbcRight")
    public void jdbcRight(@RequestParam("name") String name) {
        log.info("{}", jdbcTemplate.queryForList("select id, name from userdata where name like ?", "%" + name + "%"));
    }
}
