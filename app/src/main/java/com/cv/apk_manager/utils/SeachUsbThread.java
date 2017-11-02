
package com.cv.apk_manager.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;
import android.widget.Toast;

import com.cv.apk_manager.ApkManager;
import com.cv.apk_manager.InstallActivity;
import com.cv.apk_manager.R;

/**
 * <b>SeachUsbThread</b> TODO Asynchronous thread load usb card, apk file
 * <p>
 * <h1>Summary description:</h1> <b>SeachUsbThread</b>主要实现如下业务功能:
 * <ul>
 * <li>Asynchronous threading the apk traversal problem</li>
 * <li>After traversal will get through the collection of data storage</li>
 * </ul>
 * 本类异常场景进行处理：
 * <ul>
 * <li>null</li>
 * </ul>
 * 
 * <pre>
 *  <b>出口:</b>
 *       This through asynchronous thread to external storage amount traverse the apk. And the apk information after traversing through the Java bean entity encapsulation, stored in the collection
 * </pre>
 * 
 * </p>
 * <p>
 * Date: 2015-4-27 下午2:30:27
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
public class SeachUsbThread extends Thread {

    private List<String> exceptionFiles;

    private String exception_files = "";

    private static String TAG = "com.cv.apk_manager.utils.SeachUsbThread";

    private static final int EXCEPTION_FILES = 0x01;

    private ApkSearchTools apkSearchTools_usb;

    public Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EXCEPTION_FILES:
                    Context context = ContextUtil.getInstance();
                    Toast.makeText(
                            context,
                            context.getResources().getString(R.string.exception_files_1)
                                    + " : \n\r" + exception_files
                                    + context.getResources().getString(R.string.exception_files_2),
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    public void run() {
        ApkManager.sdread_flag = true;
        List<ApkInfo> ApksInUsb = new ArrayList<ApkInfo>();

        try {
            apkSearchTools_usb = new ApkSearchTools(ApkManager.cvContext);

            StorageManager storageManager = (StorageManager) ApkManager.cvContext
                    .getSystemService(Context.STORAGE_SERVICE);
            StorageVolume[] volumes = storageManager.getVolumeList();
            String usbPath = "";
            for (StorageVolume item : volumes) {
                usbPath = item.getPath();
                if (usbPath.equalsIgnoreCase(getSDPath())) {
                    Log.i(TAG, "- -usbPath: " + usbPath);
                    continue;
                }
                String state = storageManager.getVolumeState(usbPath);
                if (state == null || !state.equals(Environment.MEDIA_MOUNTED)) {
                    continue;
                } else {
                    // Start reading from the usb card
                    File file = new File(usbPath);
                    apkSearchTools_usb.findAllApkFile(file);
                }
                System.gc();
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("run", " SeachUsbThread printStackTrace0000000000000000000");
        }

        ApksInUsb = apkSearchTools_usb.getApkFiles();
        Log.d(TAG, "--reading is completed-apk Number :" + ApksInUsb.size());
        ApkManager.sdread_flag = false;

        exceptionFiles = apkSearchTools_usb.getExceptionFile();
        if (exceptionFiles.size() != 0) {
            for (String exception_file : exceptionFiles) {
                exception_files = exception_files + exception_file + ",\n\r";
            }
            Message msg = new Message();
            // Exception file processing
            msg.what = EXCEPTION_FILES;
            mHandler.sendMessage(msg);
        }

        if (InstallActivity.usb_reading_to_over) {
            InstallActivity.usb_reading_to_over = false;
            InstallActivity.updata_usb = true;
        }

        for (ApkInfo apk : ApksInUsb) {
            ApkManager.myApksUsb.add(apk);
        }
        apkSearchTools_usb = null;
        ApksInUsb = null;
        System.gc();
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
            Log.d(TAG, "--getSDPath---------sdCardExist--Can read sd card ---");
        }
        Log.d(TAG, "--getSDPath--------------->> Sd path system: " + sdDir);
        return sdDir;
    }

}
