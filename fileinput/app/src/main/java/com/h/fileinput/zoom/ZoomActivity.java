package com.h.fileinput.zoom;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ZoomControls;

import com.h.fileinput.R;

public class ZoomActivity extends AppCompatActivity {
    private ImageView iv_zoom,iv_cut;
    private ZoomControls zoom;
    private int displayWidth,displayHeight;
    private Bitmap bitmap;
    private Button btn_cut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoom);
        //获得屏幕分辨率大小
        displayWidth = getWindow().getWindowManager().getDefaultDisplay().getWidth();
        displayHeight = getWindow().getWindowManager().getDefaultDisplay().getHeight();
        Log.i("display", displayWidth + ":" + displayHeight);

        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.canada);

        iv_zoom = (ImageView)findViewById(R.id.imgView);

        zoom = (ZoomControls)findViewById(R.id.zoomcontrol);
        zoom.setIsZoomInEnabled(true);
        zoom.setIsZoomOutEnabled(true);
        zoom.setOnZoomInClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bmpWidth = bitmap.getWidth();
                int bmpHeight = bitmap.getHeight();

                float baseScale = (float) 1.5;
                Matrix matrix = new Matrix();
                //参数值为新宽与旧宽比以及新高与旧高的比
                matrix.postScale(baseScale, baseScale);

                if ((bmpWidth <= displayWidth) && (bmpHeight <= displayHeight)) {
                    bitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);
                }
                iv_zoom.setImageBitmap(bitmap);
            }
        });

        zoom.setOnZoomOutClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bmpWidth = bitmap.getWidth();
                int bmpHeight = bitmap.getHeight();

                float baseScale = (float) 0.5;
                Matrix matrix = new Matrix();
                //参数值为新宽与旧宽比以及新高与旧高的比
                matrix.postScale(baseScale, baseScale);

                bitmap = Bitmap.createBitmap(bitmap, 0, 0, bmpWidth, bmpHeight, matrix, false);
                iv_zoom.setImageBitmap(bitmap);
            }
        });

        //剪切图像
        btn_cut = (Button)findViewById(R.id.btn_cut);
        iv_cut = (ImageView)findViewById(R.id.iv_cut);
        btn_cut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //裁剪
                Bitmap bmp1 = Bitmap.createBitmap(bitmap,bitmap.getWidth()/3,0,bitmap.getWidth()/2,bitmap.getHeight());
                Log.i("X", bitmap.getWidth() / 3 + "====" + bitmap.getWidth());
                iv_cut.setImageBitmap(bmp1);
            }
        });
    }
}
