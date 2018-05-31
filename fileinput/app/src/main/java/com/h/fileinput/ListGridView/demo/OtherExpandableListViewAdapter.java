package com.h.fileinput.ListGridView.demo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.h.fileinput.ListGridView.ExpandBean;
import com.h.fileinput.ListGridView.ListGridActivity;
import com.h.fileinput.ListGridView.MyGridViewAdapter;
import com.h.fileinput.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/2 0002.
 */

public class OtherExpandableListViewAdapter extends BaseExpandableListAdapter {

    private Context mContext;

    List<ExpandBean> expandBeans;

    private GridView gridView;

    public OtherExpandableListViewAdapter(Context mContext, List<ExpandBean> totals) {
        this.mContext = mContext;
        this.expandBeans = totals;
    }


    @Override
    public int getGroupCount() {
        return expandBeans.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return expandBeans.get(groupPosition).getChild().size();
//        return 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return expandBeans.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return expandBeans.get(groupPosition).getChild().get(childPosition);
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
        tvGroup.setText(expandBeans.get(groupPosition).getGroup());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = View.inflate(mContext,R.layout.expandablelist_item,null);
        }
        //因为convertView的布局就是一个GridView，所以直接向下转型为GridView
        gridView = (GridView) convertView;
        OtherGridViewAdapter gridViewAdapter = new OtherGridViewAdapter(mContext,expandBeans.get(groupPosition).getChild());
        gridView.setAdapter(gridViewAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(mContext,"点击了第"+(groupPosition+1)+"组，第"+(position+1)+"项",Toast.LENGTH_SHORT).show();
                ItemBean itemBean = expandBeans.get(groupPosition).getChild().get(position);
                if(itemBean.isSelect()){
                    itemBean.setSelect(false);
                    ListGridActivity.selItems.remove(expandBeans.get(groupPosition).getChild().get(position));
                }else{
                    itemBean.setSelect(true);
                    ListGridActivity.selItems.add(expandBeans.get(groupPosition).getChild().get(position));
                }

                Log.e("demo",ListGridActivity.selItems.size()+"");
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        Log.e("demo","groupPosition="+groupPosition+",childPosition="+childPosition);
        return false;
    }
}
