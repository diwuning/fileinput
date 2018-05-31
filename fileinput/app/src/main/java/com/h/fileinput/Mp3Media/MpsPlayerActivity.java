package com.h.fileinput.Mp3Media;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.h.fileinput.R;
import com.h.fileinput.Mp3Media.TextMoveLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MpsPlayerActivity extends Activity {
    SimpleAdapter simpleAdapter;
    private ListView lv_mp3list;
    private ImageView iv_player,iv_previous,iv_next;
    private TextView tv_musicTitle;
    private ImageView iv_wrap;
    List<Mp3Info> mp3Infos;
    Mp3Info mp3Info;
    private ListView lv_popup;

    private String title;       //歌曲标题
    private String artist;      //歌曲艺术家
    private String url;         //歌曲路径
    private int listPosition;   //播放歌曲在mp3Infos的位置
    private int currentTime;    //当前歌曲播放时间
    private int duration;       //歌曲长度
    private int flag;           //播放标识

    private boolean isPlaying;              // 正在播放
    private boolean isPause;                // 暂停

    private TextView tv_currentTime;//显示当前歌曲播放时间
    private TextView tv_sbDuration;//显示歌曲长度
    private TextMoveLayout tml_seekbar;
    private SeekBar musicSeekbar;
    private int screenWidth;//屏幕宽度
    private int totalNum;
    private float moveStep;//移动的步长
    private int musicProcess;
    private TextView tv_move;
    ViewGroup.LayoutParams layoutParams;
    MyActivityReceiver receiver;
    private boolean isSeekChange = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mps_player);
        tv_currentTime = (TextView)findViewById(R.id.tv_currentTime);
        tv_sbDuration = (TextView)findViewById(R.id.tv_sbDuration);

        musicSeekbar = (SeekBar)findViewById(R.id.musicSeekbar);
        tml_seekbar = (TextMoveLayout)findViewById(R.id.tmv_seekbar);
        musicSeekbar.setEnabled(true);
        musicSeekbar.setProgress(0);

        screenWidth = getWindowManager().getDefaultDisplay().getWidth();
        layoutParams = new ViewGroup.LayoutParams(screenWidth,50);

        //随进度条移动的文本框
        tv_move = new TextView(this);
        tv_move.setTextColor(getResources().getColor(R.color.text_white));
        tml_seekbar.addView(tv_move, layoutParams);

        lv_mp3list = (ListView)findViewById(R.id.lv_mp3list);
        mp3Infos = MediaUtil.getMp3Infos(getApplicationContext());
        setListAdapter(mp3Infos);
        lv_mp3list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mp3Infos != null) {
                    mp3Info = mp3Infos.get(position);
                    tv_musicTitle.setText(mp3Info.title);
                    Intent intent = new Intent();
                    intent.putExtra("url", mp3Info.url);
                    intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
                    intent.setClass(MpsPlayerActivity.this, PlayerService1.class);
                    startService(intent);
                    //显示进度条信息
                    tv_sbDuration.setText(dateFormat(mp3Info.duration));
                    tv_currentTime.setText("00:00");
                    totalNum = (int) mp3Info.duration;
                    musicSeekbar.setMax((int) mp3Info.duration);

                    moveStep = (float) 0.004;//(screenWidth/totalNum)*10000;
                    tv_move.setText("00:00");

                    iv_player.setSelected(true);
                    isPause = false;
                    isPlaying = true;
                }
            }
        });

        musicSeekbar.setOnSeekBarChangeListener(new ViewOnSeekBarChangeListener());

        //final Button btn_start = (Button)findViewById(R.id.btn_start);
        iv_player = (ImageView) findViewById(R.id.iv_player);
        iv_player.setOnClickListener(new ViewOnClickListener());
        iv_previous = (ImageView)findViewById(R.id.iv_previous);
        iv_previous.setOnClickListener(new ViewOnClickListener());
        iv_next = (ImageView)findViewById(R.id.iv_next);
        iv_next.setOnClickListener(new ViewOnClickListener());

        tv_musicTitle = (TextView)findViewById(R.id.tv_musicTitle);

        iv_wrap = (ImageView)findViewById(R.id.iv_wrap);
        iv_wrap.setOnClickListener(new ViewOnClickListener());
