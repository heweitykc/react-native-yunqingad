package com.palmmob.yunqing_rn.activities;


import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RelativeLayout;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.palmmob.yunqing_rn.AdManager;
import com.palmmob.yunqing_rn.R;
import com.palmmob.yunqing_rn.WeakHandler;

import com.yd.base.interfaces.AdViewSpreadListener;
import com.yd.config.exception.YdError;
import com.yd.ydsdk.YdSpread;

public class SplashActivity extends AppCompatActivity implements WeakHandler.IHandler {
    private static final String TAG = "SplashAd";

    final private static String EVT_ADLOADED       = TAG + "-onAdLoaded";
    final private static String EVT_ADCLOSE        = TAG + "-onAdClose";
    final private static String EVT_ADCLICK        = TAG + "-onAdClick";
    final private static String EVT_SKIPVIDEO      = TAG + "-onSkip";
    final private static String EVT_ADERROR        = TAG + "-onAdError";

    // 开屏广告加载超时时间,建议大于1000,这里为了冷启动第一次加载到广告并且展示,示例设置了2000ms
    private static final int AD_TIME_OUT = 2000;
    private static final int MSG_GO_MAIN = 1;
    // 开屏广告加载发生超时但是SDK没有及时回调结果的时候，做的一层保护。
    private final WeakHandler mHandler = new WeakHandler(this);
    private YdSpread ydSpread;
    private RelativeLayout mSplashContainer;
    // 是否强制跳转到主页面
    private boolean mForceGoMain;
    // 开屏广告是否已经加载
    private boolean mHasLoaded;

    private String code_id;
    private int countdown;
    private boolean canJump = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mSplashContainer = this.findViewById(R.id.splash_container);

        // 读取 code id
        Bundle extras = getIntent().getExtras();
        code_id = extras.getString("codeid");
        countdown = extras.getInt("countdown");

        // 在合适的时机申请权限，如read_phone_state,防止获取不了 imei 时候，下载类广告没有填充的问题
        // 在开屏时候申请不太合适，因为该页面倒计时结束或者请求超时会跳转，在该页面申请权限，体验不好
        // TTAdManagerHolder.getInstance(this).requestPermissionIfNecessary(this);

        // 定时，AD_TIME_OUT时间到时执行，如果开屏广告没有加载则跳转到主页面
        mHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, countdown);

        // 加载开屏广告
        loadSplashAd();
    }


    private void loadSplashAd() {

        ydSpread = new YdSpread.Builder(this)
                .setKey(code_id)
                .setCountdownSeconds(countdown)
//                .setSkipMargin(new int[]{0, 0, 20, 20}) // 设置跳过按钮的外边距，数组有四个值，依次为左、上、右、下
//                .setSkipPositionRules(new int[]{RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.ALIGN_PARENT_BOTTOM})// 设置跳过按钮的位置
                .setSkipOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AdManager.sendEvent(EVT_SKIPVIDEO, "跳过");
                        doJump();
                    }
                })
                .setContainer(mSplashContainer)
                .setSpreadListener(new AdViewSpreadListener() {
                    @Override
                    public void onAdDisplay() {
                        mHasLoaded = true;
                        AdManager.sendEvent(EVT_ADLOADED, "开始展示");
                    }

                    @Override
                    public void onAdClose() {
                        mHasLoaded = true;
                        AdManager.sendEvent(EVT_ADCLOSE, "关闭");
                        jumpToMain();
                    }

                    @Override
                    public void onAdClick(String url) {
                        mHasLoaded = true;
                        AdManager.sendEvent(EVT_ADCLICK, "广告点击");
                    }

                    @Override
                    public void onAdFailed(YdError error) {
                        // 广告异常、失败，中断时会调用
                        AdManager.sendEvent(EVT_ADERROR, "加载错误");
                    }

                })
                .build();
        ydSpread.requestSpread();
    }

    private void jumpToMain() {
        if (canJump) {
            doJump();
        } else {
            canJump = true;
        }
    }

    private void doJump() {
        this.goToMainActivity();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ydSpread != null) {
            ydSpread.destroy();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        canJump = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (canJump) {
            doJump();
        }
        canJump = true;
    }

    // 关闭开屏广告方法
    private void goToMainActivity() {
        if (mSplashContainer != null) {
            mSplashContainer.removeAllViews();
        }
        this.overridePendingTransition(0, 0); // 不要过渡动画
        this.finish();
    }

    private void showToast(String msg) {
        // TToast.show(this, "splash:" + msg);
    }

    @Override
    public void handleMsg(Message msg) {
        if (msg.what == MSG_GO_MAIN) {
            if (!mHasLoaded) {
                showToast("加载超时");
                goToMainActivity();
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

