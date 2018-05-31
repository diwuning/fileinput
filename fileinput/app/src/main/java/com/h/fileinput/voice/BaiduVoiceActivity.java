package com.h.fileinput.voice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.baidu.speech.VoiceRecognitionService;
import com.baidu.voicerecognition.android.VoiceRecognitionConfig;
import com.baidu.voicerecognition.android.ui.BaiduASRDialog;
import com.baidu.voicerecognition.android.ui.BaiduASRDigitalDialog;
import com.h.fileinput.R;

public class BaiduVoiceActivity extends Activity implements RecognitionListener {
    private Button btn_baidu;
    private SpeechRecognizer speechRecognizer;
    BaiduASRDigitalDialog voiceDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_voice);

        
//        //创建识别器
//        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this,new ComponentName(this, VoiceRecognitionService.class));
//        //注册监听器
//        speechRecognizer.setRecognitionListener(this);
//        startASR();

    }
    //开始识别
    void startASR(){
        Intent intent = new Intent();
        bindParams(intent);
        speechRecognizer.startListening(intent);
    }

    void bindParams(Intent intent){
        // 设置识别参数
        intent.putExtra("sample",16000);
        intent.putExtra("language","cmn-Hans-CN");
        intent.putExtra("prop",20000);
    }

    @Override
    public void onReadyForSpeech(Bundle params) {
        // 准备就绪
    }

    @Override
    public void onBeginningOfSpeech() {
        // 开始说话处理
    }

    @Override
    public void onRmsChanged(float rmsdB) {
        // 音量变化处理
    }

    @Override
    public void onBufferReceived(byte[] buffer) {
        // 录音数据传出处理
    }

    @Override
    public void onEndOfSpeech() {
        // 说话结束处理
    }

    @Override
    public void onError(int error) {
        // 出错处理
    }

    @Override
    public void onResults(Bundle results) {
        // 最终结果处理
    }

    @Override
    public void onPartialResults(Bundle partialResults) {
        // 临时结果处理
    }

    @Override
    public void onEvent(int eventType, Bundle params) {
        // 处理事件回调
    }
}
