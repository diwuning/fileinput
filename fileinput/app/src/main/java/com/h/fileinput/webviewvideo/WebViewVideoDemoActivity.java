package com.h.fileinput.webviewvideo;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;

import com.h.fileinput.R;
/*
* WebView播放视频，可全屏播放
* */
public class WebViewVideoDemoActivity extends Activity {
    WebView webView;
    ProgressDialog waitDialog = null;
    FrameLayout video_fullView;
    View xCustomView;
    WebChromeClient.CustomViewCallback xCustomViewCallback;
    myWebChromeClient xwebchromeclient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_web_view_video_demo);
        initView();
//        loadFlash();
    }

    public void initView(){
        waitDialog = new ProgressDialog(this);
        waitDialog.setTitle("提示");
        waitDialog.setMessage("视频页面加载中……");
        waitDialog.setIndeterminate(true);
        waitDialog.setCancelable(true);
        waitDialog.show();

        webView = (WebView) findViewById(R.id.webView);
        video_fullView = (FrameLayout) findViewById(R.id.video_fullView);
        WebSettings ws = webView.getSettings();
        ws.setBuiltInZoomControls(true);//隐藏缩放按钮
        ws.setUseWideViewPort(true);//可任意比例缩放
        ws.setLoadWithOverviewMode(true);//webview加载的页面的模式
        ws.setSavePassword(true);
        ws.setSaveFormData(true);//保存表单数据
        ws.setJavaScriptEnabled(true);
        ws.setGeolocationEnabled(true);//启用地理定位
        ws.setGeolocationDatabasePath("/data/data/org.itri.html5webview/databases/");//设置定位的数据库路径
        ws.setDomStorageEnabled(true);
        ws.setSupportMultipleWindows(true);
        xwebchromeclient = new myWebChromeClient();
        webView.setWebChromeClient(xwebchromeclient);
        webView.setWebViewClient(new myWebViewClient());
        webView.loadUrl("http://play.g3proxy.lecloud.com/vod/v2/MTY5LzIwLzU5L2JjbG91ZC84MDA1OTIvdmVyXzAwXzIyLTEwMDQxNzA4MTAtYXZjLTIzMDYyNi1hYWMtMzIwMzItMTMwNjQwLTQ0MzU2MzUtMGNhOTM5YjMxYTlmZGI5NWQzZDQ5NzQ0MjIwNWFhOTctMTQ0NjYwNTg0Njc0OC5tcDQ=?b=271&mmsid=36779947&tm=1470477338505&pip=e29529ace1048e021e45cc23d11e1d04&key=48f754b3b432ee1a4d5ac13df143b01d&platid=2&splatid=206&payff=0&cuid=800592&vtype=21&dur=130&p1=3&p2=31&p3=310&cf=h5-android&p=101&playid=0&tss=no&tag=mobile&sign=bcloud_800592&termid=2&pay=0&ostype=android&hwtype=un");

    }

    public class myWebViewClient extends WebViewClient{
        public boolean shouldOverrideUrlLoading(WebView view,String url){
            view.loadUrl(url);
            return false;
        }

        public void onPageFinished(WebView view,String url){
            super.onPageFinished(view, url);
            waitDialog.dismiss();
        }
    }

    public class myWebChromeClient extends WebChromeClient{
        private View xProgressvideo;

        // 播放网络视频时全屏会被调用的方法
        public void onShowCustomView(View view,CustomViewCallback callback){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
            webView.setVisibility(View.INVISIBLE);
            //如果一个视图已经存在，那么立刻终止并新建一个
            if(xCustomView != null){
                callback.onCustomViewHidden();
                return;
            }
            video_fullView.addView(view);
            xCustomView = view;
            xCustomViewCallback = callback;
            video_fullView.setVisibility(View.VISIBLE);
        }

        //视频播放退出全屏会被调用
        public void onHideCustomView(){
            if(xCustomView == null){//不是全屏播放状态
                return;
            }
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            xCustomView.setVisibility(View.GONE);
            video_fullView.removeView(xCustomView);
            xCustomView = null;
            video_fullView.setVisibility(View.GONE);
            xCustomViewCallback.onCustomViewHidden();
            webView.setVisibility(View.VISIBLE);
        }

        // 视频加载时进程loading
        public View getVideoLoadingProgressView(){
            if(xProgressvideo == null){
                LayoutInflater inflater = LayoutInflater.from(WebViewVideoDemoActivity.this);
                xProgressvideo = inflater.inflate(R.layout.video_loading_progress,null);
            }
            return xProgressvideo;
        }
    }

    //判断视频是否全屏
    public boolean inCustomView(){
        return (xCustomView != null);
    }

    //全屏时按返加键执行退出全屏方法
    public void hideCustomView(){
        xwebchromeclient.onHideCustomView();
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onResume(){
        super.onResume();
        webView.onResume();
        webView.resumeTimers();
        //设置为横屏
        if(getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }

    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    protected void onPause(){
        super.onPause();
        webView.onPause();
        webView.pauseTimers();
    }

    protected void onDestroy(){
        super.onDestroy();
        video_fullView.removeAllViews();
        webView.loadUrl("about:blank");
        webView.stopLoading();
        webView.setWebChromeClient(null);
        webView.setWebViewClient(null);
        webView.destroy();
        webView = null;
    }

    public boolean onKeyDown(int keyCode, KeyEvent event){
        if(keyCode == KeyEvent.KEYCODE_BACK){
            if(inCustomView()){
                hideCustomView();
                return true;
            }else{
                webView.loadUrl("about:blank");
                WebViewVideoDemoActivity.this.finish();
            }
        }
        return false;
    }

}
