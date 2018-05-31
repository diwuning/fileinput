package com.h.fileinput.ListGridView;

import com.h.fileinput.ListGridView.demo.ItemBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/9 0009.
 */

public class ExpandBean {
    private String group;
    private List<ItemBean> child;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ItemBean> getChild() {
        return child;
    }

    public void setChild(List<ItemBean> child) {
        this.child = child;
    }
}
