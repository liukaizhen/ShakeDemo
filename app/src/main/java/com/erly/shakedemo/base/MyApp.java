package com.erly.shakedemo.base;

import android.app.Application;
import android.content.Context;

public class MyApp extends Application {
    private static MyApp app;

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
