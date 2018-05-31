package com.h.fileinput.dragtop;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.h.fileinput.R;

/**
 * Created by Administrator on 2017/5/20 0020.
 */

public class CoordinatorLayoutDemo extends Activity {
    private LinearLayout top_view;
    private WebView myWebView;
    private NestedScrollView myScrollView;
//    String  path = "http://api.haieco.com/quanquan/my.html?userId=201612011927394930&phoneNumber=13256397920";
    String  path = "http://api.haieco.com/quanquan/my.html?userId=201608251250230002&tuserId=26728010&phoneNumber=13256397920";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drag_layout_demo);
//        top_view = (LinearLayout) findViewById(R.id.top_view);
        myWebView = (WebView) findViewById(R.id.myWebview);
        myScrollView = (NestedScrollView) findViewById(R.id.myScrollView);
        myScrollView.setFillViewport(true);
        initWebView(path);
    }

    public void initWebView(String path) {
        WebSettings settings = myWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        //设置可以访问文件
        settings.setAllowFileAccess(true);
        //设置支持缩放
        settings.setBuiltInZoomControls(true);
        //修改某些机型二维码显示不出来的问题
        //打开DOM储存API
        settings.setDomStorageEnabled(true);
        settings.setAllowFileAccess(true);
        settings.setAppCacheEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }
        //修改WebView页面图片大小
        settings.setUseWideViewPort(true);//让webview读取网页设置的viewport，pc版网页
        settings.setLoadWithOverviewMode(true);
//        settings.setTextSize(WebSettings.TextSize.NORMAL);
        ////加载需要显示的网页
        myWebView.loadUrl(path);
        //设置Web视图
        myWebView.setWebViewClient(new MyWebViewClient());
    }

    //Web视图
    private class MyWebViewClient extends WebViewClient {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
