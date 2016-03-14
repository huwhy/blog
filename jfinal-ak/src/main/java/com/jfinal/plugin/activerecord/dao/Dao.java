package com.jfinal.plugin.activerecord.dao;

import com.jfinal.kit.StrKit;
import com.jfinal.plugin.activerecord.*;
import com.jfinal.annotation.Sql;
import com.jfinal.plugin.activerecord.util.RecordUtil;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author 胡道君 2013-7-20
 */
public class Dao<T, PK> {
    protected Table<T> table;
    protected DbPro dbPro;

    public Dao(String tableName, Class<T> beanClass) {
        this.table = TableMapping.me().getTable(tableName);
        if (this.table == null) {
            table = new Table<T>(tableName);
            table.setBeanClass(beanClass);
            TableBuilder.build(table, DbKit.getConfig());
            this.dbPro = DbPro.use();
        } else {
            table.setBeanClass(beanClass);
            Config beanConfig = DbKit.getBeanConfig(table.getBeanClass());
            if (beanConfig == null) {
                beanConfig = DbKit.getConfig();
            }
            this.dbPro = DbPro.use(beanConfig.getName());
        }
        Sql an = beanClass.getAnnotation(Sql.class);
        if (an != null && StrKit.notBlank(an.insertSQL())) {
            table.setInsertSql(an.insertSQL());
        }
    }

    /**
     * 添加记录
     */
    @SuppressWarnings("unchecked")
    public Object add(T po) {
        return dbPro.add(table, RecordUtil.toR(po, table), true);
    }

    public void adds(Collection<T> pos) throws SQLException {
        if (pos == null || pos.isEmpty()) {
            return;
        }
        dbPro.adds(table, RecordUtil.toRList(pos, table));
    }

    public void adds(String insertSQL, List<T> pos, int batchSize) throws SQLException {
        if (pos == null || pos.isEmpty()) {
            return;
        }
        dbPro.adds(insertSQL, RecordUtil.toRList(pos, table), batchSize);
    }


    public void addList(List<T> ts, String... ignoreProperties) throws SQLException {
        if (ts == null || ts.isEmpty()) {
            return;
        }
        dbPro.adds(table, RecordUtil.toRList(ts, table, ignoreProperties));
    }

    public void addList(String insertSQL, List<T> params, int batchSize) throws SQLException {
        if (params == null || params.isEmpty()) {
            return;
        }
        adds(insertSQL, params, batchSize);
    }

    public Object delByIds(Object... ids) {
        return deleteByWhere(inSql("id", ids.length), ids);
    }

    public Object deleteByPk(PK pk) {
        return dbPro.delete(table, pk);
    }

    public Object deleteByPks(PK... pks) {
        return dbPro.delete(table, pks);
    }

    public Object delete(Object t) {
        return dbPro.delete(table, t);
    }

    public Object deletes(Object... ts) {
        return dbPro.delete(table, ts);
    }

    /**
     * 根据字段ID删除记录
     */
    public Object deleteById(String tableName, Object id) {
        return dbPro.deleteById(tableName, id);
    }

    /**
     * 根据给定的删除SQL各参数删除
     */
    public Object delete(String sql, Object... params) {
        return dbPro.update(sql, params);
    }

    public Object deleteByWhere(String where, Object... params) {
        return dbPro.update(String.format("delete from %1$s where %2$s", this.getTableName(), where), params);
    }

    public boolean update(T o, String... ignoreProperties) {
        return dbPro.update(table.getName(), table.getPrimaryKeyStr(), RecordUtil.toR(o, table, ignoreProperties));
    }

    public boolean updateByPk(Object record, String pkName) {
        return dbPro.update(getTableName(), pkName, RecordUtil.toR(record));
    }

    /**
     * 更新
     */
    public boolean update(String tableName, Object record) {
        return dbPro.update(tableName, RecordUtil.toR(record));
    }

    public Object update(String sql, Object... params) {
        return dbPro.update(sql, params);
    }

    public Object updateByWhere(String set, String where, Object... params) {
        return dbPro.update(String.format("update %1$s set %2$s where %3$s", this.getTableName(), set, where), params);
    }

