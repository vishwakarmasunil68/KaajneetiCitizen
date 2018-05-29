package com.ritvi.kaajneeti.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class SourceSansProBlackTextView extends TextView {

    public SourceSansProBlackTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SourceSansProBlackTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SourceSansProBlackTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Black.ttf");
        setTypeface(tf ,1);

    }
}