package com.erly.shakedemo.ifc;

import android.hardware.SensorEvent;

/**
 * 摇一摇回调接口
 */
public interface OnShakeListener {
    void onShakeComplete(SensorEvent event);
}
