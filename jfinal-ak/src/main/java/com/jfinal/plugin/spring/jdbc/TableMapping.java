package com.jfinal.plugin.spring.jdbc;

import com.jfinal.plugin.spring.jdbc.dialect.Dialect;
import com.jfinal.plugin.spring.jdbc.dialect.MysqlDialect;

import java.util.HashMap;
import java.util.Map;

public class TableMapping {

    private final Map<String, Table> tableMapping = new HashMap<>();

    private static TableMapping me = new TableMapping();

    private Dialect dialect = new MysqlDialect();

    private TableMapping() {
    }

    public static TableMapping me() {
        return me;
    }

    public void putTable(Table table) {
        tableMapping.put(table.getName(), table);
    }

    public Table getTable(String name) {
        return tableMapping.get(name);
    }

    public Dialect getDialect() {
        return dialect;
    }

    public void setDialect(Dialect dialect) {
        this.dialect = dialect;
    }
}


