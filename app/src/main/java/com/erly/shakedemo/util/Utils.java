package com.erly.shakedemo.util;

import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.Toast;

import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps.AMap;
import com.erly.shakedemo.base.MyApp;
import com.erly.shakedemo.ifc.AnimListener;

/**
 * 工具类
 */
public class Utils {
    private static AMapLocationClientOption locationClientOption;
    private static Toast mToast;

    /**
     * 动画时间
     */
    private static final int ANIM_TIME = 300;

    /**
     * 显示Toast
     *
     * @param content
     */
    public static void showToast(CharSequence content) {
        if (TextUtils.isEmpty(content))
            return;
        if (mToast == null) {
            mToast = Toast.makeText(MyApp.getContext(), content, Toast.LENGTH_SHORT);
        } else {
            mToast.setText(content);
            mToast.setDuration(Toast.LENGTH_SHORT);
        }
        mToast.show();
    }

    /**
     * 取消Toast
     */
    public static void cancelToast() {
        if (mToast != null) {
            mToast.cancel();
            mToast = null;
        }
    }

    /**
     * 微信摇一摇动画
     * @param topView
     * @param bottomView
     */
    public static void starWxShakeAnim(View topView, View bottomView, final AnimListener listener){
        /**
         * 上动画
         */
        AnimationSet upSet = new AnimationSet(true);
        TranslateAnimation mup0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        TranslateAnimation  mup1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +0.5f);
        mup0.setDuration(ANIM_TIME);
        mup1.setDuration(ANIM_TIME);

        //延迟执行
        mup1.setStartOffset(400);
        upSet.addAnimation( mup0);
        upSet.addAnimation( mup1);
        topView.startAnimation(upSet);

        /**
         * 下动画
         */
        AnimationSet downSet = new AnimationSet(true);
        TranslateAnimation  mdn0 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                +0.5f);
        TranslateAnimation  mdn1 = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f,
                Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF,
                -0.5f);
        mdn0.setDuration(ANIM_TIME);
        mdn1.setDuration(ANIM_TIME);

        //延迟执行
        mdn1.setStartOffset(400);
        downSet.addAnimation( mdn0);
        downSet.addAnimation( mdn1);
        bottomView.startAnimation(downSet);
        downSet.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                listener.animEnd(animation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 设置地图UI
     *
     * @param aMap
     */
    public static void setMapUI(AMap aMap) {
        aMap.getUiSettings().setZoomControlsEnabled(false);
        aMap.getUiSettings().setLogoLeftMargin(-200);
        aMap.getUiSettings().setLogoBottomMargin(-200);
        aMap.getUiSettings().setCompassEnabled(true);
        aMap.getUiSettings().setScaleControlsEnabled(false);
        aMap.getUiSettings().setMyLocationButtonEnabled(false);
    }

    /**
     * 定位一次地图参数
     */
    public static AMapLocationClientOption getLocationOption() {
        if (locationClientOption == null) {
            locationClientOption = new AMapLocationClientOption();
            //设置定位为高精度模式
            locationClientOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置是否单次定位
            locationClientOption.setOnceLocation(true);
            locationClientOption.setOnceLocationLatest(true);
            //设置是否返回逆地理地址信息
            locationClientOption.setNeedAddress(true);
        }
        return locationClientOption;
    }

}
