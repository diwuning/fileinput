package com.h.fileinput.waterwave;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.ParcelUuid;

/**
 * Created by wangchm on 2016/11/30 0030.
 */
public class Wave {
    public float x;//圆心x坐标
    public float y;//圆心y坐标
    public Paint paint;//画圆的画笔
    public float width;//线条宽度
    public int radius;//圆的半径
    public int ranNum;//随机数

    public int[] randomColor = {Color.BLUE,Color.CYAN,Color.GREEN,Color.MAGENTA,Color.RED,Color.YELLOW};

    public Wave(float x,float y){
        this.x = x;
        this.y = y;
        initData();
    }

    /*
    * 初始化数据，每次点击一次都要初始化一次
    * */
    private void initData(){
        paint = new Paint();//因为点击一次需要画出不同的圆环
        paint.setAntiAlias(true);//打开抗锯齿
        ranNum = (int)(Math.random()*6);//[0,5]的随机数
        paint.setColor(randomColor[ranNum]);//设置画笔的颜色
        paint.setStyle(Paint.Style.STROKE);//描边
        paint.setStrokeWidth(width);//设置描边的宽度
        paint.setAlpha(255);
        radius = 0;
        width = 0;
    }
}
