package com.h.fileinput.FlycoTabLayout;

import android.app.Activity;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.flyco.tablayout.SlidingTabLayout;
import com.h.fileinput.R;

public class SlideTabActivity extends Activity {
    private ViewPager vp_slidingTab;
    private SlidingTabLayout slidingTab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_tab);

        slidingTab = (SlidingTabLayout) findViewById(R.id.slidingTab);
        vp_slidingTab = (ViewPager) findViewById(R.id.vp_slidingTab);
        vp_slidingTab.setAdapter(new MyPagerAdapter(this));
        slidingTab.setViewPager(vp_slidingTab);
    }

    private class MyPagerAdapter extends PagerAdapter{
        private Activity activity;

        private MyPagerAdapter(Activity activity) {
            this.activity = activity;
        }


        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object == view;
        }


        @Override
        public CharSequence getPageTitle(int pos) {
            return "选项卡" +pos;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int pos) {
            TextView tv=new TextView(activity);
            tv.setText(pos+"");
            tv.setTextSize(50.0f);
            tv.setGravity(Gravity.CENTER);
            container.addView(tv);

            return tv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
