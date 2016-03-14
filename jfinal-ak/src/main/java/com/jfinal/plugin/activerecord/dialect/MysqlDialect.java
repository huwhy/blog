/**
 * Copyright (c) 2011-2015, James Zhan 詹波 (jfinal@126.com).
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.jfinal.plugin.activerecord.dialect;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.Table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * MysqlDialect.
 */
public class MysqlDialect extends Dialect {

    private static final Pattern SPLIT_PATTERN = Pattern.compile(",");

    public String forTableInfoBuilderDoBuildTableInfo(String tableName) {
        return "select * from " + tableName + " where 1 = 2";
    }

    @Override
    public void forModelSaves(String insertSql, List<Map<String, Object>> records) {

    }


    public String forTableBuilderDoBuild(String tableName) {
        return "select * from `" + tableName + "` where 1 = 2";
    }

    public void forModelSave(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras) {
        if (StrKit.notBlank(table.getInsertSql())) {
            forModelSave(table.getInsertSql(), attrs, paras);
            sql.append(table.getInsertSql());
        } else {
            sql.append("insert into `").append(table.getName()).append("`(");
            StringBuilder temp = new StringBuilder(") values(");
            for (Entry<String, Object> e : attrs.entrySet()) {
                String colName = e.getKey();
                if (table.hasColumnLabel(colName)) {
                    if (paras.size() > 0) {
                        sql.append(", ");
                        temp.append(", ");
                    }
                    sql.append("`").append(colName).append("`");
                    temp.append("?");
                    paras.add(e.getValue());
                }
            }
            sql.append(temp.toString()).append(")");
        }
    }

    @Override
    public void forModelSave(String insertSql, Map<String, Object> attrs, List<Object> paras) {
        forModelSave(getColumns(insertSql), attrs, paras);
    }

    @Override
    public void forDbSaves(StringBuilder sql, List<List<Object>> paras, Table table,
            List<Map<String, Object>> records) {
        if (StrKit.notBlank(table.getInsertSql())) {
            for (Map<String, Object> attrs : records) {
                List<Object> para = new ArrayList<>();
                forModelSave(getColumns(table.getInsertSql()), attrs, para);
                paras.add(para);
            }
            sql.append(table.getInsertSql());
        } else {
            String tableName = table.getName();
            sql.append("insert into `");
            sql.append(tableName).append("`(");
            StringBuilder temp = new StringBuilder();
            temp.append(") values(");
            Map<String, Object> record = records.get(0);
            List<String> paraKeys = new ArrayList<>();
            for (Entry<String, Object> e : record.entrySet()) {
                if (paraKeys.size() > 0) {
                    sql.append(", ");
                    temp.append(", ");
                }
                sql.append("`").append(e.getKey()).append("`");
                temp.append("?");
                paraKeys.add(e.getKey());
            }
            sql.append(temp.toString()).append(")");
            for (Map<String, Object> r : records) {
                List<Object> para = new ArrayList<>();
                for (String key : paraKeys) {
                    para.add(r.get(key));
                }
                paras.add(para);
            }
        }
    }

    public void forModelSaves(String insertSql, List<Map<String, Object>> records, List<List<Object>> paras) {
        if (paras == null) {
            throw new NullPointerException("paras is null");
        }
        String[] columns = getColumns(insertSql);
        for (Map<String, Object> record : records) {
            List<Object> para = new ArrayList<>();
            forModelSave(columns, record, para);
            paras.add(para);
        }
    }

    public void forModelSave(String[] columns, Map<String, Object> attrs, List<Object> paras) {
        for (String column : columns) {
            paras.add(attrs.get(column));
        }
    }

    private String[] getColumns(String insertSql) {
        String columnSql = insertSql.replaceAll("`| ", "");
        int fromIndex = columnSql.indexOf("(") + 1;
        int endIndex = columnSql.indexOf(")");
        return SPLIT_PATTERN.split(columnSql.substring(fromIndex, endIndex));
    }

    @Override
    public String forModelDeleteById(Table table) {
        String[] pKeys = table.getPrimaryKey();
        StringBuilder sql = new StringBuilder(45);
        sql.append("delete from `");
        sql.append(table.getName());
        sql.append("` where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0)
                sql.append(" and ");
            sql.append("`").append(pKeys[i]).append("` = ?");
        }
        return sql.toString();
    }

