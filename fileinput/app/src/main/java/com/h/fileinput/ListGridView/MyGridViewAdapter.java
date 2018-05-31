package com.h.fileinput.ListGridView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.h.fileinput.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/3 0003.
 */

public class MyGridViewAdapter extends BaseAdapter {
    private Context mContext;

    /**
     * 每个分组下的每个子项的 GridView 数据集合
     */
    private List<String> itemGridList;
    int location = -1;

    public MyGridViewAdapter(Context mContext, List<String> itemGridList) {
        this.mContext = mContext;
        this.itemGridList = itemGridList;
    }

    public void setDataChange(int postion){
        this.location = postion;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return itemGridList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemGridList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = View.inflate(mContext, R.layout.gridview_item, null);
        }
        TextView tvGridView = (TextView) convertView.findViewById(R.id.tv_gridview);
        ImageView ivCheck = (ImageView) convertView.findViewById(R.id.iv_check);

        tvGridView.setText(itemGridList.get(position));
        return convertView;
    }

}
