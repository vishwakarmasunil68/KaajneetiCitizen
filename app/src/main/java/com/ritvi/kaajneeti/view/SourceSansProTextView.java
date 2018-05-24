package com.ritvi.kaajneeti.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class SourceSansProTextView extends TextView {

    public SourceSansProTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SourceSansProTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SourceSansProTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/sourcesanspro.ttf");
        setTypeface(tf ,1);

    }
}