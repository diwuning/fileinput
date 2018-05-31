package com.h.fileinput.picture;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.widget.Button;
import android.widget.ImageView;

import com.h.fileinput.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/*
* 给图片添加图形蒙版，
* 给图片设置圆角
* */

public class PictureTestActivity extends Activity {
    private Button btn_start;
    private ImageView iv_result,iv_pic1,iv_pic2,iv_test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_fusion);
        iv_result = (ImageView)findViewById(R.id.iv_fusion);
        iv_pic1 = (ImageView)findViewById(R.id.iv_pic1);
        iv_pic2 = (ImageView)findViewById(R.id.iv_pic2);
//        final Bitmap bitmap1 = BitmapFactory.decodeResource(getResources(),R.drawable.pic1);
//        final Bitmap bitmap2 = BitmapFactory.decodeResource(getResources(),R.drawable.timg);
        Bitmap bitmap1 = readBitMap(R.drawable.pic1);
        Bitmap bitmap2 = readBitMap(R.drawable.timg);
        iv_result.setImageBitmap(addBigFrame(bitmap1, R.drawable.pic_bian));
        iv_pic1.setImageBitmap(addBigFrame(bitmap2, R.drawable.pic_bian));
        iv_pic2.setImageBitmap(addBigFrame(bitmap1, R.drawable.pic_bian));

    }

    //给图片添加蒙版
    private Bitmap addBigFrame(Bitmap bm,int res){
        //Bitmap frameBitmap = BitmapFactory.decodeResource(getResources(),res);
        Bitmap frameBitmap = readBitMap(res);
        Drawable[] array = new Drawable[2];
        bm = Bitmap.createScaledBitmap(toRoundCorner(bm,10),frameBitmap.getWidth(),frameBitmap.getHeight(),false);
        array[0] = new BitmapDrawable(bm);
        array[1] = new BitmapDrawable(frameBitmap);
        LayerDrawable layerDrawable = new LayerDrawable(array);
        layerDrawable.setLayerInset(0, 7, 7, 7, 7);

       // layerDrawable.setBounds();
        return drawableToBitmap(layerDrawable);
    }

    //将Drawable转换成Bitmap
    private Bitmap drawableToBitmap(Drawable drawable){
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(),
                drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    //给图片加上圆角
    public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.RGB_565);;
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    //以最省内在的方式创建Bitmap
    private Bitmap readBitMap(int resId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable=true;
        options.inInputShareable = true;

//        InputStream is = getResources().openRawResource(resId);
        InputStream is = getImg(Environment.getExternalStorageDirectory()+"/download_test/downloadtest.jpg");

        return BitmapFactory.decodeStream(is,null,options);
    }

    //获取图片
    public InputStream getImg(String imgPath){
        try{
            URL url = new URL(imgPath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5*1000);
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                return is;
            }
        }catch (MalformedURLException mue){
            //URL抛出的异常
            mue.printStackTrace();
        }catch (IOException ie){
            //HttpURLConnection抛出的异常。
            ie.printStackTrace();
        }
        return null;
    }
}
