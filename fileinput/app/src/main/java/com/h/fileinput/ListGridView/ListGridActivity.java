package com.h.fileinput.ListGridView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.h.fileinput.ListGridView.demo.ItemBean;
import com.h.fileinput.ListGridView.demo.OtherExpandableListViewAdapter;
import com.h.fileinput.R;

import java.util.ArrayList;
import java.util.List;

public class ListGridActivity extends AppCompatActivity {

    ExpandableListView expandedMenuView;
    /**
     * 每个分组的名字的集合
     */
    private List<String> groupList;

    /**
     * 每个分组下的每个子项的 GridView 数据集合
     */
    private List<ItemBean> itemGridList;
    /**
     * 所有分组的所有子项的 GridView 数据集合
     */
    private List<List<ItemBean>> itemList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_grid);
        expandedMenuView = (ExpandableListView ) findViewById(R.id.expandableList);
        initData();
    }

    List<ExpandBean> totals = new ArrayList<ExpandBean>();
    public static List<ExpandBean> selBeans = new ArrayList<ExpandBean>();
    public static List<ItemBean> selItems = new ArrayList<ItemBean>();

    public void initData(){
        itemGridList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            ItemBean itemBean = new ItemBean();
            itemBean.setItem("电脑" + (i + 1));
            itemBean.setSelect(false);
        }
        for(int i=0;i<2;i++){
            ExpandBean bean = new ExpandBean();
            bean.setGroup("分组"+i);
            bean.setChild(itemGridList);
            totals.add(bean);

        }
        //创建
        OtherExpandableListViewAdapter adapter = new OtherExpandableListViewAdapter(ListGridActivity.this,totals);
        expandedMenuView.setAdapter(adapter);
        //隐藏分组指示器
        expandedMenuView.setGroupIndicator(null);
        expandedMenuView.expandGroup(0);


//        // 分组
//        groupList = new ArrayList<>();
//        groupList.add("分组1");
//        groupList.add("分组2");
//        // 每个分组下的每个子项的 GridView 数据集合
//        itemGridList = new ArrayList<>();
//        for (int i = 0; i < 4; i++) {
//            itemGridList.add("电脑" + (i + 1));
//        }
//
//        // 所有分组的所有子项的 GridView 数据集合
//        itemList = new ArrayList<>();
//        itemList.add(itemGridList);
//        itemList.add(itemGridList);
//        //创建
//        MyExpandableListViewAdapter adapter = new MyExpandableListViewAdapter(ListGridActivity.this,groupList,itemList);
//        expandedMenuView.setAdapter(adapter);
//        //隐藏分组指示器
//        expandedMenuView.setGroupIndicator(null);
//        expandedMenuView.expandGroup(0);


    }
}
