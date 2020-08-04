package com.rickjinny.mark.controller.p29_dataandcode.t03_xss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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
}
