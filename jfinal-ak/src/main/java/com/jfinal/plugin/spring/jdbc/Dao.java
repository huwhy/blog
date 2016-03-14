package com.jfinal.plugin.spring.jdbc;

import com.jfinal.annotation.Sql;
import com.jfinal.kit.StrKit;
import com.jfinal.plugin.spring.jdbc.dialect.Dialect;
import com.jfinal.plugin.spring.util.RecordUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * @author 胡道君 2013-7-20
 */
public class Dao<T, PK> extends JdbcDaoSupport {
    protected Table<T> table;
    private String tableName;
    private Class<T> beanClass;

    public Dao(String tableName, Class<T> beanClass) {
        this.tableName = tableName;
        this.beanClass = beanClass;
    }

    @PostConstruct
    public void init() {
        this.table = TableMapping.me().getTable(tableName);
        if (this.table == null) {
            table = new Table<T>(tableName);
            table.setBeanClass(beanClass);
            table.setMapper(new MyRowMapper<>(beanClass));
            TableBuilder.build(table, getConnection());
        } else {
            table.setBeanClass(beanClass);
        }
        Sql an = beanClass.getAnnotation(Sql.class);
        if (an != null) {
            if (StrKit.notBlank(an.insertSQL())) {
                table.setInsertSql(an.insertSQL());
            }
            if (StrKit.notBlank(an.updateSQL())) {
                table.setUpdateSql(an.updateSQL());
            }
        }
    }

    @Autowired
    public void setDatasource(DataSource datasource) {
        super.setDataSource(datasource);
    }

    public Dialect getDialect() {
        return TableMapping.me().getDialect();
    }

