package com.palmmob.gdt;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.ReadableMap;
import com.palmmob.yunqing_rn.AdManager;
import com.qq.e.ads.rewardvideo.ServerSideVerificationOptions;
import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAD;
import com.qq.e.ads.rewardvideo2.ExpressRewardVideoAdListener;
import com.qq.e.comm.util.AdError;
import com.yd.base.interfaces.AdViewVideoListener;
import com.yd.config.exception.YdError;
import com.yd.ydsdk.YdVideo;

import java.util.Map;

public class ExpressRewardVideo extends ReactContextBaseJavaModule {

    final private static String TAG = "GDT_RewardVideo";

    final private static String EVT_ADLOADED       = TAG + "-onAdLoaded";
    final private static String EVT_ADCACHED       = TAG + "-onAdCached";
    final private static String EVT_ADERROR        = TAG + "-onAdError";
    final private static String EVT_ADCLICK        = TAG + "-onAdClick";
    final private static String EVT_ADCLOSE        = TAG + "-onAdClose";
    final private static String EVT_SKIPVIDEO      = TAG + "-onAdSkip";
    final private static String EVT_ADSHOW         = TAG + "-onAdShow";
    final private static String EVT_VIDEOCOMPLETE  = TAG + "-onAdPlayFinish";
    final private static String EVT_VIDEOREWARD    = TAG + "-onAdRewardEffective";

    private static ReactApplicationContext mContext;
    public static Promise promise;

    private ExpressRewardVideoAD mRewardVideoAD;

    public ExpressRewardVideo(ReactApplicationContext context) {
        super(context);
        mContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @ReactMethod
    public void initAd(ReadableMap options, final Promise promise) {
        String adid = options.getString("adid");

        Log.d(TAG, "gdt reward video:  adid=" + adid );

        this.promise = promise;

        if (mRewardVideoAD == null) {

            mRewardVideoAD = new ExpressRewardVideoAD(mContext, adid, new ExpressRewardVideoAdListener() {
                @Override
                public void onAdLoaded() {
                    Log.i(TAG,
                            "onAdLoaded: VideoDuration " + mRewardVideoAD.getVideoDuration() + ", ECPMLevel " +
                                    mRewardVideoAD.getECPMLevel());
                    AdManager.sendEvent(EVT_ADLOADED, "");
                }

                @Override
                public void onVideoCached() {
                    AdManager.sendEvent(EVT_ADCACHED, "");
                    Log.i(TAG, "onVideoCached: ");
                }

                @Override
                public void onShow() {
                    Log.i(TAG, "onShow: ");
                    AdManager.sendEvent(EVT_ADSHOW, "开始展示激励视频");
                }

                @Override
                public void onExpose() {
                    Log.i(TAG, "onExpose: ");
                }

                /**
                 * 模板激励视频触发激励
                 *
                 * @param map 若选择了服务端验证，可以通过 ServerSideVerificationOptions#TRANS_ID 键从 map 中获取此次交易的 id；若未选择服务端验证，则不需关注 map 参数。
                 */
                @Override
                public void onReward(Map<String, Object> map) {
                    Object o = map.get(ServerSideVerificationOptions.TRANS_ID); // 获取服务端验证的唯一 ID
                    Log.i(TAG, "onReward " + o);
                    AdManager.sendEvent(EVT_VIDEOREWARD, o.toString());
                }

                @Override
                public void onClick() {
                    Log.i(TAG, "onClick: ");
                    AdManager.sendEvent(EVT_ADCLICK, "点击激励视频");
                }

                @Override
                public void onVideoComplete() {
                    Log.i(TAG, "onVideoComplete: ");
                    AdManager.sendEvent(EVT_VIDEOCOMPLETE, "激励视频播放完成");
                }

                @Override
                public void onClose() {
                    Log.i(TAG, "onClose: ");
                    AdManager.sendEvent(EVT_ADCLOSE, "关闭激励视频");
                }

                @Override
                public void onError(AdError error) {
                    Log.i(TAG, "onError: code = " + error.getErrorCode() + " msg = " + error.getErrorMsg());
                    AdManager.sendEvent(EVT_ADERROR, "激励视频播放出错");
                }
            });
        }
    }

    @ReactMethod
    public void setVolumeOn(ReadableMap options, final Promise promise){
        Boolean volumeon = options.getBoolean("volumeon");
        mRewardVideoAD.setVolumeOn(volumeon);
        promise.resolve(null);
    }

    @ReactMethod
    public void loadAd(ReadableMap options, final Promise promise){
        String customdata = options.getString("customdata");
        String uid = options.getString("uid");

        ServerSideVerificationOptions server_options = new ServerSideVerificationOptions.Builder()
                .setCustomData(customdata) // 设置激励视频服务端验证的自定义信息
                .setUserId(uid) // 设置服务端验证的用户信息
                .build();

        mRewardVideoAD.setServerSideVerificationOptions(server_options);
        mRewardVideoAD.loadAD();
        promise.resolve(null);
    }


    @ReactMethod
    public void showAd(ReadableMap options, final Promise promise){
        mRewardVideoAD.showAD(mContext.getCurrentActivity());
    }

//    public void onClick(View view) {
//        switch (view.getId()) {
//            case R.id.load_ad_button:
//                boolean volumeOn = ((CheckBox) findViewById(R.id.volume_on_checkbox)).isChecked();
//                if (mPosId != getPosID()) {
//                    mPosId = getPosID();
//                    initAdManager(mPosId);
//                }
//                mRewardVideoAD.setVolumeOn(volumeOn);
//                ServerSideVerificationOptions options = new ServerSideVerificationOptions.Builder()
//                        .setCustomData("APP's custom data") // 设置激励视频服务端验证的自定义信息
//                        .setUserId("APP's user id for server verify") // 设置服务端验证的用户信息
//                        .build();
//                mRewardVideoAD.setServerSideVerificationOptions(options);
//                mRewardVideoAD.loadAD();
//                mIsLoaded = false;
//                mIsCached = false;
//                break;
//            case R.id.show_ad_button:
//            case R.id.show_ad_button_activity:
//                if (mRewardVideoAD == null || !mIsLoaded) {
//                    showToast("广告未拉取成功！");
//                    return;
//                }
//                VideoAdValidity validity = mRewardVideoAD.checkValidity();
//                switch (validity) {
//                    case SHOWED:
//                    case OVERDUE:
//                        showToast(validity.getMessage());
//                        Log.i(TAG, "onClick: " + validity.getMessage());
//                        return;
//                    // 在视频缓存成功后展示，以省去用户的等待时间，提升用户体验
//                    case NONE_CACHE:
//                        showToast("广告素材未缓存成功！");
////            return;
//                    case VALID:
//                        Log.i(TAG, "onClick: " + validity.getMessage());
//                        // 展示广告
//                        break;
//                }
//                // 在视频缓存成功后展示，以省去用户的等待时间，提升用户体验
//                mRewardVideoAD
//                        .showAD(view.getId() == R.id.show_ad_button ? null : ExpressRewardVideoActivity.this);
//                break;
//        }
//    }
}