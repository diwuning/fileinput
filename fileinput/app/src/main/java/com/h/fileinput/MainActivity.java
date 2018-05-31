package com.h.fileinput;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.commons.codec.StringEncoder;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*
* 图片上传
* */
public class MainActivity extends Activity implements View.OnClickListener {
    private Button selectImage,uploadImage;
    private ImageView imageView;
    private TextView tv_picName,tv_picPath;
    public static final String requestURL = "http://103.8.220.166:40000/lechu/device/image/fileupload";
    public static final String SUCCESS="00000";
    public static final String FAILURE="00001";

    private String picPath = null;
    private String picName = null;
    public String displayMessage,retCode,retInfo;
    private static final String TAG = "uploadImage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //选择图片按钮
        selectImage = (Button) this.findViewById(R.id.btn_select);
        //上传按钮
        uploadImage = (Button) this.findViewById(R.id.btn_upload);
        //图片显示
        selectImage.setOnClickListener(this);
        uploadImage.setOnClickListener(this);
        imageView = (ImageView) this.findViewById(R.id.iv_selected);
        //String str = BaseStrem.GetImgPath();
        //显示图片名称
        tv_picName = (TextView)findViewById(R.id.tv_picName);
        //显示图片路径
        tv_picPath = (TextView)findViewById(R.id.tv_picPath);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_select:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                //回调图片类使用
                startActivityForResult(intent,RESULT_CANCELED);
                break;
            case R.id.btn_upload:
                final MyHandler myHandler = new MyHandler();
                if(picPath!=null && picPath.length()>0){
                    //对网络的访问要在单独的线程中操作，并利用handler在线程间传递数据
                    new Thread(){
                        public void run(){
                            Looper.prepare();
                            //displayMessage是一个的字符串，{"retCode":"00000","retInfo":"操作成功","retResult":""}
                            FileUtil fileUtil = new FileUtil();
                            displayMessage = fileUtil.uploadFilePost(picPath,getLocalMac(),requestURL);
                            //displayMessage = upLoadFile(picName,picPath,requestURL);
                            try{
                                JSONObject jsonObject1 = new JSONObject(displayMessage);
                                retCode = jsonObject1.get("retCode").toString();
                                retInfo = jsonObject1.get("retInfo").toString();
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                            Bundle bundle = new Bundle();
                            bundle.putString("retCode",retCode);
                            bundle.putString("retInfo",retInfo);
                            Message msg = new Message();
                            msg.setData(bundle);
                            myHandler.sendMessage(msg);
                        }
                    }.start();
                }
                break;
        }
    }

    //回传图片信息
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            Log.e(TAG,"uri="+uri);
            try{
                String[] pojo = {MediaStore.Images.Media.DATA};
                Cursor cursor = managedQuery(uri,pojo,null,null,null);
                if(cursor!=null){
                    ContentResolver cr = this.getContentResolver();
                    int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

                    cursor.moveToFirst();
                    String path = cursor.getString(column_index);
                    //获取图片名称
                    String[] name = path.split("/");
                    picName = name[name.length-1];
                    tv_picName.setText(picName+"=="+getLocalMac());
                    //当图片为jpg或png格式时显示图片
                    if(path.endsWith("jpg")||path.endsWith("png")){
                        picPath = path;
                        Bitmap bitmap = BitmapFactory.decodeStream(cr.openInputStream(uri));
                        imageView.setImageBitmap(bitmap);
                        Log.i("pictruedetail",picPath+"==="+picName);
                        tv_picPath.setText(picPath);
                    }else{
                        alert();
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }

    private void alert()
    {
        Dialog dialog = new AlertDialog.Builder(this)
                .setTitle("提示")
                .setMessage("您选择的不是有效的图片")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                picPath = null;
                            }
                        })
                .create();
        dialog.show();
    }

    //获取设备的MAC地址
    public String getLocalMac(){
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress();
    }

    class MyHandler extends Handler{
        public MyHandler(){

        }
        public MyHandler(Looper looper){
            super(looper);
        }

        public void handleMessage(Message msg) {
            retCode = msg.getData().getString("retCode");
            if(retCode.equals("00000")){
                Toast.makeText(MainActivity.this,msg.getData().getString("retInfo"),Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MainActivity.this,"操作失败",Toast.LENGTH_SHORT).show();
            }
        }
    }

    //图片上传，参数图片名称，图片路径，请求url
    public String upLoadFile(String picName1,String picPath1,String requestURL){
        String retCode=null;
        String displayStr = null;
        JSONObject totalJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("cameraId",1);
            jsonObject.put("pictureName",picName1);
            //jsonObject.put("pictureData",picPath1);
            jsonObject.put("pictureData",BaseStrem.GetImgPath(picPath1));
            //将JSON对象放到数组中
            jsonArray.put(jsonObject);
            //将数组再封装到总的JSON中
            totalJson.put("pictures",jsonArray);
            //获取MAC地址，并转换成小写不带:的形式
            String[] macAddr = getLocalMac().toLowerCase().split(":");
            String mac="";
            for(int i=0;i<macAddr.length;i++){
                mac = mac+macAddr[i];
            }
            totalJson.put("deviceId",mac);
            Log.i("JSONMAC",mac);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        Log.i("JSON", totalJson.toString());

        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(requestURL);
        try{
            StringEntity entity = new StringEntity(totalJson.toString());
            post.setEntity(entity);
            HttpResponse responseStr = client.execute(post);
            HttpEntity httpEntity = responseStr.getEntity();
            displayStr = EntityUtils.toString(httpEntity);
            Log.i("passStr", displayStr);
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return displayStr;
    }

}
