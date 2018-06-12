package com.springmvc.dto;

import java.util.List;

public class StatisticsMode {

    private List<StatisticsData> preWeek;

    private List<StatisticsData> history;

    public StatisticsMode(List<StatisticsData> preWeek, List<StatisticsData> history) {
        this.preWeek = preWeek;
        this.history = history;
    }

    public List<StatisticsData> getPreWeek() {
        return preWeek;
    }

    public void setPreWeek(List<StatisticsData> preWeek) {
        this.preWeek = preWeek;
    }

    public List<StatisticsData> getHistory() {
        return history;
    }

    public void setHistory(List<StatisticsData> history) {
        this.history = history;
    }
}
