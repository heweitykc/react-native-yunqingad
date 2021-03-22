package com.palmmob.yunqing_rn;

import android.content.Context;

import androidx.annotation.Nullable;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.UiThreadUtil;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.yd.ydsdk.manager.YdConfig;

public class AdManager extends ReactContextBaseJavaModule {
    public static Promise initPromise;

    public static ReactApplicationContext reactAppContext;
    final public static String TAG = "AdManager";


    public AdManager(ReactApplicationContext reactContext) {
        super(reactAppContext);
        reactAppContext = reactContext;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void init(final ReadableMap options, final Promise promise) {
        AdManager.initPromise = promise;

        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Context app = AdManager.this.reactAppContext.getApplicationContext();
                YdConfig.getInstance().init(app, options.getString("channelid"), true);
                AdManager.initPromise.resolve(true);
            }
        });
    }

    // 发送事件到RN
    static public void sendEvent(String eventName, String message) {
        WritableMap p = Arguments.createMap();
        p.putString("message", message);
        reactAppContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class).emit(eventName, p);
    }
}
