package com.jfinal.plugin.spring.jdbc;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CaseInsensitiveMap extends HashMap {

    private static final long serialVersionUID = 6843981594457576677L;
    private boolean toLowerCase;

    public CaseInsensitiveMap(boolean toLowerCase) {
        this.toLowerCase = toLowerCase;
    }

    public Object get(String key) {
        return super.get(convertCase(key));
    }

    public boolean containsKey(String key) {
        return super.containsKey(convertCase(key));
    }

    public Object put(String key, Object value) {
        return super.put(convertCase(key), value);
    }

    public void putAll(Map m) {
        for (Map.Entry<String, Object> e : (Set<Entry>) (m.entrySet()))
            super.put(convertCase(e.getKey()), e.getValue());
    }

    public Object remove(String key) {
        return super.remove(convertCase(key));
    }

    private Object convertCase(String key) {
        return toLowerCase ? key.toLowerCase() : key.toUpperCase();
    }
}