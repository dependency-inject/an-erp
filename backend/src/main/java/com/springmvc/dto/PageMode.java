package com.springmvc.dto;

import java.util.List;

public class PageMode<T> {

    private List<T> list;

    private T statistics;

    private long total;

    public PageMode(List<T> list, long total) {
        this.list = list;
        this.total = total;
    }

    public PageMode(List<T> list, T statistics, long total) {
        this.list = list;
        this.statistics = statistics;
        this.total = total;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public T getStatistics() {
        return statistics;
    }

    public void setStatistics(T statistics) {
        this.statistics = statistics;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
