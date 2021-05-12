package com.palmmob.gdt;

import android.content.Context;

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

import com.qq.e.comm.managers.GDTADManager;
import com.qq.e.comm.managers.setting.GlobalSetting;

public class GDTManager extends ReactContextBaseJavaModule {
    public static Promise initPromise;

    public static ReactApplicationContext reactAppContext;
    final public static String TAG = "GDTManager";

    public GDTManager(ReactApplicationContext reactContext) {
        super(reactAppContext);
        reactAppContext = reactContext;
    }

    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void init(final ReadableMap options, final Promise promise) {
        GDTManager.initPromise = promise;

        UiThreadUtil.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GDTADManager.getInstance().initWith(reactAppContext, options.getString("appid"));
                GDTManager.initPromise.resolve(true);
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
