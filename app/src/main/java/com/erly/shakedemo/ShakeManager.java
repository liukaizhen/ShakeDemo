package com.erly.shakedemo;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.widget.Toast;

public class ShakeManager implements SensorEventListener{
    private static ShakeManager ourInstance;
    private final SensorManager sensorManager;
    private final Sensor shakeSensor;
    /**
     * 传感器监听器, 摇一摇以后回调onShakeComplete方法
     */
    protected OnShakeListener mShakeListener = null;
    /**
     * 传感器检测变化的时间间隔
     */
    private static final int UPDATE_INTERVAL_TIME = 100;
    /**
     * 默认的摇一摇传感器阀值,当摇晃速度达到这值后产生作用
     */
    public static final int DEFAULT_SHAKE_SPEED = 300;
    /**
     * 手机上一个位置时重力感应坐标
     */
    private float mLastX = 0.0f;
    private float mLastY = 0.0f;
    private float mLastZ = 0.0f;
    /**
     * 上次检测时间
     */
    private long mLastUpdateTime;
    private long lastListenerTime;

    private ShakeManager() {
        sensorManager = (SensorManager) ShakeApplication.getContext()
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

        if (UPDATE_INTERVAL_TIME > timeInterval) {
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
        if (speed >= DEFAULT_SHAKE_SPEED && mShakeListener != null && listenerInterval > 2000) {
            lastListenerTime = currentUpdateTime;
            mShakeListener.onShakeComplete(event);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
