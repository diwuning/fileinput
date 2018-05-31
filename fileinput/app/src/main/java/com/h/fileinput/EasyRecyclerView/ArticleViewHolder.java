package com.h.fileinput.EasyRecyclerView;

import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h.fileinput.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class ArticleViewHolder extends BaseViewHolder<Article> {
    private ImageView iv_tea;
    private TextView tv_title;
    private TextView tv_content;

    public ArticleViewHolder(ViewGroup parent) {
        super(parent, R.layout.item_tea);
//        super(parent);
//        View view = LayoutInflater.from(itemView.getContext()).inflate(R.layout.item_tea,parent.getParent(),false);
        View view = parent.findFocus();
        this.iv_tea = (ImageView) view.findViewById(R.id.iv_tea);
        this.tv_title = (TextView) view.findViewById(R.id.tv_title);
        this.tv_content = (TextView) view.findViewById(R.id.tv_content);
    }

    public ArticleViewHolder(ViewGroup parent, @LayoutRes int res) {
        super(parent, res);
    }

    @Override
    public void setData(Article data){
        tv_title.setText("1111");
        tv_content.setText("sewegwegewg");
        iv_tea.setImageResource(R.drawable.ic_launcher);
    }
}
