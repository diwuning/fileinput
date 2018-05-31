package com.h.fileinput.AsyncTask;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.h.fileinput.BaseStrem;

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
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by h on 2016/1/26 0026.
 *
 * 生成该类的对象，并调用execute方法之后
 * 首先执行的是onProExecute方法
 * 其次执行doInBackgroup方法
 *
*/
public class UploadFileTask extends AsyncTask<String, Void, String> {
    public static final String requestURL = "http://103.8.220.166:40000/lechu/device/image/fileupload";
    String macstr;
    String retCode;
    String retInfo;
    public static final String SUCCESS="00000";
    public static final String FAILURE="00001";
    private ProgressDialog pdialog;
    private Activity context = null;

    public UploadFileTask(Activity ctx){
        this.context = ctx;
        pdialog = ProgressDialog.show(context,"正在加载……","系统正在处理您的请求");
    }

    /**
     * 这里的String参数对应AsyncTask中的第三个参数（也就是接收doInBackground的返回值）
     * 在doInBackground方法执行结束之后在运行，并且运行在UI线程当中 可以对UI空间进行设置
     */
    protected  void onPostExecute(String result){
        pdialog.dismiss();
        if(SUCCESS.equalsIgnoreCase(result)){
            Toast.makeText(context,retInfo,Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"上传失败",Toast.LENGTH_SHORT).show();
        }
    }

    //该方法运行在UI线程当中,并且运行在UI线程当中 可以对UI空间进行设置
    protected void onPreExecute(){
        //开始执行异步线程

    }

    protected void onCancelled(){
        super.onCancelled();
    }

    /**
     * 这里的String参数对应AsyncTask中的第一个参数
     * 这里的String返回值对应AsyncTask的第三个参数
     * 该方法并不运行在UI线程当中，主要用于异步操作，所有在该方法中不能对UI当中的空间进行设置和修改
     * 但是可以调用publishProgress方法触发onProgressUpdate对UI进行操作
     */
    protected String doInBackground(String... params) {
        File file=new File(params[0]);
        Log.i("file",file.getAbsolutePath());
        macstr = params[1];//主页面中调用的uploadFileTask.execute(picPath,getLocalMac());的第二个参数
        String displayMsg =  upLoadFie1(file,macstr,requestURL);
        try{
            JSONObject jsonObject1 = new JSONObject(displayMsg);
            retCode = jsonObject1.get("retCode").toString();
            retInfo = jsonObject1.get("retInfo").toString();
        }catch (Exception ex){
            ex.printStackTrace();
        }

        return  retCode;
    }
    /**
     * 这里的void参数对应AsyncTask中的第二个参数
     * 在doInBackground方法当中，，每次调用publishProgress方法都会触发onProgressUpdate执行
     * onProgressUpdate是在UI线程中执行，所有可以对UI空间进行操作
     */
    protected void onProgressUpdate(Void... values) {
    }

    public String upLoadFie1(File file,String macAddr,String requestURL){
        String displayStr=null;
        JSONObject totalJson = new JSONObject();
        JSONArray jsonArray = new JSONArray();
        JSONObject jsonObject = new JSONObject();
        try{
            jsonObject.put("cameraId",1);
            jsonObject.put("pictureName",file.getName());
           // jsonObject.put("pictureData",file.getAbsolutePath());
           jsonObject.put("pictureData", BaseStrem.GetImgPath(file.getAbsolutePath()));
            jsonArray.put(jsonObject);
            totalJson.put("pictures",jsonArray);
            //获取MAC地址，并转换成小写不带:的形式
            String[] macArr = macstr.toLowerCase().split(":");
            String mac="";
            for(int i=0;i<macArr.length;i++){
                mac = mac+macArr[i];
            }
            totalJson.put("deviceId",mac);
        }catch (Exception ex){
            ex.printStackTrace();
        }
        //String content = String.valueOf(params);
        String content =totalJson.toString();
        Log.i("JSON",content);
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
