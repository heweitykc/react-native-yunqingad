package com.palmmob.yunqing_rn;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.palmmob.yunqing_rn.activities.SplashActivity;

public class SplashAd extends ReactContextBaseJavaModule {

    String TAG = "SplashAd";
    ReactApplicationContext mContext;

    public SplashAd(@NonNull ReactApplicationContext reactContext) {
        super(reactContext);
        mContext = reactContext;
    }

    @NonNull
    @Override
    public String getName() {
        return "SplashAd";
    }

    @ReactMethod
    public void loadSplashAdHalf(ReadableMap options) {
        String codeid = options.hasKey("adid") ? options.getString("adid") : null;
        int countdown = options.hasKey("countdown") ? options.getInt("countdown") : 5;

        startSplash(codeid, countdown);
    }

    private void startSplash(String codeid, int countdown) {
        Intent intent = new Intent(mContext, SplashActivity.class);
//        try {
            intent.putExtra("codeid", codeid);
            intent.putExtra("countdown", countdown);
            final Activity context = getCurrentActivity();
            context.startActivity(intent);

//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

}
