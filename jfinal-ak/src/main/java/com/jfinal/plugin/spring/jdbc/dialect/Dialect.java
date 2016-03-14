package com.jfinal.plugin.spring.jdbc.dialect;

import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.spring.jdbc.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Dialect.
 */
public abstract class Dialect {

    public String forTableInfoBuilderDoBuildTableInfo(String tableName) {
        return "select * from " + tableName + " where 1 = 2";
    }

    public abstract String forTableBuilderDoBuild(String tableName);

    public abstract void forModelSave(Table table, Map<String, Object> attrs, StringBuilder sql, List<Object> paras);

    public abstract String forModelDeleteById(Table table, Map<String, Object> attrs, List<Object> params);

    public abstract String forModelDeleteById(Table table, List<Map<String, Object>> attrs, List<Object[]> params);

    public abstract void forModelUpdate(Table table, Map<String, Object> attrs, Set<String> modifyFlag,
            StringBuilder sql, List<Object> paras);

    public abstract String forModelFindById(Table table, String columns);

    public abstract String forDbFindById(String tableName, String[] pKeys);

    public abstract String forDbDeleteById(String tableName, String[] pKeys);

    public abstract void forDbSave(StringBuilder sql, List<Object> paras, String tableName, String[] pKeys,
            Map<String, Object> record);

    public abstract void forDbSave(StringBuilder sql, List<Object> paras, String tableName, Map<String, Object> record);

    public abstract void forDbUpdate(String tableName, String[] pKeys, Object[] ids, Map<String, Object> record,
            StringBuilder sql, List<Object> paras);

    public abstract void forPaginate(StringBuilder sql, int pageNumber, int pageSize, String select,
            String sqlExceptSelect);

    public boolean isOracle() {
        return false;
    }

    public abstract void forDbSaves(StringBuilder sql, List<List<Object>> paras, Table table,
            List<Map<String, Object>> records);

    public abstract void forModelSaves(String insertSql, List<Map<String, Object>> records);

    public abstract void forModelSave(String insertSql, Map<String, Object> attrs, List<Object> paras);

    public boolean isTakeOverDbPaginate() {
        return false;
    }

    public Page<Map<String, Object>> takeOverDbPaginate(Connection conn, int pageNumber, int pageSize, String select,
            String sqlExceptSelect, Object... paras) throws SQLException {
        throw new RuntimeException("You should implements this method in " + getClass().getName());
    }


    public void forDbUpdate(Table info, Map<String, Object> record, StringBuilder sql, List<Object> paras) {
        sql.append("update ").append(info.getName()).append(" set ");
        Set<String> pks = new HashSet<String>(Arrays.asList(info.getPrimaryKey()));
        for (Map.Entry<String, Object> e : record.entrySet()) {
            String colName = e.getKey();
            if (!pks.contains(colName)) {
                if (paras.size() > 0) {
                    sql.append(", ");
                }
                sql.append("").append(colName).append(" = ? ");
                paras.add(e.getValue());
            }
        }
        sql.append("where ");
        for (String pk : pks) {
            sql.append(pk).append(" = ? and ");
            paras.add(record.get(pk));
        }
        sql.delete(sql.length() - 5, sql.length());
    }


    public boolean isTakeOverModelPaginate() {
        return false;
    }

    public void fillStatement(PreparedStatement pst, List<Object> paras) throws SQLException {
        for (int i = 0, size = paras.size(); i < size; i++) {
            pst.setObject(i + 1, paras.get(i));
        }
    }

    public void fillStatement(PreparedStatement pst, Object... paras) throws SQLException {
        for (int i = 0; i < paras.length; i++) {
            pst.setObject(i + 1, paras[i]);
        }
    }

    public String getDefaultPrimaryKey() {
        return "id";
    }

    protected boolean isPrimaryKey(String colName, String[] pKeys) {
        for (String pKey : pKeys)
            if (colName.equalsIgnoreCase(pKey))
                return true;
        return false;
    }

    /**
     * 一、forDbXxx 系列方法中若有如下两种情况之一，则需要调用此方法对 pKeys 数组进行 trim():
     * 1：方法中调用了 isPrimaryKey(...)：为了防止在主键相同情况下，由于前后空串造成 isPrimaryKey 返回 false
     * 2：为了防止 tableName、colName 与数据库保留字冲突的，添加了包裹字符的：为了防止串包裹区内存在空串
     * 如 mysql 使用的 "`" 字符以及 PostgreSql 使用的 "\"" 字符
     * 不满足以上两个条件之一的 forDbXxx 系列方法也可以使用 trimPrimaryKeys(...) 方法让 sql 更加美观，但不是必须
     * <p>
     * 二、forModelXxx 由于在映射时已经trim()，故不再需要调用此方法
     */
    protected void trimPrimaryKeys(String[] pKeys) {
        for (int i = 0; i < pKeys.length; i++)
            pKeys[i] = pKeys[i].trim();
    }
}






