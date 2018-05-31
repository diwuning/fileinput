package com.h.fileinput.pay;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;

import com.h.fileinput.R;
import com.h.fileinput.waterspread.WaveView;
import com.h.fileinput.waterwave.WaterWaveView2;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.logging.Logger;

public class PayDemoActivity extends AppCompatActivity {

    String url = "https://api.douban.com/v2/book/17604305";
    WaterWaveView2 waterWaveView2;
    WaveView waveView;

    public int[] randomColor = {Color.BLUE,Color.CYAN,Color.GREEN,Color.MAGENTA,Color.RED,Color.YELLOW};
    public int ranNum;//随机数
    public int[] randomSpeed = {0,100,200,300,400,500};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_demo);

        waterWaveView2 = (WaterWaveView2) findViewById(R.id.www);

        initWaveView();

//        initData();
    }

    public void initWaveView(){
        waveView = (WaveView) findViewById(R.id.waveview);
        //设置波纹的颜色
//        waveView.setColor(Color.RED);
        //通过随机数设置颜色
        ranNum = (int)(Math.random()*6);//[0,5]的随机数
        waveView.setColor(randomColor[ranNum]);
        waveView.setInitialRadius(20);
        waveView.setMaxRadius(200);
        //设置圆环是充满还是描边
        waveView.setStyle(Style.STROKE);
        waveView.setSpeed(randomSpeed[ranNum]);
    }

    public boolean onTouchEvent(MotionEvent event){
        super.onTouchEvent(event);
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                waveView.start();
                break;
            case MotionEvent.ACTION_UP:
                waveView.stop();
                break;
        }
        return true;
    }

//    public void initData(){
//
//        new Thread(){
//            public void run(){
//                Looper.prepare();
//                String data = getDataFromServer();
//                Log.d("PayDemoActivity",data+"==============");
//                try {
//                    JSONObject obj = new JSONObject(data);
//
//                    for(int i=0;i<obj.length();i++){
//                        Log.d("PayDemoActivity",obj.keys().next());
//                    }
//
//                    Log.d("PayDemoActivity",obj.keys().toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//
//    public String getDataFromServer(){
//        HttpClient client = new DefaultHttpClient();
//        HttpGet get = new HttpGet(url);
//        String dataStream = null;
//        try {
//            HttpResponse response = client.execute(get);
//            HttpEntity entity = response.getEntity();
//            dataStream = EntityUtils.toString(entity);
//            Log.d("PayDemoActivity",dataStream+"111111");
//            if(response.getStatusLine().getStatusCode()==200){
//                Log.d("PayDemoActivity","获取数据成功");
//            }else{
//                Log.d("PayDemoActivity","获取数据失败");
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return dataStream;
//    }
}
