package com.palmmob.yunqing_rn;

import android.view.Choreographer;
import android.view.View;
import android.view.ViewGroup;

import com.palmmob.yunqing_rn.views.BannerAdView;

public class Utils {

    // for fix addView not showing ====
    public static void setupLayoutHack(final BannerAdView view) {
        Choreographer.getInstance().postFrameCallback(new Choreographer.FrameCallback() {
            // 这个事件很关键，不然不能触发再次渲染，让 view 在RN里渲染成功!!
            @Override
            public void doFrame(long frameTimeNanos) {
                manuallyLayoutChildren(view);
                view.getViewTreeObserver().dispatchOnGlobalLayout();
                Choreographer.getInstance().postFrameCallback(this);
            }
        });
    }

    // 这个函数很关键，不然不能触发再次渲染，让 view 在RN里渲染成功!!
    public static void manuallyLayoutChildren(BannerAdView view) {
        int w,h;
        for (int i = 0; i < view.getChildCount(); i++) {
            View child = view.getChildAt(i);
            child.measure(View.MeasureSpec.makeMeasureSpec(view.getMeasuredWidth(), View.MeasureSpec.EXACTLY),
                    View.MeasureSpec.makeMeasureSpec(view.getMeasuredHeight(), View.MeasureSpec.EXACTLY));
            w = child.getMeasuredWidth();
            h = child.getMeasuredWidth();
            child.layout(0, 0, w, h);
        }
    }
}