package com.h.fileinput.GridAnimator;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.h.fileinput.R;

import java.util.List;

/**
 * Created by Administrator on 2017/3/16 0016.
 */
public class GridAdapter extends BaseAdapter {
    private List<String> mList;
    private Context mContext;

    public GridAdapter(List<String> list, Context context){
        mList = list;
        mContext = context;
    }
    @Override
    public int getCount() {
        return mList == null? 0:mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        String text = mList.get(position);
        ViewHolder holder;
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.card_desk_grid_item,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        convertView.setVisibility(View.INVISIBLE);
        holder.textView.setText(text);
        int count = 3-position%3;
        //列表项一个接一个从右边滑动显示,一行显示完显示下一行
        final TranslateAnimation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF,count,
                Animation.RELATIVE_TO_SELF,0,
                Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        translateAnimation.setDuration(count*100);
        //效果与上面的方法是一样的
//        final Animation animation = AnimationUtils.loadAnimation(mContext,R.anim.slide_in_right);
//        animation.setDuration(count*100);
        final View finalConverView = convertView;
        convertView.postDelayed(new Runnable() {
            @Override
            public void run() {
                finalConverView.startAnimation(translateAnimation);
            }
        },position*200);

        translateAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                finalConverView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(position == mList.size()-1){
                    if(mListener != null){
                        mListener.onAnimationEnd();
                    }
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        return convertView;
    }

    class ViewHolder{
        TextView textView;
        public ViewHolder(View view){
            textView = (TextView) view.findViewById(R.id.tv);
        }
    }

    public interface OnLastItemAnimationEndListener{
        void onAnimationEnd();
    }

    private OnLastItemAnimationEndListener mListener;

    public void setOnLastItemAnimationEndLister(OnLastItemAnimationEndListener listener){
        mListener = listener;
    }
}
