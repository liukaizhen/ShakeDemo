package com.erly.shakedemo.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;

/**
 * 工具类
 */
public class Utils {
    /**
     * 动画时间
     */
    private static final int ANIM_TIME = 300;

    /**
     * 微信摇一摇动画
     * @param topView
     * @param bottomView
     */
    public static void starWxShakeAnim(View topView,View bottomView){
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
    }
}
