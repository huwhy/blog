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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Cross Package Invoking pattern for package activerecord.
 */
public abstract class CPI {

    public static <T> List<T> query(Connection conn, String sql, Object... paras) throws SQLException {
        return Db.query(DbKit.config, conn, sql, paras);
    }

    public static <T> List<T> query(String configName, Connection conn, String sql, Object... paras) throws SQLException {
        return Db.query(DbKit.getConfig(configName), conn, sql, paras);
    }

    /**
     * Return the columns map of the record
     * @return the columns map of the record
    public static final Map<String, Object> getColumns(Record record) {
    return record.getColumns();
    } */

    public static List<Map<String, Object>> find(Connection conn, String sql, Object... paras) throws SQLException {
        return Db.find(DbKit.config, conn, sql, paras);
    }

    public static List<Map<String, Object>> find(String configName, Connection conn, String sql, Object... paras) throws SQLException {
        return Db.find(DbKit.getConfig(configName), conn, sql, paras);
    }

    public static int update(Connection conn, String sql, Object... paras) throws SQLException {
        return Db.update(DbKit.config, conn, sql, paras);
    }

    public static int update(String configName, Connection conn, String sql, Object... paras) throws SQLException {
        return Db.update(DbKit.getConfig(configName), conn, sql, paras);
    }
}
