package com.h.fileinput.Glin.bean;

/**
 * Created by wangchm on 2016/9/13 0013.
 * 音乐评分
 */
public class RatingMusic {
    private int max;
    private String average;//评分
    private int numRaters;//评星数
    private int min;//最低评分

    public RatingMusic(){}
    public RatingMusic(int max, String average, int numRaters, int min) {
        this.setMax(max);
        this.setAverage(average);
        this.setNumRaters(numRaters);
        this.setMin(min);
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public String getAverage() {
        return average;
    }

    public void setAverage(String average) {
        this.average = average;
    }

    public int getNumRaters() {
        return numRaters;
    }

    public void setNumRaters(int numRaters) {
        this.numRaters = numRaters;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
