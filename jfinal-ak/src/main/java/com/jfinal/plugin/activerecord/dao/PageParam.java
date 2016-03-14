package com.jfinal.plugin.activerecord.dao;

import com.jfinal.kit.StrKit;

import java.util.HashMap;
import java.util.Map;

public class PageParam {
    public static final String ASC = "asc";
    public static final String DESC = "desc";

    private int perSize;
    private int curNo;
    private String key;
    private Object val;
    private String alias;
    private int dic;
    private Map<String, String> orderMap = new HashMap<String, String>();

    public PageParam(int perSize, int curNo){
        this.perSize = perSize;
        this.curNo = curNo;
        orderMap.clear();
    }

    public PageParam(int perSize, int curNo, String key, Object val){
        this.perSize = perSize;
        this.curNo = curNo;
        this.key = key;
        this.val = val;
        orderMap.clear();
    }

    public PageParam(int perSize, int curNo, String key, Object val, String alias){
        this.perSize = perSize;
        this.curNo = curNo;
        this.key = key;
        this.val = val;
        this.alias = alias;
        orderMap.clear();
    }

    public void putSort(String field, String sort){
        orderMap.put(field, sort);
    }

    public void setSort(String sorts) {
        if (StrKit.notBlank(sorts)) {
            String[] sortArr = sorts.split(" ");
            for (String sort : sortArr) {
                if (sort.contains("_")) {
                    String[] kv = sort.split("_");
                    if (StrKit.notBlank(kv[1]))
                    orderMap.put(kv[0], kv[1]);
                }
            }
        }
    }

    public String getSortValue() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : orderMap.entrySet()) {
            if (StrKit.notBlank(entry.getValue())) {
                sb.append(entry.getKey()).append("_").append(entry.getValue()).append(" ");
            }
        }
        if (orderMap.size() > 0) {
            sb = sb.deleteCharAt(sb.length() - 1);
        }
        return sb.toString();
    }

    public void replaceSortField(String key, String field) {
        String sort = orderMap.get(key);
        if (sort != null) {
            orderMap.put(field, sort);
            orderMap.remove(key);
        }
    }

    public String buildSQL(){
        StringBuilder sql = new StringBuilder(" ");
        if(orderMap.size() > 0){
            sql.append(" order by ");
            String prefix = alias != null ? alias + "." : "";
            for (Map.Entry<String, String> entry : orderMap.entrySet()){
                sql.append(prefix).append(entry.getKey()).append(" ").append(entry.getValue()).append(",");
            }
            sql.deleteCharAt(sql.length() - 1);
        }
        sql.append(" limit ").append(perSize * (curNo - 1)).append(", ").append(perSize);
        return sql.toString();
    }

    public int getPerSize() {
        return perSize;
    }

    public void setPerSize(int perSize) {
        this.perSize = perSize;
    }

    public int getCurNo() {
        return curNo;
    }

    public void setCurNo(int curNo) {
        this.curNo = curNo;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public Object getVal() {
        return val;
    }

    public void setVal(Object val) {
        this.val = val;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public int getDic() {
        return dic;
    }

    public void setDic(int dic) {
        this.dic = dic;
    }

    public Map<String, String> getSortMap() {
        return this.orderMap;
    }
}
