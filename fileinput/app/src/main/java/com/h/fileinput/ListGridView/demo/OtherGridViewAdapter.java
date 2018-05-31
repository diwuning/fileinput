package com.h.fileinput.ListGridView.demo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.h.fileinput.R;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by Administrator on 2017/11/3 0003.
 */

public class OtherGridViewAdapter extends BaseAdapter {
    private Context mContext;

    /**
     * 每个分组下的每个子项的 GridView 数据集合
     */
    private List<ItemBean> itemGridList;
    int location = -1;

    public OtherGridViewAdapter(Context mContext, List<ItemBean> itemGridList) {
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
        if(itemGridList.get(position).isSelect()){
            ivCheck.setVisibility(View.VISIBLE);
        }else{
            ivCheck.setVisibility(View.GONE);
        }
        Log.e("demo",""+itemGridList.get(position).getItem());
        tvGridView.setText(itemGridList.get(position).getItem());
        return convertView;
    }

}
