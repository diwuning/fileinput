package com.h.fileinput;

import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by h on 2016/1/28 0028.
 */
public class FileUtil {
    public static final String requestURL = "http://103.8.220.166:40000/lechu/device/image/fileupload";

    public String picName;
    public String displayStr;

    public String uploadFilePost(String picPath,String mac,String requestURL){
        JSONObject totalJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("cameraId",1);
            //获取图片名称
            String[] name = picPath.split("/");
            picName = name[name.length-1];
            jsonObject.put("pictureName",picName);;
            jsonObject.put("pictureData",BaseStrem.GetImgPath(picPath));
            //将JSON对象放到数组中
            jsonArray.put(jsonObject);
            //将数组再封装到总的JSON中
            totalJson.put("pictures",jsonArray);
            //获取MAC地址，并转换成小写不带:的形式
            String[] macAddr = mac.toLowerCase().split(":");
            String mac1="";
            for(int i=0;i<macAddr.length;i++){
                mac1 = mac1+macAddr[i];
            }
            totalJson.put("deviceId",mac1);
        }catch (Exception ex){
            ex.printStackTrace();
        }
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