//        spinner = (Spinner)findViewById(R.id.warpSpinner);
        //播放音乐时实时更新进度条
        receiver = new MyActivityReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.h.fileinput.action.MUSIC_CURRENT");
        registerReceiver(receiver,filter);

    }

    //将歌曲时长转化成时间格式
    public String dateFormat(long duration1){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm:ss");
        String dateStr = simpleDateFormat.format(duration1);
        return dateStr;
    }
    //按钮的单击事件
    public class ViewOnClickListener implements View.OnClickListener{
        Intent intent = new Intent();

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.iv_player:
                    if(isPlaying){
                        iv_player.setSelected(false);
                        iv_previous.setSelected(false);
                        //Intent intent = new Intent();
                        intent.setAction("com.h.fileinput.action.MUSIC_SERVICE");
                        intent.putExtra("MSG", AppConstant.PlayerMsg.PAUSE_MSG);
                        intent.setClass(MpsPlayerActivity.this, PlayerService1.class);
                        startService(intent);
                        isPlaying = false;
                        isPause =true;
                    }else if(isPause){
                        iv_player.setSelected(true);
                        iv_previous.setSelected(false);
                        //Intent intent = new Intent();
                        intent.setAction("com.h.fileinput.action.MUSIC_SERVICE");
                        intent.putExtra("MSG", AppConstant.PlayerMsg.CONTINUE_MSG);
                        intent.setClass(MpsPlayerActivity.this, PlayerService1.class);
                        startService(intent);
                        isPause = false;
                        isPlaying = true;
                    }
//                    else{
//                        iv_player.setSelected(true);
//                        intent.setAction("com.h.fileinput.action.MUSIC_SERVICE");
//                        intent.putExtra("MSG", AppConstant.PlayerMsg.PLAY_MSG);
//                        intent.putExtra("listPosition", 0);
//                        intent.putExtra("url",mp3Infos.get(0).url);
//                        intent.setClass(MpsPlayerActivity.this, PlayerService1.class);
//                        startService(intent);
//                        isPause = false;
//                        isPlaying = true;
//                    }
                    break;
                case R.id.iv_previous:
                    previous_music();
                    break;
                case R.id.iv_next:
                    next_music();
                    break;
                case R.id.iv_wrap:
                    //弹出循环模式列表
                    PopupMenu popupMenu = new PopupMenu(getApplicationContext());
                    popupMenu.showAsDropDown(v);
                    popupMenu.addOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Toast.makeText(getApplicationContext(),"position="+position+"",Toast.LENGTH_SHORT).show();
                            Intent wrapIntent = new Intent();
                            wrapIntent.setAction("com.h.fileinput.action.CTL_ACTION");
                            switch (position){
                                case 0:
                                    iv_wrap.setImageResource(R.drawable.repeat_on);
                                    break;
                                case 1:
                                    iv_wrap.setImageResource(R.drawable.wrap_on);
                                    break;
                                case 2:
                                    iv_wrap.setImageResource(R.drawable.mp3_list_on);
                                    break;
                                case 3:
                                    iv_wrap.setImageResource(R.drawable.wrap_on);
                            }
                            wrapIntent.putExtra("control", position + 1);
                            sendBroadcast(wrapIntent);
                        }
                    });
                    break;
            }
        }
    }
    //上一首
    private void previous_music(){
        iv_previous.setSelected(true);
        iv_player.setSelected(true);
        iv_next.setSelected(false);
        listPosition = listPosition-1;
        if(listPosition>=0){
            Mp3Info mp3Info = mp3Infos.get(listPosition);
            tv_musicTitle.setText(mp3Info.title);
            url = mp3Info.url;
            Intent intent = new Intent();
            intent.setAction("com.h.fileinput.action.MUSIC_SERVICE");
            intent.putExtra("url", mp3Info.url);
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("MSG",AppConstant.PlayerMsg.PRIVIOUS_MSG);
            intent.setClass(MpsPlayerActivity.this, PlayerService1.class);
            startService(intent);
            isPause = false;
            isPlaying = true;
        }

    }
    //下一首
    private void next_music(){
        iv_next.setSelected(true);
        iv_player.setSelected(true);
        iv_previous.setSelected(false);
        listPosition = listPosition+1;
        if(listPosition<= mp3Infos.size()-1){
            Mp3Info mp3Info = mp3Infos.get(listPosition);
            tv_musicTitle.setText(mp3Info.title);
            url = mp3Info.url;
            Intent intent = new Intent();
            intent.setAction("com.h.fileinput.MUSIC_SERVICE");
            intent.putExtra("url", mp3Info.url);
            intent.putExtra("listPosition", listPosition);
            intent.putExtra("MSG",AppConstant.PlayerMsg.NEXT_MSG);
            intent.setClass(MpsPlayerActivity.this,PlayerService1.class);
            startService(intent);
            isPause = false;
            isPlaying = true;
        }
    }
    //手动调节seekbar
    public class ViewOnSeekBarChangeListener implements SeekBar.OnSeekBarChangeListener{

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            int moveInt = (int)(progress*moveStep);
            tv_move.layout(moveInt, 10, screenWidth, 60);
            tv_move.setText(dateFormat(progress));
            tv_currentTime.setText(dateFormat(progress));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {
        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            //拖动进度条使之从选中的位置开始播放
            musicProcess = seekBar.getProgress();
            Toast.makeText(getApplicationContext(),"=======musicProgress="+musicProcess,Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setAction("com.h.fileinput.action.MUSIC_SERVICE");
            intent.putExtra("MSG", AppConstant.PlayerMsg.PROGRESS_CHANGE);
            intent.putExtra("progress", musicProcess);
            intent.putExtra("url", mp3Info.url);
            intent.setClass(MpsPlayerActivity.this, PlayerService1.class);
            startService(intent);
            //在暂停状态下手动seekbar进入播放状态
            iv_player.setSelected(true);
            isPlaying = true;
            isPause = false;
        }
    }

    //实时更新进度条
    class MyActivityReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            int currTime = intent.getIntExtra("currentTime",-1);
            musicSeekbar.setProgress(currTime);
            tv_currentTime.setText(dateFormat(currTime));

        }
    }

    //填充歌曲列表
    public void setListAdapter(List<Mp3Info> mp3InfoList){
        //迭代List集合，把每一个Mp3Info对象的所有属性，保存到Map对象当中
        List<HashMap<String,String>> mapList = new ArrayList<HashMap<String,String>>();
        for(Iterator iterator= mp3InfoList.iterator();iterator.hasNext();){
            Mp3Info mp3Info = (Mp3Info)iterator.next();
            HashMap<String,String> map = new HashMap<String,String>();
            map.put("title",mp3Info.title);
            map.put("artist",mp3Info.artist);
            SimpleDateFormat dateFormat = new SimpleDateFormat("mm:ss");
            String timeStr = dateFormat.format(mp3Info.duration);
            map.put("duration",timeStr);
            map.put("size",String.valueOf(mp3Info.size));
            map.put("url",mp3Info.url);
            mapList.add(map);
        }
        simpleAdapter = new SimpleAdapter(this,mapList,R.layout.mp3_item,new String[]{"title","artist","duration"},
                new int[]{R.id.tv_musicTitle,R.id.musicArtist,R.id.musicDuration});
        lv_mp3list.setAdapter(simpleAdapter);
    }

    public void onDestroy(){
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
