package com.h.fileinput.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.h.fileinput.R;


/**
 * Created by geyanyan on 2017/12/12.
 */

public class RotateImageView extends ImageView {
    public RotateImageView(Context context) {
        super(context);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RotateImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setImageDrawable(@Nullable Drawable drawable) {
        super.setImageDrawable(drawable);
        Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_loading);
        startAnimation(animation);
    }
}
