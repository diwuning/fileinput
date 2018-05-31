package com.h.fileinput.FragmentPager;

import java.io.Serializable;

/**
 * Created by YangFy on 2017/5/19.
 */

public class TabCategory implements Serializable {
    private String categoryId;
    private String categoryName;
    private int categoryImage;

    public TabCategory() {

    }
    public TabCategory(String categoryName) {
        this.categoryName = categoryName;
    }

    public TabCategory(String categoryId, String categoryName, int categoryImage) {
        this.categoryId = categoryId;
        this.categoryName = categoryName;
        this.categoryImage = categoryImage;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(int categoryImage) {
        this.categoryImage = categoryImage;
    }

    @Override
    public String toString() {
        return "TabCategory{" +
                "categoryId='" + categoryId + '\'' +
                ", categoryName='" + categoryName + '\'' +
                ", categoryImage=" + categoryImage +
                '}';
    }
}
