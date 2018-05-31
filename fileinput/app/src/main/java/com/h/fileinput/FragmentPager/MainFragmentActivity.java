package com.h.fileinput.FragmentPager;

import android.app.Activity;
import android.os.Bundle;
import android.widget.GridView;

import com.h.fileinput.R;

public class MainFragmentActivity extends Activity {
    private GridView gv_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_fragment);
        gv_tab = (GridView) findViewById(R.id.gv_tab);

    }
}
