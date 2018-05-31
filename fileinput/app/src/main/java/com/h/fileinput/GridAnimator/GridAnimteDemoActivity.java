package com.h.fileinput.GridAnimator;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.GridView;

import com.h.fileinput.R;

import java.util.ArrayList;
import java.util.List;
/*
* GridView各个项以动画方式载入，android的4种动画效果
* */
public class GridAnimteDemoActivity extends AppCompatActivity {
    GridView gv_animate;
    Button btn_refresh;
    private List<String> list;
    GridAdapter adapter;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_animte_demo);

        gv_animate = (GridView) findViewById(R.id.gv_animate);
        btn_refresh = (Button) findViewById(R.id.refresh);
        btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_refresh.setVisibility(View.INVISIBLE);
                adapter.notifyDataSetChanged();
            }
        });

        list = new ArrayList<>();
        for(int i=0;i<9;i++){
            list.add("动画"+i);
        }
        mContext = GridAnimteDemoActivity.this;
        adapter = new GridAdapter(list,mContext);

        //刷新按钮的动画效果，fromXValue的值为-1.0f指从右边进入，1.0f指从左边进入，其他都为0时
        //fromYValue的值为1.0f时，从下向上显示，为-1.0f时，从上向下显示
        /*
        * final TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,-1.0f,
                Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,1.0f);
        * 从左上角进入再从下向上移动
        * */
        /*
        * final TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,1.0f,
                Animation.RELATIVE_TO_PARENT,1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        * final TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,0,
                Animation.RELATIVE_TO_PARENT,1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        * 上面两种设置没有多大区别
        * */

        /*
        * final TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,-1.0f,
                Animation.RELATIVE_TO_PARENT,1.0f,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        * 先从左向右显示，再从右向左显示
        * */

        /*
        * final TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,-1.0f,
                Animation.RELATIVE_TO_PARENT,1.0f,Animation.RELATIVE_TO_SELF,-1.0f,Animation.RELATIVE_TO_SELF,1.0f);
        * 从左上方进，再从右下方向上
        * */

        /*
        * final TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,-1.0f,
                Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_SELF,9,Animation.RELATIVE_TO_SELF,0);
        * 第三个参数设置从下向上移动的距离
        * */
        final TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT,1,
                Animation.RELATIVE_TO_PARENT,0,Animation.RELATIVE_TO_SELF,0,Animation.RELATIVE_TO_SELF,0);
        animation.setDuration(200);
        animation.setAnimationListener(new UserAnimationListener());
        //渐变动画
        final AlphaAnimation alphaAnimation = new AlphaAnimation(0,1);
        alphaAnimation.setDuration(200);
        alphaAnimation.setAnimationListener(new UserAnimationListener());
        //缩放动画
        /*
        * ScaleAnimation(float fromX, float toX, float fromY, float toY,int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
        *   float fromX 动画起始时 X坐标上的伸缩尺寸
            float toX 动画结束时 X坐标上的伸缩尺寸
            float fromY 动画起始时Y坐标上的伸缩尺寸
            float toY 动画结束时Y坐标上的伸缩尺寸
            int pivotXType 动画在X轴相对于物件位置类型
            float pivotXValue 动画相对于物件的X坐标的开始位置
            int pivotYType 动画在Y轴相对于物件位置类型
            float pivotYValue 动画相对于物件的Y坐标的开始位置
        * */
        /*
        * final ScaleAnimation animation =new ScaleAnimation(0.0f, 1.4f, 0.0f, 1.4f,
            Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        * 从中间开始向外扩大，大到一定程度后，再一下子缩小成要求的尺寸
        * 前面四个参数换成1.4f,0.0f,1.4f,0.0f的话，按钮从四周向中心缩小，消失后再从中心一下子弹出来
        * 将RELATIVE_TO_SELF改成RELATIVE_TO_PARENT后，按钮从屏幕中间逐渐放大到顶部消失，再从顶部一下子显示在规定位置
        * 若前一个为RELATIVE_TO_PARENT，后一个为RELATIVE_TO_SELF，效果跟两个都设为RELATIVE_TO_SELF差不多
        * 而前一个设为RELATIVE_TO_SELF，后一个设为RELATIVE_TO_PARENT，效果则跟都设为RELATIVE_TO_PARENT差不多
        * */

        /*
        * final ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f,1.4f,0.0f,1.4f,
                Animation.RELATIVE_TO_SELF,0.0f,
                Animation.RELATIVE_TO_SELF,0.0f);
        * 从左上角开始向右下角放大，大到一定程度后，再一下子缩小成要求的尺寸
        * 后面两个数值改成1.0f的话，则是从右下角开始向左上角放大，大到一定程度再缩小
        * 前面四个参数两两互换的话，则是从右下角开始逐渐缩小，一直缩到左上角后，再从左上角一下子弹出来
        * */
        final ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f,1.4f,0.0f,1.4f,
                Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        scaleAnimation.setDuration(2000);
        scaleAnimation.setAnimationListener(new UserAnimationListener());

        //旋转动画
        /*
        * RotateAnimation (float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue)
            参数说明：
            float fromDegrees：旋转的开始角度。
            float toDegrees：旋转的结束角度。
            int pivotXType：X轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
            float pivotXValue：X坐标的伸缩值。
            int pivotYType：Y轴的伸缩模式，可以取值为ABSOLUTE、RELATIVE_TO_SELF、RELATIVE_TO_PARENT。
            float pivotYValue：Y坐标的伸缩值。
        *final RotateAnimation rotateAnimation = new RotateAnimation(0.0f,360f,Animation.RELATIVE_TO_SELF,0.5f,
                Animation.RELATIVE_TO_SELF,0.5f);
        * 以按钮中间为中心点，旋转360度
        * RELATIVE_TO_SELF换成RELATIVE_TO_PARENT时，以屏幕中间为中心点，按钮绕屏幕一周
        * RELATIVE_TO_PARENT，并将两个伸缩值设为0.0f时，以按钮左上角为中心点，旋转360，设为RELATIVE_TO_SELF也一样。
        * RELATIVE_TO_PARENT，两个伸缩值都为1.0f时，先从按钮右下角开始旋转出屏幕，再从屏幕的左中下方旋转回原位置
        * */
        final RotateAnimation rotateAnimation = new RotateAnimation(0.0f,360f,Animation.RELATIVE_TO_PARENT,0.0f,
                Animation.RELATIVE_TO_PARENT,0.0f);
        rotateAnimation.setDuration(2000);
        rotateAnimation.setAnimationListener(new UserAnimationListener());

        //GridView的动画效果执行完成后，再显示按钮的动画
        adapter.setOnLastItemAnimationEndLister(new GridAdapter.OnLastItemAnimationEndListener() {
            @Override
            public void onAnimationEnd() {
                btn_refresh.startAnimation(rotateAnimation);
            }
        });

        gv_animate.setAdapter(adapter);

    }

    class UserAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {
            btn_refresh.setVisibility(View.VISIBLE);
        }

        @Override
        public void onAnimationEnd(Animation animation) {

        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }
}
