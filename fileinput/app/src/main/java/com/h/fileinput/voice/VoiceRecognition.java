package com.h.fileinput.voice;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.h.fileinput.R;

import java.util.ArrayList;
import java.util.List;

public class VoiceRecognition extends Activity implements View.OnClickListener {
    private static final int VOICE_RECOGNITION_REQUEST_CODE = 1234;
    private ListView mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_recognition);
        Button btn_speak = (Button) findViewById(R.id.btn_speak);
        mList = (ListView) findViewById(R.id.list);
        PackageManager pm = getPackageManager();
        List<ResolveInfo> activities = pm.queryIntentActivities(new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH),0);
        if(activities.size() != 0){
            btn_speak.setOnClickListener(this);
        }else{
            btn_speak.setEnabled(false);
            btn_speak.setText("Recognizer not present");
        }
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_speak){
            startVoiceRecognitionActivity();
        }
    }

    private void startVoiceRecognitionActivity(){
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        //语言模式和自由模式的语音识别
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        //提示语音开始
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,"请说话");
        //开始语音识别
        startActivityForResult(intent,VOICE_RECOGNITION_REQUEST_CODE);
    }

    protected  void onActivityResult(int requestCode,int resultCode,Intent data){
        //回调获取从谷歌得到的数据
        if(requestCode == VOICE_RECOGNITION_REQUEST_CODE && resultCode == RESULT_OK){
            //取得语音的字符
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            mList.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,matches));
        }
        super.onActivityResult(requestCode,resultCode,data);
    }
}
