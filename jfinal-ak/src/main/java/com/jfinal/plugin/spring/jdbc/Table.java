/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.plugin.spring.jdbc;

import com.jfinal.kit.StrKit;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.Map;

/**
 * Table save the table meta info like column name and column type.
 */
public class Table<T> {

    private String name;
    private String primaryKeyStr = null;
    private String[] primaryKey = null;
    private Map<String, Class<?>> columnTypeMap = new CaseInsensitiveMap(true);

    private String insertSql;

    private String updateSql;

    private Class<T> beanClass;

    private RowMapper<T> mapper;

    public Table(String name) {
        if (StrKit.isBlank(name))
            throw new IllegalArgumentException("Table name can not be blank.");
        this.name = name.trim();
    }

    public Table(String name, String primaryKey) {
        if (StrKit.isBlank(name))
            throw new IllegalArgumentException("Table name can not be blank.");
        if (StrKit.isBlank(primaryKey))
            throw new IllegalArgumentException("Primary key can not be blank.");

        this.name = name.trim();
        setPrimaryKey(primaryKey.trim());
    }

    void setPrimaryKey(String primaryKey) {
        this.primaryKeyStr = primaryKey;
        String[] arr = primaryKey.split(",");
        for (int i = 0; i < arr.length; i++)
            arr[i] = arr[i].trim();
        this.primaryKey = arr;
    }

    void setColumnTypeMap(Map<String, Class<?>> columnTypeMap) {
        if (columnTypeMap == null)
            throw new IllegalArgumentException("columnTypeMap can not be null");

        this.columnTypeMap = columnTypeMap;
    }

    public String getName() {
        return name;
    }

    void setColumnType(String columnLabel, Class<?> columnType) {
        columnTypeMap.put(columnLabel, columnType);
    }

    public String column(String name) {
        return ActiveRecordPlugin.nameStrategy.column(name);
    }

    public String name(String column) {
        return ActiveRecordPlugin.nameStrategy.name(column);
    }

    public Class<?> getColumnType(String columnLabel) {
        return columnTypeMap.get(columnLabel);
    }

    /**
     * model.save() need know what columns belongs to himself that he can saving to db.
     * Think about auto saving the related table's column in the future.
     */
    public boolean hasColumnLabel(String columnLabel) {
        return columnTypeMap.containsKey(columnLabel);
    }

    /**
     * update() and delete() need this method.
     */
    public String[] getPrimaryKey() {
        return primaryKey;
    }

    public String getPrimaryKeyStr() {
        return primaryKeyStr;
    }

    public Class<T> getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class<T> beanClass) {
        this.beanClass = beanClass;
    }

    public Map<String, Class<?>> getColumnTypeMap() {
        return Collections.unmodifiableMap(columnTypeMap);
    }

    public String getInsertSql() {
        return insertSql;
    }

    public void setInsertSql(String insertSql) {
        this.insertSql = insertSql;
    }

    public String getUpdateSql() {
        return updateSql;
    }

    public void setUpdateSql(String updateSql) {
        this.updateSql = updateSql;
    }

    public RowMapper<T> getMapper() {
        return mapper;
    }

    public void setMapper(RowMapper<T> mapper) {
        this.mapper = mapper;
    }
}






