package com.jfinal.plugin.spring.util;

import com.jfinal.annotation.EnumVal;
import com.jfinal.annotation.Transient;
import com.jfinal.plugin.spring.jdbc.PageList;
import com.jfinal.plugin.spring.jdbc.RecordBuilder;
import com.jfinal.plugin.spring.jdbc.Table;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

public class RecordUtil {


    public static <T> T toC(Map<String, Object> record, Class<T> clazz, String... ignoreProperties) {
        return toT(record, clazz, ignoreProperties);
    }

    public static <T> List<T> toCList(Collection<Map<String, Object>> records, Class<T> clazz,
            String... ignoreProperties) {
        List<T> list = new ArrayList<>(records.size());
        if (records.size() > 0) {
            list = new ArrayList<>(records.size());
            for (Map<String, Object> record : records) {
                list.add(toC(record, clazz, ignoreProperties));
            }
        }
        return list;
    }

    public static <T> List<Map<String, Object>> toRList(Collection<T> list, String... ignoreProperties) {
        List<Map<String, Object>> records = new ArrayList<>(list.size());
        for (T t : list) {
            records.add(toR(t, ignoreProperties));
        }
        return records;
    }

    public static <T> Map<String, Object> toR(T t, String... ignoreProperties) {
        return toRecord(t, ignoreProperties);
    }

