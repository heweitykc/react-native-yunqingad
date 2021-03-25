package com.palmmob.yunqing_rn.views;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import com.bytedance.sdk.openadsdk.AdSlot;
import com.bytedance.sdk.openadsdk.FilterWord;
import com.bytedance.sdk.openadsdk.TTAdConstant;
import com.bytedance.sdk.openadsdk.TTAdDislike;
import com.bytedance.sdk.openadsdk.TTAdNative;
import com.bytedance.sdk.openadsdk.TTAppDownloadListener;
import com.bytedance.sdk.openadsdk.TTNativeExpressAd;
import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.palmmob.yunqing_rn.AdManager;
import com.palmmob.yunqing_rn.Utils;
import com.palmmob.yunqing_rn.R;
import com.yd.base.interfaces.AdViewBannerListener;
import com.yd.config.exception.YdError;
import com.yd.config.utils.LogcatUtil;
import com.yd.ydsdk.YdBanner;

import java.util.List;
import static com.facebook.react.bridge.UiThreadUtil.runOnUiThread;

public class BannerAdView extends RelativeLayout {

    private static final String TAG = "BannerAdView";

    private Activity mContext;
    private ReactContext reactContext;
    private String _codeid = "";

    private int _expectedWidth = 0;
    private int _expectedHeight = 0; // 高度0 自适应

    final protected RelativeLayout mExpressContainer;
    YdBanner ydBanner;

    public BannerAdView(ReactContext context) {
        super(context);

        mContext = context.getCurrentActivity();
        reactContext = context;
        inflate(context, R.layout.banner_view, this);
        mExpressContainer = (RelativeLayout)findViewById(R.id.banner_container);

        // 这个函数很关键，不然不能触发再次渲染，让 view 在 RN 里渲染成功!!
        Utils.setupLayoutHack(this);
    }

    public void setExceptWidth(int width) {
        Log.d(TAG,  ", setWidth:" + width);
        _expectedWidth = width;
    }

    public void setExceptHeight(int height) {
        Log.d(TAG, ", setHeight:" + height);
        _expectedHeight = height;
    }

    public int getExceptWidth() {
        return _expectedWidth;
    }

    public int getExceptHeight() {
        return _expectedHeight;
    }

    public void setCodeId(String codeId) {
        Log.d(TAG, "setCodeId: " + codeId);
        _codeid = codeId;
    }

    public void showAd() {
        runOnUiThread((new Thread(new Runnable() {
            public void run() {
                loadAd();
            }
        })));
    }

    private void loadAd() {
        if(_expectedWidth <= 0 || _expectedHeight <= 0 || _codeid == "") {
            return;
        }
        final BannerAdView _this = this;

        if(ydBanner != null){
            mExpressContainer.removeAllViews();
            ydBanner.destroy();
            ydBanner = null;
        }

        ydBanner = new YdBanner.Builder(mContext)
                .setKey(_codeid)
                .setUserId(AdManager.vuid)
                .setNickname(AdManager.nickname)
                .setBannerListener(new AdViewBannerListener() {

                    @Override
                    public void onReceived(View view) {
                        if (view != null) {
                            mExpressContainer.addView(view);
                            _this.onAdLoaded("banner加载完成");
                        }
                    }

                    @Override
                    public void onClosed() {
                        _this.onAdClose("banner关闭");
                    }

                    @Override
                    public void onAdClick(String url) {
                        _this.onAdClick();
                    }

                    @Override
                    public void onAdFailed(YdError error) {
                        _this.onAdError("banner加载失败");
                    }
                })
                .build();

        ydBanner.requestBanner();
    }

    // 外部事件..
    public void onAdError(String message) {
        WritableMap event = Arguments.createMap();
        event.putString("message", message);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdError", event);
    }

    public void onAdClick() {
        WritableMap event = Arguments.createMap();
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdClick", event);
    }

    public void onAdClose(String reason) {
        Log.d(TAG, "onAdClose: " + reason);
        WritableMap event = Arguments.createMap();
        event.putString("reason", reason);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdClose", event);
    }

    public void onAdLoaded(String reason) {
        Log.d(TAG, "onAdLoaded: " + reason);
        WritableMap event = Arguments.createMap();
        event.putString("reason", reason);
        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(getId(), "onAdLoaded", event);
    }

}