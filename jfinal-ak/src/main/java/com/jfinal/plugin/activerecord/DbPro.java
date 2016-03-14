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

package com.jfinal.plugin.activerecord;

import com.jfinal.plugin.activerecord.cache.ICache;
import com.jfinal.plugin.activerecord.dao.PageList;
import com.jfinal.plugin.activerecord.dao.PageParam;
import com.jfinal.plugin.activerecord.util.RecordUtil;

import java.sql.*;
import java.util.*;

import static com.jfinal.plugin.activerecord.DbKit.NULL_PARA_ARRAY;
import static com.jfinal.plugin.activerecord.DbKit.getConfig;

/**
 * DbPro. Professional database query and update tool.
 */
public class DbPro {

    private final Config config;
    private static final Map<String, DbPro> map = new HashMap<>();

    public DbPro() {
        if (DbKit.config == null)
            throw new RuntimeException("The main config is null, initialize ActiveRecordPlugin first");
        this.config = DbKit.config;
    }

    public DbPro(String configName) {
        this.config = DbKit.getConfig(configName);
        if (this.config == null)
            throw new IllegalArgumentException("Config not found by configName: " + configName);
    }

    public static DbPro use(String configName) {
        DbPro result = map.get(configName);
        if (result == null) {
            result = new DbPro(configName);
            map.put(configName, result);
        }
        return result;
    }

    public static DbPro use() {
        return use(DbKit.config.name);
    }

    <T> List<T> query(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        List result = new ArrayList();
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        int colAmount = rs.getMetaData().getColumnCount();
        if (colAmount > 1) {
            while (rs.next()) {
                Object[] temp = new Object[colAmount];
                for (int i = 0; i < colAmount; i++) {
                    temp[i] = rs.getObject(i + 1);
                }
                result.add(temp);
            }
        } else if (colAmount == 1) {
            while (rs.next()) {
                result.add(rs.getObject(1));
            }
        }
        DbKit.closeQuietly(rs, pst);
        return result;
    }

