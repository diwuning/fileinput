package com.h.fileinput.AnalyzeHtml;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.h.fileinput.R;
import com.h.fileinput.db.greenBean.CookBean;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by Administrator on 2017/3/17 0017.
 */
public class CardViewAdapter extends BaseAdapter {
    private Context mContext;
    private List<CookBean> cookBeans;

    public CardViewAdapter(Context context,List<CookBean> list){
        this.mContext = context;
        this.cookBeans = list;
    }
    @Override
    public int getCount() {
        return cookBeans == null?0:cookBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CardHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.cardview_item,null);
            holder = new CardHolder();
            holder.tv_name = (TextView) convertView.findViewById(R.id.tv_menuName);
            holder.img = (ImageView) convertView.findViewById(R.id.iv_menuImg);
            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_item);
            convertView.setTag(holder);
        }else{
            holder = (CardHolder) convertView.getTag();
        }
        CookBean cookBean = cookBeans.get(position);
        holder.tv_name.setText(cookBean.getTitle());
        ImageLoader.getInstance().displayImage(cookBean.getImgPath(),holder.img);
        holder.tv_content.setText(cookBean.getCreateUser());
        return convertView;
    }

    class CardHolder{
        TextView tv_name;
        ImageView img;
        TextView tv_content;
    }
}
