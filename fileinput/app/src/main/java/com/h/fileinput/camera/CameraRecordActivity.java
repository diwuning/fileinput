package com.h.fileinput.camera;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.widget.Button;

import com.h.fileinput.R;
import com.h.fileinput.recorder.LineView;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class CameraRecordActivity extends Activity implements View.OnClickListener {
    private SurfaceView sfv_camera;
    private SurfaceHolder mSurfaceHolder;
    private Camera camera;
    private MediaRecorder mMediaRecorder;
    private boolean isPreview;//是否预览
    private Button btn_focus,btn_camera;
    private String savePath = Environment.getExternalStorageDirectory()+"/cameratest/";
    private OnRecordFinishListener mOnRecordFinishListener;// 录制完成回调接口

    private int mWidth;// 视频分辨率宽度
    private int mHeight;// 视频分辨率高度
    private boolean isOpenCamera;// 是否一开始就打开摄像头
    public File mVecordFile = null;// 文件
    private Animator animator;
    private LineView lv_wx;
    int lineWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Window window = getWindow();//获得当前窗口
        requestWindowFeature(Window.FEATURE_NO_TITLE);//设置没有标题
        //设置全屏
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //设置屏幕一直处于高亮状态
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //先将窗口上的显示方式规定死，然后才能设置窗口上的显示组件。
        setContentView(R.layout.activity_camera_record);

        mWidth=320;
        mHeight=240;
        isOpenCamera=true;

        sfv_camera = (SurfaceView)findViewById(R.id.sfv_camera);
        //设置像素
        //sfv_camera.getHolder().setFixedSize(800, 600);
        //设置surfaceView不维护自己的缓冲区,而是等待屏幕的渲染引擎将内容推送到用户面前
        mSurfaceHolder = sfv_camera.getHolder();
        mSurfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        //加入回调方法
        mSurfaceHolder.addCallback(new SurfaceCallBack());

        lv_wx = (LineView) findViewById(R.id.lv_wx);

        btn_focus = (Button)findViewById(R.id.btn_focus);
        btn_focus.setOnClickListener(this);
        btn_camera = (Button)findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(this);
        btn_camera.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                record(new OnRecordFinishListener() {
                    @Override
                    public void onRecordFinish() {

                    }
                });
                //设置进度条
                startAnimator();
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.i("animator", "stop");
                        stop();
//                        Intent backIntent = new Intent();
//                        backIntent.putExtra("path", mVecordFile.getAbsoluteFile().toString());
//                        setResult(RESULT_OK, backIntent);
//                        finish();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                return false;
            }
        });

        //向中间缩短的进度条设置
        ViewTreeObserver observer = lv_wx.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                lineWidth = lv_wx.getMeasuredWidth();
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_focus:
                camera.autoFocus(null);//自动对焦
                break;
            case R.id.btn_camera:
                camera.takePicture(null,null,new TakePictureCallBack());
                break;
        }
    }

    //拍照
    private class TakePictureCallBack implements Camera.PictureCallback{
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0,data.length);
            //判断路径中的文件夹是否存在，不存在则创建
            File newFile = new File(savePath);
            if(!newFile.exists()){
                newFile.mkdir();
            }
            File file = new File(savePath,System.currentTimeMillis()+".jpg");
            try{
                FileOutputStream fos = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                fos.close();
            }catch (IOException ie){
                ie.printStackTrace();
            }
            camera.stopPreview();
            camera.startPreview();

        }
    }

    private class SurfaceCallBack implements SurfaceHolder.Callback{

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            camera = Camera.open();//打开硬件摄像头
            //得到窗口管理器
            WindowManager wm = (WindowManager)getSystemService(Context.WINDOW_SERVICE);
            //得到当前屏幕
            Display display = wm.getDefaultDisplay();
            //得到摄像头的参数
            Camera.Parameters parameters = camera.getParameters();
            //设置照片预览的大小
            parameters.setPreviewSize(display.getWidth(), display.getHeight());
            //设置每秒多少帧
            parameters.setPreviewFrameRate(3);
            //设置照片的格式
            parameters.setPictureFormat(PixelFormat.JPEG);
            //设置照片的质量
            //parameters.setJpegQuality(85);
            //设置照片的大小
            //parameters.setPictureSize(display.getWidth(), display.getHeight());
            //camera.setParameters(parameters);
            try{
                //通过SurfaceView显示取景画面
                camera.setPreviewDisplay(mSurfaceHolder);
                camera.setDisplayOrientation(getPreviewDegree(CameraRecordActivity.this));
            }catch (IOException ie){
                ie.printStackTrace();
            }
            camera.startPreview();//开始预览
            isPreview = true;

        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
            if(camera != null){
                if(isPreview){
                    camera.stopPreview();
                    camera.release();
                }
            }
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

    //录制完成回调接口
    public interface OnRecordFinishListener {
        public void onRecordFinish();
    }

    private void initRecord() throws IOException {
        mMediaRecorder = new MediaRecorder();
//        mMediaRecorder.reset();
//        mMediaRecorder.prepare();
        if (camera != null)
            mMediaRecorder.setCamera(camera);
        mMediaRecorder.setOnErrorListener(null);
        mMediaRecorder.setPreviewDisplay(mSurfaceHolder.getSurface());
        mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);// 视频源
        mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);// 音频源
        mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);// 视频输出格式
        mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);// 音频格式
        mMediaRecorder.setVideoSize(mWidth, mHeight);// 设置分辨率：
        // mMediaRecorder.setVideoFrameRate(16);// 这个我把它去掉了，感觉没什么用
        mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);// 设置帧频率，然后就清晰了
        mMediaRecorder.setOrientationHint(270);// 输出旋转90度，保持竖屏录制
        mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);// 视频录制格式
        // mediaRecorder.setMaxDuration(Constant.MAXVEDIOTIME * 1000);
        mMediaRecorder.setOutputFile(mVecordFile.getAbsolutePath());
        mMediaRecorder.prepare();
        try {
            mMediaRecorder.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (RuntimeException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始录制视频
     *
     * @author liuyinjun
     * @date 2015-2-5
    //     * @param fileName
    //     *            视频储存位置
     * @param onRecordFinishListener
     *            达到指定时间之后回调接口
     */
    public void record(final OnRecordFinishListener onRecordFinishListener) {
        this.mOnRecordFinishListener = onRecordFinishListener;
        createRecordDir();
        try {
            if (!isOpenCamera)// 如果未打开摄像头，则打开
                initCamera();
            initRecord();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void createRecordDir() {
        File sampleDir = new File(Environment.getExternalStorageDirectory() + File.separator+"RecordVideo/");

        if (!sampleDir.exists()) {
            sampleDir.mkdirs();
        }
        File vecordDir = sampleDir;
        // 创建文件
        try {
            mVecordFile = File.createTempFile("recording", ".mp4", vecordDir);//mp4格式
            //LogUtils.i(mVecordFile.getAbsolutePath());
            Log.d("Path:",mVecordFile.getAbsolutePath());
        } catch (IOException e) {
        }
    }

    /**
     * 初始化摄像头
     *
     * @author lip
     * @date 2015-3-16
     * @throws IOException
     */
    private void initCamera() throws IOException {


        if (camera != null) {
            freeCameraResource();
        }
        try {
//            mCamera = Camera.open();
//            if(findFrontCamera()){
//                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
//            }else{
                camera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
//            }

        } catch (Exception e) {
            e.printStackTrace();
            freeCameraResource();
        }
        if (camera == null)
            return;

//        setCameraParams();
//        camera.setDisplayOrientation(90);
        camera.setPreviewDisplay(mSurfaceHolder);
        camera.startPreview();
        camera.unlock();
    }

    /**
     * 释放摄像头资源
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    private void freeCameraResource() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.lock();
            camera.release();
            camera = null;
        }
    }

    //判断前置摄像头是否存在
    private boolean findFrontCamera(){
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
            if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT ) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return true;
            }
        }
        return false;
    }

    //判断后置摄像头是否存在
    private int findBackCamera(){
        int cameraCount = 0;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        cameraCount = Camera.getNumberOfCameras(); // get cameras number

        for ( int camIdx = 0; camIdx < cameraCount;camIdx++ ) {
            Camera.getCameraInfo( camIdx, cameraInfo ); // get camerainfo
            if ( cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK ) {
                // 代表摄像头的方位，目前有定义值两个分别为CAMERA_FACING_FRONT前置和CAMERA_FACING_BACK后置
                return camIdx;
            }
        }
        return -1;
    }

    private void startAnimator(){
        animator = ObjectAnimator.ofInt(lv_wx,"layoutWidth",lineWidth,0);
        animator.setDuration(6000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

    /**
     * 停止拍摄
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void stop() {
        stopRecord();
        releaseRecord();
        freeCameraResource();
    }

    /**
     * 停止录制
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    public void stopRecord() {
        if (mMediaRecorder != null) {
            // 设置后不会崩
            mMediaRecorder.setOnErrorListener(null);
            mMediaRecorder.setPreviewDisplay(null);
            try {
                mMediaRecorder.stop();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (RuntimeException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 释放资源
     *
     * @author liuyinjun
     * @date 2015-2-5
     */
    private void releaseRecord() {
        if (mMediaRecorder != null) {
            mMediaRecorder.setOnErrorListener(null);
            try {
                mMediaRecorder.release();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mMediaRecorder = null;
    }
}