    /**
     */
    public <T> List<T> query(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return query(config, conn, sql, paras);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * @param sql an SQL statement
     * @see #query(String, Object...)
     */
    public <T> List<T> query(String sql) {        // return  List<object[]> or List<object>
        return query(sql, NULL_PARA_ARRAY);
    }

    /**
     * Execute sql query and return the first result. I recommend add "limit 1" in your sql.
     *
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return Object[] if your sql has select more than one column,
     * and it return Object if your sql has select only one column.
     */
    public <T> T queryFirst(String sql, Object... paras) {
        List<T> result = query(sql, paras);
        return (result.size() > 0 ? result.get(0) : null);
    }

    /**
     * @param sql an SQL statement
     * @see #queryFirst(String, Object...)
     */
    public <T> T queryFirst(String sql) {
        // return queryFirst(sql, NULL_PARA_ARRAY);
        List<T> result = query(sql, NULL_PARA_ARRAY);
        return (result.size() > 0 ? result.get(0) : null);
    }

    // 26 queryXxx method below -----------------------------------------------

    /**
     * Execute sql query just return one column.
     *
     * @param <T>   the type of the column that in your sql's select statement
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return List<T>
     */
    public <T> T queryColumn(String sql, Object... paras) {
        List<T> result = query(sql, paras);
        if (result.size() > 0) {
            T temp = result.get(0);
            if (temp instanceof Object[])
                throw new ActiveRecordException("Only ONE COLUMN can be queried.");
            return temp;
        }
        return null;
    }

    public <T> T queryColumn(String sql) {
        return (T) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public String queryStr(String sql, Object... paras) {
        return (String) queryColumn(sql, paras);
    }

    public String queryStr(String sql) {
        return (String) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public Integer queryInt(String sql, Object... paras) {
        return (Integer) queryColumn(sql, paras);
    }

    public Integer queryInt(String sql) {
        return (Integer) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public Long queryLong(String sql, Object... paras) {
        return (Long) queryColumn(sql, paras);
    }

    public Long queryLong(String sql) {
        return (Long) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public Double queryDouble(String sql, Object... paras) {
        return (Double) queryColumn(sql, paras);
    }

    public Double queryDouble(String sql) {
        return (Double) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public Float queryFloat(String sql, Object... paras) {
        return (Float) queryColumn(sql, paras);
    }

    public Float queryFloat(String sql) {
        return (Float) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public java.math.BigDecimal queryBigDecimal(String sql, Object... paras) {
        return (java.math.BigDecimal) queryColumn(sql, paras);
    }

    public java.math.BigDecimal queryBigDecimal(String sql) {
        return (java.math.BigDecimal) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public byte[] queryBytes(String sql, Object... paras) {
        return (byte[]) queryColumn(sql, paras);
    }

    public byte[] queryBytes(String sql) {
        return (byte[]) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public java.util.Date queryDate(String sql, Object... paras) {
        return (java.util.Date) queryColumn(sql, paras);
    }

    public java.util.Date queryDate(String sql) {
        return (java.util.Date) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public java.sql.Time queryTime(String sql, Object... paras) {
        return (java.sql.Time) queryColumn(sql, paras);
    }

    public java.sql.Time queryTime(String sql) {
        return (java.sql.Time) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public java.sql.Timestamp queryTimestamp(String sql, Object... paras) {
        return (java.sql.Timestamp) queryColumn(sql, paras);
    }

    public java.sql.Timestamp queryTimestamp(String sql) {
        return (java.sql.Timestamp) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public Boolean queryBoolean(String sql, Object... paras) {
        return (Boolean) queryColumn(sql, paras);
    }

    public Boolean queryBoolean(String sql) {
        return (Boolean) queryColumn(sql, NULL_PARA_ARRAY);
    }

    public Number queryNumber(String sql, Object... paras) {
        return (Number) queryColumn(sql, paras);
    }

    public Number queryNumber(String sql) {
        return (Number) queryColumn(sql, NULL_PARA_ARRAY);
    }
    // 26 queryXxx method under -----------------------------------------------

    /**
     * Execute sql update
     */
    int update(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        int result = pst.executeUpdate();
        DbKit.closeQuietly(pst);
        return result;
    }

    /**
     * Execute update, insert or delete sql statement.
     *
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return either the row count for <code>INSERT</code>, <code>UPDATE</code>,
     * or <code>DELETE</code> statements, or 0 for SQL statements
     * that return nothing
     */
    public int update(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return update(config, conn, sql, paras);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * @param sql an SQL statement
     * @see #update(String, Object...)
     */
    public int update(String sql) {
        return update(sql, NULL_PARA_ARRAY);
    }

    List<Map<String, Object>> find(Config config, Connection conn, String sql, Object... paras) throws SQLException {
        PreparedStatement pst = conn.prepareStatement(sql);
        config.dialect.fillStatement(pst, paras);
        ResultSet rs = pst.executeQuery();
        List<Map<String, Object>> result = RecordBuilder.build(config, rs);
        DbKit.closeQuietly(rs, pst);
        return result;
    }

    /**
     */
    public List<Map<String, Object>> find(String sql, Object... paras) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return find(config, conn, sql, paras);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * @param sql the sql statement
     */
    public List<Map<String, Object>> find(String sql) {
        return find(sql, NULL_PARA_ARRAY);
    }

    /**
     * Find first record. I recommend add "limit 1" in your sql.
     *
     * @param sql   an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras the parameters of sql
     * @return the Record object
     */
    public Map<String, Object> findFirst(String sql, Object... paras) {
        List<Map<String, Object>> result = find(sql, paras);
        return result.size() > 0 ? result.get(0) : null;
    }

    /**
     * @param sql an SQL statement
     * @see #findFirst(String, Object...)
     */
    public Map<String, Object> findFirst(String sql) {
        List<Map<String, Object>> result = find(sql, NULL_PARA_ARRAY);
        return result.size() > 0 ? result.get(0) : null;
    }

    /**
     * Find record by id with default primary key.
     * <pre>
     * Example:
     * Record user = DbPro.use().findById("user", 15);
     * </pre>
     *
     * @param tableName the table name of the table
     * @param idValue   the id value of the record
     */
    public Map<String, Object> findById(String tableName, Object idValue) {
        return findById(tableName, config.dialect.getDefaultPrimaryKey(), idValue);
    }

    /**
     * Find record by id.
     * <pre>
     * Example:
     * Record user = DbPro.use().findById("user", "user_id", 123);
     * Record userRole = DbPro.use().findById("user_role", "user_id, role_id", 123, 456);
     * </pre>
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param idValue    the id value of the record, it can be composite id values
     */
    public Map<String, Object> findById(String tableName, String primaryKey, Object... idValue) {
        String[] pKeys = primaryKey.split(",");
        if (pKeys.length != idValue.length)
            throw new IllegalArgumentException("primary key number must equals id value number");

        String sql = config.dialect.forDbFindById(tableName, pKeys);
        List<Map<String, Object>> result = find(sql, idValue);
        return result.size() > 0 ? result.get(0) : null;
    }

    /**
     * Delete record by id with default primary key.
     * <pre>
     * Example:
     * DbPro.use().deleteById("user", 15);
     * </pre>
     *
     * @param tableName the table name of the table
     * @param idValue   the id value of the record
     * @return true if delete succeed otherwise false
     */
    public boolean deleteById(String tableName, Object idValue) {
        return deleteById(tableName, config.dialect.getDefaultPrimaryKey(), idValue);
    }

    /**
     * Delete record by id.
     * <pre>
     * Example:
     * DbPro.use().deleteById("user", "user_id", 15);
     * DbPro.use().deleteById("user_role", "user_id, role_id", 123, 456);
     * </pre>
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param idValue    the id value of the record, it can be composite id values
     * @return true if delete succeed otherwise false
     */
    public boolean deleteById(String tableName, String primaryKey, Object... idValue) {
        String[] pKeys = primaryKey.split(",");
        if (pKeys.length != idValue.length)
            throw new IllegalArgumentException("primary key number must equals id value number");

        String sql = config.dialect.forDbDeleteById(tableName, pKeys);
        return update(sql, idValue) >= 1;
    }

    public boolean delete(Table table, Object... values) {
        if (values.length == 0)
            return false;
        String[] pKeys = table.getPrimaryKey();
        String sql = config.dialect.forModelDeleteById(table);
        int[] r = batch(sql, paramsToArray(pKeys, RecordUtil.toRList(Arrays.asList(values))), 100);
        return r.length > 0;
    }

    /**
     * Delete record.
     * <pre>
     * Example:
     * boolean succeed = DbPro.use().delete("user", "id", user);
     * </pre>
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record     the record
     * @return true if delete succeed otherwise false
     */
    public boolean delete(String tableName, String primaryKey, Map<String, Object> record) {
        return deleteById(tableName, primaryKey, record.get(primaryKey));
    }

    /**
     * <pre>
     * Example:
     * boolean succeed = DbPro.use().delete("user", user);
     * </pre>
     *
     * @see #delete(String, String, Map<String, Object>)
     */
    public boolean delete(String tableName, Map<String, Object> record) {
        String defaultPrimaryKey = config.dialect.getDefaultPrimaryKey();
        return deleteById(tableName, defaultPrimaryKey, record.get(defaultPrimaryKey));
    }

    public PageList<Map<String, Object>> paginate(String sql, PageParam pageParam, Object... paras) {
        List result = query(getCountSQL(sql), paras);
        int size = result.size();
        long totalRow = 0;
        int perSize = pageParam.getPerSize();
        int curNo = pageParam.getCurNo();
        if (size == 1)
            totalRow = ((Number) result.get(0)).longValue();
        else if (size > 1)
            totalRow = result.size();
        else {
            return new PageList<Map<String, Object>>(new ArrayList<Map<String, Object>>(0), totalRow, perSize, curNo);
        }

        int totalPage = (int) (totalRow / perSize);
        if (totalRow % perSize != 0) {
            totalPage++;
        }

        List<Map<String, Object>> list = find(sql + pageParam.buildSQL(), paras);
        return new PageList<Map<String, Object>>(list, totalRow, perSize, curNo);
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

    boolean save(Config config, Connection conn, String tableName, String primaryKey,
            Map<String, Object> record) throws SQLException {
        String[] pKeys = primaryKey.split(",");
        List<Object> paras = new ArrayList<Object>();
        StringBuilder sql = new StringBuilder();
        config.dialect.forDbSave(sql, paras, tableName, pKeys, record);

        PreparedStatement pst;
        if (config.dialect.isOracle())
            pst = conn.prepareStatement(sql.toString(), pKeys);
        else
            pst = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);

        config.dialect.fillStatement(pst, paras);
        int result = pst.executeUpdate();
        getGeneratedKey(pst, record, pKeys);
        DbKit.closeQuietly(pst);
        return result >= 1;
    }

    /**
     * Get id after save record.
     */
    private void getGeneratedKey(PreparedStatement pst, Map<String, Object> record,
            String[] pKeys) throws SQLException {
        ResultSet rs = pst.getGeneratedKeys();
        for (String pKey : pKeys)
            if (record.get(pKey) == null || config.dialect.isOracle())
                if (rs.next())
                    record.put(pKey, rs.getObject(1));
        rs.close();
    }

    /**
     * Save record.
     * <pre>
     * Example:
     * Record userRole = new Record().set("user_id", 123).set("role_id", 456);
     * DbPro.use().save("user_role", "user_id, role_id", userRole);
     * </pre>
     *
     * @param tableName  the table name of the table
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record     the record will be saved
     */
    public boolean save(String tableName, String primaryKey, Map<String, Object> record) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return save(config, conn, tableName, primaryKey, record);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * @see #save(String, String, Map<String, Object>)
     */
    public boolean save(String tableName, Map<String, Object> record) {
        return save(tableName, config.dialect.getDefaultPrimaryKey(), record);
    }

    boolean update(Config config, Connection conn, String tableName, String primaryKey,
            Map<String, Object> record) throws SQLException {
        String[] pKeys = primaryKey.split(",");
        Object[] ids = new Object[pKeys.length];

        for (int i = 0; i < pKeys.length; i++) {
            ids[i] = record.get(pKeys[i].trim());    // .trim() is important!
            if (ids[i] == null)
                throw new ActiveRecordException(
                        "You can't update record without Primary Key, " + pKeys[i] + " can not be null.");
        }

        StringBuilder sql = new StringBuilder();
        List<Object> paras = new ArrayList<Object>();
        config.dialect.forDbUpdate(tableName, pKeys, ids, record, sql, paras);

        if (paras.size() <= 1) {    // Needn't update
            return false;
        }

        return update(config, conn, sql.toString(), paras.toArray()) >= 1;
    }

    /**
     * Update Record.
     * <pre>
     * Example:
     * DbPro.use().update("user_role", "user_id, role_id", record);
     * </pre>
     *
     * @param tableName  the table name of the Record save to
     * @param primaryKey the primary key of the table, composite primary key is separated by comma character: ","
     * @param record     the Record object
     */
    public boolean update(String tableName, String primaryKey, Map<String, Object> record) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return update(config, conn, tableName, primaryKey, record);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * Update record with default primary key.
     * <pre>
     * Example:
     * DbPro.use().update("user", record);
     * </pre>
     *
     * @see #update(String, String, Map<String, Object>)
     */
    public boolean update(String tableName, Map<String, Object> record) {
        return update(tableName, config.dialect.getDefaultPrimaryKey(), record);
    }

    /**
     */
    public Object execute(ICallback callback) {
        return execute(config, callback);
    }

    /**
     * Execute callback. It is useful when all the API can not satisfy your requirement.
     *
     * @param config   the Config object
     * @param callback the ICallback interface
     */
    Object execute(Config config, ICallback callback) {
        Connection conn = null;
        try {
            conn = config.getConnection();
            return callback.call(conn);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            config.close(conn);
        }
    }

    /**
     * Execute transaction.
     *
     * @param config           the Config object
     * @param transactionLevel the transaction level
     * @param atom             the atom operation
     * @return true if transaction executing succeed otherwise false
     */
    boolean tx(Config config, int transactionLevel, IAtom atom) {
        Connection conn = config.getThreadLocalConnection();
        if (conn != null) {    // Nested transaction support
            try {
                if (conn.getTransactionIsolation() < transactionLevel)
                    conn.setTransactionIsolation(transactionLevel);
                boolean result = atom.run();
                if (result)
                    return true;
                throw new NestedTransactionHelpException(
                        "Notice the outer transaction that the nested transaction return false");    // important:can not return false
            } catch (SQLException e) {
                throw new ActiveRecordException(e);
            }
        }

        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            config.setThreadLocalConnection(conn);
            conn.setTransactionIsolation(transactionLevel);
            conn.setAutoCommit(false);
            boolean result = atom.run();
            if (result)
                conn.commit();
            else
                conn.rollback();
            return result;
        } catch (NestedTransactionHelpException e) {
            if (conn != null)
                try {
                    conn.rollback();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            return false;
        } catch (Throwable t) {
            if (conn != null)
                try {
                    conn.rollback();
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            throw t instanceof RuntimeException ? (RuntimeException) t : new ActiveRecordException(t);
        } finally {
            try {
                if (conn != null) {
                    if (autoCommit != null)
                        conn.setAutoCommit(autoCommit);
                    conn.close();
                }
            } catch (Throwable t) {
                t.printStackTrace();    // can not throw exception here, otherwise the more important exception in previous catch block can not be thrown
            } finally {
                config.removeThreadLocalConnection();    // prevent memory leak
            }
        }
    }

    public boolean tx(int transactionLevel, IAtom atom) {
        return tx(config, transactionLevel, atom);
    }

    /**
     * Execute transaction with default transaction level.
     *
     * @see #tx(int, IAtom)
     */
    public boolean tx(IAtom atom) {
        return tx(config, config.getTransactionLevel(), atom);
    }

    /**
     * Find Record by cache.
     *
     * @param cacheName the cache name
     * @param key       the key used to get date from cache
     * @return the list of Record
     * @see #find(String, Object...)
     */
    public List<Map<String, Object>> findByCache(String cacheName, Object key, String sql, Object... paras) {
        ICache cache = config.getCache();
        List<Map<String, Object>> result = cache.get(cacheName, key);
        if (result == null) {
            result = find(sql, paras);
            cache.put(cacheName, key, result);
        }
        return result;
    }

    /**
     * @see #findByCache(String, Object, String, Object...)
     */
    public List<Map<String, Object>> findByCache(String cacheName, Object key, String sql) {
        return findByCache(cacheName, key, sql, NULL_PARA_ARRAY);
    }

    /**
     * Find first record by cache. I recommend add "limit 1" in your sql.
     *
     * @param cacheName the cache name
     * @param key       the key used to get date from cache
     * @param sql       an SQL statement that may contain one or more '?' IN parameter placeholders
     * @param paras     the parameters of sql
     * @return the Record object
     * @see #findFirst(String, Object...)
     */
    public Map<String, Object> findFirstByCache(String cacheName, Object key, String sql, Object... paras) {
        ICache cache = config.getCache();
        Map<String, Object> result = cache.get(cacheName, key);
        if (result == null) {
            result = findFirst(sql, paras);
            cache.put(cacheName, key, result);
        }
        return result;
    }

    /**
     * @see #findFirstByCache(String, Object, String, Object...)
     */
    public Map<String, Object> findFirstByCache(String cacheName, Object key, String sql) {
        return findFirstByCache(cacheName, key, sql, NULL_PARA_ARRAY);
    }

    private int[] batch(Config config, Connection conn, String sql, Object[][] paras,
            int batchSize) throws SQLException {
        if (paras == null || paras.length == 0)
            throw new IllegalArgumentException("The paras array length must more than 0.");
        if (batchSize < 1)
            throw new IllegalArgumentException("The batchSize must more than 0.");

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int[] result = new int[paras.length];
        PreparedStatement pst = conn.prepareStatement(sql);
        for (int i = 0; i < paras.length; i++) {
            for (int j = 0; j < paras[i].length; j++) {
                Object value = paras[i][j];
                if (config.dialect.isOracle()) {
                    if (value instanceof java.sql.Date)
                        pst.setDate(j + 1, (java.sql.Date) value);
                    else if (value instanceof java.sql.Timestamp)
                        pst.setTimestamp(j + 1, (java.sql.Timestamp) value);
                    else
                        pst.setObject(j + 1, value);
                } else
                    pst.setObject(j + 1, value);
            }
            pst.addBatch();
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = pst.executeBatch();
                if (isInTransaction == false)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        int[] r = pst.executeBatch();
        if (isInTransaction == false)
            conn.commit();
        for (int k = 0; k < r.length; k++)
            result[pointer++] = r[k];
        DbKit.closeQuietly(pst);
        return result;
    }

    private int[] batch(Config config, String sql, Object[][] paras, int batchSize) throws SQLException {
        if (paras == null || paras.length == 0)
            throw new IllegalArgumentException("The paras array length must more than 0.");
        if (batchSize < 1)
            throw new IllegalArgumentException("The batchSize must more than 0.");

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int[] result = new int[paras.length];
        Connection conn = config.getConnection();
        PreparedStatement pst = conn.prepareStatement(sql);
        for (int i = 0; i < paras.length; i++) {
            for (int j = 0; j < paras[i].length; j++) {
                Object value = paras[i][j];
                if (config.dialect.isOracle()) {
                    if (value instanceof java.sql.Date)
                        pst.setDate(j + 1, (java.sql.Date) value);
                    else if (value instanceof java.sql.Timestamp)
                        pst.setTimestamp(j + 1, (java.sql.Timestamp) value);
                    else
                        pst.setObject(j + 1, value);
                } else
                    pst.setObject(j + 1, value);
            }
            pst.addBatch();
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = pst.executeBatch();
                if (!isInTransaction)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        int[] r = pst.executeBatch();
        if (!isInTransaction)
            conn.commit();
        for (int k = 0; k < r.length; k++)
            result[pointer++] = r[k];
        DbKit.closeQuietly(pst);
        return result;
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     * <pre>
     * Example:
     * String sql = "insert into user(name, cash) values(?, ?)";
     * int[] result = DbPro.use().batch("myConfig", sql, new Object[][]{{"James", 888}, {"zhanjin", 888}});
     * </pre>
     *
     * @param sql   The SQL to execute.
     * @param paras An array of query replacement parameters.  Each row in this array is one set of batch replacement values.
     * @return The number of rows updated per statement
     */
    public int[] batch(String sql, Object[][] paras, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sql, paras, batchSize);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            if (autoCommit != null)
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            config.close(conn);
        }
    }

    private int[] batch(Config config, Connection conn, String sql, String columns, List<Map<String, Object>> list,
            int batchSize) throws SQLException {
        if (list == null || list.size() == 0)
            return new int[0];
        String[] columnArray = columns.split(",");
        for (int i = 0; i < columnArray.length; i++)
            columnArray[i] = columnArray[i].trim();

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int size = list.size();
        int[] result = new int[size];
        PreparedStatement pst = conn.prepareStatement(sql);
        for (int i = 0; i < size; i++) {
            Map<String, Object> map = list.get(i);
            for (int j = 0; j < columnArray.length; j++) {
                Object value = map.get(columnArray[j]);
                if (config.dialect.isOracle()) {
                    if (value instanceof java.sql.Date)
                        pst.setDate(j + 1, (java.sql.Date) value);
                    else if (value instanceof java.sql.Timestamp)
                        pst.setTimestamp(j + 1, (java.sql.Timestamp) value);
                    else
                        pst.setObject(j + 1, value);
                } else
                    pst.setObject(j + 1, value);
            }
            pst.addBatch();
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = pst.executeBatch();
                if (!isInTransaction)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        int[] r = pst.executeBatch();
        if (!isInTransaction)
            conn.commit();
        for (int k = 0; k < r.length; k++)
            result[pointer++] = r[k];
        DbKit.closeQuietly(pst);
        return result;
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     * <pre>
     * Example:
     * String sql = "insert into user(name, cash) values(?, ?)";
     * int[] result = DbPro.use().batch("myConfig", sql, "name, cash", modelList, 500);
     * </pre>
     *
     * @param sql               The SQL to execute.
     * @param columns           the columns need be processed by sql.
     * @param modelOrRecordList model or record object list.
     * @param batchSize         batch size.
     * @return The number of rows updated per statement
     */
    public int[] batch(String sql, String columns, List modelOrRecordList, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sql, columns, modelOrRecordList, batchSize);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            if (autoCommit != null)
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            config.close(conn);
        }
    }

    private int[] batch(Config config, Connection conn, List<String> sqlList, int batchSize) throws SQLException {
        if (sqlList == null || sqlList.size() == 0)
            throw new IllegalArgumentException("The sqlList length must more than 0.");
        if (batchSize < 1)
            throw new IllegalArgumentException("The batchSize must more than 0.");

        boolean isInTransaction = config.isInTransaction();
        int counter = 0;
        int pointer = 0;
        int size = sqlList.size();
        int[] result = new int[size];
        Statement st = conn.createStatement();
        for (int i = 0; i < size; i++) {
            st.addBatch(sqlList.get(i));
            if (++counter >= batchSize) {
                counter = 0;
                int[] r = st.executeBatch();
                if (isInTransaction == false)
                    conn.commit();
                for (int k = 0; k < r.length; k++)
                    result[pointer++] = r[k];
            }
        }
        int[] r = st.executeBatch();
        if (isInTransaction == false)
            conn.commit();
        for (int k = 0; k < r.length; k++)
            result[pointer++] = r[k];
        DbKit.closeQuietly(st);
        return result;
    }

    /**
     * Execute a batch of SQL INSERT, UPDATE, or DELETE queries.
     * <pre>
     * Example:
     * int[] result = DbPro.use().batch("myConfig", sqlList, 500);
     * </pre>
     *
     * @param sqlList   The SQL list to execute.
     * @param batchSize batch size.
     * @return The number of rows updated per statement
     */
    public int[] batch(List<String> sqlList, int batchSize) {
        Connection conn = null;
        Boolean autoCommit = null;
        try {
            conn = config.getConnection();
            autoCommit = conn.getAutoCommit();
            conn.setAutoCommit(false);
            return batch(config, conn, sqlList, batchSize);
        } catch (Exception e) {
            throw new ActiveRecordException(e);
        } finally {
            if (autoCommit != null)
                try {
                    conn.setAutoCommit(autoCommit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            config.close(conn);
        }
    }

    /**
     * for DbKit.removeConfig(configName)
     */
    static void removeDbProWithConfig(String configName) {
        map.remove(configName);
    }

    public void adds(Table table, List<Map<String, Object>> records) throws SQLException {
        final StringBuilder sql = new StringBuilder();
        final List<List<Object>> params = new ArrayList<>();
        config.dialect.forDbSaves(sql, params, table, records);
        tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                batch(getConfig(), sql.toString(), paramsToArray(params), 100);
                return true;
            }
        });
    }

    public void adds(final String insertSQL, List<Map<String, Object>> records,
            final int batchSize) throws SQLException {
        final List<List<Object>> params = new ArrayList<>();
        config.dialect.forModelSaves(insertSQL, records);
        tx(new IAtom() {
            @Override
            public boolean run() throws SQLException {
                batch(getConfig(), insertSQL, paramsToArray(params), batchSize);
                return true;
            }
        });
    }

    protected Object[][] paramsToArray(List<List<Object>> params) {
        Object[][] array = new Object[params.size()][params.get(0).size()];
        int i = 0;
        for (List<Object> paras : params) {
            int j = 0;
            for (Object para : paras) {
                array[i][j++] = para;
            }
            i++;
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

    public Object add(Table table, Map<String, Object> record, boolean pk) {
        Object r = 0;
        Connection conn = null;
        PreparedStatement ps;
        ResultSet rs;
        try {
            conn = config.getConnection();
            List<Object> params = new ArrayList<>();
            StringBuilder sql = new StringBuilder();
            config.dialect.forModelSave(table, record, sql, params);
            if (pk) {
                ps = conn.prepareStatement(sql.toString(), Statement.RETURN_GENERATED_KEYS);
            } else {
                ps = conn.prepareStatement(sql.toString());
            }
            if (params.size() > 0) {
                for (int i = 0; i < params.size(); i++) {
                    ps.setObject(i + 1, params.get(i));
                }
            }
            r = ps.executeUpdate();
            if (pk) {
                rs = ps.getGeneratedKeys();
                if (rs.next()) {
                    r = rs.getLong(1);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            config.close(conn);
        }
        return r;
    }
}