    public void forModelUpdate(Table table, Map<String, Object> attrs, Set<String> modifyFlag, StringBuilder sql,
            List<Object> paras) {
        sql.append("update `").append(table.getName()).append("` set ");
        String[] pKeys = table.getPrimaryKey();
        for (Entry<String, Object> e : attrs.entrySet()) {
            String colName = e.getKey();
            if (modifyFlag.contains(colName) && table.hasColumnLabel(colName)) {
                boolean isKey = false;
                for (String pKey : pKeys)    // skip primaryKeys
                    if (pKey.equalsIgnoreCase(colName)) {
                        isKey = true;
                        break;
                    }

                if (isKey)
                    continue;

                if (paras.size() > 0)
                    sql.append(", ");
                sql.append("`").append(colName).append("` = ? ");
                paras.add(e.getValue());
            }
        }
        sql.append(" where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0)
                sql.append(" and ");
            sql.append("`").append(pKeys[i]).append("` = ?");
            paras.add(attrs.get(pKeys[i]));
        }
    }

    public String forModelFindById(Table table, String columns) {
        StringBuilder sql = new StringBuilder("select ");
        columns = columns.trim();
        if ("*".equals(columns)) {
            sql.append("*");
        } else {
            String[] arr = columns.split(",");
            for (int i = 0; i < arr.length; i++) {
                if (i > 0)
                    sql.append(",");
                sql.append("`").append(arr[i].trim()).append("`");
            }
        }

        sql.append(" from `");
        sql.append(table.getName());
        sql.append("` where ");
        String[] pKeys = table.getPrimaryKey();
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0)
                sql.append(" and ");
            sql.append("`").append(pKeys[i]).append("` = ?");
        }
        return sql.toString();
    }

    public String forDbFindById(String tableName, String[] pKeys) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        StringBuilder sql = new StringBuilder("select * from `").append(tableName).append("` where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0)
                sql.append(" and ");
            sql.append("`").append(pKeys[i]).append("` = ?");
        }
        return sql.toString();
    }

    public String forDbDeleteById(String tableName, String[] pKeys) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        StringBuilder sql = new StringBuilder("delete from `").append(tableName).append("` where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0)
                sql.append(" and ");
            sql.append("`").append(pKeys[i]).append("` = ?");
        }
        return sql.toString();
    }

    public void forDbSave(StringBuilder sql, List<Object> paras, String tableName, String[] pKeys,
            Map<String, Object> record) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        sql.append("insert into `");
        sql.append(tableName).append("`(");
        StringBuilder temp = new StringBuilder();
        temp.append(") values(");

        for (Entry<String, Object> e : record.entrySet()) {
            if (paras.size() > 0) {
                sql.append(", ");
                temp.append(", ");
            }
            sql.append("`").append(e.getKey()).append("`");
            temp.append("?");
            paras.add(e.getValue());
        }
        sql.append(temp.toString()).append(")");
    }

    public void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Map<String, Object> record) {
        tableName = tableName.trim();
        sql.append("insert into `");
        sql.append(tableName).append("`(");
        StringBuilder temp = new StringBuilder();
        temp.append(") values(");

        for (Entry<String, Object> e : record.entrySet()) {
            if (paras.size() > 0) {
                sql.append(", ");
                temp.append(", ");
            }
            sql.append("`").append(e.getKey()).append("`");
            temp.append("?");
            paras.add(e.getValue());
        }
        sql.append(temp.toString()).append(")");
    }

    public void forDbUpdate(String tableName, String[] pKeys, Object[] ids, Map<String, Object> record,
            StringBuilder sql, List<Object> paras) {
        tableName = tableName.trim();
        trimPrimaryKeys(pKeys);

        sql.append("update `").append(tableName).append("` set ");
        for (Entry<String, Object> e : record.entrySet()) {
            String colName = e.getKey();
            if (!isPrimaryKey(colName, pKeys)) {
                if (paras.size() > 0) {
                    sql.append(", ");
                }
                sql.append("`").append(colName).append("` = ? ");
                paras.add(e.getValue());
            }
        }
        sql.append(" where ");
        for (int i = 0; i < pKeys.length; i++) {
            if (i > 0)
                sql.append(" and ");
            sql.append("`").append(pKeys[i]).append("` = ?");
            paras.add(ids[i]);
        }
    }

    public void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select, String sqlExceptSelect) {
        int offset = pageSize * (pageNumber - 1);
        sql.append(select).append(" ");
        sql.append(sqlExceptSelect);
        sql.append(" limit ").append(offset).append(", ")
                .append(pageSize);    // limit can use one or two '?' to pass paras
    }
}
