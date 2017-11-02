
package com.cv.apk_manager.utils;

import android.app.Application;

/**
 * @since 2.0.0
 */
public class ContextUtil extends Application {
    private static ContextUtil instance;

    public static ContextUtil getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
