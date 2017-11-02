
package com.cv.apk_manager.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import java.io.File;

/**
 * <b>AutoInstall</b> TODO Install the APK implementation class.
 * <p>
 * <h1>Summary description:</h1> <b>AutoInstall</b>主要实现如下业务功能:
 * <ul>
 * <li>Realize the apk installation</li>
 * </ul>
 * 本类异常场景进行处理：
 * <ul>
 * null</li>
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
 *       This kind of context, and the path of the apk, and call the system interface to realize the apk installation.
 * </pre>
 * 
 * </p>
 * <p>
 * Date: 2015-4-27 下午2:18:01
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
public class AutoInstall {

    private static final String TAG = "com.cv.apk_manager.utils.AutoInstall";

    private static String mUrl;
	
	private static String installerPackageName;

    private static Context mContext;

    /**
     * External incoming url to locate need to install the APK
     * 
     * @param url
     */
    public static void setUrl(String url) {
        mUrl = url;
    }
	
	/**
     * External incoming url to locate need to install the APK
     * 
     * @param packageName
     */
    public static void setPackageName(String packageName) {
        installerPackageName = packageName;
    }

    /**
     * The installation
     * 
     * @param context Receiving external incoming context
     */
    @SuppressLint("LongLogTag")
    public static void install(Context context) {
        mContext = context;
        // The core is a few words of code below
        Log.i(TAG, "--install-----" + Uri.fromFile(new File(mUrl)));
        Log.i(TAG, "--install ----- packageName " + installerPackageName);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(new File(mUrl)),
                "application/vnd.android.package-archive");
		intent.putExtra(Intent.EXTRA_INSTALLER_PACKAGE_NAME,installerPackageName);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        mContext.startActivity(intent);

    }
}
