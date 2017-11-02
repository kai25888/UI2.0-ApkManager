
package com.cv.apk_manager.utils;

import java.io.File;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;

/**
 * <b>SearchMyFileInfoTool</b> TODO Detailed information query file (click on
 * the GridView children call)
 * <p>
 * <h1>Summary description:</h1> <b>SearchMyFileInfoTool</b>主要实现如下业务功能:
 * <ul>
 * <li>By passing in the apk file path, to deal with the apk, access to the file
 * information and encapsulated</li>
 * </ul>
 * 本类异常场景进行处理：
 * <ul>
 * <li>null</li>
 * </ul>
 * 
 * <pre>
 *  <b>入口</b>(入口参数):
 *  参数一:  <b>String</b> strPath : The Apk absolute path
 *  参数二:  <b>Context</b> context : For a activity passed in the context of the object, can the activity itself
 * such as MainActivity in this
 * </pre>
 * 
 * <pre>
 *  <b>出口:</b>
 *      Detailed information query file (click on the GridView children call)
 * </pre>
 * 
 * </p>
 * <p>
 * Date: 2015-4-27 下午2:34:43
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
public class SearchMyFileInfoTool {

    private static final String TAG = "com.cv.apk_manager.utils.SearchMyFileInfoTool";

    /** Said has been installed, and with this the apk is a version now */
    private static int INSTALLED = 0;

    /**
     * Said has been installed, version is lower than this version now, you can
     * click on the button to update
     */
    private static int INSTALLED_UPDATE = 1;

    /** Said not installed */
    private static int UNINSTALLED = 2;

    /**
     * @param filePath String: The Apk absolute path
     * @param cvContext Context:context
     * @return MyFile: The custom file entity
     * @throws Exception :file not find exception
     */
    public static MyFile searchMyFileInfo(String filePath, Context cvContext) throws Exception {

        MyFile myFile = new MyFile();
        File file = new File(filePath);

        PackageManager pm = cvContext.getPackageManager();
        PackageInfo packageInfo = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        ApplicationInfo appInfo = packageInfo.applicationInfo;

        appInfo.sourceDir = filePath;
        appInfo.publicSourceDir = filePath;

        // Usb device path eg: /mnt/usb/sda1
        String usbPath = filePath.substring(0, 13);
        myFile.setUsbPath(usbPath);

        // Usb device name
        String usbName = Tools.getVolumeLabel(cvContext,usbPath);
        myFile.setUsbName(usbName);

        // apk Relative paths
        String apkRelativePath = filePath.replace(usbPath, usbName.trim());
        myFile.setApkRelativePath(apkRelativePath);
        Log.d(TAG, "--searchMyFileInfo-------apkRelativePath " + apkRelativePath);

        // icon
        myFile.setApk_icon(appInfo.loadIcon(pm));

        // The package name
        String packageName = packageInfo.packageName;
        myFile.setPackageName(packageName);

        // The file size
        String fileSizeString = Tools.formetFileSize(Tools.getFileSizes(file));
        myFile.setFileSize(fileSizeString);

        // By application of
        myFile.setLabel(appInfo.loadLabel(pm) + "");

        // Apk's absolute path
        myFile.setFilePath(filePath);

        // In the name of the version apk String
        String versionName = packageInfo.versionName;
        myFile.setVersionName(versionName);

        // Apk file name aa.apk
        myFile.setFileName(file.getName());

        // Apk version number int
        int versionCode = packageInfo.versionCode;
        myFile.setVersionCode(versionCode);

        // Install the handle type
        int type = doType(pm, packageName, versionCode, myFile);
        myFile.setInstallStatus(type);

        Log.d(TAG, "--searchMyFileInfo--: " + type + " The file name: " + file.getName());
        return myFile;
    }

    /**
     * Determine the application installed on the phone
     * 
     * @param pm PackageManager
     * @param packageName String To determine the application package name
     * @param versionCode int To determine the application version number
     * @return int: Installation type (0 installed 1 has update 2 not installed)
     */
    private static int doType(PackageManager pm, String packageName, int versionCode, MyFile myFile) {
        List<PackageInfo> pakageinfos = pm
                .getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES);

        for (PackageInfo pi : pakageinfos) {
            String pi_packageName = pi.packageName;
            int pi_versionCode = pi.versionCode;
            // If the package name in existing in the application system has
            // been installed
            if (packageName.endsWith(pi_packageName)) {
                if (versionCode == pi_versionCode) {
                    // Has been installed, no update,
                    return INSTALLED;
                } else if (versionCode > pi_versionCode) {
                    // Has been installed, have update
                    return INSTALLED_UPDATE;
                }
            }
        }
        // Not installed, the application can be installed
        return UNINSTALLED;
    }

}
