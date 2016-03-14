package com.jfinal.plugin.spring.jdbc;

public interface ColumnNameStrategy {

    ColumnNameStrategy me = new NameStrategy();

    String name(String column);

    String column(String name);
}
