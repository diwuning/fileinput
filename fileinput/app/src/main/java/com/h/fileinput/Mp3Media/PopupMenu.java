package com.h.fileinput.Mp3Media;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.h.fileinput.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 2016/3/2 0002.
 */
public class PopupMenu {
    private PopupWindow popupWindow;
    ArrayList<String> itemList;
    private ListView wrapList;
    private Context context;
    private String[] items = {"单曲循环","全部循环","顺序播放","随机播放"};
    public PopupMenu(Context context){
        this.context = context;
        itemList = new ArrayList<String>(5);
        View view = LayoutInflater.from(context).inflate(R.layout.popup_window,null);
        //设置listView
        wrapList = (ListView)view.findViewById(R.id.lv_popup);
        wrapList.setAdapter(new PopAdapter());
        popupWindow = new PopupWindow(view,200,240);
        for(String s:items){
            itemList.add(s);
        }
        //点击popupwindow以外的区域使popupwindow消失（很神奇的）
        popupWindow.setBackgroundDrawable(new BitmapDrawable());

    }

    public void addOnItemClickListener(AdapterView.OnItemClickListener listener){
        wrapList.setOnItemClickListener(listener);
    }

    public void showAsDropDown(View parent){
        popupWindow.showAsDropDown(parent,0,0);
        popupWindow.setFocusable(true);
        //设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        popupWindow.update();
    }

    private class PopAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if(convertView == null){
                convertView = LayoutInflater.from(context).inflate(R.layout.popup_item,null);
                holder = new ViewHolder();
                holder.groupItem = (TextView)convertView.findViewById(R.id.tv_popupItem);
                convertView.setTag(holder);
            }else {
                holder = (ViewHolder)convertView.getTag();
            }
            holder.groupItem.setText(itemList.get(position));
            return convertView;
        }

        private class ViewHolder{
            TextView groupItem;
        }
    }
}
