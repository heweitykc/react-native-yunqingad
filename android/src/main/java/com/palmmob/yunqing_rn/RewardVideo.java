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
                        // 视频展示时调用
                        GDTManager.sendEvent(EVT_ADLOADED, "开始展示激励视频");
                    }

                    @Override
                    public void onAdClose() {
                        // 视频关闭时调用
                        GDTManager.sendEvent(EVT_ADCLOSE, "关闭激励视频");
                    }

                    @Override
                    public void onVideoPrepared() {
                        progressDialog.dismiss();
                        // 视频加载完成时调用
                        if (ydVideo != null && ydVideo.isReady()) {
                            ydVideo.show();
                        }
                    }

                    @Override
                    public void onVideoReward() {
                        // 视频播放完毕奖励时调用
                        Log.i("RewardVideoActivity", "onVideoReward");
                        GDTManager.sendEvent(EVT_VIDEOREWARD, "奖励");
                    }

                    @Override
                    public void onVideoCompleted() {
                        Log.i("RewardVideoActivity", "onVideoCompleted");
                        GDTManager.sendEvent(EVT_VIDEOCOMPLETE, "激励视频播放完成");
                    }

                    @Override
                    public void onAdClick(String url) {
                        // 视频点击时调用
                        GDTManager.sendEvent(EVT_ADCLICK, "点击激励视频");
                    }

                    @Override
                    public void onSkipVideo() {
                        GDTManager.sendEvent(EVT_SKIPVIDEO, "跳过激励视频");
                    }

                    @Override
                    public void onAdFailed(YdError error) {
                        progressDialog.dismiss();
                        GDTManager.sendEvent(EVT_ADERROR, "激励视频播放出错");
                    }

                })
                .build();

        ydVideo.requestRewardVideo();
    }
}
