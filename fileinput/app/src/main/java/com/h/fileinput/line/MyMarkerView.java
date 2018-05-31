package com.h.fileinput.line;

import android.content.Context;
import android.widget.TextView;

import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.utils.MarkerView;
import com.github.mikephil.charting.utils.Utils;
import com.h.fileinput.R;

/**
 * Created by Administrator on 2016/12/5 0005.
 * 设置点击chart图对应的数据弹出标注
 */
public class MyMarkerView extends MarkerView {
    private TextView tvContent;

    /**
     * Constructor. Sets up the MarkerView with a custom layout resource.
     *
     * @param context
     * @param layoutResource the layout resource to use for the MarkerView
     */
    public MyMarkerView(Context context, int layoutResource) {
        super(context, layoutResource);
        tvContent = (TextView) findViewById(R.id.tvContent);
    }

    @Override
    public void refreshContent(Entry e, int dataSetIndex) {
        if(e instanceof CandleEntry){
            CandleEntry ce = (CandleEntry) e;
            tvContent.setText(""+ Utils.formatNumber(ce.getHigh(),0,true));
        }else{
            tvContent.setText(""+e.getVal());
        }
    }
}
