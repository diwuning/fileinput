package com.h.fileinput.camera;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.h.fileinput.R;
import com.h.fileinput.recorder.LineView;

public class RecordCameraActivity extends Activity {
    private static final String TAG = "RecordCameraActivity";
    private RecorderCameraView mrv_wx;
    private RelativeLayout rl_wx;
    private LineView lv_wx;
    private MediaRecorder recorder;
    private Animator animator;
    private boolean isRunning;
    int lineWidth;
    CircleProgressView circleProgressView;
    int progress=0;

    boolean isLongPressed = false;
    long downTime = 0;
    long pressTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin_record);

        mrv_wx = (RecorderCameraView)findViewById(R.id.mrv_wx);
        rl_wx = (RelativeLayout)findViewById(R.id.rl_wx);
        lv_wx = (LineView)findViewById(R.id.lv_wx);

        circleProgressView = (CircleProgressView) findViewById(R.id.circleProgress);

        //向中间缩短的进度条设置
        ViewTreeObserver observer = rl_wx.getViewTreeObserver();
        observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                lineWidth = rl_wx.getMeasuredWidth();

                return true;
            }
        });

        circleProgressView.setBgColor(getResources().getColor(R.color.text_white));
        circleProgressView.setProgressColor(getResources().getColor(R.color.colorPrimaryDark));
        ViewTreeObserver observerCircle = circleProgressView.getViewTreeObserver();
        observerCircle.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
//                lineWidth = rl_wx.getMeasuredWidth();
                progress = circleProgressView.getmProgress();
                return true;
            }
        });

        //开始录制
//        rl_wx.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                mrv_wx.record(new RecorderCameraView.OnRecordFinishListener() {
//                    @Override
//                    public void onRecordFinish() {
//
//                    }
//                });
//                //设置进度条
//                circleProgressView.setVisibility(View.VISIBLE);
//                startAnimator();
//                animator.addListener(new Animator.AnimatorListener() {
//                    @Override
//                    public void onAnimationStart(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationEnd(Animator animation) {
//                        Log.i("animator", "stop");
//                        mrv_wx.stop();
////                        Intent backIntent = new Intent();
////                        backIntent.putExtra("path", mrv_wx.mVecordFile.getAbsoluteFile().toString());
////                        setResult(RESULT_OK, backIntent);
////                        finish();
//                    }
//
//                    @Override
//                    public void onAnimationCancel(Animator animation) {
//
//                    }
//
//                    @Override
//                    public void onAnimationRepeat(Animator animation) {
//
//                    }
//                });
//                return false;
//            }
//        });

//        rl_wx.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mrv_wx.take();
//            }
//        });
        //长按事件
        rl_wx.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()){
                    case MotionEvent.ACTION_DOWN:
                        downTime = event.getDownTime();
                        pressTime = event.getEventTime();
                        Log.d(TAG, "onTouch: pressTime="+pressTime);

                        handler.postDelayed(task, 100);
                        break;
                    case MotionEvent.ACTION_UP:
                        Log.i("animator", "ACTION_UP");
                        if(isLongPressed){
                            isLongPressed = false;
                            if(animator.isRunning()){
                                animator.end();
                                Log.i("animator", "end");
                                mrv_wx.stop();
                            }
                        }

                        handler.removeCallbacks(task);
                        break;

                }
                return false;
            }
        });
    }



    /*
     * 定时器设置，实现计时
     */
    private Handler handler = new Handler();
    long longPressedTime = 0;
    private Runnable task = new Runnable() {
        public void run() {
            longPressedTime = longPressedTime+100;
            Log.d(TAG, "longPressedTime="+longPressedTime);

            if(longPressedTime > 500){
                Log.d(TAG, "onTouch: longPressedTime="+longPressedTime);
                isLongPressed = true;
                mrv_wx.record(new RecorderCameraView.OnRecordFinishListener() {
                    @Override
                    public void onRecordFinish() {

                    }
                });
                //设置进度条
                circleProgressView.setVisibility(View.VISIBLE);
                startAnimator();
                animator.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Log.i("animator", "stop");
                        mrv_wx.stop();
//                        Intent backIntent = new Intent();
//                        backIntent.putExtra("path", mrv_wx.mVecordFile.getAbsoluteFile().toString());
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
            }else{
                handler.postDelayed(this, 100);
            }
        }
    };

    private void startAnimator(){
//        animator = ObjectAnimator.ofInt(lv_wx,"layoutWidth",lineWidth,0);
        animator = ObjectAnimator.ofInt(circleProgressView,"progress",progress);
        animator.setDuration(60000);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
    }



}
