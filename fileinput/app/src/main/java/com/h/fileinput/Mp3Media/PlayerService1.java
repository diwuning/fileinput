package com.h.fileinput.Mp3Media;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

/**
 * Created by h on 2016/3/1 0001.
 */
public class PlayerService1 extends Service {
    private MediaPlayer mediaPlayer;
    private String path;
    private int msg;
    private boolean isPause;
    private int current = 0; //记录当前正在播放的音乐
    private List<Mp3Info> mp3Infos;
    private int status = 3; //播放状态，默认为顺序播放
    private MyReceiver myReceiver;//自定义广播接收器
    private int currentTime; //播放进度
    private int duration;//播放长度
    //服务要发送的一些action
    public static final String UPDATE_ACTION = "com.h.fileinput.action.UPDATE_ACTION"; //更新动作
    public static final String CTL_ACTION = "com.h.fileinput.action.CTL_ACTION";//控制动作
    public static final String MUSIC_CURRENT = "com.h.fileinput.action.MUSIC_CURRENT"; //当前音乐播放时间更新动作
    public static final String MUSIC_DURATION = "com.h.fileinput.action.MUSIC_DURATION"; //新音乐长度更新动作
    public static final String REPEAT_STATE = "com.h.fileinput.action.REPEAT_STATE";//循环动作
    /*
    * handler用来接收消息，来发送广播更新播放时间
    * */
    private Handler handler = new Handler(){
      public void handleMessage(Message msg){
          if(msg.what ==1){
              if(mediaPlayer != null){
                  currentTime = mediaPlayer.getCurrentPosition();//获取当前音乐播放的位置
                  //Toast.makeText(getApplicationContext(),"handler=",Toast.LENGTH_SHORT).show();
                  Intent intent = new Intent();
                  intent.setAction(MUSIC_CURRENT);
                  intent.putExtra("currentTime",currentTime);
                  sendBroadcast(intent);
                  handler.sendEmptyMessageDelayed(1,1000);
              }
          }
      }
    };

