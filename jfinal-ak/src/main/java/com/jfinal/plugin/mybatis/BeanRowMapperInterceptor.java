package com.jfinal.plugin.mybatis;

import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.ibatis.executor.resultset.ResultSetHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.NotWritablePropertyException;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.dao.DataRetrievalFailureException;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

import com.jfinal.annotation.Ignore;

@Intercepts({
        @Signature(method = "handleResultSets", type = ResultSetHandler.class, args = {
                Statement.class})})
public class BeanRowMapperInterceptor implements Interceptor {

    private static Map<Class, Map<String, PropertyDescriptor>> cacheFields     = new HashMap<>();
    private static Map<Class, Set<String>>                     cacheProperties = new HashMap<>();

    public Object intercept(Invocation invocation) throws Throwable {
        Statement st = (Statement) invocation.getArgs()[0];
        ResultSet rs = st.getResultSet();
        MappedStatement mappedStatement = (MappedStatement) ReflectUtil.getFieldValue(invocation.getTarget(), "mappedStatement");

        return queryList(invocation, rs, mappedStatement);
    }

    private Object queryList(Invocation invocation, ResultSet rs, MappedStatement mappedStatement) throws InvocationTargetException, IllegalAccessException, SQLException {
        Object result;
        Class mappedClass = mappedStatement.getResultMaps().get(0).getType();
        if (mappedClass.getPackage().getName().equals("java.lang")
                || mappedClass.getPackage().getName().equals("java.util")) {
            result = invocation.proceed();
        } else {
            result = mapRow(rs, mappedClass);
        }
        return result;
    }

    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    public void setProperties(Properties properties) {
    }

    public List<Object> mapRow(ResultSet rs, Class mappedClass) throws SQLException {
        Assert.state(mappedClass != null, "Mapped class was not specified");
        List<Object> list = new ArrayList<>();
        ResultSetMetaData rsmd = rs.getMetaData();
        Map<String, PropertyDescriptor> mappedFields = getMappedFields(mappedClass);
        int columnCount = rsmd.getColumnCount();
        while (rs.next()) {
            Object mappedObject = BeanUtils.instantiate(mappedClass);
            BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(mappedObject);
            for (int index = 1; index <= columnCount; index++) {
                String column = JdbcUtils.lookupColumnName(rsmd, index);
                PropertyDescriptor pd = mappedFields.get(lowerCaseName(column.replaceAll(" ", "")));
                if (pd != null) {
                    Ignore ignore = pd.getWriteMethod().getAnnotation(Ignore.class);
                    if (ignore != null) {
                        continue;
                    }
                    try {
                        Object value = getColumnValue(rs, index, pd);
                        if (EnumValue.class.isAssignableFrom(pd.getPropertyType())) {
                            Object[] enumConstants = pd.getPropertyType().getEnumConstants();
                            EnumValue ev = null;
                            for (Object v : enumConstants) {
                                EnumValue v1 = (EnumValue) v;
                                if (v1.getValue().equals(value)) {
                                    ev = v1;
                                }
                            }
                            if (ev == null) {
                                throw new RuntimeException(pd.getPropertyType().getName() + " enum value 不存在:" + value);
                            }
                            value = ev;
                        }
                        bw.setPropertyValue(pd.getName(), value);
                    } catch (NotWritablePropertyException ex) {
                        throw new DataRetrievalFailureException(
                                "Unable to map column " + column + " to property " + pd.getName(), ex);
                    }
                }
            }
            list.add(mappedObject);
        }
        return list;
    }

    protected String lowerCaseName(String name) {
        return name.toLowerCase(Locale.US);
    }

    protected Object getColumnValue(ResultSet rs, int index, PropertyDescriptor pd) throws SQLException {
        return JdbcUtils.getResultSetValue(rs, index, pd.getPropertyType());
    }

    private Map<String, PropertyDescriptor> getMappedFields(Class mappedClass) {
        Map<String, PropertyDescriptor> map = BeanRowMapperInterceptor.cacheFields.get(mappedClass);
        if (map == null) {
            synchronized (mappedClass) {
                map = BeanRowMapperInterceptor.cacheFields.get(mappedClass);
                if (map == null) {
                    initialize(mappedClass);
                    map = BeanRowMapperInterceptor.cacheFields.get(mappedClass);
                }
            }
        }
        return map;
    }

    protected void initialize(Class mappedClass) {
        Map<String, PropertyDescriptor> mappedFields = new HashMap<>();
        Set<String> mappedProperties = new HashSet<>();
        PropertyDescriptor[] pds = BeanUtils.getPropertyDescriptors(mappedClass);
        for (PropertyDescriptor pd : pds) {
            if (pd.getWriteMethod() != null) {
                mappedFields.put(lowerCaseName(pd.getName()), pd);
                String underscoredName = underscoreName(pd.getName());
                if (!lowerCaseName(pd.getName()).equals(underscoredName)) {
                    mappedFields.put(underscoredName, pd);
                }
                mappedProperties.add(pd.getName());
            }
        }
        cacheFields.put(mappedClass, mappedFields);
        cacheProperties.put(mappedClass, mappedProperties);
    }

    protected String underscoreName(String name) {
        if (!StringUtils.hasLength(name)) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(lowerCaseName(name.substring(0, 1)));
        for (int i = 1; i < name.length(); i++) {
            String s = name.substring(i, i + 1);
            String slc = lowerCaseName(s);
            if (!s.equals(slc)) {
                result.append("_").append(slc);
            } else {
                result.append(s);
            }
        }
        return result.toString();
    }

}