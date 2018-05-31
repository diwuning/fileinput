package com.h.fileinput.weixinrecorder;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.h.fileinput.R;
import com.h.fileinput.TestActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.logging.Logger;

public class ShowPicActivity extends AppCompatActivity implements View.OnClickListener {
    private final static String ALBUM_PATH = Environment.getExternalStorageDirectory() + "/download_img/";
    private static final String TAG = "ShowPicActivity";
    String imgName;
    ImageView iv_play;

    ImageView ivBack2;

    ImageView ivConfirm;
    TextView tvRepeat;

    String videoPath;
    SurfaceView sfvDisplay;
    private SurfaceHolder sfh_display2;
    MediaPlayer player;
    boolean isRecord = false;
    private int position;
    private int isScreenConfigChange;
    private String videoName;
    String model = android.os.Build.MODEL;//手机的型号

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_pic);

        ivBack2 = (ImageView) findViewById(R.id.iv_back2);
        ivBack2.setOnClickListener(this);
        ivConfirm = (ImageView) findViewById(R.id.iv_confirm);
        ivConfirm.setOnClickListener(this);
        tvRepeat = (TextView) findViewById(R.id.tv_repeat);
        tvRepeat.setOnClickListener(this);
        sfvDisplay = (SurfaceView) findViewById(R.id.sfv_display);


        iv_play = (ImageView) findViewById(R.id.iv_playPic);
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        if (data.get("isPosition") != null) {
            position = (int) data.get("isPosition");
        }
        if (data.get("isScreenConfigChange") != null) {
            isScreenConfigChange = (int) data.get("isScreenConfigChange");
        }

        if (data.getBoolean("isRecord")) {
            isRecord = true;
            videoPath = data.getString("video_path");
            videoName = data.getString("video_name");
            iv_play.setVisibility(View.GONE);
            sfvDisplay.setVisibility(View.VISIBLE);
            Log.d(TAG, "videoPath=" + videoPath);
            showVideo();
        } else {
            int degree = CameraConfiguration.getPreviewDegree1(ShowPicActivity.this,position);
            Log.d(TAG,"角度" + degree);
            WindowManager manager = (WindowManager) ShowPicActivity.this.getSystemService(Context.WINDOW_SERVICE);

            int rotation = manager.getDefaultDisplay().getRotation();
            Log.d(TAG,"角度rotation" + rotation);
            //返回值0表示关闭了重力感应（锁定方向），1表示开启了重力感应（旋转）
            try {
                int a = Settings.System.getInt(getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
            } catch (Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }
            Log.d(TAG,"返回值0表示关闭了重力感应（锁定方向），1表示开启了重力感应（旋转）" + rotation);
            isRecord = false;
            iv_play.setVisibility(View.VISIBLE);
            sfvDisplay.setVisibility(View.GONE);
            //    setImageBitmap(data.getByteArray("bytes"));
            Bitmap cameraBitmap;
            if (position == 1) {
                if (isScreenConfigChange == 3) {
                    if (model.equals("MI 5")) {
                        cameraBitmap = rotaingImageView(270);
                    } else {
                        cameraBitmap = rotaingImageView(90);
                    }
                } else {
                    cameraBitmap = rotaingImageView(270);
                }
            } else {
                cameraBitmap = rotaingImageView(90);
            }
            imgName = Calendar.getInstance().getTimeInMillis() + ".jpg";
            saveFile(cameraBitmap, imgName);
            iv_play.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv_play.setImageBitmap(cameraBitmap);
        }

    }

    /**
     * 旋转图片
     *
     * @param angle
     * @param
     * @return Bitmap
     */
    public Bitmap rotaingImageView(int angle) {
        Bitmap cameraBitmap = byte2Bitmap();
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        if (position == 1) {
            matrix.postScale(-1, 1);// 镜像水平翻转
        }
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(cameraBitmap, 0, 0,
                cameraBitmap.getWidth(), cameraBitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    /**
     * 将MainActivity传过来的图片显示在界面当中
     *
     * @param bytes
     */
    public void setImageBitmap(byte[] bytes) {

        Bitmap cameraBitmap = byte2Bitmap();
        // 根据拍摄的方向旋转图像（纵向拍摄时要需要将图像选择90度)
        Matrix matrix = new Matrix();
        matrix.setRotate(CameraConfiguration.getPreviewDegree1(this, position));

        cameraBitmap = Bitmap
                .createBitmap(cameraBitmap, 0, 0, cameraBitmap.getWidth(),
                        cameraBitmap.getHeight(), matrix, true);
        imgName = Calendar.getInstance().getTimeInMillis() + ".jpg";
        saveFile(cameraBitmap, imgName);
        iv_play.setScaleType(ImageView.ScaleType.CENTER_CROP);
        iv_play.setImageBitmap(cameraBitmap);
    }

    //保存图片到本地
    public void saveFile(Bitmap bm, String imgName) {
        File dirFile = new File(ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }

        File myFile = new File(ALBUM_PATH + imgName);
        try {
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(myFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * 从Bundle对象中获取数据
     *
     * @return
     */
    public byte[] getImageFormBundle() {
        Intent intent = getIntent();
        Bundle data = intent.getExtras();
        byte[] bytes = data.getByteArray("bytes");
//        byte[] bytes = TakePicActivity.getDatea();
        return bytes;
    }

    /**
     * 将字节数组的图形数据转换为Bitmap
     *
     * @return
     */
    private Bitmap byte2Bitmap() {
        byte[] data = getImageFormBundle();
        // 将byte数组转换成Bitmap对象
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back2:
                Intent intent = new Intent(ShowPicActivity.this, TakePicActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.iv_confirm:
                Intent i = new Intent(this, TestActivity.class);
                if (isRecord) {
                    i.putExtra("toMainPath", videoPath);//视频文件路径
                    i.putExtra("videoname", videoName);//视频文件名
                } else {
                    i.putExtra("toMainPath", ALBUM_PATH + imgName);
                    i.putExtra("takephoto", imgName);
                }
                i.putExtra("isRecord", isRecord);
                //要启动的activity已经在当前的任务中，那么在该activity之上的activity都会关闭，并且intent会传递给在栈顶的activity

                //如果 Activity 已经是运行在 Task 的 top，则该 Activity 将不会再被启动
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(i);

                break;
            case R.id.tv_repeat:
                Intent intent1 = new Intent(ShowPicActivity.this, TakePicActivity.class);
                startActivity(intent1);
                finish();
                break;
        }
    }

    //显示并播放录制的视频
    public void showVideo() {
        sfh_display2 = sfvDisplay.getHolder();
        sfh_display2.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                player = new MediaPlayer();
                player.setAudioStreamType(AudioManager.STREAM_MUSIC);
                player.setDisplay(sfh_display2);
                try {
                    player.setDataSource(videoPath);
                    player.prepare();
                    player.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {

            }
        });
        sfh_display2.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            Intent intent = new Intent(ShowPicActivity.this, TakePicActivity.class);
            startActivity(intent);
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

}
