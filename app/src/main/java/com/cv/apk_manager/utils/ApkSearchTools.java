
package com.cv.apk_manager.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

/**
 * <b>ApkSearchTools</b> TODO Usb SPK information (basic, detailed information
 * see Myfile) .
 * <p>
 * <h1>Summary description:</h1> <b>ApkSearchTools</b>主要实现如下业务功能:
 * <ul>
 * <li>Realize the apk lookup, collection and storage</li>
 * </ul>
 * 本类异常场景进行处理：
 * <ul>
 * <li>Find out the apk abnormal or damaged</li>
 * </ul>
 * 
 * <pre>
 *  <b>入口</b>(入口参数):
 *  参数一:  <b>Context</b> context : For a activity passed in the context of the object, can the activity itself
 * Such as MainActivity. This
 * </pre>
 * 
 * </p>
 * <p>
 * Date: 2015-4-27 下午2:14:46
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
@SuppressLint("DefaultLocale")
public class ApkSearchTools {

    private Context cvContext;

    private ApkInfo apkInfo;

    private List<ApkInfo> myApkFiles;

    /** Used to store the apk files have been damaged */
    private List<String> exceptionFile;

    PackageManager pm;

    ApplicationInfo appInfo;

    PackageInfo packageInfo;

    /** The apk icon */
    Drawable apk_icon;

    /** The apk application name */
    String label;

    /** The absolute path of the apk */
    String apk_path;

    /** Apk file name */
    String name_s;

    private int gcCount = 0;

    /**
     * Sorting collection and return the apk
     * 
     * @return List<ApkInfo>: The apk collection
     */
    @SuppressWarnings("unchecked")
    public List<ApkInfo> getApkFiles() {
        // (according to the sorting myApkFiles apk file name)
        Collections.sort(myApkFiles);
        return myApkFiles;
    }

    /**
     * A constructor
     * 
     * @param cvContext
     */
    public ApkSearchTools(Context cvContext) {
        super();
        this.cvContext = cvContext;
        myApkFiles = new ArrayList<ApkInfo>();
        exceptionFile = new ArrayList<String>();
    }

    /**
     * @param file File:To find the
     * @throws Exception
     */
    public void findAllApkFile(File file) throws Exception {
        if (file.isFile()) {
            name_s = file.getName();
            try {
                Log.d("zzzzz", "scanning sd apk info...." + name_s);
                if (name_s.toLowerCase().endsWith(".apk")) {
                    apkInfo = new ApkInfo();
                    apk_path = file.getAbsolutePath();
                    pm = cvContext.getPackageManager();
                    packageInfo = pm.getPackageArchiveInfo(apk_path, PackageManager.GET_ACTIVITIES);

                    appInfo = packageInfo.applicationInfo;
                    appInfo.sourceDir = apk_path;
                    appInfo.publicSourceDir = apk_path;
                    apk_icon = appInfo.loadIcon(pm);
                    /**
                     * This method will be done in the scan later take up disk
                     * file.Pull out after the process is killed
                     */
                    label = (String) appInfo.loadLabel(pm);

                    apkInfo.setApk_icon(apk_icon);
                    apkInfo.setLabel(label);
                    apkInfo.setFileName(name_s);
                    apkInfo.setApk_path(apk_path);
                    myApkFiles.add(apkInfo);

                    pm = null;
                    packageInfo = null;
                    appInfo = null;
                    apk_icon = null;
                    label = null;

                    if (gcCount > 10) {
                        System.gc();
                        gcCount = 0;
                    }

                }
            } catch (Exception e) {
                exceptionFile.add(name_s);
                e.printStackTrace();
                Log.d("com.cv.apk_manager.utils.ApkSearchTools", "--CvExceptionFile---->> "
                        + name_s);
            }
        } else {
            File[] files = file.listFiles();
            if (files != null && files.length > 0) {
                for (File file_str : files) {
                    findAllApkFile(file_str);
                }
            }
        }
    }

    /**
     * Abnormal returns collection of files
     * 
     * @return List<String> Abnormal the apk
     */
    public List<String> getExceptionFile() {
        return exceptionFile;
    }

}
