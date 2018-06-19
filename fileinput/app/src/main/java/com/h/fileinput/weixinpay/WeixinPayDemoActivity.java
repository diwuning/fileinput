package com.h.fileinput.weixinpay;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.Button;

import com.h.fileinput.R;
import com.tencent.mm.opensdk.modelpay.PayReq;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

public class WeixinPayDemoActivity extends Activity {
    private String url;
    private IWXAPI iwxapi;
    private Button btn_pay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weixin_pay_demo);
        btn_pay = (Button) findViewById(R.id.btn_weixinPay);
        btn_pay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                wechatpay(v);
            }
        });
        url = "http://wxpay.wxutil.com/pub_v2/app/app_pay.php";
        initWechat();
    }

    //初始化微信支付api
    private void initWechat(){
        iwxapi = WXAPIFactory.createWXAPI(this,Constants.APP_ID);
    }


    public void wechatpay(View view){
        Runnable payRunnable = new Runnable() {
            @Override
            public void run() {
                PayReq req = new PayReq();//调起微信APP的对象
                //设置必要的参数
                req.appId = Constants.APP_ID;
                req.partnerId = "1900006771";//微信支付分配的商户号
                req.prepayId = "wx1313245777817266cd4136b83145624297";//微信返回的交易会话id
                req.packageValue = "Sign=WXPay";
                req.nonceStr = "0e4d5a6e97fbf0f9da5d071f70ed1f1a";//随机字符串
                req.timeStamp = "1528867497";
                req.sign = "443A70542AEC42108A17A386A2675398";
                iwxapi.sendReq(req);
            }
        };
        Thread payThread = new Thread(payRunnable);
        payThread.start();
    }
}
