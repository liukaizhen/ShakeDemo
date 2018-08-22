package com.erly.shakedemo.util;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

import com.erly.shakedemo.base.MyApp;
import com.erly.shakedemo.ifc.OnShakeListener;

/**
 * 摇一摇管理类
 */
public class ShakeManager implements SensorEventListener{
    /**
     * 传感器加速度时间间隔100ms
     */
    private static final int ACC_TIME = 100;
    /**
     * 摇一摇临界值
     */
    public static final int SHAKE_THRESHOLD = 300;
    private static ShakeManager ourInstance;
    private final SensorManager sensorManager;
    private final Sensor shakeSensor;
    protected OnShakeListener mShakeListener = null;
    private long mLastUpdateTime;//上次检测时间
    private long lastListenerTime;//上次回调时间
    /**
     * 手机上一个位置时重力感应坐标
     */
    private float mLastX = 0.0f;
    private float mLastY = 0.0f;
    private float mLastZ = 0.0f;

    private ShakeManager() {
        sensorManager = (SensorManager) MyApp.getContext()
                .getSystemService(Context.SENSOR_SERVICE);
        shakeSensor = sensorManager
                .getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
    }

    public static ShakeManager getInstance() {
        if (ourInstance == null){
            synchronized (ShakeManager.class){
                if (ourInstance == null){
                    ourInstance = new ShakeManager();
                }
            }
        }
        return ourInstance;
    }

    /**
     * 设置监听
     * @param listener
     */
    public void setShakeListener(OnShakeListener listener) {
        this.mShakeListener = listener;
    }

    /**
     * 注册传感器
     * @param context
     */
    public void register(Context context){
        if (sensorManager == null || shakeSensor == null){
            Toast.makeText(context,"传感器初始化失败",Toast.LENGTH_SHORT).show();
            return;
        }
        mLastUpdateTime = System.currentTimeMillis();
        sensorManager.registerListener(this,shakeSensor,SensorManager.SENSOR_DELAY_GAME);
    }

    /**
     * 解除注册
     */
    public void unRegister(){
        if (sensorManager == null || shakeSensor == null){
            return;
        }
        mLastX = 0.0f;mLastY = 0.0f;mLastZ = 0.0f;
        sensorManager.unregisterListener(this,shakeSensor);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // 现在检测时间
        long currentUpdateTime = System.currentTimeMillis();
        // 两次检测的时间间隔
        long timeInterval = Math.abs(currentUpdateTime - mLastUpdateTime);
        long listenerInterval = Math.abs(currentUpdateTime - lastListenerTime);

        if (ACC_TIME > timeInterval) {
            return;
        }
        mLastUpdateTime = currentUpdateTime;
        // 获得x,y,z坐标
        float x = event.values[0];
        float y = event.values[1];
        float z = event.values[2];

        // 获得x,y,z的变化值
        float deltaX = x - mLastX;
        float deltaY = y - mLastY;
        float deltaZ = z - mLastZ;

        // 将现在的坐标变成last坐标
        mLastX = x;
        mLastY = y;
        mLastZ = z;

        // 获取摇晃速度
        double speed = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ)
                / timeInterval * 1000;
        // 达到速度阀值且两次间隔时间大于2s,回调给开发者
        if (speed >= SHAKE_THRESHOLD && mShakeListener != null && listenerInterval > 2000) {
            lastListenerTime = currentUpdateTime;
            mShakeListener.onShakeComplete(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
