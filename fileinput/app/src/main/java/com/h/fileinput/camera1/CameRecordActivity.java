package com.h.fileinput.camera1;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.h.fileinput.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CameRecordActivity extends AppCompatActivity {
    private Button mVideoStartBtn;
    private SurfaceView mSurfaceview;
    private MediaRecorder mMediaRecorder;
    private SurfaceHolder mSurfaceHolder;
    private File mRecVedioPath;
    private File mRecAudioFile;
    private TextView timer;
    private int hour = 0;
    private int minute = 0;
    private int second = 0;
    private boolean bool;
    private int parentId;
    protected Camera camera;
    protected boolean isPreview;
    private Drawable iconStart;
    private Drawable iconStop;
    private boolean isRecording = true; // true表示没有录像，点击开始；false表示正在录像，点击暂停

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置全屏
//        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
//        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        setContentView(R.layout.activity_came_record);

        iconStart = getResources().getDrawable(R.drawable.play_normal);

        iconStop = getResources().getDrawable(R.drawable.screenshot);

        parentId = getIntent().getIntExtra("parentId", 0);

        timer = (TextView) findViewById(R.id.arc_hf_video_timer);

        mVideoStartBtn = (Button) findViewById(R.id.arc_hf_video_start);

        mSurfaceview = (SurfaceView) this.findViewById(R.id.arc_hf_video_view);

        // 设置计时器不可见
        timer.setVisibility(View.GONE);

        // 设置缓存路径
        mRecVedioPath = new File(Environment.getExternalStorageDirectory()
                .getAbsolutePath() + "/hfdatabase/video/temp/");

        if (!mRecVedioPath.exists()) {
            mRecVedioPath.mkdirs();
        }

        // 绑定预览视图
        final SurfaceHolder holder = mSurfaceview.getHolder();

        holder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (camera != null) {
                    if (isPreview) {
                        camera.stopPreview();
                        isPreview = false;
                    }
                    camera.release();
                    camera = null; // 记得释放
                }
                mSurfaceview = null;
                mSurfaceHolder = null;
                mMediaRecorder = null;
            }

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    camera = Camera.open();
                    Camera.Parameters parameters = camera.getParameters();
                    parameters.setPreviewFrameRate(5); // 每秒5帧
                    parameters.setPictureFormat(PixelFormat.JPEG);// 设置照片的输出格式
                    parameters.set("jpeg-quality", 85);// 照片质量
//                    parameters.set("orientation", "portrait");
                    camera.setParameters(parameters);
                    camera.setPreviewDisplay(holder);
                    camera.setDisplayOrientation(getPreviewDegree(CameRecordActivity.this));
                    camera.startPreview();
                    isPreview = true;
//                    initCamera();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                mSurfaceHolder = holder;
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format,int width, int height) {
                mSurfaceHolder = holder;
            }
        });

        holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mVideoStartBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isRecording) {
                    /*
                     * 点击开始录像
                     */
                    if (isPreview) {
                        camera.stopPreview();
                        camera.release();
                        camera = null;
                    }

                    second = 0;
                    minute = 0;
                    hour = 0;
                    bool = true;
                    if (mMediaRecorder == null)
                        mMediaRecorder = new MediaRecorder();
                    else
                        mMediaRecorder.reset();
//                    initCamera();
//                    mMediaRecorder.setCamera(camera);
                    mMediaRecorder.setPreviewDisplay(holder.getSurface());
                    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
                    mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
                    mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);
                    mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    mMediaRecorder.setVideoSize(320, 240);
                    mMediaRecorder.setVideoEncodingBitRate(1 * 1024 * 1024);// 设置帧频率，然后就清晰了
