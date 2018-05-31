package com.h.fileinput.filedownload;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.h.fileinput.R;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
/*
* 图片下载
* */
public class ImgDownActivity extends Activity implements View.OnClickListener {
    private TextView tv_path1,tv_path2;
    private Button btn_obtain,btn_downImg1,btn_downImg2;
    private ImageView iv_img1,iv_img2;
    private String netImgStr;
    private String retCode,retInfo;
    private Bitmap bm1,bm2;
    private ProgressDialog mDialog;
    private String passMsg1,passMsg2;

    private final static String reqeustURL  = "http://103.8.220.166:40000/lechu/device/image/filedownload";
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory()+"/download_test/";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fild_down_load);
        //获取网络图片信息
        btn_obtain = (Button)findViewById(R.id.btn_obtain);
        btn_obtain.setOnClickListener(this);

        tv_path1 = (TextView)findViewById(R.id.tv_imgPath1);
        tv_path2 = (TextView)findViewById(R.id.tv_imgPath2);
        iv_img1 = (ImageView)findViewById(R.id.iv_img1);
        iv_img2 = (ImageView)findViewById(R.id.iv_img2);
        btn_downImg1 = (Button)findViewById(R.id.btn_down1);
        btn_downImg2 = (Button)findViewById(R.id.btn_down2);
        btn_downImg1.setOnClickListener(this);
        btn_downImg2.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_obtain:
                final ObtainHandler obtainHandler = new ObtainHandler();
                new Thread(){
                    public void run(){
                        Looper.prepare();
                        ImgDownUtil imgDownUtil = new ImgDownUtil();
                        //netImgStr = getImgFromServer(getLocalMac());
                        netImgStr = imgDownUtil.getImgFromServer(getLocalMac());
                        Log.i("inputstream",netImgStr);
                        //Bundle mBundle = getBundleFromStr(netImgStr);
                        Bundle mBundle = imgDownUtil.getBundleFromStr(netImgStr);

                        //获取图片信息，并将其转换成byte[]
                        List<PictureSet> imgList = (ArrayList)(mBundle.getStringArrayList("retResult"));
                        byte[] data = null;
                        List<byte[]> byteList = new ArrayList<byte[]>();
                        for(int j=0;j<imgList.size();j++){
                            data = imgDownUtil.getImg(imgList.get(j).pictureData);
                            byteList.add(data);
                            mBundle.putByteArray("imgPath" + j, data);
                        }

                        Message msg = new Message();
                        msg.setData(mBundle);
                        obtainHandler.sendMessage(msg);
                    }
                }.start();
                break;
            case R.id.btn_down1:
                mDialog = ProgressDialog.show(ImgDownActivity.this,"保存图片","图片正在保存，请稍候……",true);
                final SaveImgHandler saveImgHandler = new SaveImgHandler();
                new Thread(){
                    public void run(){
                        Looper.prepare();
                        ImgDownUtil imgDownUtil = new ImgDownUtil();
                        try{
                            imgDownUtil.saveFile(bm1, "bm1.jpg");
                            passMsg1 = "图片保存成功！";
                        }catch (Exception ex){
                            ex.printStackTrace();
                            passMsg1 = "图片保存失败！";
                        }
                        saveImgHandler.sendMessage(saveImgHandler.obtainMessage());
                    }
                }.start();
                break;
            case R.id.btn_down2:
                mDialog = ProgressDialog.show(ImgDownActivity.this,"图片保存","图片正在保存，请稍候……",true);
                final SaveImgHandler saveImgHandler1 = new SaveImgHandler();
                new Thread(){
                    public void run(){
                        Looper.prepare();
                        try{
                            ImgDownUtil imgDownUtil = new ImgDownUtil();
                            imgDownUtil.saveFile(bm2,"bm2.jpg");
                            passMsg1 = "图片保存成功！";
                        }catch (Exception ex){
                            ex.printStackTrace();
                            passMsg1 = "图片保存失败！";
                        }
                        saveImgHandler1.sendMessage(saveImgHandler1.obtainMessage());
                    }
                }.start();
                break;
        }
    }

    //获取图片信息，并显示在页面上
    class ObtainHandler extends Handler{
        public ObtainHandler(){

        }
        public ObtainHandler(Looper l){
            super(l);
        }
        //更新UI
        public void handleMessage(Message msg){
            Bundle bakBundle = msg.getData();
            retCode = bakBundle.getString("retCode");
            List<PictureSet> list = (ArrayList)(bakBundle.getStringArrayList("retResult"));

            if(retCode.equals("00000")){
                Toast.makeText(ImgDownActivity.this,bakBundle.getString("retInfo"),Toast.LENGTH_SHORT).show();
                //显示网络图片的路径
                tv_path1.setText(list.get(0).pictureData);
                tv_path2.setText(list.get(1).pictureData);
                //显示图片
                byte[] imgByte1 = bakBundle.getByteArray("imgPath1");
                bm1 = BitmapFactory.decodeByteArray(imgByte1,0,imgByte1.length);
                Log.i("bm1Info",bm1.getConfig().toString());
                iv_img1.setImageBitmap(bm1);
                byte[] imgByte2 = bakBundle.getByteArray("imgPath2");
                bm2 = BitmapFactory.decodeByteArray(imgByte2,0,imgByte2.length);
                iv_img2.setImageBitmap(bm2);
            }
        }
    }

    //图片保存成功后关闭等待对话框，并弹出页面提示
    class SaveImgHandler extends Handler{
        public SaveImgHandler(){
        }
        public SaveImgHandler(Looper l){
            super(l);
        }
        public void handleMessage(Message msg){
            mDialog.dismiss();
            Toast.makeText(ImgDownActivity.this,passMsg1,Toast.LENGTH_SHORT).show();
        }
    }

    //获取设备的MAC地址
    public String getLocalMac(){
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        String macStr = info.getMacAddress();
        //将MAC地址转换成小写不带:的形式
        String[] macAddr = macStr.toLowerCase().split(":");
        String mac1="";
        for(int i=0;i<macAddr.length;i++){
            mac1 = mac1+macAddr[i];
        }
        return mac1;
    }
}
