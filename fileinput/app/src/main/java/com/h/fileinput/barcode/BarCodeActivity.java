package com.h.fileinput.barcode;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.h.fileinput.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class BarCodeActivity extends Activity {
    private EditText et_content;
    private Button btn_barcode,btn_QRcode;
    private ImageView iv_barcode,iv_QRcode;
    private TextView tv_detail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bar_code);
        initView();
    }

    private void initView(){
        et_content = (EditText)findViewById(R.id.et_content);
        btn_barcode = (Button)findViewById(R.id.btn_barcode);
        iv_barcode = (ImageView)findViewById(R.id.iv_barcode);
        btn_QRcode = (Button)findViewById(R.id.btn_QRcode);
        iv_QRcode = (ImageView)findViewById(R.id.iv_QRcode);
        tv_detail = (TextView)findViewById(R.id.tv_detail);
        String date = "2015/1/1";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");


            Date date1 = new Date(date);
//            tv_detail.setText(simpleDateFormat.format(date1));
//        tv_detail.setText(String.valueOf(date1.getTime()));

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c_EndCal = Calendar.getInstance();
        c_EndCal.setTime(new Date());
        c_EndCal.add(Calendar.DAY_OF_YEAR, 1);
        String time = sdf.format(c_EndCal.getTime());
        String longdate = String.valueOf(c_EndCal.getTime().getTime());
        tv_detail.setText(String.valueOf(c_EndCal.getTime()+"===="+new Date(Long.valueOf(longdate)).getTime()+"====="+new Date(date).getTime()));
        Toast.makeText(getApplicationContext(),"c_EndCal.getTime()="+c_EndCal.getTime()+",========"+c_EndCal.getTime().getTime(),Toast.LENGTH_LONG).show();

        setListener();
    }

    private void setListener(){
        btn_QRcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_content.getText().toString().trim();
                Bitmap bmp = null;
                if(str != null && !"".equals(str)){
                    bmp = createQRCode(str);
                }

                if(bmp != null){
                    iv_QRcode.setImageBitmap(bmp);
                }
            }
        });

        btn_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = et_content.getText().toString().trim();
                int size = str.length();
                for (int i = 0; i < size; i++) {
                    int c = str.charAt(i);
                    if ((19968 <= c && c < 40623)) {
                        Toast.makeText(BarCodeActivity.this, "生成条形码的时刻不能是中文", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }
                Bitmap bmp = null;
                try {
                    if (str != null && !"".equals(str)) {
                        bmp = CreateBarCode(str);
                    }
                } catch (WriterException e) {
                    e.printStackTrace();
                }
                if (bmp != null) {
                    iv_barcode.setImageBitmap(bmp);
                }
            }
        });

    }

    /**
     * 将指定的内容生成成二维码
     *
     * @param content 将要生成二维码的内容
     * @return 返回生成好的二维码事件
     * @throws WriterException WriterException异常
     */
    public Bitmap createQRCode(String content){
        //生成二维矩阵，编码时指定大小，不要生成了图片以后再进行缩放，这样会模糊导致识别失败
        BitMatrix matrix = null;
        try {
            matrix = new MultiFormatWriter().encode(content, BarcodeFormat.QR_CODE,400,400);

        } catch (WriterException e) {
            e.printStackTrace();
        }
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        //二维矩阵转为一维像素数组，也就是一直横着排了。
        int[] pixels = new int[width*height];
        //两个for循环是图片横列扫描的结果
        for(int y = 0;y<height;y++){
            for(int x=0;x<width;x++){
                if(matrix.get(x,y)){
                    pixels[y*width+x] = 0xff000000;
                }
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(width,height, Bitmap.Config.ARGB_8888);
        //通过像素数组生成bitmap，具体参考api
        bitmap.setPixels(pixels,0,width,0,0,width,height);
        return bitmap;
    }

    /**
     * 用于将给定的内容生成成条形码 注：目前生成内容为中文的话将直接报错，要修改底层jar包的内容
     *
     * @param content 将要生成一维码的内容
     * @return 返回生成好的一维码bitmap
     * @throws WriterException WriterException异常
     */
    public Bitmap CreateBarCode(String content) throws WriterException {
        // 生成一维条码,编码时指定大小,不要生成了图片以后再进行缩放,这样会模糊导致识别失败
        BitMatrix matrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.CODE_128, 500, 200);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (matrix.get(x, y)) {
                    pixels[y * width + x] = 0xff000000;
                }
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        // 通过像素数组生成bitmap,具体参考api
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

}
