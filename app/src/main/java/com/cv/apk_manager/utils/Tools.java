
package com.cv.apk_manager.utils;

import java.io.File;
import java.io.FileInputStream;
import java.lang.Character.UnicodeBlock;
import java.text.DecimalFormat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;
import android.os.ServiceManager;
import android.os.storage.IMountService;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.TextUtils;
import android.util.Log;

import com.cultraview.tv.storage.CtvMStorageManager;
import com.cv.apk_manager.ApkManager;

/**
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0 Tools.
 */
public class Tools {

    private static final String TAG = ".Tools ";

    public static boolean isShuldFiled(String pkgename) {
        if (pkgename.equals("com.mstar.tv.tvplayer.ui")) {
            return true;
        } else if (pkgename.equals("com.android.wididemo")) {
            return true;
        } else if (pkgename.equals("com.assem.launcher")) {
            return true;
        } else if (pkgename.equals("com.android.contacts")) {
            return true;
        } else if (pkgename.equals("com.android.videoeditor")) {
            return true;
        } else if (pkgename.equals("com.android.email")) {
            return true;
        } else if (pkgename.equals("net.myvst.v2")) {
            return true;
        } else if (pkgename.equals("com.android.settings")) {
            return true;
        } else if (pkgename.equals("com.mstar.tvsetting.hotkey")) {
            return true;
        } else if (pkgename.equals("com.android.providers.downloads.ui")) {
            return true;
        } else if (pkgename.equals("com.cv.apk_manager")) {
            return true;
        }
        return false;
    }

    public static boolean isNeedClean(String pkgename) {
        if (pkgename.equals("com.android.defcontainer")) {
            return true;// 应用程序访问权限
        } else if (pkgename.equals("com.android.keychain")) {
            return true;// 密钥链
        } else if (pkgename.equals("com.android.externalstorage")) {
            return true;// 外部存储设备
        } else if (pkgename.equals("com.android.providers.userdictionary")) {
            return true;// User Dictionary
        } else if (pkgename.equals("com.svox.pico")) {
            return true;// TTS 语音识别系统
        }else if (pkgename.equals("com.cultraview.launcher")) {
            return true;
        }
        return false;
    }

    private static IMountService mService = IMountService.Stub.asInterface(ServiceManager
            .getService("mount"));

    /**
     * @return SD is available ?
     */
    public static boolean detectSDCardAvailability() {
        boolean result = false;
        try {
            StorageManager storageManager = (StorageManager) ApkManager.cvContext
                    .getSystemService(Context.STORAGE_SERVICE);
            StorageVolume[] volumes = storageManager.getVolumeList();
            String usbPath = "";
            for (StorageVolume item : volumes) {
                usbPath = item.getPath();
                if (usbPath.equalsIgnoreCase(getSDPath())) {
                    Log.i(TAG, "-usbPath: " + usbPath);
                    continue;
                }
                String state = storageManager.getVolumeState(usbPath);
                if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
                    continue;
                } else {
                    // Start reading from the usb card
                    File file = new File(usbPath);
                    if (!TextUtils.isEmpty(file.getName())) {
                        result = true;
                    }
                }
            }
        } catch (Exception e) {
            // Can't create file, SD Card is not available
            result = false;
            e.printStackTrace();
        } finally {
            System.out.println("Usb Device Availability:" + result);
        }
        return result;
    }

    /**
     * @return String SD card path ("": sd card not present)
     */
    public static String getSDPath() {
        String sdDir = "";
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED); // To determine whether a
                                                       // sd card
        if (sdCardExist) {
            // Get the root of sd
            sdDir = Environment.getExternalStorageDirectory().toString();
        }
        return sdDir;
    }

    /**
     * @param fileS fileS
     * @return fileSizeString File size converted file size
     */
    public static String formetFileSize(long fileS) {
        // Convert the file size
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "K";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "M";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "G";
        }
        return fileSizeString;
    }

    /**
     * Get File Size
     * 
     * @param file :File To determine the size of the file
     * @return fileSize :long File Size
     * @throws Exception Files to abnormal
     */
    @SuppressWarnings("resource")
    public static long getFileSizes(File file) throws Exception {
        long fileSize = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            fileSize = fis.available();
        } else {
            file.createNewFile();
            System.out.println("File does not exist");
        }
        return fileSize;
    }

    /**
     * Get the device name
     * 
     * @param mountPoint usb Device Path
     * @return usb The device name
     */
    public static String getVolumeLabel(Context context,String mountPoint) {
        CtvMStorageManager storageManager = CtvMStorageManager.getInstance(context);
        String volumeLabel = storageManager.getVolumeLabel(mountPoint);
        if (null == volumeLabel) {
            return " ";
        }
        return volumeLabel;
    }

    @SuppressLint("DefaultLocale")
    private static String utf8ToUnicode(String inStr) {
        char[] myBuffer = inStr.toCharArray();
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < inStr.length(); i++) {
            UnicodeBlock ub = UnicodeBlock.of(myBuffer[i]);
            if (ub == UnicodeBlock.BASIC_LATIN) {
                String unicode = "\\u" + myBuffer[i];
                sb.append(unicode);
            } else {
                short s = (short) myBuffer[i];
                String hexS = Integer.toHexString(s);
                String unicode = "\\u" + hexS;
                sb.append(unicode.toLowerCase());
            }
        }
        return sb.toString();
    }

}
