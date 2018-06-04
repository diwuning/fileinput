package com.h.fileinput;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.mylibrary.LibrarieActivity;
import com.h.fileinput.AnalyzeHtml.AnalyzeHtmlActivity;
import com.h.fileinput.EasyRecyclerView.CommonRecyclerActivity;
import com.h.fileinput.FragmentPager.FragmentPagerActivity;
import com.h.fileinput.Glin.GlinDemoActivity;
import com.h.fileinput.GridAnimator.GridAnimteDemoActivity;
import com.h.fileinput.ListGridView.ListGridActivity;
import com.h.fileinput.Mp3Media.MpsPlayerActivity;
import com.h.fileinput.baiduMap.AddressActivity;
import com.h.fileinput.barcode.BarCodeActivity;
import com.h.fileinput.camera.CameraRecordActivity;
import com.h.fileinput.camera.RecordCameraActivity;
import com.h.fileinput.camera1.CameRecordActivity;
import com.h.fileinput.dragtop.CoordinatorLayoutDemo;
import com.h.fileinput.dragtop.DragTopDemoActivity;
import com.h.fileinput.easytouch.FloatMenuActivity;
import com.h.fileinput.easytouch.service.AuxiliaryService;
import com.h.fileinput.filedownload.ImgDownActivity;
import com.h.fileinput.fontmanager.FontManageDemoActivity;
import com.h.fileinput.line.LineDemoActivity;
import com.h.fileinput.pay.PayDemoActivity;
import com.h.fileinput.periscope.PeriscopeActivity;
import com.h.fileinput.picture.PicSpecialActivity;
import com.h.fileinput.recorder.Recorder2Activity;
import com.h.fileinput.userinfo.UserInfoActivity;
import com.h.fileinput.video.SurfaceActivity;
import com.h.fileinput.videorecorder.RecorderActivity;
import com.h.fileinput.webviewvideo.WebViewVideoDemoActivity;
import com.h.fileinput.weixinrecorder.TakePicActivity;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

public class TestActivity extends Activity {

    private Button btn_video,btn_audio,btn_picture,btn_recordPlay,btn_record2,btn_wxrecord;
    private Button btn_update,btn_down,btn_barcode,btn_addr,btn_mainAddr,btn_payDemo,btn_easy;
    private Button btn_periscope,btn_fontManage,btn_wifi,btn_line,btn_camera;
    private Button btn_gv_animate,btn_dragTop,btn_dragLayout,btn_analyzeHtml,btn_google_map,btn_userInfo;
    private Button btn_fragment;
    TextView tv_addr;
    private SurfaceView sfv_display2;
    private SurfaceHolder sfh_display2;
    MediaPlayer player;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btn_audio = (Button)findViewById(R.id.btn_audio);
        btn_audio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent audioIntent = new Intent(TestActivity.this, MpsPlayerActivity.class);
                audioIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(audioIntent);
            }
        });
        //播放本地视频
        btn_video = (Button)findViewById(R.id.btn_video);
        btn_video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent videoIntent = new Intent(TestActivity.this, SurfaceActivity.class);
                videoIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(videoIntent);
            }
        });

        //图片蒙版
        btn_picture = (Button)findViewById(R.id.btn_picture);
        btn_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent picIntent = new Intent(TestActivity.this, PicSpecialActivity.class);
                picIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(picIntent);
            }
        });
        //录制视频并在本页播放
        btn_recordPlay = (Button)findViewById(R.id.btn_recordPlay);
        btn_recordPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordIntent = new Intent(TestActivity.this, RecorderActivity.class);
                recordIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(recordIntent);
            }
        });
        //仿微信录制视频，显示进度条，录制完成后，跳转到首页进行播放
        btn_record2 = (Button)findViewById(R.id.btn_record2);
        btn_record2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent recordIntent = new Intent(TestActivity.this, Recorder2Activity.class);
