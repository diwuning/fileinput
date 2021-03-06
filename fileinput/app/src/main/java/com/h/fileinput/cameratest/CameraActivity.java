package com.h.fileinput.cameratest;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.h.fileinput.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraActivity extends Activity {
    SurfaceView surfaceView;
    RelativeLayout rl_btn;
    private Camera camera;
    private Camera.Parameters parameters = null;

    Bundle bundle = null; // 声明一个Bundle对象，用来存储数据
    ImageView iv_play,scalePic;
    Button takepicture;
    RelativeLayout rl_playPic;
//    RelativeLayout fl_camera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        iv_play = (ImageView) findViewById(R.id.iv_playPic);
        scalePic = (ImageView) findViewById(R.id.scalePic);
        takepicture = (Button) findViewById(R.id.takepicture);
//        rl_playPic = (RelativeLayout) findViewById(R.id.rl_playPic);
//        fl_camera = (RelativeLayout) findViewById(R.id.fl_camera);
        surfaceView = (SurfaceView) this
                .findViewById(R.id.surfaceView);
        rl_btn = (RelativeLayout) this.findViewById(R.id.buttonLayout);
        surfaceView.getHolder()
                .setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceView.getHolder().setFixedSize(176, 144); //设置Surface分辨率
        surfaceView.getHolder().setKeepScreenOn(true);// 屏幕常亮
        surfaceView.getHolder().addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数
    }

    /**
     * 按钮被点击触发的事件
     *
     * @param v
     */
    public void btnOnclick(View v) {
        if (camera != null) {
            switch (v.getId()) {
                case R.id.takepicture:
                    // 拍照
                    camera.takePicture(null, null, new MyPictureCallback());
                    break;
            }
        }
    }

    byte[] picData;
    private final class MyPictureCallback implements Camera.PictureCallback{

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            try{
                bundle = new Bundle();
                bundle.putByteArray("bytes",data);//将图片字节数据保存在bundle中，实现数据交换
                picData = data;
//                saveToSDCard(data);
//                camera.startPreview();//拍完照后，重新开始预览
                if (bundle == null) {
                    Toast.makeText(getApplicationContext(), "请先拍照",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent();
                    intent.setClass(getApplicationContext(),ShowPicActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 将拍下来的照片存放在SD卡中
     * @param data
     * @throws IOException
     */
    public static void saveToSDCard(byte[] data) throws IOException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss"); // 格式化时间
        String filename = format.format(date) + ".jpg";
        File fileFolder = new File(Environment.getExternalStorageDirectory()
                + "/finger/");
        if (!fileFolder.exists()) { // 如果目录不存在，则创建一个名为"finger"的目录
            fileFolder.mkdir();
        }
        File jpgFile = new File(fileFolder, filename);
        FileOutputStream outputStream = new FileOutputStream(jpgFile); // 文件输出流
        outputStream.write(data); // 写入sd卡中
        outputStream.close(); // 关闭输出流
    }

    private final class SurfaceCallback implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                camera = Camera.open(); // 打开摄像头
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(getPreviewDegree(CameraActivity.this));
                camera.startPreview(); // 开始预览
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            parameters = camera.getParameters(); // 获取各项参数
            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
            parameters.setPreviewSize(width, height); // 设置预览大小
            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
            parameters.setJpegQuality(80); // 设置照片质量
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                camera.release(); // 释放照相机
                camera = null;
            }
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        switch (keyCode){
            case KeyEvent.KEYCODE_CAMERA:
                if(camera != null && event.getRepeatCount() == 0){
                    // 拍照
                    //注：调用takePicture()方法进行拍照是传入了一个PictureCallback对象——当程序获取了拍照所得的图片数据之后
                    //，PictureCallback对象将会被回调，该对象可以负责对相片进行保存或传入网络
                    camera.takePicture(null,null,new MyPictureCallback());
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }

    /**
     * 图片被点击触发的时间
     *
     * @param v
     */
    public void imageClick(View v) {
        if (v.getId() == R.id.scalePic) {
            if (bundle == null) {
                Toast.makeText(getApplicationContext(), "请先拍照",
                        Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(this, ShowPicActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        }
    }

    /**
     * 将MainActivity传过来的图片显示在界面当中
     *
     */
    public void setImageBitmap() {
        Bitmap cameraBitmap = BitmapFactory.decodeByteArray(picData, 0, picData.length);
        // 根据拍摄的方向旋转图像（纵向拍摄时要需要将图像选择90度)
        Matrix matrix = new Matrix();
        matrix.setRotate(CameraActivity.getPreviewDegree(this));
        cameraBitmap = Bitmap
                .createBitmap(cameraBitmap, 0, 0, cameraBitmap.getWidth(),
                        cameraBitmap.getHeight(), matrix, true);
        iv_play.setImageBitmap(cameraBitmap);
    }

    /**
     * 将字节数组的图形数据转换为Bitmap
     *
     * @return
     */
//    private Bitmap byte2Bitmap() {
//        // 将byte数组转换成Bitmap对象
//        Bitmap bitmap = BitmapFactory.decodeByteArray(picData, 0, picData.length);
//        return bitmap;
//    }
}
