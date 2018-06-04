package com.h.fileinput.Glin.bean;

/**
 * Created by Administrator on 2016/9/6 0006.
 */
public class AvatarsBody {
    private String small;
    private String large;
    private String medium;

    public AvatarsBody(){}
    public AvatarsBody(String small, String large, String medium) {
        this.setSmall(small);
        this.setLarge(large);
        this.setMedium(medium);
    }

    public String getSmall() {
        return small;
    }

    public void setSmall(String small) {
        this.small = small;
    }

    public String getLarge() {
        return large;
    }

    public void setLarge(String large) {
        this.large = large;
    }

    public String getMedium() {
        return medium;
    }

    public void setMedium(String medium) {
        this.medium = medium;
    }

    public String toString() {
        return "AvatarsBody{" +
                "small='" + small + '\'' +
                ", large='" + large + '\'' +
                ", medium='" + medium + '\'' +
                '}';
    }
}
