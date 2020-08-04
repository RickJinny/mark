package com.rickjinny.mark.controller.p29_dataandcode.t03_xss;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Arrays;

public class XssRequestWrapper extends HttpServletRequestWrapper {

    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String[] getParameterValues(String parameter) {
        // 获取多个参数值的时候，所有参数值应用 clean 方法逐一清洁
        return Arrays.stream(super.getParameterValues(parameter)).map(this::clean).toArray(String[]::new);
    }

    @Override
    public String getHeader(String name) {
        // 同样清洁请求头
        return clean(super.getHeader(name));
    }

    @Override
    public String getParameter(String name) {
        // 获取参数单一值，也要处理
        return clean(super.getParameter(name));
    }

    /**
     * clean 方法就是对值进行 html 转义
     */
    private String clean(String value) {
        return StringUtils.isEmpty(value) ? "" : HtmlUtils.htmlEscape(value);
    }
}
