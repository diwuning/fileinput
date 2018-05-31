package com.h.fileinput.cameratest.camerarecorder;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.h.fileinput.R;
import com.h.fileinput.cameratest.ShowPicActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameraNormalActivity extends Activity {
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    RelativeLayout rl_btn;
    private Camera camera;
    private Camera.Parameters parameters = null;

    Bundle bundle = null; // 声明一个Bundle对象，用来存储数据
    ImageView iv_play,scalePic;
    Button takepicture;
    RelativeLayout rl_playPic;
//    RelativeLayout fl_camera;

    private Drawable iconStart;
    private Drawable iconStop;
    protected boolean isPreview;
    private MediaRecorder mMediaRecorder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);

        iv_play = (ImageView) findViewById(R.id.iv_playPic);
        scalePic = (ImageView) findViewById(R.id.scalePic);
        takepicture = (Button) findViewById(R.id.takepicture);
        iconStart = getResources().getDrawable(R.drawable.play_normal);

        iconStop = getResources().getDrawable(R.drawable.screenshot);
        // 设置缓存路径
        mRecVedioPath = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/hfdatabase/video/temp/");

        if (!mRecVedioPath.exists()) {
            mRecVedioPath.mkdirs();
        }
//        rl_playPic = (RelativeLayout) findViewById(R.id.rl_playPic);
//        fl_camera = (RelativeLayout) findViewById(R.id.fl_camera);
        surfaceView = (SurfaceView) this
                .findViewById(R.id.surfaceView);
        rl_btn = (RelativeLayout) this.findViewById(R.id.buttonLayout);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        surfaceHolder.setFixedSize(176, 144); //设置Surface分辨率
        surfaceHolder.setKeepScreenOn(true);// 屏幕常亮
        surfaceHolder.addCallback(new SurfaceCallback());//为SurfaceView的句柄添加一个回调函数
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
                parameters = camera.getParameters();
                parameters.setPreviewFrameRate(5); // 每秒5帧
                parameters.setPictureFormat(PixelFormat.JPEG);// 设置照片的输出格式
                parameters.set("jpeg-quality", 85);// 照片质量
//                    parameters.set("orientation", "portrait");
                camera.setParameters(parameters);
                camera.setPreviewDisplay(holder); // 设置用于显示拍照影像的SurfaceHolder对象
                camera.setDisplayOrientation(getPreviewDegree(CameraNormalActivity.this));
                camera.startPreview(); // 开始预览
                isPreview = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
            surfaceHolder = holder;
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//            parameters = camera.getParameters(); // 获取各项参数
//            parameters.setPictureFormat(PixelFormat.JPEG); // 设置图片格式
//            parameters.setPreviewSize(width, height); // 设置预览大小
//            parameters.setPreviewFrameRate(5);  //设置每秒显示4帧
//            parameters.setPictureSize(width, height); // 设置保存的图片尺寸
//            parameters.setJpegQuality(80); // 设置照片质量
            surfaceHolder = holder;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (camera != null) {
                if (isPreview) {
                    camera.stopPreview();
                    isPreview = false;
                }
                camera.release(); // 释放照相机
                camera = null;
            }
            surfaceHolder = null;
            surfaceView = null;
            mMediaRecorder = null;

        }
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

    private boolean isRecording = true; // true表示没有录像，点击开始；false表示正在录像，点击暂停
    private File mRecVedioPath;
    private File mRecAudioFile;
    /**
     * 图片被点击触发的时间
     *
     * @param v
     */
    public void imageClick(View v) {
//        if (v.getId() == R.id.scalePic) {
//            if (bundle == null) {
//                Toast.makeText(getApplicationContext(), "请先拍照",
//                        Toast.LENGTH_SHORT).show();
//            } else {
//                Intent intent = new Intent(this, ShowPicActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }
//        }
        if(isRecording){
            /*
             * 点击开始录像
             */
            if (isPreview) {
                camera.stopPreview();
                camera.release();
                camera = null;
            }
            if (mMediaRecorder == null)
                mMediaRecorder = new MediaRecorder();
            else
                mMediaRecorder.reset();

            mMediaRecorder.setPreviewDisplay(surfaceHolder.getSurface());
            mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
            mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
            mMediaRecorder.setVideoSize(320, 240);
            mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);// 设置帧频率，然后就清晰了
//                    mMediaRecorder.setVideoFrameRate(15);
            mMediaRecorder.setOrientationHint(getPreviewDegree(CameraNormalActivity.this));// 输出旋转90度，保持竖屏录制
            try {
                mRecAudioFile = File.createTempFile("Vedio", ".mp4",mRecVedioPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());

            try {
                mMediaRecorder.prepare();
//                timer.setVisibility(View.VISIBLE);
//                handler.postDelayed(task, 1000);
                mMediaRecorder.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            showMsg("开始录制");
            scalePic.setBackgroundDrawable(iconStop);
            isRecording = !isRecording;
        }else{
            /*
                     * 点击停止
                     */
            try {
//                bool = false;

                mMediaRecorder.stop();
//                timer.setText(format(hour) + ":" + format(minute) + ":"+ format(second));
                mMediaRecorder.release();
                mMediaRecorder = null;
                videoRename();
            } catch (Exception e) {
                e.printStackTrace();
            }

            isRecording = !isRecording;
            scalePic.setBackgroundDrawable(iconStart);
            showMsg("录制完成，已保存");
        }
    }

    /*
     * 生成video文件名字
     */
    protected void videoRename() {

        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ "/hfdatabase/video/0/";

        String fileName = new SimpleDateFormat("yyyyMMddHHmmss")
                .format(new Date()) + ".mp4";

        File out = new File(path);

        if (!out.exists()) {
            out.mkdirs();
        }

        out = new File(path, fileName);

        if (mRecAudioFile.exists())
            mRecAudioFile.renameTo(out);

    }

    /*
     * 消息提示
     */
    private Toast toast;

    public void showMsg(String arg) {
        if (toast == null) {
            toast = Toast.makeText(this, arg, Toast.LENGTH_SHORT);
        } else {
            toast.cancel();
            toast.setText(arg);
        }

        toast.show();
    }

    /**
     * 将MainActivity传过来的图片显示在界面当中
     *
     */
    public void setImageBitmap() {
        Bitmap cameraBitmap = BitmapFactory.decodeByteArray(picData, 0, picData.length);
        // 根据拍摄的方向旋转图像（纵向拍摄时要需要将图像选择90度)
        Matrix matrix = new Matrix();
        matrix.setRotate(CameraNormalActivity.getPreviewDegree(this));
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
