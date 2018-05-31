package com.h.fileinput.FragmentPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h.fileinput.R;
import com.h.fileinput.Utils;

import java.util.ArrayList;
import java.util.List;
/*
* ViewPager中嵌套fragment的例子
* */

public class FragmentPagerActivity extends AppCompatActivity {
    private static final String TAG = "FragmentPagerActivity";
    private ImageView nav_head_right,nav_head_back;
    private TextView nav_head_title;
    private HorizontalScrollView hsv_tab;
    private ViewPager vp_cate;
    private LinearLayout ll_tab;
    Context mContext;

    //导航栏的item个数
    private final int ITEMNUM = 4;
    //导航栏每个item的宽度
    private int itemWidth = 0;
    private int gridHeight = 96;
    private int lineHeight = 45;

    private int currentPosition = 0;

    //导航栏布局列表
    private List<RelativeLayout> headLayoutList = new ArrayList<>();
    private ArrayList<Fragment> fragmentList = new ArrayList<>();

    private List<TabCategory> recipesCategoryList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_pager);
        mContext =FragmentPagerActivity.this;
        itemWidth = Utils.getScreenWidth(mContext)/ITEMNUM;
        handleIntent();
        initView();
        initViewDate();
    }

    public void initView(){
        nav_head_back = (ImageView) findViewById(R.id.nav_head_back);
        nav_head_right = (ImageView) findViewById(R.id.nav_head_right);

        nav_head_title = (TextView) findViewById(R.id.nav_head_title);
        nav_head_title.setText("豆瓣电影");
        nav_head_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        vp_cate = (ViewPager) findViewById(R.id.vp_cate);
        hsv_tab = (HorizontalScrollView) findViewById(R.id.hsv_tab);
        ll_tab = (LinearLayout) findViewById(R.id.ll_tab);
    }

    private void handleIntent() {
//        tag_id = getIntent().getStringExtra("tag_id");
//        equipment_type = getIntent().getStringExtra("equipment_type");
//        cookbook_tag_name = getIntent().getStringExtra("cookbook_tag_name");
    }

    private void initDemoData(){
        recipesCategoryList = new ArrayList<TabCategory>();
        TabCategory category3 = new TabCategory();
        category3.setCategoryId("in_theaters");
        category3.setCategoryName("正在上映");
        recipesCategoryList.add(category3);
        TabCategory category4 = new TabCategory();
        category4.setCategoryId("coming_soon");
        category4.setCategoryName("即将上映");
        recipesCategoryList.add(category4);
        TabCategory category1 = new TabCategory();
        category1.setCategoryId("weekly");
        category1.setCategoryName("口碑榜");
        recipesCategoryList.add(category1);
        TabCategory category2 = new TabCategory();
        category2.setCategoryId("top250");
        category2.setCategoryName("top250");
        recipesCategoryList.add(category2);
    }

    public void initViewDate(){
        int selectPosition = getIntent().getIntExtra("position", -1);
        currentPosition = selectPosition > -1 ? selectPosition : 0;
        initDemoData();
//        if(getIntent().getSerializableExtra("cateList") != null){
//            TabCateBean recipeCateBean = (TabCateBean) getIntent().getSerializableExtra("cateList");
//            if(recipeCateBean.getCategories() != null && recipeCateBean.getCategories().size() !=0){
//                recipesCategoryList = recipeCateBean.getCategories();
//            }
//
//        }

        initHead();
        initFragment();
        if (selectPosition != -1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(200);
                        handler.sendEmptyMessage(SET_POSITION);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
    }

    private static final int SET_POSITION = 0x1101;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SET_POSITION:
                    vp_cate.setCurrentItem(currentPosition);
                    break;
                default:
                    break;
            }
        }
    };

    public void initHead(){
        if(recipesCategoryList.size() ==0){
            return;
        }
        headLayoutList.clear();
        ll_tab.removeAllViews();

        for(int i=0;i<recipesCategoryList.size();i++){
            //标题文字
            RelativeLayout layout = new RelativeLayout(mContext);
            TextView textView = new TextView(mContext);
            textView.setText(recipesCategoryList.get(i).getCategoryName());
            textView.setGravity(Gravity.CENTER);
            textView.setTextColor(mContext.getResources().getColor(R.color.text_black));
            textView.setBackgroundResource(R.color.text_white);

            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    itemWidth,gridHeight
            );
            layout.addView(textView,params);

            //标题分割线
            View lineView = new View(mContext);
            lineView.setBackgroundResource(R.color.colorPrimaryDark);
            RelativeLayout.LayoutParams lineParams = new RelativeLayout.LayoutParams(1,lineHeight);
            lineParams.addRule(RelativeLayout.CENTER_VERTICAL);
            layout.addView(lineView,lineParams);
            layout.setBackgroundResource(R.color.text_gray);

            //添加到布局里
            ll_tab.addView(layout,itemWidth,gridHeight);
            layout.setOnClickListener(onCategoryClickListener);
            layout.setTag(i);

            //默认选中第一个
            if(i == currentPosition){
                textView.setBackgroundResource(R.color.color_orange);
                layout.setBackgroundResource(R.color.color_orange);
                lineView.setVisibility(View.GONE);
            }else if(currentPosition != recipesCategoryList.size() && i == currentPosition +1){
                lineView.setVisibility(View.GONE);
            }

            headLayoutList.add(layout);
        }
    }

    /*
      * 初始化菜谱列表Fragment
      */
    private void initFragment() {
        if (recipesCategoryList.size() == 0) {
            return;
        }

        fragmentList.clear();//清空

        for (int i = 0; i < recipesCategoryList.size(); i++) {
            Bundle data = new Bundle();
            data.putString("tag_id",recipesCategoryList.get(i).getCategoryId());
            data.putString("cookbook_tag_name",recipesCategoryList.get(i).getCategoryName());
            DoubanMoviesFragment newfragment = new DoubanMoviesFragment();
            newfragment.setArguments(data);
            fragmentList.add(newfragment);
        }

        Log.e(TAG, "初始化Fragment: =======================>" + fragmentList.size());
        TabFragmentPagerAdapter mAdapetr = new TabFragmentPagerAdapter(this.getSupportFragmentManager(), fragmentList);
        vp_cate.setAdapter(mAdapetr);
        vp_cate.addOnPageChangeListener(onPageChangeListener);
    }

    /**
     * viewpager滑动监听
     */
    public ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int arg2) {
        }

        @Override
        public void onPageSelected(int position) {
            Log.e(TAG, "onPageSelected:========================> " + position);
            currentPosition = position;
            hsv_tab.smoothScrollTo((position - 1) * itemWidth, 0);
            cleanNavColor();
            setHeadSelect(position);
        }
    };

    /**
     * 导航栏设置为默认样子
     */
    public void cleanNavColor() {
        for (int i = 0; i < headLayoutList.size(); i++) {
            headLayoutList.get(i).setBackgroundResource(R.color.text_white1);
            headLayoutList.get(i).getChildAt(0).setBackgroundResource(R.color.text_white1);
            headLayoutList.get(i).getChildAt(1).setVisibility(View.VISIBLE);
            TextView textView = (TextView) headLayoutList.get(i).getChildAt(0);
            textView.setTextColor(Color.parseColor("#000000"));
            textView.setBackgroundResource(R.color.text_white);
            headLayoutList.get(i).setBackgroundResource(R.color.text_gray);
        }
    }

    /**
     * 设置选中的head样式
     */
    public void setHeadSelect(int position) {
        headLayoutList.get(position).setBackgroundResource(R.color.color_orange);
        RelativeLayout tmp = headLayoutList.get(position);
        tmp.getChildAt(1).setVisibility(View.INVISIBLE);
        if (position != headLayoutList.size() - 1)
            headLayoutList.get(position + 1).getChildAt(1).setVisibility(View.INVISIBLE);
        tmp.getChildAt(0).setBackgroundResource(R.color.color_orange);
        TextView textView = (TextView) tmp.getChildAt(0);
        textView.setTextColor(Color.parseColor("#ffffff"));
    }

    private View.OnClickListener onCategoryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            currentPosition = (int) view.getTag();
            vp_cate.setCurrentItem(currentPosition);
            cleanNavColor();
            setHeadSelect(currentPosition);
        }
    };
}
