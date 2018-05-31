package com.h.fileinput.waterwave;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by wangchm on 2016/11/30 0030.
 * 水波纹效果，点击单圆逐渐扩大，移动多个圆环
 */
public class WaterWaveView2 extends View {
    //存放圆环的集合
    private ArrayList<Wave> mList;
    //是否是移动
    private boolean isMove = false;
    //界面刷新
    private Handler mHandler = new Handler(){
        public void handleMessage(Message msg){
            invalidate();//刷新界面，会执行onDraw方法
        }
    };

    public WaterWaveView2(Context context) {
        super(context);
    }

    public WaterWaveView2(Context context, AttributeSet attrs) {
        super(context, attrs);
        mList = new ArrayList<Wave>();
    }

    public WaterWaveView2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    //获得圆心，并且删除集合中透明度为0的圆环，通知handler调用onDraw()方法
    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float x = event.getX();
                float y = event.getY();
                deleteItem();
                Wave wave = new Wave(x,y);
                mList.add(wave);

                //刷新界面
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float x1= event.getX();
                float y1 = event.getY();
                deleteItem();
                Wave wave1 = new Wave(x1,y1);
                mList.add(wave1);

                invalidate();
                break;
        }
        return true;
    }

    //删除透明度已经为0的圆环
    private void deleteItem(){
        for(int i=0;i<mList.size();i++){
            if(mList.get(i).paint.getAlpha() == 0){
                mList.remove(i);
            }
        }
    }

    //重写onDraw()方法，循环绘制圆环
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        //避免程序一运行就进行绘制
        if(mList.size()>0){
            //对集合中的圆环对象循环绘制
            for(int i=0;i<mList.size();i++){
                Wave wave = mList.get(i);
                canvas.drawCircle(wave.x,wave.y,wave.radius,wave.paint);
                wave.radius +=3;

                //对画笔透明度进行操作
                int alpha = wave.paint.getAlpha();
                if(alpha < 80){
                    alpha = 0;
                }else{
                    alpha -= 3;
                }

                //设置画笔宽度和透明度
                wave.paint.setStrokeWidth(wave.radius/8);
                wave.paint.setAlpha(alpha);
                mHandler.sendEmptyMessageDelayed(1,100);
            }
        }
    }
}