//                recordIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                startActivity(recordIntent);
                startActivityForResult(recordIntent, 30);
            }
        });

        //馨小厨留言板的自定义拍照和摄像
        sfv_display2 = (SurfaceView)findViewById(R.id.sfv_display2);
        btn_wxrecord = (Button)findViewById(R.id.btn_weixinrecord);
        btn_wxrecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent wxIntent = new Intent(TestActivity.this, TakePicActivity.class);
                startActivity(wxIntent);
            }
        });

        //图片上传
        btn_update = (Button)findViewById(R.id.btn_update);
        btn_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String str = "2AF5";
                Log.e("FraFridgeFood","str==="+str+",======十进制="+Integer.parseInt(str,16));
                Intent updateIntent = new Intent(TestActivity.this, MainActivity.class);
                updateIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(updateIntent);
            }
        });
        //图片下载
        btn_down = (Button)findViewById(R.id.btn_down);
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent downIntent = new Intent(TestActivity.this, ImgDownActivity.class);
                downIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(downIntent);
            }
        });

        btn_barcode = (Button)findViewById(R.id.btn_twoCode);
        btn_barcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barIntent = new Intent(TestActivity.this, BarCodeActivity.class);
                barIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(barIntent);
            }
        });

        btn_addr = (Button)findViewById(R.id.btn_addr);
        btn_addr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent barIntent = new Intent(TestActivity.this, AddressActivity.class);
                barIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(barIntent);
            }
        });

        btn_mainAddr = (Button)findViewById(R.id.btn_mainAddr);
        btn_mainAddr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent barIntent = new Intent(TestActivity.this, LibrarieActivity.class);
                Intent barIntent = new Intent(TestActivity.this, DragTopDemoActivity.class);
                barIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(barIntent);
            }
        });

        tv_addr = (TextView) findViewById(R.id.tv_addr);
        //水波纹效果
        btn_payDemo = (Button) findViewById(R.id.btn_payDemo);
        btn_payDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, PayDemoActivity.class);
                startActivity(intent);
            }
        });
        //上拉加载
        btn_easy = (Button) findViewById(R.id.btn_easy);
        btn_easy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, CommonRecyclerActivity.class);
                startActivity(intent);
            }
        });

        initView();
        initView1();

    }

    public void initView1(){
        btn_fragment = (Button) findViewById(R.id.btn_fragment);
        btn_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TestActivity.this, FragmentPagerActivity.class);
                startActivity(intent);
            }
        });
    }

    public void initView(){
        //点赞效果
        btn_periscope = (Button) findViewById(R.id.btn_periscope);
        btn_periscope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, PeriscopeActivity.class);
                startActivity(intent);
            }
        });

        btn_fontManage = (Button) findViewById(R.id.btn_fontManage);
        btn_fontManage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, FontManageDemoActivity.class);
                startActivity(intent);
            }
        });

        //在webview中播放视频
        btn_wifi = (Button) findViewById(R.id.btn_wifi);
        btn_wifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, WebViewVideoDemoActivity.class);
//                intent.setClassName("com.android.settings","com.android.setting.wifi.WifiSetupActivity");
//                intent.putExtra("firstRun",true);
                startActivity(intent);
            }
        });
        //曲线图
        btn_line = (Button) findViewById(R.id.btn_line);
        btn_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, LineDemoActivity.class);
                startActivity(intent);
            }
        });
        btn_camera = (Button) findViewById(R.id.btn_camera);
        btn_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, CameraRecordActivity.class);
                startActivity(intent);
            }
        });

        //GridView的动画效果
        btn_gv_animate = (Button) findViewById(R.id.btn_gv_animate);
        btn_gv_animate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, GridAnimteDemoActivity.class);
                startActivity(intent);
            }
        });
        btn_dragTop = (Button)findViewById(R.id.btn_dragTop);
        btn_dragTop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, DragTopDemoActivity.class);
                startActivity(intent);
            }
        });
        btn_dragLayout = (Button)findViewById(R.id.btn_dragLayout);
        btn_dragLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, CoordinatorLayoutDemo.class);
                startActivity(intent);
            }
        });
        //用jsoup解析html
        btn_analyzeHtml = (Button)findViewById(R.id.btn_analyzeHtml);
        btn_analyzeHtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, AnalyzeHtmlActivity.class);
                startActivity(intent);
            }
        });

        btn_google_map = (Button) findViewById(R.id.btn_google_map);
        btn_google_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startService(new Intent(TestActivity.this, AuxiliaryService.class));
