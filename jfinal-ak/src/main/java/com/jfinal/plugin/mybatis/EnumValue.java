package com.jfinal.plugin.mybatis;

/**
 * 枚举值接口
 * @param <T>
 */
public interface EnumValue<T> {
    T getValue();

    String getCnName();
}
