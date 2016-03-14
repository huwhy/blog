package com.jfinal.plugin.spring;

import org.springframework.context.ApplicationContext;

public class SpringUtils {
    private static ApplicationContext context;

    public static void setContext(ApplicationContext context) {
        SpringUtils.context = context;
    }

    public static boolean isSupport() {
        return context != null;
    }

    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }

    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }

}
