package com.jfinal.plugin.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.jfinal.plugin.IPlugin;

public class SpringPlugin implements IPlugin {

    private String[] configLocations;

    public SpringPlugin(String... configLocations) {
        this.configLocations = configLocations;
    }

    @Override
    public boolean start() {
        SpringUtils.setContext(new ClassPathXmlApplicationContext(configLocations));
        return true;
    }

    @Override
    public boolean stop() {
        return true;
    }
}
