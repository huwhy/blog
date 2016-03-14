package com.jfinal.plugin.spring;

import com.jfinal.plugin.IPlugin;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringPlugin implements IPlugin {

    private Class<?> configClass;

    public SpringPlugin(Class<?> configClass) {
        this.configClass = configClass;
    }

    @Override
    public boolean start() {
        SpringUtils.setContext(new AnnotationConfigApplicationContext(configClass));
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }
}
