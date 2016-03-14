package com.jfinal.plugin.activerecord.util;

import com.jfinal.annotation.EnumValType;
import com.jfinal.annotation.EnumValue;

import java.util.HashMap;
import java.util.Map;

public class EnumUtils {

    private static final Map<Class, Map<String, Enum>> nameEnums = new HashMap<Class, Map<String, Enum>>();
    private static final Map<Class, Map<Integer, Enum>> ordinalEnums = new HashMap<Class, Map<Integer, Enum>>();

    public static <T extends  Enum> T getEnum(Class<T> e, EnumValType type, Object val) {
        T t;
        if (type == EnumValType.Name) {
            t = getEnum(e, val.toString());
        } else if (type == EnumValType.Manual){
            t = getEnumByValue(e, val);
        } else {
            t = EnumUtils.getEnum(e, Integer.parseInt(val.toString()));
        }
        return t;
    }

    public static <T extends  Enum> Object getVal(T t, EnumValType type) {
        Object val;
        if (type == EnumValType.Name) {
            val = t.name();
        } else if (type == EnumValType.Manual){
            val = ((EnumValue) t).getVal();
        } else {
            val = t.ordinal();
        }
        return val;
    }

    private static  <T extends Enum> T getEnum(Class<T> clazz, String name) {
        if (!clazz.isEnum()) {
            throw new RuntimeException(clazz.getName() + "is not enum");
        }
        Map<String, Enum> enumMap = nameEnums.get(clazz);
        if (enumMap == null) {
            enumMap = new HashMap<String, Enum>();
            for (T t : clazz.getEnumConstants()) {
                enumMap.put(t.name(), t);
            }
            nameEnums.put(clazz, enumMap);
        }
        return (T) enumMap.get(name);
    }

    private static  <T extends Enum> T getEnum(Class<T> clazz, int ordinal) {
        if (!clazz.isEnum()) {
            throw new RuntimeException(clazz.getName() + "is not enum");
        }
        Map<Integer, Enum> enumMap = ordinalEnums.get(clazz);
        if (enumMap == null) {
            enumMap = new HashMap<>();
            for (T t : clazz.getEnumConstants()) {
                enumMap.put(t.ordinal(), t);
            }
            ordinalEnums.put(clazz, enumMap);
        }
        return (T) enumMap.get(ordinal);
    }

    private static  <T extends Enum> T getEnumByValue(Class<T> clazz, Object value) {
        if (!clazz.isEnum()) {
            throw new RuntimeException(clazz.getName() + "is not enum");
        }
        Map<Integer, Enum> enumMap = ordinalEnums.get(clazz);
        if (enumMap == null) {
            enumMap = new HashMap<>();
            for (T t : clazz.getEnumConstants()) {
                enumMap.put(t.ordinal(), t);
            }
            ordinalEnums.put(clazz, enumMap);
        }
        EnumValue ev = (EnumValue) enumMap.get(0);
        return (T) ev.valOf(value);
    }
}
