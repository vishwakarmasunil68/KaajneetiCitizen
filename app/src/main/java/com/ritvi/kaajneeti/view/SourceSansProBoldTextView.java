package com.ritvi.kaajneeti.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class SourceSansProBoldTextView extends TextView {

    public SourceSansProBoldTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public SourceSansProBoldTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public SourceSansProBoldTextView(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/SourceSansPro-Bold.ttf");
        setTypeface(tf ,1);

    }
}