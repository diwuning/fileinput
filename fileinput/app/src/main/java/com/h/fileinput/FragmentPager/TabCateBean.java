package com.h.fileinput.FragmentPager;


import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2018/5/23.
 */

public class TabCateBean implements Serializable {
    private List<TabCategory> categories;

    public TabCateBean() {
    }


    public List<TabCategory> getCategories() {
        return categories;
    }

    public void setCategories(List<TabCategory> categories) {
        this.categories = categories;
    }
}
