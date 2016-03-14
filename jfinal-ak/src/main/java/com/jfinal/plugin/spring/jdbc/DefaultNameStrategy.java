package com.jfinal.plugin.spring.jdbc;

public class DefaultNameStrategy implements ColumnNameStrategy {
    @Override
    public String name(String column) {
        return column;
    }

    @Override
    public String column(String name) {
        return name;
    }
}
