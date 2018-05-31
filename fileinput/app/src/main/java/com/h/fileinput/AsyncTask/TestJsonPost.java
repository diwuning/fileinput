package com.h.fileinput.AsyncTask;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.h.fileinput.R;

/**
 * Created by h on 2016/1/28 0028.
 */
public class TestJsonPost extends Activity implements OnClickListener {

    private Button selectImage,uploadImage;
    private ImageView imageView;
    private TextView tv_picName,tv_picPath;
    public static final String requestURL = "http://103.8.220.166:40000/lechu/device/image/fileupload";
    public static final String SUCCESS="00000";
    public static final String FAILURE="00001";

    private String picPath = null;
    private String picName = null;
    private static final String TAG = "uploadImage";

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
                if(picPath != null && picPath.length()>0){
                    UploadFileTask uploadFileTask = new UploadFileTask(this);
                    uploadFileTask.execute(picPath,getLocalMac());
                }
                break;
        }

    }


    //回传图片信息
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == Activity.RESULT_OK){
            Uri uri = data.getData();
            Log.e(TAG, "uri=" + uri);
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
                        Toast.makeText(getApplicationContext(),"图片格式错误",Toast.LENGTH_SHORT).show();
                    }
                }
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
    }
    //获取设备的MAC地址
    public String getLocalMac(){
        WifiManager wifiManager = (WifiManager)getSystemService(WIFI_SERVICE);
        WifiInfo info = wifiManager.getConnectionInfo();
        return info.getMacAddress();
    }
}
