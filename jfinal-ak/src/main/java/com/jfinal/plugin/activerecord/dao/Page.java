package com.jfinal.plugin.activerecord.dao;

public class Page {
    private long totalNum;
    private int perSize;
    private int curNo;

    public Page(){}

    public Page(long totalNum, int perSize, int curNo){
        this.totalNum = totalNum;
        this.perSize = perSize;
        this.curNo = curNo;
    }

    public long getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(long totalNum) {
        this.totalNum = totalNum;
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

    public int getTotalPage(){
        return (int) getTotalNum() / getPerSize() + (getTotalNum() % getPerSize() > 0 ? 1 : 0);
    }
}
