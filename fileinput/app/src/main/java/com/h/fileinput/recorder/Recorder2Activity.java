package com.h.fileinput.recorder;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.MediaRecorder;
import android.os.Environment;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.h.fileinput.R;
import com.h.fileinput.video.SurfaceActivity;

import java.io.File;
import java.io.IOException;

public class Recorder2Activity extends Activity implements Camera.PreviewCallback {
    private SurfaceView sfv_record2;
    private RelativeLayout rl_record2;
    private SurfaceHolder holder2;
    MediaRecorder recorder2;
    String path = Environment.getExternalStorageDirectory()+File.separator+"RecordVideo/";
    File saveFile;
    Camera camera;
    private boolean isRunning = false;
    //设置拍摄视频时的进度条
    private LineView lv_time;
    int lineWidth;
    Animator animator;
    //设置拍摄开始音和结束音
    AudioManager manager;
    int volume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder2);
//        btn_startRecord2 = (Button)findViewById(R.id.btn_startRecord2);
//        btn_stopRecord2 = (Button)findViewById(R.id.btn_stopRecord2);
        rl_record2 = (RelativeLayout)findViewById(R.id.ll_record2);
        rl_record2.setOnClickListener(new TestRecordListener());
//        btn_stopRecord2.setOnClickListener(new TestRecordListener());
        sfv_record2 = (SurfaceView)findViewById(R.id.sfv_record2);
        holder2 = sfv_record2.getHolder();
        //holder2.addCallback(this);
        holder2.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

//        btn_stopRecord2.setEnabled(false);
        // 设置分辨率
        holder2.setFixedSize(320, 240);

        lv_time = (LineView)findViewById(R.id.lv_time);
        ViewTreeObserver observer = lv_time.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                lineWidth = lv_time.getMeasuredWidth();
                Log.i("lineWidth", lineWidth + "");
                return true;
            }
        });

        //
        manager = (AudioManager)getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        manager.setStreamMute(AudioManager.STREAM_SYSTEM, false);
        volume = manager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        Toast.makeText(getApplicationContext(),volume+"",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        Toast.makeText(getApplicationContext(),"11111",Toast.LENGTH_SHORT).show();
    }

    class TestRecordListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.ll_record2:
//                    // 创建保存录制视频的视频文件
                    saveFile = new File(path + "testvideo.3gp");
                    // 创建MediaPlayer对象
                    recorder2 = new MediaRecorder();
                    recorder2.reset();

                    // 设置从麦克风采集声音(或来自录像机的声音AudioSource.CAMCORDER)
                    recorder2.setAudioSource(MediaRecorder.AudioSource.MIC);

                    recorder2.setOnErrorListener(null);
                    // 设置从摄像头采集图像
                    recorder2.setVideoSource(MediaRecorder.VideoSource.CAMERA);
                    recorder2.setOrientationHint(90);
                    // 设置视频文件的输出格式
                    // 必须在设置声音编码格式、图像编码格式之前设置
                    recorder2.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                    // 设置声音编码的格式
                    recorder2.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
                    // 设置图像编码的格式
                    recorder2.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
                    recorder2.setVideoSize(320, 240);
                    // 每秒 4帧
//                    recorder.setVideoFrameRate(20);
                    recorder2.setOutputFile(saveFile.getAbsolutePath());
                    // 指定使用SurfaceView来预览视频
                    recorder2.setPreviewDisplay(holder2.getSurface());
                    try {
                        recorder2.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // 开始录制
                    recorder2.start();
                    System.out.println("---recording---");
//                    // 让record按钮不可用。
//                    btn_startRecord2.setEnabled(false);
//                    // 让stop按钮可用。
//                    btn_stopRecord2.setEnabled(true);
                    isRunning = true;
                    startAnimator();
                    animator.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                            Log.i("animator", "stop");
                            recorder2.stop();
                            recorder2.release();
                            recorder2= null;
                            //camera.lock();
//                            btn_stopRecord2.setEnabled(false);
//                            btn_startRecord2.setEnabled(true);
                            Intent backIntent = new Intent();
                            backIntent.putExtra("path",saveFile.getAbsoluteFile().toString());
                            setResult(RESULT_OK, backIntent);
                            finish();
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    });
                    break;
            }
        }
    }

    private void startAnimator(){
        animator = ObjectAnimator.ofInt(lv_time,"layoutWidth",lineWidth,0);
        animator.setDuration(6000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }

}