//                Intent intentUhome = new Intent();
//                PackageManager packageManager = getPackageManager();
//                intentUhome = packageManager.getLaunchIntentForPackage("com.spotify.music");
//                intentUhome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                startActivity(intentUhome);
                Intent intent = new Intent(TestActivity.this, FloatMenuActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        //个人信息
        btn_userInfo = (Button) findViewById(R.id.btn_userInfo);
        btn_userInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(TestActivity.this, UserInfoActivity.class);
                Intent intent = new Intent(TestActivity.this, ListGridActivity.class);
                startActivity(intent);
            }
        });

        //Glin
        Button btn_Glin = (Button) findViewById(R.id.btn_Glin);
        btn_Glin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TestActivity.this, GlinDemoActivity.class);
                startActivity(intent);
            }
        });
    }
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Recorder2Activity.RESULT_OK ){
            if(requestCode == 30){
                path = data.getStringExtra("path");
                sfv_display2.setVisibility(View.VISIBLE);
                sfh_display2 = sfv_display2.getHolder();
                sfh_display2.addCallback(new SurfaceHolder.Callback() {
                    @Override
                    public void surfaceCreated(SurfaceHolder holder) {
                        player = new MediaPlayer();
                        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                        player.setDisplay(sfh_display2);
                        try {
                            player.setDataSource(path);
                            player.prepare();
                            player.start();
                            player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                                @Override
                                public void onCompletion(MediaPlayer mp) {
                                    player.reset();
                                    try {
                                        player.setDataSource(path);
                                        player.prepare();
                                        player.start();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                    }

                    @Override
                    public void surfaceDestroyed(SurfaceHolder holder) {

                    }
                });
                sfh_display2.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
                //play(path);

            }
        }

    }
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        Log.e("getRunningServiceInfo","onNewIntent");
//        getRunningServiceInfo(TestActivity.this,"com.h.mynote");
        ActivityManager mActivityManager = (ActivityManager) this
                .getSystemService(Context.ACTIVITY_SERVICE);
//        mActivityManager.restartPackage("com.h.mynote");
        mActivityManager.killBackgroundProcesses("com.h.fileinput.action.MUSIC_SERVICE");
//        List<ActivityManager.RunningAppProcessInfo> processInfos = mActivityManager.getRunningAppProcesses();
//        for(ActivityManager.RunningAppProcessInfo info:processInfos){
//            if(info.processName.equals("com.h.mynote")){
//                int pid = info.pid;
//                Method method = null;
//                try {
//                    method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
//                    try {
//                        method.invoke(mActivityManager, "com.h.mynote");
//                    } catch (IllegalAccessException e) {
//                        e.printStackTrace();
//                    } catch (InvocationTargetException e) {
//                        e.printStackTrace();
//                    }
//                } catch (NoSuchMethodException e) {
//                    e.printStackTrace();
//                } catch (ClassNotFoundException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }
//        PackageManager pm = this.getPackageManager();
//        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(new Intent(Intent.ACTION_MAIN),PackageManager.MATCH_DEFAULT_ONLY);
//        Collections.sort(resolveInfos,new ResolveInfo.DisplayNameComparator(pm));
//        for(ResolveInfo resolveInfo:resolveInfos){
//            String activityName = resolveInfo.activityInfo.name; // 获得该应用程序的启动Activity的name
//            String pkgName = resolveInfo.activityInfo.packageName; // 获得应用程序的包名
////            String serviceName = resolveInfo.serviceInfo.processName;
//            if(pkgName.equals("com.h.mynote")){
//                // 为应用程序的启动Activity 准备Intent
//                Intent launchIntent = new Intent();
//                launchIntent.setComponent(new ComponentName(pkgName,
//                        activityName));
////                            launchIntent.removeCategory(Intent.CATEGORY_HOME);
//                Log.e("touch","isBackground = true"+",pkgName"+pkgName);
////                            pm.removePackageFromPreferred(pkgName);
//                pm.clearPackagePreferredActivities(pkgName);
////                            this.stopService(launchIntent);
////                            intent.setAction("com.h.fileinput.action.MUSIC_SERVICE");
////                            intent.putExtra("MSG", 2);
////                            intent.setClass(TestActivity.this, PlayerService1.class);
////                            startService(intent);
//            }
//
//        }
    }

    public void getRunningServiceInfo(Context context , String packageName) {
        ActivityManager mActivityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppServicees()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningServiceInfo> runServiceList = mActivityManager
                .getRunningServices(1000);
        System.out.println(runServiceList.size());
        // ServiceInfo Model类 用来保存所有进程信息
        for (ActivityManager.RunningServiceInfo runServiceInfo : runServiceList) {
            ComponentName serviceCMP = runServiceInfo.service;
            String serviceName = serviceCMP.getShortClassName(); // service 的类名
            String pkgName = serviceCMP.getPackageName(); // 包名
//            Log.e("getRunningServiceInfo","packageName="+packageName+",serviceName="+serviceName+",pkgName="+pkgName);
            if (pkgName.equals(packageName)) {

//                mActivityManager.forceStopPackage(packageName);
//                mActivityManager.killBackgroundProcesses(packageName);
                //mActivityManager.killBackgroundProcesses(packageName);
//                int pid = android.os.Process.getUidForName("com.h.mynote");
//                Log.e("getRunningServiceInfo","packageName="+packageName+",serviceName="+serviceName+",pkgName="+pkgName+",pid="+pid);
//                android.os.Process.killProcess(pid);
                Intent launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(pkgName,
                        serviceName));
                launchIntent.setAction("com.h.fileinput.action.MUSIC_SERVICE");
                launchIntent.putExtra("MSG", 2);

                startService(launchIntent);
//                this.stopService(launchIntent);

            }

        }
    }
}
