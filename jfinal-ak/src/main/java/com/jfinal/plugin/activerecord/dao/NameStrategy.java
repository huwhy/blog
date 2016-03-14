package com.jfinal.plugin.activerecord.dao;

public class NameStrategy implements ColumnNameStrategy {

    @Override
    public String name(String column) {
        StringBuilder name = new StringBuilder();
        char[] chars = column.toCharArray();
        for(int i = 0; i < chars.length; i++){
            if(chars[i] == '_'){
                name.append(Character.toUpperCase(chars[i + 1]));
                i++;
            } else {
                name.append(chars[i]);
            }
        }
        return name.toString();
    }

    @Override
    public String column(String name) {
        StringBuilder column = new StringBuilder();
        for(char c : name.toCharArray()){
            if(Character.isUpperCase(c)){
                column.append("_").append(Character.toLowerCase(c));
            } else {
                column.append(c);
            }
        }
        return column.toString();
    }
}
