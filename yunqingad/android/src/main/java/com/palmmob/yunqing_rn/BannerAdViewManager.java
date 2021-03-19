package com.palmmob.yunqing_rn;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.annotations.ReactPropGroup;
import com.palmmob.yunqing_rn.views.BannerAdView;

import java.util.Map;

public class BannerAdViewManager extends ViewGroupManager<BannerAdView> {

    public static final String TAG = "BannerAd";
    private ReactContext mContext;

    public BannerAdViewManager(ReactApplicationContext context) {
        mContext = context;
    }

    @NonNull
    @Override
    public String getName() {
        return TAG;
    }

    @NonNull
    @Override
    protected BannerAdView createViewInstance(@NonNull ThemedReactContext reactContext) {
        return new BannerAdView(reactContext);
    }

    @Override
    public boolean needsCustomLayoutForChildren() {
        return false;
    }

    @ReactProp(name = "mediaId")
    public void setMediaId(BannerAdView view, @Nullable String mediaId) {
        view.setCodeId(mediaId);
    }

    @ReactProp(name = "adWidth")
    public void setAdWidth(BannerAdView view, @Nullable int adWidth) {
        view.setExceptWidth(adWidth);
    }

    @ReactProp(name = "adHeight")
    public void setAdHeight(BannerAdView view, @Nullable int adHeight) {
        view.setExceptHeight(adHeight);
    }

    @Override
    protected void onAfterUpdateTransaction(BannerAdView view) {
        super.onAfterUpdateTransaction(view);
        view.showAd();
    }

    @Override
    public Map getExportedCustomBubblingEventTypeConstants() {
        return MapBuilder.builder()
                .put("onAdClick",
					MapBuilder.of(
						"phasedRegistrationNames",
						MapBuilder.of("bubbled", "onAdClick")))
                .put("onAdError",
					MapBuilder.of(
						"phasedRegistrationNames",
						MapBuilder.of("bubbled", "onAdError")))
                .put("onAdClose",
					MapBuilder.of(
						"phasedRegistrationNames",
						MapBuilder.of("bubbled", "onAdClose")))
                .put("onAdLoaded",
					MapBuilder.of(
						"phasedRegistrationNames",
						MapBuilder.of("bubbled", "onAdLoaded")))
                .build();
    }


}
