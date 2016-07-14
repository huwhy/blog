package cn.huwhy.katyusha.common;

import java.io.Serializable;
import java.util.List;

import com.jfinal.plugin.mybatis.Term;

public class Paging<T> implements Serializable {

    public static Paging empty = new Paging<>(0, 10, 1);
    /**
     * 总条数
     */
    private long totalNum;
    /**
     * 每页条数 默认10
     */
    private long pageSize = 10;
    /**
     * 当前页
     */
    private long pageNum;

    /**
     * 数据
     */
    private List<T> data;

    public Paging() {
    }

    public Paging(Term term, List<T> data) {
        this.totalNum = term.getTotalNum();
        this.pageSize = term.getPageSize();
        this.pageNum = term.getPageNum();
        this.data = data;
    }

    public Paging(long totalNum, long pageSize, long pageNum) {
        this.totalNum = totalNum;
        this.pageSize = pageSize;
        this.pageNum = pageNum;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
    }

    public long getPageSize() {
        return pageSize;
    }

    public void setPageSize(long pageSize) {
        this.pageSize = pageSize;
    }

    public long getPageNum() {
        return pageNum;
    }

    public void setPageNum(long pageNum) {
        this.pageNum = pageNum;
    }

    public long getTotalPage() {
        return this.totalNum / this.pageSize + (this.totalNum % this.pageSize > 0 ? 1 : 0);
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }
}