//                    mMediaRecorder.setVideoFrameRate(15);
                    mMediaRecorder.setOrientationHint(270);// 输出旋转90度，保持竖屏录制
                    try {
                        mRecAudioFile = File.createTempFile("Vedio", ".mp4",mRecVedioPath);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mMediaRecorder.setOutputFile(mRecAudioFile.getAbsolutePath());

                    try {
                        mMediaRecorder.prepare();
                        timer.setVisibility(View.VISIBLE);
                        handler.postDelayed(task, 1000);
                        mMediaRecorder.start();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    showMsg("开始录制");
                    mVideoStartBtn.setBackgroundDrawable(iconStop);
                    isRecording = !isRecording;
                } else {
                    /*
                     * 点击停止
                     */
                    try {
                        bool = false;

                        mMediaRecorder.stop();
                        timer.setText(format(hour) + ":" + format(minute) + ":"+ format(second));
                        mMediaRecorder.release();
                        mMediaRecorder = null;
                        videoRename();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    isRecording = !isRecording;
                    mVideoStartBtn.setBackgroundDrawable(iconStart);
                    showMsg("录制完成，已保存");

                    try {
                        initCamera();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Button btnImgStart = (Button) findViewById(R.id.arc_hf_img_start);

        btnImgStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (mMediaRecorder != null) {

                    try {
                        bool = false;
                        mMediaRecorder.stop();
                        timer.setText(format(hour) + ":" + format(minute) + ":"+ format(second));
                        mMediaRecorder.release();
                        mMediaRecorder = null;
                        videoRename();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    isRecording = !isRecording;

                    mVideoStartBtn.setBackgroundDrawable(iconStart);
                    showMsg("录制完成，已保存");

                    try {
                        initCamera();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

                if (camera != null) {
                    camera.autoFocus(null);
                    camera.takePicture(null, null, new Camera.PictureCallback() {

                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {

                            Bitmap bitmap = BitmapFactory.decodeByteArray(data,0 , data.length);

                            Matrix matrix = new Matrix();

//                            // 设置缩放
//                            matrix.postScale(5f, 4f);
//
//                            bitmap = Bitmap.createBitmap(bitmap, 0, 0,
//                                    bitmap.getWidth(), bitmap.getHeight(),
//                                    matrix, true);

                            String path = Environment.getExternalStorageDirectory()
                                    .getAbsolutePath()+ "/hfdatabase/img/"
                                    + String.valueOf(parentId) + "/";

                            String fileName = new SimpleDateFormat(
                                    "yyyyMMddHHmmss").format(new Date())
                                    + ".jpg";
                            File out = new File(path);

                            if (!out.exists()) {
                                out.mkdirs();
                            }
                            out = new File(path, fileName);

                            try {
                                FileOutputStream outStream = new FileOutputStream(out);
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100,outStream);
                                outStream.close();
                                camera.startPreview();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            showMsg("拍照成功");
                        }
                    }); // 拍照
                }
            }
        });

    }

    //初始化相机
    public void initCamera(){
        camera = Camera.open();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setPreviewFrameRate(5); // 每秒5帧
        parameters.setPictureFormat(PixelFormat.JPEG);// 设置照片的输出格式
        parameters.set("jpeg-quality", 85);// 照片质量
        parameters.set("orientation", "portrait");
        camera.setParameters(parameters);
        try {
            camera.setPreviewDisplay(mSurfaceHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        camera.setDisplayOrientation(getPreviewDegree(CameRecordActivity.this));
        camera.startPreview();
        isPreview = true;
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

    /*
     * 生成video文件名字
     */
    protected void videoRename() {

        String path = Environment.getExternalStorageDirectory()
                .getAbsolutePath()+ "/hfdatabase/video/"
                + String.valueOf(parentId) + "/";

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
     * 定时器设置，实现计时
     */
    private Handler handler = new Handler();
    int count = 0;
    private Runnable task = new Runnable() {
        public void run() {
            count ++;
            int timeTotal = 60*1000;
            if(count >= timeTotal){
                bool = false;
            }

            if (bool) {
                handler.postDelayed(this, 1000);
                second++;
                if (second >= 60) {
                    minute++;
                    second = second % 60;
                }
                if (minute >= 60) {
                    hour++;
                    minute = minute % 60;
                }
                timer.setText(format(hour) + ":" + format(minute) + ":" + format(second));

            }

        }

    };

    /*

     * 格式化时间

     */

    public String format(int i) {
        String s = i + "";

        if (s.length() == 1) {
            s = "0" + s;
        }
        return s;

    }

    /*
     * 覆写返回键监听
     */
    @Override
    public void onBackPressed() {
        if (mMediaRecorder != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            videoRename();
        }
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
        onBackPressed();
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
}