    private static <T> Map<String, Object> toRecord(T c, String... ignoreProperties) {
        if (c == null) {
            return null;
        }
        Map<String, Object> record = new HashMap<String, Object>();
        Set<String> ignoreSet = new HashSet<>(Arrays.asList(ignoreProperties));
        ignoreSet.add("class");
        try {
            Object val;
            String name;
            for (PropertyDescriptor pd : BeanUtils.getProperties(c.getClass())) {
                name = pd.getName();
                if (ignoreSet.contains(name))
                    continue;
                Method readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    Transient an = readMethod.getAnnotation(Transient.class);
                    if (an != null) {
                        continue;
                    }
                    Field f;
                    try {
                        f = c.getClass().getDeclaredField(name);
                    } catch (NoSuchFieldException e) {
                        f = c.getClass().getSuperclass().getDeclaredField(name);
                    }
                    an = f.getAnnotation(Transient.class);
                    if (an != null) {
                        continue;
                    }
                    val = readMethod.invoke(c);
                    if (val != null) {
                        record.put(name, val);
                    }
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return record;
    }

    public static <T> PageList<T> toPageList(PageList<Map<String, Object>> pageList, Class<T> clazz,
            String... ignoreProperties) {
        return new PageList<>(toCList(pageList.getData(), clazz, ignoreProperties), pageList.getPage());
    }

    private static <T> T toT(Map<String, Object> record, Class<T> beanClass, String... ignoreProperties) {
        T t = null;
        if (record != null) {
            Set<String> ignoreSet = new HashSet<>(Arrays.asList(ignoreProperties));
            ignoreSet.add("class");
            Object val = null;
            String name = null;
            Method writeMethod = null;
            try {
                t = beanClass.newInstance();
                PropertyDescriptor[] pds = BeanUtils.getProperties(beanClass);
                for (PropertyDescriptor pd : pds) {
                    name = pd.getName();
                    if (ignoreSet.contains(name))
                        continue;
                    writeMethod = pd.getWriteMethod();
                    if (writeMethod != null) {
                        Transient an = writeMethod.getAnnotation(Transient.class);
                        if (an != null) {
                            continue;
                        }
                        Field f;
                        try {
                            f = beanClass.getDeclaredField(name);
                        } catch (NoSuchFieldException e) {
                            f = beanClass.getSuperclass().getDeclaredField(name);
                        }
                        if (f == null) {
                            continue;
                        }
                        an = f.getAnnotation(Transient.class);
                        if (an != null) {
                            continue;
                        }
                        val = record.get(name);
                        if (val != null) {
                            if (pd.getPropertyType().isEnum()) {
                                Class<Enum> e = (Class<Enum>) pd.getPropertyType();
                                EnumVal ev = f.getAnnotation(EnumVal.class);
                                if (ev != null && val != null) {
                                    val = EnumUtils.getEnum(e, ev.value(), val);
                                }
                            }
                            writeMethod.invoke(t, val);
                        }
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
                throw new RuntimeException(
                        "method:" + writeMethod + ", val:" + (val == null ? "" : val.getClass()) + "-" + val, e);
            } catch (Exception e) {
                throw new RuntimeException(
                        "method:" + writeMethod + ", val:" + (val == null ? "" : val.getClass()) + "-" + val, e);
            }
        }
        return t;
    }


    // table info
    public static <T> T toC(Map<String, Object> record, Table<T> info, String... ignoreProperties) {
        return toT(record, info, ignoreProperties);
    }

    public static <T> List<T> toCList(Collection<Map<String, Object>> records, Table<T> info,
            String... ignoreProperties) {
        List<T> list = new ArrayList<>(records.size());
        if (records.size() > 0) {
            list = new ArrayList<>(records.size());
            for (Map<String, Object> record : records) {
                list.add(toC(record, info, ignoreProperties));
            }
        }
        return list;
    }

    public static <T> List<Map<String, Object>> toRList(Collection<T> list, Table<T> info, String... ignoreProperties) {
        List<Map<String, Object>> records = new ArrayList<>(list.size());
        for (T t : list) {
            records.add(toR(t, info, ignoreProperties));
        }
        return records;
    }

    public static <T> Map<String, Object> toR(T c, Table<T> info, String... ignoreProperties) {
        return toRecord(c, info, ignoreProperties);
    }

    private static <T> Map<String, Object> toRecord(T c, Table<T> info, String... ignoreProperties) {
        if (c == null || info == null) {
            return null;
        }
        Map<String, Object> record = new HashMap<>();
        Set<String> ignoreSet = new HashSet<>(Arrays.asList(ignoreProperties));
        ignoreSet.add("class");
        try {
            Object val;
            String name;
            for (PropertyDescriptor pd : BeanUtils.getProperties(c.getClass())) {
                name = pd.getName();
                if (ignoreSet.contains(name))
                    continue;
                Method readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    Transient an = readMethod.getAnnotation(Transient.class);
                    if (an != null) {
                        continue;
                    }
                    Field f;
                    try {
                        f = c.getClass().getDeclaredField(name);
                    } catch (NoSuchFieldException e) {
                        f = c.getClass().getSuperclass().getDeclaredField(name);
                    }
                    an = f.getAnnotation(Transient.class);
                    if (an != null) {
                        continue;
                    }
                    val = readMethod.invoke(c);
                    if (val == null) {
                        continue;
                    }
                    String column = info.column(name);
                    if (column != null) {
                        EnumVal ev = f.getAnnotation(EnumVal.class);
                        if (ev != null) {
                            Enum enu = (Enum) val;
                            val = EnumUtils.getVal(enu, ev.value());
                        }
                        record.put(column, val);
                    }
                }
            }
        } catch (InvocationTargetException | IllegalAccessException | NoSuchFieldException e) {
            e.printStackTrace();
        }
        return record;
    }

    public static <T> PageList<T> toPageList(PageList<Map<String, Object>> pageList, Table<T> info) {
        return new PageList<>(toCList(pageList.getData(), info), pageList.getPage());
    }

    private static <T> T toT(Map<String, Object> record, Table<T> info, String... ignoreProperties) {
        T t = null;
        if (record != null && info != null) {
            Class<T> beanClass = info.getBeanClass();
            Set<String> ignoreSet = new HashSet<>(Arrays.asList(ignoreProperties));
            ignoreSet.add("class");
            String name = null;
            Object val = null;
            Method writeMethod = null;
            try {
                t = beanClass.newInstance();
                PropertyDescriptor[] pds = BeanUtils.getProperties(beanClass);
                for (PropertyDescriptor pd : pds) {
                    name = pd.getName();
                    if (ignoreSet.contains(name))
                        continue;
                    writeMethod = pd.getWriteMethod();
                    if (writeMethod != null) {
                        Transient an = writeMethod.getAnnotation(Transient.class);
                        if (an != null) {
                            continue;
                        }
                        Field f;
                        try {
                            f = beanClass.getDeclaredField(name);
                        } catch (NoSuchFieldException e) {
                            f = beanClass.getSuperclass().getDeclaredField(name);
                        }
                        an = f.getAnnotation(Transient.class);
                        if (an != null) {
                            continue;
                        }
                        String col = info.column(name);
                        if (col == null) {
                            val = record.get(name);
                        } else {
                            val = record.get(col);
                        }
                        if (val == null) {
                            continue;
                        }
                        Class<Enum> e = (Class<Enum>) pd.getPropertyType();
                        EnumVal ev = f.getAnnotation(EnumVal.class);
                        if (ev != null) {
                            val = EnumUtils.getEnum(e, ev.value(), val);
                        }
                        if (val != null) {
                            writeMethod.invoke(t, val);
                            val = null;
                        }
                    }
                }
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
                throw new RuntimeException(
                        "method:" + writeMethod + ", val:" + (val == null ? "" : val.getClass()) + "-" + val, e);
            } catch (Exception e) {
                throw new RuntimeException(
                        "method:" + writeMethod + ", val:" + (val == null ? "" : val.getClass()) + "-" + val, e);
            }
        }
        return t;
    }

    public static <T> List<T> po(ResultSet rs, Table<T> table, String... ignoreProperties) throws SQLException {
        List<T> result = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        int columnCount = rsmd.getColumnCount();
        String[] labelNames = new String[columnCount + 1];
        int[] types = new int[columnCount + 1];
        buildLabelNamesAndTypes(rsmd, labelNames, types);
        Class<T> beanClass = table.getBeanClass();
        Set<String> ignoreSet = new HashSet<>(Arrays.asList(ignoreProperties));
        ignoreSet.add("class");
        Map<String, PropertyDescriptor> pds = BeanUtils.getPropertyMap(beanClass);
        String fieldName = null;
        Object value = null;
        Method writeMethod = null;
        while (rs.next()) {
            try {
                T o = beanClass.newInstance();
                for (int i = 1; i <= columnCount; i++) {
                    fieldName = table.name(labelNames[i]);
                    value = getColumnValue(rs, types[i], i);
                    if (value == null || ignoreSet.contains(fieldName))
                        continue;
                    PropertyDescriptor pd = pds.get(fieldName);
                    if (pd == null)
                        continue;
                    writeMethod = pd.getWriteMethod();
                    if (writeMethod != null) {
                        Transient an = writeMethod.getAnnotation(Transient.class);
                        if (an != null) {
                            continue;
                        }
                        Field f;
                        try {
                            f = beanClass.getDeclaredField(fieldName);
                        } catch (NoSuchFieldException e) {
                            f = beanClass.getSuperclass().getDeclaredField(fieldName);
                        }
                        an = f.getAnnotation(Transient.class);
                        if (an != null) {
                            continue;
                        }
                        Class<Enum> e = (Class<Enum>) pd.getPropertyType();
                        EnumVal ev = f.getAnnotation(EnumVal.class);
                        if (ev != null) {
                            value = EnumUtils.getEnum(e, ev.value(), value);
                        }
                        if (value != null) {
                            writeMethod.invoke(o, value);
                            value = null;
                        }
                    }
                    result.add(o);
                }
            } catch (InvocationTargetException | IllegalAccessException | InstantiationException | NoSuchFieldException e) {
                throw new RuntimeException(
                        "method:" + writeMethod + ", val:" + (value == null ? "" : value.getClass()) + "-" + value, e);
            } catch (Exception e) {
                throw new RuntimeException(
                        "method:" + writeMethod + ", val:" + (value == null ? "" : value.getClass()) + "-" + value, e);
            }
        }
        return result;
    }

    private static Object getColumnValue(ResultSet rs, int type, int i) throws SQLException {
        Object value;
        if (type < Types.BLOB)
            value = rs.getObject(i);
        else if (type == Types.CLOB)
            value = RecordBuilder.handleClob(rs.getClob(i));
        else if (type == Types.NCLOB)
            value = RecordBuilder.handleClob(rs.getNClob(i));
        else if (type == Types.BLOB)
            value = RecordBuilder.handleBlob(rs.getBlob(i));
        else
            value = rs.getObject(i);
        return value;
    }

    private static final void buildLabelNamesAndTypes(ResultSetMetaData rsmd, String[] labelNames,
            int[] types) throws SQLException {
        for (int i = 1; i < labelNames.length; i++) {
            labelNames[i] = rsmd.getColumnLabel(i);
            types[i] = rsmd.getColumnType(i);
        }
    }
}