    public void onCreate(){
        super.onCreate();
        Log.d("service", "service created");
        mediaPlayer = new MediaPlayer();
        mp3Infos = MediaUtil.getMp3Infos(PlayerService1.this);

        //设置音乐播放完成时的监听器
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if(status ==1){//单曲循环
                    mediaPlayer.start();
                    Toast.makeText(getApplicationContext(),"11单曲循环",Toast.LENGTH_SHORT).show();
                }else if(status == 2) {//全部循环
                    current++;
                    if(current>mp3Infos.size()-1){//变为第一首的位置继续播放
                        current = 0;
                    }
                    Intent sendIntent = new Intent(UPDATE_ACTION);
                    sendIntent.putExtra("current",current);
                    Toast.makeText(getApplicationContext(),"11全部循环",Toast.LENGTH_SHORT).show();
                    //发送广播，将被activity组件中的BroadcastReceiver接收到
                    sendBroadcast(sendIntent);
                    path = mp3Infos.get(current).url;
                    play(0);
                }else if(status == 3){//顺序播放
                    current++;//下一首位置
                    if(current<=mp3Infos.size()-1){
                        Intent sendIntent = new Intent(UPDATE_ACTION);
                        sendIntent.putExtra("current",current);
                        //发送广播，
                        sendBroadcast(sendIntent);
                        Toast.makeText(getApplicationContext(),"11顺序播放",Toast.LENGTH_SHORT).show();
                        path = mp3Infos.get(current).url;
                        play(0);
                    }else{
                        mediaPlayer.seekTo(0);
                        current = 0;
                        Intent sendIntent = new Intent(UPDATE_ACTION);
                        sendIntent.putExtra("current", current);
                        sendBroadcast(sendIntent);
                    }
                }else if(status == 4){//随机播放
                    current = getRandomIndex(mp3Infos.size()-1);
                    System.out.println("currentIndex ->"+current);
                    Intent sendIntent = new Intent(UPDATE_ACTION);
                    sendIntent.putExtra("current",current);
                    //发送广播
                    sendBroadcast(sendIntent);
                    Toast.makeText(getApplicationContext(),"11随机播放",Toast.LENGTH_SHORT).show();
                    path = mp3Infos.get(current).url;
                    play(0);
                }
            }
        });
        myReceiver = new MyReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(CTL_ACTION);
        registerReceiver(myReceiver,filter);
    }

    //获取随机位置
    protected int getRandomIndex(int end){
        int index = (int)(Math.random()*end);
        return index;
    }

    public IBinder onBind(Intent arg0){
        return null;
    }

    public void onStart(Intent intent,int startId){
        path = intent.getStringExtra("url");
        current = intent.getIntExtra("listPosition", -1); //当前播放的歌曲在mp3Infos的位置
        msg = intent.getIntExtra("MSG",0); //播放信息
        if(msg == AppConstant.PlayerMsg.PLAY_MSG){//直接播放音乐
            Toast.makeText(getApplicationContext(),msg+"=====播放",Toast.LENGTH_SHORT).show();
            play(0);
        }else if(msg == AppConstant.PlayerMsg.PAUSE_MSG){
            Toast.makeText(getApplicationContext(),msg+"=====暂停",Toast.LENGTH_SHORT).show();
            pause();
        }else if(msg == AppConstant.PlayerMsg.STOP_MSG){
            Toast.makeText(getApplicationContext(),msg+"=====停止",Toast.LENGTH_SHORT).show();
            stop();
        }else if(msg == AppConstant.PlayerMsg.CONTINUE_MSG){//继续播放
            Toast.makeText(getApplicationContext(),msg+"=====继续",Toast.LENGTH_SHORT).show();
            resume();
        }else if(msg == AppConstant.PlayerMsg.PRIVIOUS_MSG){//上一首
            Toast.makeText(getApplicationContext(),msg+"=====上一首",Toast.LENGTH_SHORT).show();
            previous();
        }else if(msg == AppConstant.PlayerMsg.NEXT_MSG){
            next();
        }else if(msg == AppConstant.PlayerMsg.PROGRESS_CHANGE){//进度更新
            currentTime = intent.getIntExtra("progress",-1);
            Log.i("playerservice","进度更新");
            play(currentTime);
        }else if(msg == AppConstant.PlayerMsg.PLAYING_MSG){
            handler.sendEmptyMessage(1);
        }
        super.onStart(intent,startId);

    }
    /*
    * 播放音乐
    * */
    private void play(int currentTime){
        try{
            mediaPlayer.reset();//把各项参数恢复到初始状态
            mediaPlayer.setDataSource(path);
            mediaPlayer.prepare();//进行缓冲
            mediaPlayer.setOnPreparedListener(new PreparedListener(currentTime));//注册一个监听器
            handler.sendEmptyMessage(1);
        }catch (IOException ie){
            ie.printStackTrace();
        }
    }
    private void pause(){
        if(mediaPlayer != null && mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            isPause = true;
        }
    }
    private void resume(){
        if(isPause){
            mediaPlayer.start();
            isPause = false;
        }
    }
    private void previous(){
        Intent sendIntent = new Intent(UPDATE_ACTION);
        sendIntent.putExtra("current",current);
        sendBroadcast(sendIntent);
        play(0);
    }
    private void next(){
        Intent sendIntent = new Intent(UPDATE_ACTION);
        sendIntent.putExtra("current",current);
        sendBroadcast(sendIntent);
        play(0);
    }
    private void stop(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            try {
                mediaPlayer.prepare();//在调用stop后如果需要通过start进行播放，需要之前调用prepare函数
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    public void onDestroy(){
        if(mediaPlayer != null){
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
    /*
    * 实现一个onPrepareListener接口，当音乐准备好的时候开始播放
    * */
    private final class PreparedListener implements MediaPlayer.OnPreparedListener{
        private int currentTime;
        public PreparedListener(int currentTime){
            this.currentTime = currentTime;
        }


        @Override
        public void onPrepared(MediaPlayer mp) {
            mediaPlayer.start();
            if(currentTime>0){//如果音乐不是从头播放
                mediaPlayer.seekTo(currentTime);
                Log.i("playerservice",String.valueOf(currentTime));
            }
            Intent intent = new Intent();
            intent.setAction(MUSIC_DURATION);
            duration = mediaPlayer.getDuration();
            //Toast.makeText(getApplicationContext(),"prepared",Toast.LENGTH_SHORT).show();
            intent.putExtra("duration",duration); //通过intent来传递歌曲的总长度
            sendBroadcast(intent);
        }
    }

    public class MyReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int control = intent.getIntExtra("control",-1);
            switch (control){
                case 1:
                    status = 1;//将播放状态置为1表示单曲循环
                    Toast.makeText(context,"111111",Toast.LENGTH_SHORT).show();
                    break;
                case 2:
                    status = 2;//全部循环
                    Toast.makeText(context,"2222222222222",Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    status = 3;//顺序播放
                    Toast.makeText(context,"3333333",Toast.LENGTH_SHORT).show();
                    break;
                case 4:
                    status = 4; //随机播放
                    Toast.makeText(context,"4444444",Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
