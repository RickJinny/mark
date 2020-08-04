package com.rickjinny.mark.controller.p29_dataandcode.t02_codeinject;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.SimpleBindings;
import java.util.HashMap;
import java.util.Map;

/**
 * 2、小心动态执行代码时，代码注入漏洞
 */
@Slf4j
@RequestMapping(value = "/codeInject")
@RestController
public class CodeInjectController {

    private ScriptEngineManager scriptEngineManager = new ScriptEngineManager();

    // 获取 JavaScript 脚本引擎
    private ScriptEngine scriptEngine = scriptEngineManager.getEngineByName("js");

    /**
     * 通过 ScriptEngineManager 获得一个 JavaScript 脚本引擎，使用 Java 代码来动态执行 JavaScript 代码，实现当外部传入的
     * 用户名为 admin 的时候，返回1，否则返回0。
     * 这样本身没有什么问题，但是把传入的用户名修改为：hello',java.lang.System.exit(0)' 就可以达到关闭整个程序的目的。
     */
    @RequestMapping(value = "/wrong")
    public Object wrong(@RequestParam("name") String name) {
        try {
            // 通过 eval 动态执行 JavaScript 脚本，这里 name 参数通过字符串拼接的方式混入 JavaScript 代码
            return scriptEngine.eval(String.format("var name = '%s'; name == 'admin' ? 1 : 0", name));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/right")
    public Object right(@RequestParam("name") String name) {
        try {
            // 外部传入的参数
            Map<String, Object> param = new HashMap<>();
            param.put("name", name);
            // name 参数作为绑定传给 eval 方法，而不是拼接 JavaScript 代码
            return scriptEngine.eval("name == 'admin' ? 1 : 0", new SimpleBindings(param));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @RequestMapping(value = "/right2")
    public Object right2(@RequestParam("name") String name) {
        // 使用沙箱执行脚本
        ScriptingSandbox scriptingSandbox = new ScriptingSandbox(scriptEngine);
        return scriptingSandbox.eval(String.format("var name = '%s'; name == 'admin' ? 1 : 0", name));
    }
}