    public Object updateByWhereAndTable(String tableName, String set, String where, Object... params) {
        return dbPro.update(String.format("update %1$s set %2$s where %3$s", tableName, set, where), params);
    }

    public T getByPK(PK pk) {
        if (pk.getClass() == table.getBeanClass()) {
            Map<String, Object> record = RecordUtil.toR(pk);
            StringBuilder pkWhere = new StringBuilder();
            List<Object> params = new ArrayList<>();
            for (String key : table.getPrimaryKey()) {
                pkWhere.append(" ").append(key).append("=? and");
                params.add(record.get(key));
            }
            pkWhere.delete(pkWhere.length() - 4, pkWhere.length());
            return getByWhere(pkWhere.toString(), params.toArray());
        } else {
            return getByWhere(table.getPrimaryKey()[0] + "=?", pk);
        }
    }

    /**
     * 根据字段ID获取记录
     */
    public T getById(String tableName, Object id) {
        Map<String, Object> record = dbPro.findById(tableName, id);
        return RecordUtil.toC(record, table);
    }

    /**
     * 根据字段ID获取记录
     */
    public T getById(Object id) {
        return getById(getTableName(), id);
    }

    public T getByWhere(String where, Object... params) {
        Map<String, Object> record = dbPro
                .findFirst(String.format("select * from %1$s where %2$s", this.getTableName(), where), params);
        return RecordUtil.toC(record, table);
    }

    public T getBySQL(String sql, Object... params) {
        return (T) RecordUtil.toC(find(sql, params), table);
    }

    public List<T> findSQL(String sql, Object... params) {
        return findList(sql, params);
    }

    public List<T> findAll() {
        return findList(String.format("select * from %1$s", this.getTableName()));
    }

    public <E> List<E> findSQL(String sql, Class<E> clazz, Object... params) {
        return RecordUtil.toCList(dbPro.find(sql, params), clazz);
    }

    public List<T> findByWhere(String where, Object... params) {
        List<Map<String, Object>> records = dbPro
                .find(String.format("select * from %1$s where %2$s", this.getTableName(), where), params);
        return RecordUtil.toCList(records, table);
    }

    public List<T> findList(String sql, Object... params) {
        List<Map<String, Object>> records = dbPro.find(sql, params);
        return RecordUtil.toCList(records, table);
    }

    /**
     * 获取单列值的List
     */
    @SuppressWarnings("unchecked")
    public <E> List<E> findSingleList(String sql, Object... params) {
        List<Map<String, Object>> records = dbPro.find(sql, params);
        List<E> reList = new ArrayList<E>(records.size());
        for (Map<String, Object> r : records) {
            reList.add((E) r.values().toArray()[0]);
        }
        return reList;
    }

    /**
     * 获取单个值
     */
    @SuppressWarnings("unchecked")
    public <E> E getSingle(String sql, Object... params) {
        List<E> record = this.findSingleList(sql, params);
        if (record != null) {
            return record.get(0);
        } else {
            return null;
        }
    }

    public Map<String, Object> find(String sql, Object... params) {
        return dbPro.findFirst(sql, params);
    }


    public PageList<T> findPaging(StringBuilder sql, PageParam pageParam, List<Object> paras) {
        return findPaging(sql.toString(), pageParam, paras);
    }

    /**
     * 分页查询
     */
    public PageList<T> findPaging(String sql, PageParam pageParam, List<Object> paras) {
        PageList<Map<String, Object>> paginate = dbPro.paginate(sql, pageParam, paras.toArray());
        return RecordUtil.toPageList(paginate, table);
    }

    public <V> PageList<V> findPaging(StringBuilder sql, PageParam pageParam, List<Object> paras, Class<V> clazz) {
        PageList<Map<String, Object>> pageList = dbPro.paginate(sql.toString(), pageParam, paras.toArray());
        return RecordUtil.toPageList(pageList, clazz);
    }

    public String getTableName() {
        return this.table.getName();
    }


    public String inSql(String colName, int size) {
        StringBuilder sb = new StringBuilder();
        sb.append(colName).append(" in (");
        for (int i = 0; i < size; i++) {
            sb.append("?,");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(")");
        return sb.toString();
    }

    public String notInSql(String colName, int size) {
        return " not " + inSql(colName, size);
    }
}