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

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * RecordBuilder.
 */
public class RecordBuilder {

    public static List<Map<String, Object>> build(ResultSet rs) throws SQLException {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        buildLabelNamesAndTypes(rsmd, labelNames, types);
        while (rs.next()) {
            Map<String, Object> record = new HashMap<>();
            for (int i = 1; i <= columnCount; i++) {
                Object value;
                if (types[i] < Types.BLOB)
                    value = rs.getObject(i);
                else if (types[i] == Types.CLOB)
                    value = handleClob(rs.getClob(i));
                else if (types[i] == Types.NCLOB)
                    value = handleClob(rs.getNClob(i));
                else if (types[i] == Types.BLOB)
                    value = handleBlob(rs.getBlob(i));
                else
                    value = rs.getObject(i);

                record.put(labelNames[i], value);
            }
            result.add(record);
        }
        return result;
    }

    private static final void buildLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames,
            int[] types) throws SQLException {
        for (int i = 1; i < labelNames.length; i++) {
            labelNames[i] = rsmd.getColumnLabel(i);
            types[i] = rsmd.getColumnType(i);
        }
    }


    public static byte[] handleBlob(Blob blob) throws SQLException {
        if (blob == null)
            return null;

        InputStream is = null;
        try {
            is = blob.getBinaryStream();
            byte[] data = new byte[(int) blob.length()];        // byte[] data = new byte[is.available()];
            is.read(data);
            is.close();
            return data;
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static String handleClob(Clob clob) throws SQLException {
        if (clob == null)
            return null;

        Reader reader = null;
        try {
            reader = clob.getCharacterStream();
            char[] buffer = new char[(int) clob.length()];
            reader.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

}





