package com.rickjinny.mark.controller.p27_clientdata.t02_TrustClientParameter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping(value = "/trustClientParameter")
@Slf4j
@RestController
public class TrustClientParameterController {

    private Map<Integer, Country> allCountries = new HashMap<>();

    public TrustClientParameterController() {
        allCountries.put(1, new Country(1, "China"));
        allCountries.put(2, new Country(2, "US"));
        allCountries.put(3, new Country(3, "UK"));
        allCountries.put(4, new Country(4, "Japan"));
    }

    @RequestMapping(value = "/index")
    public String index(ModelMap modelMap) {
        List<Country> countries = new ArrayList<>();
        // 从数据库中，查出 id < 4 的三个国家
        countries.addAll(allCountries.values().stream()
                .filter(country -> country.getId() < 4)
                .collect(Collectors.toList()));
        modelMap.addAttribute("countries", countries);
        return "index";
    }

    /**
     * 如果直接使用客户端传递过来的国家id，很可能用户注册成其他国家
     */
    @ResponseBody
    @RequestMapping(value = "wrong")
    public String wrong(@RequestParam("countryId") Integer countryId) {
        return allCountries.get(countryId).getName();
    }

    /**
     * 使用客户端传递过来的参数，对参数进行有效性校验
     */
    @ResponseBody
    @RequestMapping(value = "/right")
    public String right(@RequestParam("countryId") Integer countryId) {
        if (countryId < 1 || countryId > 3) {
            throw new RuntimeException("非法参数");
        }
        return allCountries.get(countryId).getName();
    }

    /**
     * 使用 Spring Validation 采用注解的方式进行参数校验。
     */
    @RequestMapping(value = "/better")
    @ResponseBody
    public String better(@RequestParam("countryId")
                         @Min(value = 1, message = "非法参数")
                         @Max(value = 3, message = "非法参数") Integer countryId) {
        return allCountries.get(countryId).getName();
    }
}
