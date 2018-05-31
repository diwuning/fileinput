package com.h.fileinput.filedownload;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by h on 2016/2/1 0001.
 */
public class ImgDownUtil {
    private final static String reqeustURL  = "http://103.8.220.166:40000/lechu/device/image/filedownload";
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory()+"/download_test/";
    private Bundle bundle;

    public ImgDownUtil(){

    }

    //从服务器上获取响应数据
    public String getImgFromServer(String mac){
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(reqeustURL+"/"+mac+"/");
        String imgStream = null;
        try{
            HttpResponse hresponse = client.execute(httpGet);
            HttpEntity entity = hresponse.getEntity();
            imgStream = EntityUtils.toString(entity);
//            is = entity.getContent();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return imgStream;
    }

    //将从服务器上获取的字符串转化成Bundle
    public Bundle getBundleFromStr(String str){
        try{
            JSONObject jsonObject = new JSONObject(str);
            bundle = new Bundle();
            bundle.putString("retCode",jsonObject.getString("retCode").toString());
            bundle.putString("retInfo",jsonObject.getString("retInfo").toString());
            //JSONObject subObject = new JSONObject(jsonObject.getString("retResult"));
            String jsonStr = jsonObject.getString("retResult");
//            String str = jsonStr.substring(1).replace("]", "");
            JSONArray jsonArr = new JSONArray(jsonStr);

            List<PictureSet> psList = new ArrayList<PictureSet>();
            for(int i=0;i<jsonArr.length();i++){
                JSONObject subObject = new JSONObject(jsonArr.get(i).toString());
                PictureSet ps = new PictureSet();
                ps.cameraId = subObject.getInt("cameraId");
                ps.pictureName = subObject.getString("pictureName");
                ps.createTime = subObject.getString("createTime");
                ps.pictureData = subObject.getString("pictureData");
                psList.add(ps);
                Log.i("subObject", jsonArr.get(i).toString());
            }
            bundle.putStringArrayList("retResult", (ArrayList) psList);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return bundle;
    }

    //获取图片
    public byte[] getImg(String imgPath){
        try{
            URL url = new URL(imgPath);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(5*1000);
            conn.setRequestMethod("GET");
            InputStream is = conn.getInputStream();
            if(conn.getResponseCode() == HttpURLConnection.HTTP_OK){
                return readStream(is);
            }
        }catch (MalformedURLException mue){
            //URL抛出的异常
            mue.printStackTrace();
        }catch (IOException ie){
            //HttpURLConnection抛出的异常。
            ie.printStackTrace();
        }
        return null;
    }
    //将图片流转化为数据
    public static byte[] readStream(InputStream inputStream){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len =0;
        try{
            while ((len = inputStream.read(buffer))!=-1){
                byteArrayOutputStream.write(buffer,0,len);
            }
            byteArrayOutputStream.close();
            inputStream.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }


    //保存图片到本地
    public void saveFile(Bitmap bm,String imgName){
        File dirFile = new File(ALBUM_PATH);
        if(!dirFile.exists()){
            dirFile.mkdir();
        }

        File myFile = new File(ALBUM_PATH+imgName);
        try{
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myFile));
            bm.compress(Bitmap.CompressFormat.JPEG,80,bos);
            bos.flush();
            bos.close();
        }catch (Exception ex){
            ex.printStackTrace();
        }

    }
}
