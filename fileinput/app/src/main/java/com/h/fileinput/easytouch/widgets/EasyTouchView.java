package com.h.fileinput.easytouch.widgets;

import java.io.DataOutputStream;
import java.io.OutputStream;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.h.fileinput.R;


public class EasyTouchView extends View {
    private Context mContext;
    private WindowManager mWManager;
    private LayoutParams mWMParams;
    private View mTouchView;
    private ImageView mIconImageView = null;
    private PopupWindow mPopuWin;
    private ServiceListener mSerLisrener;
    private View mSettingTable;
    private int mTag = 0;
    private int midX;
    private int midY;
    private int mOldOffsetX;
    private int mOldOffsetY;

    private Toast mToast;
    private Timer mTimer = null;
    private TimerTask mTask = null;

    public EasyTouchView(Context context, ServiceListener listener) {
        super(context);
        mContext = context;
        mSerLisrener = listener;
    }

    public void initTouchViewEvent() {
        initEasyTouchViewEvent();
        
        initSettingTableView();
    }
    
    private void initEasyTouchViewEvent() {
        // ��������view WindowManager����
        mWManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        midX = mWManager.getDefaultDisplay().getWidth() / 2 - 25;
        midY = mWManager.getDefaultDisplay().getHeight() / 2 - 44;
        mTouchView = LayoutInflater.from(mContext).inflate(R.layout.easy_touch_view, null);
        mIconImageView = (ImageView) mTouchView.findViewById(R.id.easy_touch_view_imageview);
        mTouchView.setBackgroundColor(Color.TRANSPARENT);
        
        mTouchView.setOnTouchListener(mTouchListener);
        mTouchView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isBackground(mContext)){
                    Log.e("touch","isBackground = true"+",android.os.Process.myPid()"+android.os.Process.myPid());
                    PackageManager pm = mContext.getPackageManager();
                    //resolveActivity(Intent intent, int flags)
                    //Category必须带有CATEGORY_DEFAULT的Activity，才匹配
                    ResolveInfo homeInfo =
                            pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
                    ActivityInfo ai = homeInfo.activityInfo;
                    Intent startIntent = new Intent(Intent.ACTION_MAIN);
                    startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    Log.e("EasyTouch",""+ai.packageName+","+ai.name);
                    startIntent.setComponent(new ComponentName("com.h.fileinput", "com.h.fileinput.TestActivity"));
                    startActivitySafely(startIntent);
                }else{
                    Log.e("touch","isBackground = false");
                    ActivityManager am = (ActivityManager)getContext().getSystemService (Context.ACTIVITY_SERVICE);
                    am.restartPackage("com.h.mynote");
                }
                quitTouchView();
            }
        });
        WindowManager wm = mWManager;
        LayoutParams wmParams = new LayoutParams();
        mWMParams = wmParams;
        wmParams.type = LayoutParams.TYPE_SYSTEM_ALERT; // �����2002��ʾϵͳ�����ڣ���Ҳ��������2003��
        wmParams.flags = 40; // ��������ɿ�
        wmParams.width = 100;
        wmParams.height = 100;
        wmParams.format = -3; // ͸��
        wm.addView(mTouchView, wmParams);
    }
    
    private void initSettingTableView() {
        mSettingTable = LayoutInflater.from(mContext).inflate(R.layout.show_setting_table, null);
        Button commonUseButton = (Button) mSettingTable.findViewById(R.id.show_setting_table_item_common_use_button);
        Button screenLockButton = (Button) mSettingTable.findViewById(R.id.show_setting_table_item_screen_lock_button);
        Button notificationButton = (Button) mSettingTable.findViewById(R.id.show_setting_table_item_notification_button);
        
        Button phoneButton = (Button) mSettingTable.findViewById(R.id.show_setting_table_item_phone_button);
        Button pageButton = (Button) mSettingTable.findViewById(R.id.show_setting_table_item_page_button);
        Button cameraButton = (Button) mSettingTable.findViewById(R.id.show_setting_table_item_camera_button);
        
        Button backButton = (Button) mSettingTable.findViewById(R.id.show_setting_table_item_back_button);
        Button homeButton = (Button) mSettingTable.findViewById(R.id.show_setting_table_item_home_button);
        Button exitTouchButton = (Button) mSettingTable.findViewById(R.id.show_setting_table_item_exit_touch_button);
        
        commonUseButton.setOnClickListener(mClickListener);
        screenLockButton.setOnClickListener(mClickListener);
        notificationButton.setOnClickListener(mClickListener);
        
        phoneButton.setOnClickListener(mClickListener);
        pageButton.setOnClickListener(mClickListener);
        cameraButton.setOnClickListener(mClickListener);
        
        backButton.setOnClickListener(mClickListener);
        homeButton.setOnClickListener(mClickListener);
        exitTouchButton.setOnClickListener(mClickListener);
    }

    private OnClickListener mClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
            case R.id.show_setting_table_item_common_use_button:
                hideSettingTable("常用");
                break;
            case R.id.show_setting_table_item_screen_lock_button:
                hideSettingTable("锁屏");
                break;
            case R.id.show_setting_table_item_notification_button:
                hideSettingTable("通知");
                break;
                
            case R.id.show_setting_table_item_phone_button:
                hideSettingTable("电话");
                break;
            case R.id.show_setting_table_item_page_button:
                hideSettingTable("1");
                break;
            case R.id.show_setting_table_item_camera_button:
                hideSettingTable("相机");
                break;
                
            case R.id.show_setting_table_item_back_button:
                if(isBackground(mContext)){
                    Log.e("touch","isBackground = true"+",android.os.Process.myPid()"+android.os.Process.myPid());
                    PackageManager pm = mContext.getPackageManager();
                    //resolveActivity(Intent intent, int flags)
                    //Category必须带有CATEGORY_DEFAULT的Activity，才匹配
                    ResolveInfo homeInfo =
                            pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
                    ActivityInfo ai = homeInfo.activityInfo;
                    Intent startIntent = new Intent(Intent.ACTION_MAIN);
                    startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    Log.e("EasyTouch",""+ai.packageName+","+ai.name);
                    startIntent.setComponent(new ComponentName("com.h.fileinput", "com.h.fileinput.TestActivity"));
                    startActivitySafely(startIntent);
                }else{
                    Log.e("touch","isBackground = false");
//                    ActivityManager am = (ActivityManager)getContext().getSystemService (Context.ACTIVITY_SERVICE);
//                    am.restartPackage("com.h.mynote");
                    PackageManager pm = mContext.getPackageManager();
                    //resolveActivity(Intent intent, int flags)
                    //Category必须带有CATEGORY_DEFAULT的Activity，才匹配
                    ResolveInfo homeInfo =
                            pm.resolveActivity(new Intent(Intent.ACTION_MAIN).addCategory(Intent.CATEGORY_HOME), 0);
                    ActivityInfo ai = homeInfo.activityInfo;
                    Intent startIntent = new Intent(Intent.ACTION_MAIN);
                    startIntent.addCategory(Intent.CATEGORY_LAUNCHER);
                    Log.e("EasyTouch",""+ai.packageName+","+ai.name);
                    startIntent.setComponent(new ComponentName("com.h.fileinput", "com.h.fileinput.TestActivity"));
                    startActivitySafely(startIntent);
                }
                quitTouchView();
                break;
            case R.id.show_setting_table_item_home_button:
                hideSettingTable("主页");
                break;
            case R.id.show_setting_table_item_exit_touch_button:
                quitTouchView();
                break;
            }

        }
    };


    public void getRunningServiceInfo(Context context ,String packageName) {
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
                int pid = android.os.Process.getUidForName("com.h.mynote");
                Log.e("getRunningServiceInfo","packageName="+packageName+",serviceName="+serviceName+",pkgName="+pkgName+",pid="+pid);
                android.os.Process.killProcess(pid);
//                Intent launchIntent = new Intent();
//                launchIntent.setComponent(new ComponentName(pkgName,
//                        serviceName));
//                mContext.stopService(launchIntent);

            }

        }
    }


    private void startActivitySafely(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            mContext.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(mContext, "null",
                    Toast.LENGTH_SHORT).show();
        } catch (SecurityException e) {
            Toast.makeText(mContext, "null",
                    Toast.LENGTH_SHORT).show();
        }
    }


    public void execShell(String cmd){
        try{
            //权限设置
            Process p = Runtime.getRuntime().exec("su");
            //获取输出流
            OutputStream outputStream = p.getOutputStream();
            DataOutputStream dataOutputStream=new DataOutputStream(outputStream);
            //将命令写入
            dataOutputStream.writeBytes(cmd);
            //提交命令
            dataOutputStream.flush();
            //关闭流操作
            dataOutputStream.close();
            outputStream.close();
        }
        catch(Throwable t)
        {
            t.printStackTrace();
        }
    }

    public static boolean isBackground(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(context.getPackageName())) {
                if (appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_BACKGROUND) {
                    Log.i("后台", appProcess.processName);
                    return true;
                }else{
                    Log.i("前台", appProcess.processName);
                    return false;
                }
            }
        }
        return false;
    }

    private void quitTouchView() {
        hideSettingTable("退出");
        
        mWManager.removeView(mTouchView);
        mSerLisrener.OnCloseService(true);
        
        clearTimerThead();
    }
    
    private OnTouchListener mTouchListener = new OnTouchListener() {
        float lastX, lastY;
        int paramX, paramY;

        public boolean onTouch(View v, MotionEvent event) {
            final int action = event.getAction();

            float x = event.getRawX();
            float y = event.getRawY();

            if (mTag == 0) {
                mOldOffsetX = mWMParams.x; // ƫ����
                mOldOffsetY = mWMParams.y; // ƫ����
            }

            switch (action) {
            case MotionEvent.ACTION_DOWN:
                motionActionDownEvent(x, y);
                break;
                
            case MotionEvent.ACTION_MOVE:
                motionActionMoveEvent(x, y);
                break;
                
            case MotionEvent.ACTION_UP:
                motionActionUpEvent(x, y);
                break;

            default:
                break;
            }
            
            return true;
        }
        
        private void motionActionDownEvent(float x, float y) {
            lastX = x;
            lastY = y;
            paramX = mWMParams.x;
            paramY = mWMParams.y;
        }
        
        private void motionActionMoveEvent(float x, float y) {
            int dx = (int) (x - lastX);
            int dy = (int) (y - lastY);
            mWMParams.x = paramX + dx;
            mWMParams.y = paramY + dy;
            mTag = 1;
            
            // ����������λ��
            mWManager.updateViewLayout(mTouchView, mWMParams);
        }
        
        private void motionActionUpEvent(float x, float y) {
            int newOffsetX = mWMParams.x;
            int newOffsetY = mWMParams.y;
            if (mOldOffsetX == newOffsetX && mOldOffsetY == newOffsetY) {
                mPopuWin = new PopupWindow(mSettingTable, LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                mPopuWin.setTouchInterceptor(new OnTouchListener() {

                    public boolean onTouch(View v, MotionEvent event) {
                        if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                            hideSettingTable();
                            return true;
                        }
                        return false;
                    }
                });
                
                mPopuWin.setBackgroundDrawable(new BitmapDrawable());
                mPopuWin.setTouchable(true);
                mPopuWin.setFocusable(true);
                mPopuWin.setOutsideTouchable(true);
                mPopuWin.setContentView(mSettingTable);
                
                if (Math.abs(mOldOffsetX) > midX) {
                    if (mOldOffsetX > 0) {
                        mOldOffsetX = midX;
                    } else {
                        mOldOffsetX = -midX;
                    }
                }
                
                if (Math.abs(mOldOffsetY) > midY) {
                    if (mOldOffsetY > 0) {
                        mOldOffsetY = midY;
                    } else {
                        mOldOffsetY = -midY;
                    }
                }
                
                mPopuWin.setAnimationStyle(R.style.AnimationPreview);
                mPopuWin.setFocusable(true);
                mPopuWin.update();
                mPopuWin.showAtLocation(mTouchView, Gravity.CENTER, -mOldOffsetX, -mOldOffsetY);
                
                // TODO
                mIconImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.transparent));
                
                catchSettingTableDismiss();
            } else {
                mTag = 0;
            }
        }
    };
    
    private void catchSettingTableDismiss() {
        mTimer = new Timer();
        mTask = new TimerTask() {
            
            @Override
            public void run() {
                if (mPopuWin == null || !mPopuWin.isShowing()) {
                    handler.sendEmptyMessage(0x0);
                }
            }
        };
        
        mTimer.schedule(mTask, 0, 100);
    }
    
    private void clearTimerThead() {
        if (mTask != null) {
            mTask.cancel();
            mTask = null;
        }
        
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            mIconImageView.setBackgroundDrawable(getResources().getDrawable(R.drawable.touch_ic));
        };
    };
    
    public void showToast(Context context, String text) {
        if (mToast == null) {
            mToast = Toast.makeText(context, text, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(text);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    private void hideSettingTable(String content) {
        hideSettingTable();
        showToast(mContext, content);
    }
    
    private void hideSettingTable() {
        if (null != mPopuWin) {
            mPopuWin.dismiss();
        }
    }

    public interface ServiceListener {
        public void OnCloseService(boolean isClose);
    }
}