    /**
     * 添加记录
     */
    @SuppressWarnings("unchecked")
    public Object add(T po) {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder();
        Map<String, Object> record = RecordUtil.toR(po, table);
        getDialect().forModelSave(table, record, sql, params);
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            if (params.size() > 0) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
            }
            Object r = ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                r = rs.getObject(1);
            }
            return r;
        } catch (SQLException e) {
            throw new SpringJdbcException(e);
        }
    }

    public void adds(Collection<T> pos) throws SQLException {
        if (pos == null || pos.isEmpty()) {
            return;
        }
        final StringBuilder sql = new StringBuilder();
        final List<List<Object>> params = new ArrayList<>();
        getDialect().forDbSaves(sql, params, table, RecordUtil.toRList(pos));
        int[] ids = getJdbcTemplate().batchUpdate(sql.toString(), paramsToArray(params));
    }

    public void adds(String insertSQL, List<T> pos) throws SQLException {
        if (pos == null || pos.isEmpty()) {
            return;
        }
        final List<List<Object>> params = new ArrayList<>();
        getDialect().forModelSaves(insertSQL, RecordUtil.toRList(pos));
        getJdbcTemplate().batchUpdate(insertSQL, paramsToArray(params));
    }

    public int delete(T t) {
        List<Object> params = new ArrayList<>();
        String sql = getDialect().forModelDeleteById(table, RecordUtil.toR(t, table), params);
        return getJdbcTemplate().update(sql, params.toArray());
    }

    public void deletes(T... ts) {
        List<Object[]> params = new ArrayList<>();
        List<Map<String, Object>> maps = RecordUtil.toRList(Arrays.asList(ts), table);
        String sql = getDialect().forModelDeleteById(table, maps, params);
        getJdbcTemplate().batchUpdate(sql, params);
    }

    /**
     * 根据给定的删除SQL各参数删除
     */
    public Object delete(String sql, Object... params) {
        return getJdbcTemplate().update(sql, params);
    }

    public int deleteByWhere(String where, Object... params) {
        return getJdbcTemplate()
                .update(String.format("delete from %1$s where %2$s", this.getTableName(), where), params);
    }


    public int deleteById(Object id) {
        return this.deleteByWhere("id=?", id);
    }

    public int deleteByIds(Object... ids) {
        return this.deleteByWhere(inSql("id", ids.length), ids);
    }

    public int update(T o, String... ignoreProperties) {
        Map<String, Object> record = RecordUtil.toR(o, table, ignoreProperties);

        StringBuilder sql = new StringBuilder();
        List<Object> paras = new ArrayList<Object>();
        getDialect().forDbUpdate(table, record, sql, paras);
        return getJdbcTemplate().update(sql.toString(), paras);
    }

    public Object update(String sql, Object... params) {
        return getJdbcTemplate().update(sql, params);
    }

    public Object updateByWhere(String set, String where, Object... params) {
        return update(String.format("update %1$s set %2$s where %3$s", this.getTableName(), set, where), params);
    }

    public Object updateByWhereAndTable(String tableName, String set, String where, Object... params) {
        return update(String.format("update %1$s set %2$s where %3$s", tableName, set, where), params);
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
    public T getById(Object id) {
        return getByWhere("id=?", id);
    }

    public T getByWhere(String where, Object... params) {
        return getBySQL(String.format("select * from %1$s where %2$s", this.getTableName(), where), params);
    }

    public T getBySQL(String sql, Object... params) {
        List<T> ts = findSQL(sql, params);
        if (ts != null && !ts.isEmpty()) {
            return ts.get(0);
        }
        return null;
    }

    public List<T> findSQL(String sql, Object... params) {
        return findList(sql, params);
    }

    public List<T> findAll() {
        return findList(String.format("select * from %1$s", this.getTableName()));
    }

    public <E> List<E> findSQL(String sql, Class<E> clazz, Object... params) {
        return getJdbcTemplate().query(sql, new MyRowMapper<E>(clazz), params);
    }

    public List<T> findByWhere(String where, Object... params) {
        String sql = String.format("select * from %1$s where %2$s", this.getTableName(), where);
        return findList(sql, params);

    }

    public List<T> findList(String sql, Object... params) {
        return getJdbcTemplate().query(sql, table.getMapper(), params);
    }

    /**
     * 获取单列值的List
     */
    @SuppressWarnings("unchecked")
    public <E> List<E> findSingleList(String sql, Class<E> clazz, Object... params) {
        return getJdbcTemplate().queryForList(sql, clazz, params);
    }

    /**
     * 获取单个值
     */
    @SuppressWarnings("unchecked")
    public <E> E getSingle(String sql, Class<E> clazz, Object... params) {
        return getJdbcTemplate().queryForObject(sql, clazz, params);
    }

    public PageList<T> findPaging(StringBuilder sql, PageParam pageParam, List<Object> paras) {
        return findPaging(sql.toString(), pageParam, paras);
    }

    /**
     * 分页查询
     */
    public PageList<T> findPaging(String sql, PageParam pageParam, List<Object> paras) {
        PageList<T> paginate = paginate(sql, table.getBeanClass(), pageParam, paras.toArray());
        return paginate;
    }

    public <V> PageList<V> findPaging(StringBuilder sql, PageParam pageParam, List<Object> paras, Class<V> clazz) {
        PageList<V> paginate = paginate(sql.toString(), clazz, pageParam, paras.toArray());
        return paginate;
    }

    public <E> PageList<E> paginate(String sql, Class<E> clazz, PageParam pageParam, Object... paras) {
        int size = getSingle(getCountSQL(sql), Integer.class, paras);
        long totalRow = 0;
        int perSize = pageParam.getPerSize();
        int curNo = pageParam.getCurNo();
        if (size == 0) {
            return new PageList<>(new ArrayList<E>(0), totalRow, perSize, curNo);
        }

        List<E> list = findSQL(sql + pageParam.buildSQL(), clazz, paras);
        return new PageList<>(list, totalRow, perSize, curNo);
    }

    public static String getCountSQL(String sql) {
        String temp = sql.toLowerCase();
        int selectIndex = temp.indexOf("select ");
        int fromIndex = sql.toLowerCase().indexOf("from ");
        int index = temp.indexOf("select ", selectIndex + 7);
        while (index != -1 && index < fromIndex) {
            fromIndex = temp.indexOf("from ", fromIndex + 5);
            selectIndex = index;
            index = temp.indexOf("select ", selectIndex + 7);
        }
        int orderIndex = sql.lastIndexOf("order by");
        if (orderIndex != -1) {
            return "select count(1) " + sql.substring(fromIndex, orderIndex);
        } else {
            return "select count(1) " + sql.substring(fromIndex);
        }
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


    protected List<Object[]> paramsToArray(List<List<Object>> params) {
        List<Object[]> array = new ArrayList<>(params.size());
        for (List<Object> paras : params) {
            array.add(paras.toArray());
        }
        return array;
    }

    protected Object[][] paramsToArray(String[] keys, List<Map<String, Object>> records) {
        Object[][] array = new Object[records.size()][keys.length];
        int i = 0;
        for (Map<String, Object> record : records) {
            int j = 0;
            for (String key : keys) {
                array[i][j++] = record.get(key);
            }
            i++;
        }
        return array;
    }
}