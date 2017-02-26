package com.jameswong.worklist;

import android.app.Application;

/**
 * ****************************************************
 * author: JamesWong
 * created on: 17/02/22 下午6:15
 * e-mail: PassionateWsj@outlook.com
 * name:
 * desc:
 * ****************************************************
 */
public class IApp extends Application {
    private static IApp mInstance = null;

    // 双重校验锁单例模式
    public static synchronized IApp getInstance() {
        if (mInstance == null) {
            synchronized (IApp.class) {
                if (mInstance == null)
                    mInstance = new IApp();
            }
        }
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
