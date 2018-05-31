package com.h.fileinput.easytouch;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.h.fileinput.R;
import com.h.fileinput.fontmanager.FontManageDemoActivity;
/*
* 仿ios home键
* */
public class FloatMenuActivity extends AppCompatActivity {
    private static final String TAG = "FloatMenuActivity";

    WindowManager mWindowManager;
    WindowManager.LayoutParams wmParams;
    LinearLayout mFloatLayout;
    ImageView mFloatView;
    Button btn_jump;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_float_menu);
        btn_jump = (Button) findViewById(R.id.btn_jump);
        btn_jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FloatMenuActivity.this, FontManageDemoActivity.class);
                startActivity(intent);
            }
        });
//        createFloatView();
    }

    private void createFloatView()
    {
        //获取LayoutParams对象
        wmParams = new WindowManager.LayoutParams();

        //获取的是LocalWindowManager对象
        mWindowManager = this.getWindowManager();
        Log.i(TAG, "mWindowManager1--->" + this.getWindowManager());
        //mWindowManager = getWindow().getWindowManager();
        Log.i(TAG, "mWindowManager2--->" + getWindow().getWindowManager());

        //获取的是CompatModeWrapper对象
        //mWindowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        Log.i(TAG, "mWindowManager3--->" + mWindowManager);
        wmParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        wmParams.format = PixelFormat.RGBA_8888;;
        wmParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        wmParams.gravity = Gravity.LEFT | Gravity.TOP;
        wmParams.x = 0;
        wmParams.y = 0;
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;

        LayoutInflater inflater = this.getLayoutInflater();//LayoutInflater.from(getApplication());

        mFloatLayout = (LinearLayout) inflater.inflate(R.layout.easy_touch_view, null);
        mWindowManager.addView(mFloatLayout, wmParams);
        //setContentView(R.layout.main);
        mFloatView = (ImageView)mFloatLayout.findViewById(R.id.easy_touch_view_imageview);

        Log.i(TAG, "mFloatView" + mFloatView);
        Log.i(TAG, "mFloatView--parent-->" + mFloatView.getParent());
        Log.i(TAG, "mFloatView--parent--parent-->" + mFloatView.getParent().getParent());
        //绑定触摸移动监听
        mFloatView.setOnTouchListener(new View.OnTouchListener()
        {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                // TODO Auto-generated method stub
                wmParams.x = (int)event.getRawX() - mFloatLayout.getWidth()/2;
                //25为状态栏高度
                wmParams.y = (int)event.getRawY() - mFloatLayout.getHeight()/2 - 40;
                mWindowManager.updateViewLayout(mFloatLayout, wmParams);
                return false;
            }
        });

        //绑定点击监听
        mFloatView.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                // TODO Auto-generated method stub
//                Intent intent = new Intent(FloatMenuActivity.this, ResultActivity.class);
//                startActivity(intent);
            }
        });

    }

    public void onResume(){
        super.onResume();
        createFloatView();
        Log.e(TAG,"onResume");
    }

    public void onPause(){
        super.onPause();
        mWindowManager.removeView(mFloatLayout);
        Log.e(TAG,"onPause");
    }
}
