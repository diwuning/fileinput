package com.h.fileinput.video;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.h.fileinput.Mp3Media.MediaUtil;
import com.h.fileinput.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;
/*
* 播放视频，并可以截屏
* 视频时长转换去时区差
* 进度条实时更新
* */

public class SurfaceActivity extends Activity implements SurfaceHolder.Callback {
    MediaPlayer player;
    SurfaceView surfaceView;
    SurfaceHolder surfaceHolder;
    VideoInfo videoInfo;
    ImageView iv_videoplay,iv_videoPause,iv_videoPrevious,iv_videoNext,iv_shot,iv_display;
    SeekBar sb_video;
    RelativeLayout rl_videoSeek;
    private final static String SAVEPATH = Environment.getExternalStorageDirectory()+"/screenshot/";
    TextView tv_videoStart,tv_videoEnd;
    private long videoStart,videoEnd,totalNum,moveStep,screenWidth,currentTime;
    VideoReceiver videoReceiver;
    int videoProgress;//进度条的当前进度
    boolean isDisplay=false;//是否显示进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_surface);
        surfaceView = (SurfaceView)findViewById(R.id.sfv_video);
        surfaceHolder = surfaceView.getHolder();  //SurfaceHolder是SurfaceView的控制接口
        surfaceHolder.addCallback(this);//因为这个类实现了SurfaceHolder.Callback接口，所以回调参数直接this
        surfaceHolder.setFixedSize(320, 220); //显示的分辨率，不设置为默认
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
//        ibtn_play = (ImageButton)findViewById(R.id.ibtn_play);
        iv_videoplay = (ImageView)findViewById(R.id.iv_videoplay);
        iv_videoplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(currentTime);
            }
        });
        iv_videoPause = (ImageView)findViewById(R.id.iv_videoPause);
        iv_videoPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
            }
        });
        iv_videoPrevious  = (ImageView)findViewById(R.id.iv_vPrevious);
        iv_videoNext  = (ImageView)findViewById(R.id.iv_videoNext);
        iv_videoNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.stop();
                finish();
            }
        });
        List<VideoInfo>  videoInfos = MediaUtil.getVideoList(getApplicationContext());
        Toast.makeText(getApplicationContext(),videoInfos.size()+"",Toast.LENGTH_SHORT).show();
        if(videoInfos != null && videoInfos.size() !=0){
            videoInfo = videoInfos.get(1);
        }


        //点击播放画面会显示进度条，再点击一次，进度条消失
        rl_videoSeek = (RelativeLayout)findViewById(R.id.rl_videoSeek);
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isDisplay){
                    rl_videoSeek.setVisibility(View.VISIBLE);
                    isDisplay = true;
                }else{
                    rl_videoSeek.setVisibility(View.INVISIBLE);
                    isDisplay = false;
                }

            }
        });
        //进度条操作
        sb_video = (SeekBar)findViewById(R.id.sb_video);
        tv_videoStart = (TextView)findViewById(R.id.tv_videoStart);
        tv_videoEnd = (TextView)findViewById(R.id.tv_videoEnd);
        videoStart = 0;
        videoEnd = videoInfo.duration;
//        videoEnd = 0;
        totalNum = videoEnd - videoStart;
        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        moveStep = screenWidth/totalNum;
        sb_video.setMax((int) totalNum);
        sb_video.setProgress(0);
        tv_videoEnd.setText(dateFormat(totalNum));

        sb_video.setOnSeekBarChangeListener(new VideoOnSeekBarChangeListener());
        //为更新进度条的receiver注册
        videoReceiver = new VideoReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.h.fileinput.action.VIDEO_CURRENT");
        registerReceiver(videoReceiver,intentFilter);

        iv_display = (ImageView)findViewById(R.id.iv_display);

        //截屏
        iv_shot = (ImageView)findViewById(R.id.iv_shot);
        if(videoInfo == null){
            iv_shot.setEnabled(false);
        }
        iv_shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                player.pause();
                Bitmap bitmap = null;
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(videoInfo.path);
                int currentPosition = player.getCurrentPosition();
                bitmap = retriever.getFrameAtTime(currentPosition*1000,MediaMetadataRetriever.OPTION_CLOSEST_SYNC);

                File dirFile = new File(SAVEPATH);
                if(!dirFile.exists()){
                    dirFile.mkdir();
                }
                File file = new File(SAVEPATH+System.currentTimeMillis()+".jpg");
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(file);

                    bitmap.compress(Bitmap.CompressFormat.JPEG,100,fos);
                    iv_display.setImageBitmap(bitmap);
                    fos.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally {
                    retriever.release();
                }
                player.start();
            }
        });

    }
    //发送广播，实时更新播放位置
    private Handler videoHandler = new Handler(){
        public void handleMessage(Message msg){
            if(msg.what == 1){
                if(player != null){
                    currentTime = player.getCurrentPosition();//获取当前播放位置
                    Intent intent = new Intent("com.h.fileinput.action.VIDEO_CURRENT");
                    intent.putExtra("currentTime",currentTime);
                    sendBroadcast(intent);
                    videoHandler.sendEmptyMessageDelayed(1,1000);
                }
            }
        }
    };

    class VideoReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            long videoCurrent = intent.getLongExtra("currentTime",-1);
            int videoCurrents = Integer.parseInt(videoCurrent+"");
            sb_video.setProgress(videoCurrents);
            tv_videoStart.setText(dateFormat(videoCurrent));
        }
    }

    //将视频时长转化成时间格式，并去掉时区差
    public String dateFormat(long duration1){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");

        //获取当前时区
        Calendar calendar = Calendar.getInstance();
        TimeZone currentZone = calendar.getTimeZone();
        //获取减去时区差后的时间
        long zoneTime = duration1 - currentZone.getRawOffset();
        String dateStr = simpleDateFormat.format(zoneTime);

        return dateStr;
    }

    private void play(long currentTime){
        player.reset();
        try {
            String path = "";
            if(videoInfo == null){
               path = "http://line.xcook.cn:8888/CookbookResourcePlatform-api/v1/cookbook/cookbook.playurl.get";
            }else{
                path = videoInfo.path;
            }
            player.setDataSource(path);
            player.prepare();
            player.setOnPreparedListener(new VideoOnPreparedLisenter(currentTime));
            videoHandler.sendEmptyMessage(1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //视频播放监听
    public class VideoOnPreparedLisenter implements MediaPlayer.OnPreparedListener{
        private long currentTime;

        public VideoOnPreparedLisenter(long currentTime){
            this.currentTime = currentTime;
        }

        @Override
        public void onPrepared(MediaPlayer mp) {
            player.start();
            if(currentTime>0){
                player.seekTo((int)currentTime);
            }
        }
    }

    //进度条改变监听事件
    class VideoOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            //tv_videoStart.setText(progress);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            videoProgress = seekBar.getProgress();
            play(videoProgress);
        }
    }

    //在创建时激发，一般在这里调用画图的线程。
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //必须在surface创建后才能初始化MediaPlayer，否则不会显示图像
        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
        //player.setVolume(20, 20);
        //设置显示视频显示在SurfaceHolder上
        player.setDisplay(surfaceHolder);
        try {
            player.setDataSource(videoInfo.path);
            player.prepare();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //在surface的大小发生改变时激发
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //销毁时激发，一般在这里将画图的线程停止、释放。
    }

    protected void onDestroy(){
        super.onDestroy();
        unregisterReceiver(videoReceiver);
        if(player.isPlaying()){
            player.stop();
        }
        player.release();
        player = null;

    }
}
