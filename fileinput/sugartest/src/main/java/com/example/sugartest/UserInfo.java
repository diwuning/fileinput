package com.example.sugartest;

import com.orm.SugarRecord;

/**
 * Created by Administrator on 2017/10/20 0020.
 */

public class UserInfo extends SugarRecord {
    private int userId;
    private String userName;
    private String userImg;
    private String birthday;
    private float height;
    private float weight;
    private String[] tags;
    private String phone;
    private boolean faceId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserImg() {
        return userImg;
    }

    public void setUserImg(String userImg) {
        this.userImg = userImg;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isFaceId() {
        return faceId;
    }

    public void setFaceId(boolean faceId) {
        this.faceId = faceId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
