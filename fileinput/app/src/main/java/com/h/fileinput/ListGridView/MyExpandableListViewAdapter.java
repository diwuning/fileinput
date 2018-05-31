package com.h.fileinput.ListGridView;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.h.fileinput.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class MyExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    /**
     * 每个分组的名字的集合
     */
    private List<String> groupList;

    /**
     * 所有分组的所有子项的 GridView 数据集合
     */
    private List<List<String>> itemList;

    private GridView gridView;

    public MyExpandableListViewAdapter(Context mContext, List<String> groupList, List<List<String>> itemList) {
        this.mContext = mContext;
        this.groupList = groupList;
        this.itemList = itemList;
    }


    @Override
    public int getGroupCount() {
        return groupList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return itemList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return groupList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return itemList.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(mContext, R.layout.expandablelist_group,null);
        }
//        ImageView ivGroup = (ImageView) convertView.findViewById(R.id.iv_group);
        TextView tvGroup = (TextView) convertView.findViewById(R.id.tv_group);
        // 如果是展开状态，就显示展开的箭头，否则，显示折叠的箭头
//        if(isExpanded){
//            ivGroup.setImageResource(R.drawable.btn_down);
//        }else {
//            ivGroup.setImageResource(R.drawable.btn_next);
//        }
        //设置分组组名
        tvGroup.setText(groupList.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(mContext,R.layout.expandablelist_item,null);
        }
        //因为convertView的布局就是一个GridView，所以直接向下转型为GridView
        gridView = (GridView) convertView;
        MyGridViewAdapter gridViewAdapter = new MyGridViewAdapter(mContext,itemList.get(groupPosition));
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext,"点击了第"+(groupPosition+1)+"组，第"+(position+1)+"项",Toast.LENGTH_SHORT).show();
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }
}
