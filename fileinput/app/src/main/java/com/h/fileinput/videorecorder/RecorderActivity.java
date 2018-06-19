package com.h.fileinput.videorecorder;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.h.fileinput.R;

import java.io.IOException;

public class RecorderActivity extends ActionBarActivity {

    private MovieRecorderView movieRV;
    private Button startBtn,stopBtn,playBtn,pauseBtn,takeBtn;
    private SurfaceView playView;
    private MediaPlayer player;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recorder);
        //防止截屏
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_SECURE);

        initViews();
        initEvents();
        init();
    }

    private void init(){
        player = new MediaPlayer();
        playView = (SurfaceView)this.findViewById(R.id.play_surfaseV);
        //设置SurfaceView自己不管理的缓冲区
        playView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        playView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (position > 0) {
                    //开始播放
                    play();
                    player.seekTo(position);
                    position = 0;
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
    }

    private void initViews(){
        movieRV = (MovieRecorderView)findViewById(R.id.movie_rv);
        //录制
        startBtn = (Button)findViewById(R.id.start_btn);
        stopBtn = (Button)findViewById(R.id.stop_btn);
        //播放
        playBtn = (Button)findViewById(R.id.play_btn);
        pauseBtn = (Button)findViewById(R.id.pause_btn);
        //拍照
        takeBtn = (Button) findViewById(R.id.take_btn);
    }

    private void initEvents(){
        //开始录制
        startBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieRV.record(new MovieRecorderView.OnRecordFinishListener() {
                    @Override
                    public void onRecordFinish() {

                    }
                });
            }
        });
        //停止录制
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                movieRV.stop();
            }
        });
        //播放已录制视频
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play();
            }
        });
        //暂停
        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(player.isPlaying()){
                    player.pause();
                }else{
                    player.start();
                }
            }
        });
        takeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    protected void onPause(){
        //判断是否正在播放
        if(player.isPlaying()){
            //如果正在播放我们就先保存这个播放位置
            position = player.getCurrentPosition();
            player.stop();
        }
        super.onPause();
    }

    private void play(){
        Log.i("play","");
        player.reset();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //设置需要播放的视频
        String path = movieRV.getmVecordFile().getAbsolutePath();
        try {
            player.setDataSource(path);
            Log.i("play:",path);
            //把视频画面输出到SurfaceView
            player.setDisplay(playView.getHolder());
            player.prepare();
            //播放
            player.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
