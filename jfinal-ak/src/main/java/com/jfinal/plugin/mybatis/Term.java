package com.jfinal.plugin.mybatis;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public abstract class Term implements Serializable {
    /**
     * 分页数
     */
    private Long pageSize = 15L;
    /**
     * 查询页
     */
    private Long pageNum = 1L;
    /**
     * 总记录数
     */
    private Long totalNum = 0L;

    /**
     * 排序
     */
    private Map<String, Sort> sorts;

    public Long getPageSize() {
        return pageSize;
    }

    public void setPageSize(Long pageSize) {
        this.pageSize = pageSize;
    }

    public Long getPageNum() {
        return pageNum;
    }

    public void setPageNum(Long pageNum) {
        this.pageNum = pageNum;
    }

    public Long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(Long totalNum) {
        this.totalNum = totalNum;
    }

    public Long getStart() {
        return (this.pageNum - 1) * this.pageSize;
    }

    public Map<String, Sort> getSorts() {
        return sorts;
    }

    public void setSorts(Map<String, Sort> sorts) {
        this.sorts = sorts;
    }

    public void addSort(String field, Sort sort) {
        if (this.sorts == null) {
            this.sorts = new HashMap<>();
        }
        this.sorts.put(field, sort);
    }

    public enum Sort {
        ASC,
        DESC;

        public String getValue() {
            return name();
        }
    }
}
