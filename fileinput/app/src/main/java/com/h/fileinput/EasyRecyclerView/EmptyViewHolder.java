package com.h.fileinput.EasyRecyclerView;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

import com.h.fileinput.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class EmptyViewHolder extends BaseViewHolder<Article> {

    public EmptyViewHolder(ViewGroup itemView) {
        super(itemView, R.layout.view_empty);
    }

    public EmptyViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
    }
}
