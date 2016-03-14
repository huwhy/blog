package com.jfinal.plugin.activerecord.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public abstract class BeanUtils {

    public static <T> PropertyDescriptor[] getProperties(Class<T> beanClass) {
        try {
            return CachedIntrospectionResults.forClass(beanClass).getPropertyDescriptors();
        } catch (IntrospectionException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> Map<String, PropertyDescriptor> getPropertyMap(Class<T> beanClass) {
        Map<String, PropertyDescriptor> map = new HashMap<>();
        for (PropertyDescriptor pd : getProperties(beanClass)) {
            map.put(pd.getName(), pd);
        }
        return map;
    }

    public static <T> T copyProperties(Object source, Class<T> clazz, String... ignoreProperties) {
        T t = null;
        try {
            t = clazz.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        copyProperties(source, t, ignoreProperties);
        return t;
    }

    public static void copyProperties(Object source, Object target, String... ignoreProperties) {
        if (source == null || target == null) return;
        try {
            Set<String> ignoreSet = new HashSet<String>(Arrays.asList(ignoreProperties));
            ignoreSet.add("class");
            PropertyDescriptor[] sourcePds = CachedIntrospectionResults.forClass(source.getClass()).getPropertyDescriptors();
            CachedIntrospectionResults targetResults = CachedIntrospectionResults.forClass(target.getClass());
            Method readMethod;
            Method writeMethod;
            Object val;
            String name;
            for (PropertyDescriptor pd : sourcePds) {
                name = pd.getName();
                if (ignoreSet.contains(name)) continue;
                readMethod = pd.getReadMethod();
                if (readMethod != null) {
                    val = readMethod.invoke(source);
                    if (val != null) {
                        writeMethod = targetResults.getPropertyDescriptor(name).getWriteMethod();
                        if (writeMethod != null) {
                            writeMethod.invoke(target, val);
                        }
                    }
                }
            }
        } catch (IntrospectionException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}

class CachedIntrospectionResults {
    static final Map<Class<?>, Object> classCache = new WeakHashMap<Class<?>, Object>();
    private final BeanInfo beanInfo;
    private final Map<String, PropertyDescriptor> propertyDescriptorCache;

    private CachedIntrospectionResults(Class<?> beanClass) throws IntrospectionException {
        try {
            BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
            this.beanInfo = beanInfo;
            this.propertyDescriptorCache = new LinkedHashMap<String, PropertyDescriptor>();

            // This call is slow so we do it once.
            PropertyDescriptor[] pds = this.beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd : pds) {
                if (Class.class.equals(beanClass) &&
                        ("classLoader".equals(pd.getName()) || "protectionDomain".equals(pd.getName()))) {
                    continue;
                }
                this.propertyDescriptorCache.put(pd.getName(), pd);
            }
        } catch (IntrospectionException ex) {
            throw new RuntimeException("Failed to obtain BeanInfo for class [" + beanClass.getName() + "]", ex);
        }
    }

    static CachedIntrospectionResults forClass(Class<?> beanClass) throws IntrospectionException {
        CachedIntrospectionResults results;
        Object value;
        synchronized (classCache) {
            value = classCache.get(beanClass);
        }
        if (value instanceof Reference) {
            Reference<CachedIntrospectionResults> ref = (Reference<CachedIntrospectionResults>) value;
            results = ref.get();
        } else {
            results = (CachedIntrospectionResults) value;
        }
        if (results == null) {
            results = new CachedIntrospectionResults(beanClass);
            synchronized (classCache) {
                classCache.put(beanClass, new WeakReference<CachedIntrospectionResults>(results));
            }
        }
        return results;
    }

    public PropertyDescriptor getPropertyDescriptor(String name) {
        return this.propertyDescriptorCache.get(name);
    }

    public PropertyDescriptor[] getPropertyDescriptors() {
        PropertyDescriptor[] pds = new PropertyDescriptor[this.propertyDescriptorCache.size()];
        int i = 0;
        for (PropertyDescriptor pd : this.propertyDescriptorCache.values()) {
            pds[i] = pd;
            i++;
        }
        return pds;
    }
}
