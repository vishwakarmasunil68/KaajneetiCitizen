package com.ritvi.kaajneeti.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;
import android.widget.TextView;

@SuppressLint("AppCompatCustomView")
public class EditTextProximaNovaLight extends EditText {

    public EditTextProximaNovaLight(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public EditTextProximaNovaLight(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public EditTextProximaNovaLight(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "fonts/proxima_nova_light.otf");
        setTypeface(tf ,1);

    }
}