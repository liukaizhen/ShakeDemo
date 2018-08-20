package com.erly.shakedemo;

import android.hardware.SensorEvent;

public interface OnShakeListener {
    public void onShakeComplete(SensorEvent event);
}
