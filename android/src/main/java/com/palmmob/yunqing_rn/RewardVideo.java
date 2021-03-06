package com.palmmob.yunqing_rn;

import android.app.Activity;
import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.palmmob.gdt.GDTManager;
import com.yd.base.interfaces.AdViewVideoListener;
import com.yd.config.exception.YdError;
import com.yd.ydsdk.YdVideo;

public class RewardVideo extends ReactContextBaseJavaModule {

    final private static String TAG = "RewardVideoAd";
    
    final private static String EVT_ADERROR        = TAG + "-onAdError";
    final private static String EVT_ADCLICK        = TAG + "-onAdClick";
    final private static String EVT_ADCLOSE        = TAG + "-onAdClose";
    final private static String EVT_SKIPVIDEO      = TAG + "-onAdSkip";
    final private static String EVT_ADLOADED       = TAG + "-onAdShow";
    final private static String EVT_VIDEOCOMPLETE  = TAG + "-onAdPlayFinish";
    final private static String EVT_VIDEOREWARD    = TAG + "-onAdRewardEffective";

    private static ReactApplicationContext mContext;
    public static Promise promise;

    private ProgressDialog progressDialog;
    private YdVideo ydVideo;
//    private String key = "dev_android_gdt_stimulatevideo";

    public RewardVideo(ReactApplicationContext context) {
        super(context);
        mContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void loadRewardVideoAd(ReadableMap options, final Promise promise) {
        String adid = options.getString("adid");
        Log.d(TAG, "startAd:  appId: " + adid );

        this.loadRewardVideoAd(adid);
    }

    private void loadRewardVideoAd(String adid) {
        Activity context = mContext.getCurrentActivity();
        progressDialog = new ProgressDialog(context);
        progressDialog.show();

        if(ydVideo != null) {
            ydVideo.requestRewardVideo();
            return;
        }

        ydVideo = new YdVideo.Builder(context)
                .setKey(adid)
                .setMaxTimeoutSeconds(10)
                .setVideoListener(new AdViewVideoListener() {

                    @Override
                    public void onAdShow() {
                        // ?????????????????????
                        GDTManager.sendEvent(EVT_ADLOADED, "????????????????????????");
                    }

                    @Override
                    public void onAdClose() {
                        // ?????????????????????
                        GDTManager.sendEvent(EVT_ADCLOSE, "??????????????????");
                    }

                    @Override
                    public void onVideoPrepared() {
                        progressDialog.dismiss();
                        // ???????????????????????????
                        if (ydVideo != null && ydVideo.isReady()) {
                            ydVideo.show();
                        }
                    }

                    @Override
                    public void onVideoReward() {
                        // ?????????????????????????????????
                        Log.i("RewardVideoActivity", "onVideoReward");
                        GDTManager.sendEvent(EVT_VIDEOREWARD, "??????");
                    }

                    @Override
                    public void onVideoCompleted() {
                        Log.i("RewardVideoActivity", "onVideoCompleted");
                        GDTManager.sendEvent(EVT_VIDEOCOMPLETE, "????????????????????????");
                    }

                    @Override
                    public void onAdClick(String url) {
                        // ?????????????????????
                        GDTManager.sendEvent(EVT_ADCLICK, "??????????????????");
                    }

                    @Override
                    public void onSkipVideo() {
                        GDTManager.sendEvent(EVT_SKIPVIDEO, "??????????????????");
                    }

                    @Override
                    public void onAdFailed(YdError error) {
                        progressDialog.dismiss();
                        GDTManager.sendEvent(EVT_ADERROR, "????????????????????????");
                    }

                })
                .build();

        ydVideo.requestRewardVideo();
    }
}
