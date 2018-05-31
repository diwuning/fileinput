package com.h.fileinput.periscope;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.h.fileinput.R;

public class PeriscopeActivity extends AppCompatActivity {
    private Button btn1;
    private PeriscopeLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_periscope);
        layout = (PeriscopeLayout) findViewById(R.id.playout);
        btn1 = (Button) findViewById(R.id.btn_btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.addHeart();
            }
        });
    }
}
