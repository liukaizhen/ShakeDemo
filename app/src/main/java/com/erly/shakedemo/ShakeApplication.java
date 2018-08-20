package com.erly.shakedemo;

import android.app.Application;
import android.content.Context;

public class ShakeApplication extends Application {
    private static ShakeApplication app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }

    /**
     * 获取app上下文
     * @return
     */
    public static Context getContext(){
        return app;
    }
}
