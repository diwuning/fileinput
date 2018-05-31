package com.h.fileinput.picture;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.h.fileinput.R;

import java.io.InputStream;

public class PicSpecialActivity extends AppCompatActivity {
    ImageView iv_img3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_special);

        iv_img3 = (ImageView) findViewById(R.id.iv_img3);
        Bitmap bitmap = readBitMap(R.drawable.pic6);
        iv_img3.setImageBitmap(addBigFrame(bitmap,R.drawable.bdspeech_mask_deep));

    }

    //给图片添加蒙版
    private Bitmap addBigFrame(Bitmap bm,int res){
        //Bitmap frameBitmap = BitmapFactory.decodeResource(getResources(),res);
        Bitmap frameBitmap = readBitMap(res);
        Drawable[] array = new Drawable[2];
        frameBitmap = Bitmap.createScaledBitmap(frameBitmap,bm.getWidth(),bm.getHeight(),false);
        array[0] = new BitmapDrawable(bm);
        array[1] = new BitmapDrawable(frameBitmap);
        LayerDrawable layerDrawable = new LayerDrawable(array);
        layerDrawable.setLayerInset(0, 0, 0, 0, 0);

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

    //以最省内在的方式创建Bitmap
    private Bitmap readBitMap(int resId){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        options.inPurgeable=true;
        options.inInputShareable = true;

        InputStream is = getResources().openRawResource(resId);
//        InputStream is = getImg(Environment.getExternalStorageDirectory()+"/download_test/downloadtest.jpg");

        return BitmapFactory.decodeStream(is,null,options);
    }
}
