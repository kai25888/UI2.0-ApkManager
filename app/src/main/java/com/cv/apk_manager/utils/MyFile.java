
package com.cv.apk_manager.utils;

import android.graphics.drawable.Drawable;

/**
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0
 */
public class MyFile {

    /** Obtain the apk icon */
    private Drawable apk_icon;

    /**
     * The apk installation state Install processing types: 0. Has been
     * installed, no update, can unload the application;1. Has been installed,
     * have update;2. Not installed, the application can be installed
     */
    private int installStatus;

    /** The package name */
    private String packageName;

    /** Apk's absolute path */
    private String filePath;

    /** The relative path to the apk */
    private String apkRelativePath;

    /** Application of the label */
    public String label;

    /** Apk version of the name String */
    private String versionName;

    /** The apk int version number */
    private int versionCode;

    /** Apk file name aa. Apk */
    public String fileName;

    /** The file size */
    public String usbName;

    /** Usb device name */
    public String usbPath;

    /**
     * file Size
     */
    public String fileSize;

    public MyFile() {
        super();
    }

    public String getLabel() {
        return label;
    }

    public String getFileSize() {
        return fileSize;
    }

    public void setFileSize(String fileSize) {
        this.fileSize = fileSize;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getApkRelativePath() {
        return apkRelativePath;
    }

    public void setApkRelativePath(String apkRelativePath) {
        this.apkRelativePath = apkRelativePath;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Drawable getApk_icon() {
        return apk_icon;
    }

    public void setApk_icon(Drawable apk_icon) {
        this.apk_icon = apk_icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public int getInstallStatus() {
        return installStatus;
    }

    public void setInstallStatus(int installStatus) {
        this.installStatus = installStatus;
    }

    public String getUsbName() {
        return usbName;
    }

    public void setUsbName(String usbName) {
        this.usbName = usbName;
    }

    public String getUsbPath() {
        return usbPath;
    }

    public void setUsbPath(String usbPath) {
        this.usbPath = usbPath;
    }

}
