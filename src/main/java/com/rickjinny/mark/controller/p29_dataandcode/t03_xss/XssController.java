package com.rickjinny.mark.controller.p29_dataandcode.t03_xss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@RestController
@RequestMapping(value = "/xss")
public class XssController {

    @Autowired
    private UserRepository userRepository;

    /**
     * 显示 xss 页面
     */
    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap) {
        // 查数据库
        User user = userRepository.findById(1L).orElse(new User());
        // 给 View 提供 Model
        modelMap.addAttribute("username", user.getName());
        return "xss";
    }

    /**
     * 保存用户信息
     */
    @PostMapping(value = "/save")
    public String save(@RequestParam("username") String username, HttpServletRequest request) {
        User user = new User();
        user.setId(1L);
        user.setName(request.getParameter("username"));
        userRepository.save(user);
        // 保存完成后，重定向到首页
        return "redirect:/xss";
    }

    @PutMapping
    public void put(@RequestBody User user) {
        userRepository.save(user);
    }

    @ResponseBody
    @RequestMapping("/query")
    public User query() {
        return userRepository.findById(1L).orElse(new User());
    }

    /**
     * 服务端读取 Cookie
     */
    @RequestMapping(value = "/readCookie")
    @ResponseBody
    public String readCookie(@CookieValue("test") String cookieValue) {
        return cookieValue;
    }

    /**
     * 服务端写入 Cookie
     */
    @RequestMapping(value = "/writeCookie")
    @ResponseBody
    public void writeCookie(@RequestParam("httpOnly") boolean httpOnly, HttpServletResponse response) {
        Cookie cookie = new Cookie("test", "haha");
        // 根据 httpOnly 入参决定是否开启 httpOnly 属性
        cookie.setHttpOnly(httpOnly);
        response.addCookie(cookie);
    }
}
