package com.h.fileinput.recorder;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by h on 2016/3/10 0010.
 */
public class LineView extends View {

    public LineView(Context context) {
        super(context);
    }
    public LineView(Context context,AttributeSet attrs){
        super(context,attrs);
    }

    public void setLayoutWidth(int width){
        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = width;
        setLayoutParams(params);
    }
}
