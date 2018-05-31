package com.h.fileinput.fontmanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.h.fileinput.R;
/*
* 字体控制
* */

public class FontManageDemoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_font_manage_demo);

        FontsManager.initFormAssets(FontManageDemoActivity.this,"fonts/sao.ttf");
        Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FontsManager.changeFonts(FontManageDemoActivity.this);
                Toast.makeText(FontManageDemoActivity.this,"替换成功",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
