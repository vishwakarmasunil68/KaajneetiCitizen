package com.ritvi.kaajneeti;

import android.app.Application;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        final TwitterAuthConfig authConfig = new TwitterAuthConfig("odTUxR2y7jhDIb1ImhiGE4VDY", "FFKtAo7BeyDoEoUeRXZUq1FwHAjCHutOXZc4gcimEmG4cOMWKV");
        Fabric.with(this, new Twitter(authConfig));
    }
}
