package com.rickjinny.mark.controller.p29_dataandcode.t03_xss;


import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public class XssFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new XssRequestWrapper((HttpServletRequest) request), response);
    }
}
