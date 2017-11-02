
package com.cv.apk_manager.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * <b>AutoUninstall</b> TODO Uninstall the APK implementation class.
 * <p>
 * <h1>Summary description:</h1> <b>AutoUninstall</b>主要实现如下业务功能:
 * <ul>
 * <li>The application uninstall function implementation tools</li>
 * </ul>
 * 本类异常场景进行处理：
 * <ul>
 * <li>null</li>
 * </ul>
 * 
 * <pre>
 *  <b>入口</b>(入口参数):
 *  参数一:  <b>Context</b> context : For a activity passed in the context of the object, can the activity itself
 * such as MainActivity in this
 * </pre>
 * 
 * <pre>
 *  <b>出口:</b>
 *       This kind of context, and the application of the package name, call the system interface, realize the apk is used for unloading.
 * </pre>
 * 
 * </p>
 * <p>
 * Date: 2015-4-27 下午2:19:49
 * </p>
 * <p>
 * Package: com.cv.apk_manager.utils
 * <p>
 * Copyright: (C), 2015-4-27, CultraView
 * </p>
 * 
 * @author Design: Marco.Song (songhong@cultraview.com)
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 1.0.0
 */
public class AutoUninstall {

    private static String mUrl;

    private static Context mContext;

    /**
     * External incoming url to locate need to install the APK
     * 
     * @param url :Complete path (package name eg: com. Demo. CanavaCancel)
     */
    public static void setUrl(String url) {

        mUrl = url;
    }

    /**
     * uninstall
     * 
     * @param context Receiving external incoming context
     */
    public static void uninstall(Context context) {
        mContext = context;
        // The core is a few words of code below
        Uri packageURI = Uri.parse("package:" + mUrl);
        Intent uninstallIntent = new Intent(Intent.ACTION_DELETE, packageURI);
        // uninstallIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(uninstallIntent);
    }
}
