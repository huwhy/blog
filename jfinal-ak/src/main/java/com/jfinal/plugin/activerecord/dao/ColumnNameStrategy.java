package com.jfinal.plugin.activerecord.dao;

public interface ColumnNameStrategy {

    ColumnNameStrategy me = new NameStrategy();

    String name(String column);

    String column(String name);
}
