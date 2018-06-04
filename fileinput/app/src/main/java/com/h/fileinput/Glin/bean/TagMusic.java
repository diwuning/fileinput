package com.h.fileinput.Glin.bean;

/**
 * Created by wangchm on 2016/9/13 0013.
 * 歌曲标签
 */
public class TagMusic {
    private String count;
    private String name;

    public TagMusic(){}
    public TagMusic(String count, String name) {
        this.setCount(count);
        this.setName(name);
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
