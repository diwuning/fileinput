package com.h.fileinput.weixinrecorder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.Log;
import android.view.View;

import com.h.fileinput.R;

/**
 * Created by wangchm on 2017/1/7 0007.
 * 自定义拍照功能的焦点框
 */
public class MyView extends View {

    Context m_context;
    int w,h;
    public MyView(Context context,int w,int h) {
        super(context);
        // TODO Auto-generated constructor stub
        this.w = w;
        this.h = h;
        m_context=context;
    }

    public MyView(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
        m_context=context;
    }

    //重写OnDraw（）函数，在每次重绘时自主实现绘图
    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);


        //设置画笔基本属性
        Paint paint=new Paint();
        paint.setAntiAlias(true);//抗锯齿功能
        paint.setColor(getResources().getColor(R.color.colorPrimary));  //设置画笔颜色
        paint.setStyle(Paint.Style.STROKE);//设置填充样式   Style.FILL/Style.FILL_AND_STROKE/Style.STROKE
        paint.setStrokeWidth(5);//设置画笔宽度
        paint.setShadowLayer(10, 15, 15, Color.GREEN);//设置阴影

        //设置画布背景颜色
//        canvas.drawRGB(255, 255,255);

        //画圆
//        canvas.drawCircle(390, 200, 150, paint);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.msg_camera_focus);
        Matrix matrix = new Matrix();
        matrix.postScale((float)0.3,(float)0.3);
        bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
        int width = bitmap.getWidth();
        Log.d("MyView","width="+width+","+(w-width/2));
        canvas.drawBitmap(bitmap,w-180,h-width/2,paint);
        if(bitmap != null && !bitmap.isRecycled()){
            bitmap.recycle();
            bitmap = null;
        }
    }

}
