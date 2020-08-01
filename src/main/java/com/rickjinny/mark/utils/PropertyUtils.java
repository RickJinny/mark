package com.rickjinny.mark.utils;

import lombok.extern.slf4j.Slf4j;

import java.util.Properties;

@Slf4j
public class PropertyUtils {

    public static void loadPropertySource(Class clazz, String fileName) {
        try {
            Properties properties = new Properties();
            properties.load(clazz.getResourceAsStream(fileName));
            properties.forEach((k, v) -> {
                log.info("{} = {}", k, v);
                System.setProperty(k.toString(), v.toString());
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
