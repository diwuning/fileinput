package com.h.fileinput.EasyRecyclerView;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.h.fileinput.R;
import com.jude.easyrecyclerview.adapter.BaseViewHolder;
import com.jude.easyrecyclerview.adapter.RecyclerArrayAdapter;

import java.util.List;

/**
 * Created by Administrator on 2016/11/15 0015.
 */
public class ArticleForRecyclerAdapter extends RecyclerArrayAdapter<Article> {
    public ArticleForRecyclerAdapter(Context context) {
        super(context);
    }

    public ArticleForRecyclerAdapter(Context context, List<Article> objects) {
        super(context, objects);
    }

    @Override
    public BaseViewHolder OnCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tea,parent,false);

//        if(viewType == -1){
//            return new EmptyViewHolder(parent);
//        }
//        return new ArticleViewHolder(parent);
        return new ArticleHolder(view);
    }

    public int getViewType(int position){
        if(mObjects.size() ==0){
            return -1;
        }
        return super.getViewType(position);
    }

    public class ArticleHolder extends BaseViewHolder<Article>{
        private ImageView iv_img;
        private TextView tv_title;
        private TextView tv_content;

        public ArticleHolder(View itemView) {
            super(itemView);
            iv_img = (ImageView) itemView.findViewById(R.id.iv_tea);
            tv_title = (TextView) itemView.findViewById(R.id.tv_title);
            tv_content = (TextView) itemView.findViewById(R.id.tv_content);
        }

        public void setData(Article data){
            tv_title.setText(data.getTitle());
            tv_content.setText(data.getContent());
            iv_img.setImageResource(R.drawable.ic_launcher);
        }
    }
}
