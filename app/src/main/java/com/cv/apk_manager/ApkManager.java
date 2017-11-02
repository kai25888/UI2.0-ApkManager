
package com.cv.apk_manager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cv.apk_manager.utils.ApkInfo;
import com.cv.apk_manager.utils.ApkSearchTools;
import com.cv.apk_manager.utils.Constant;
import com.cv.apk_manager.utils.SeachUsbThread;
import com.cv.apk_manager.utils.Tools;

/**
 * @Company: com.cultraview
 * @date: 2016
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0
 */
public class ApkManager extends Activity implements OnClickListener, OnFocusChangeListener {

    private static final String TAG = "ApkManager";

    private static final int SYS_NO_APK = 0x01;

    private final int loginSize = 3;

    private final FrameLayout[] fls = new FrameLayout[loginSize];

    private final ImageView[] imageViews = new ImageView[loginSize];

    private boolean isExit = false;

    /** True: read system apk is false: not finished reading it */
    public static boolean sys_read_over = false;

    /** Set the sd card 1. False: no 2. True: there are;Set the initial is true */
    public static boolean sd = false;

    /** Read sd card state of true: the false reading: read the complete */
    public static boolean sdread_flag = false;

    /** Read the state of the sys true: the false reading: read the complete */
    public static boolean sys_read_flag = true;

    private ApkSearchTools apkSearchTools_sys;

    public static List<ApkInfo> myApksUsb;

    public static List<ApkInfo> myApksSys;

    // public static List<ApkInfo> myApksClean;

    /** Store all installed software information */
    public List<PackageInfo> allPackageInfos;

    /** Installation of the software to store user information */
    public static List<PackageInfo> userPackageInfos;

    /** Information storage system software installed */
    public static List<PackageInfo> sysPackageInfos;

    public static List<PackageInfo> cleanPackageInfos;

    public static Context cvContext;

    private UninstallOrInstallReceiver uninstallOrInstallReceiver;

    private final TextView[] mv_page_apps = new TextView[3];

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SYS_NO_APK:
                    Toast.makeText(ApkManager.this, R.string.MainActivity_sys_no_apks,
                            Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    final BroadcastReceiver broadcastRec = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            System.gc();
            loadUsb();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        cvContext = ApkManager.this;
        myApksUsb = new ArrayList<ApkInfo>();
        // The apk asynchronous thread to read a usb card
        loadUsb();
        // Asynchronous thread reading system memory
        new Thread(new SeachSystemThread()).start();
        // The apk asynchronous thread reading system memory
        new Thread(new SeachSysApksThread()).start();
        setContentView(R.layout.activity_main);
        initView();
        fls[0].requestFocus();
        initFilter();
    }

    private void initView() {
        fls[0] = (FrameLayout) findViewById(R.id.login_fl_1);
        fls[1] = (FrameLayout) findViewById(R.id.login_fl_2);
        fls[2] = (FrameLayout) findViewById(R.id.login_fl_3);
        imageViews[0] = ((ImageView) findViewById(R.id.login_iv_uninstall));
        imageViews[1] = ((ImageView) findViewById(R.id.login_iv_install));
        imageViews[2] = ((ImageView) findViewById(R.id.login_iv_appatv));
        mv_page_apps[0] = ((TextView) findViewById(R.id.login_tv_uninstall));
        mv_page_apps[1] = ((TextView) findViewById(R.id.login_tv_install));
        mv_page_apps[2] = ((TextView) findViewById(R.id.login_tv_exit));

        imageViews[0].setOnClickListener(this);
        imageViews[1].setOnClickListener(this);
        imageViews[2].setOnClickListener(this);
        fls[0].setOnFocusChangeListener(this);
        fls[1].setOnFocusChangeListener(this);
        fls[2].setOnFocusChangeListener(this);

    }

    private void initFilter() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        intentFilter.addDataScheme("file");
        try {
            // Register to monitor function
            registerReceiver(broadcastRec, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
        uninstallOrInstallReceiver = new UninstallOrInstallReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(Constant.UNINSTALL_STRING);
        filter.addAction(Constant.INSTALL_STRING);
        filter.addDataScheme("package");
        // Registered unloading radio
        registerReceiver(uninstallOrInstallReceiver, filter);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        Log.i("cp", "onfocuschange");
        switch (v.getId()) {
            case R.id.login_fl_1:
                updateFocusData(hasFocus, 0);
                break;
            case R.id.login_fl_2:
                updateFocusData(hasFocus, 1);
                break;
            case R.id.login_fl_3:
                updateFocusData(hasFocus, 2);
                break;
            default:
                break;
        }
    }

    public void updateFocusData(boolean hasFocus, int index) {
        Log.i(TAG, hasFocus + " updateFocusData " + index);
        // Update top view
        if (hasFocus) {
            mv_page_apps[index].setSelected(true);
        } else {
            mv_page_apps[index].setSelected(false);
        }
        // Start animation
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d(TAG, "-onKeyDown" + keyCode);
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            if (fls[0].isFocused()) {
                Intent intentUninstall = new Intent();
                intentUninstall.setClass(ApkManager.this, UninstallActivity.class);
                ApkManager.this.startActivity(intentUninstall);
                return true;
            } else if (fls[1].isFocused()) {
                Intent intentInstall = new Intent();
                intentInstall.setClass(ApkManager.this, InstallActivity.class);
                ApkManager.this.startActivity(intentInstall);
                return true;
            } else if (fls[2].isFocused()) {
                Intent intentAppatv = new Intent();
                intentAppatv.setClass(ApkManager.this, CleanOrStopAppActivity.class);
                ApkManager.this.startActivity(intentAppatv);
                return true;
            }
        }
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitBy2Click();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "-onClick" + view.getId());
        switch (view.getId()) {
            case R.id.login_iv_uninstall:
                Intent intentUninstall = new Intent();
                intentUninstall.setClass(ApkManager.this, UninstallActivity.class);
                ApkManager.this.startActivity(intentUninstall);
                break;
            case R.id.login_iv_install:
                Intent intentInstall = new Intent();
                intentInstall.setClass(ApkManager.this, InstallActivity.class);
                ApkManager.this.startActivity(intentInstall);
                break;
            case R.id.login_iv_appatv:
                Intent intentAppatv = new Intent();
                intentAppatv.setClass(ApkManager.this, CleanOrStopAppActivity.class);
                ApkManager.this.startActivity(intentAppatv);
                break;
            default:
                break;
        }
    }

    public static void loadUsb() {
        if (Tools.detectSDCardAvailability() && sdread_flag == false) {
            sd = true;
            myApksUsb.clear();
            new Thread(new SeachUsbThread()).start();
        } else {
            sd = false;
        }
    }

    public class SeachSystemThread extends Thread {
        @Override
        public void run() {
            sys_read_over = false;
            // To obtain the system install all software information
            allPackageInfos = getPackageManager().getInstalledPackages(0);
            PackageManager pm = cvContext.getPackageManager();
            // Users to install software packet
            userPackageInfos = new ArrayList<PackageInfo>();
            // System installation software packet
            sysPackageInfos = new ArrayList<PackageInfo>();
            cleanPackageInfos = new ArrayList<PackageInfo>();
            Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
            mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            List<ResolveInfo> mApps = getPackageManager().queryIntentActivities(mainIntent, 0);
            boolean isok;
            // Cycle took out all the software information
            for (int i = 0; i < allPackageInfos.size(); i++) {
                // Each software information
                PackageInfo temp = allPackageInfos.get(i);
                cleanPackageInfos.add(temp);
                ApplicationInfo appInfo = temp.applicationInfo;
                if ((appInfo.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0
                        || (appInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    isok = false;
                    for (ResolveInfo resolveInfo : mApps) {
                        if ((resolveInfo.loadLabel(pm) + "").equals(pm
                                .getApplicationLabel(temp.applicationInfo) + "")) {
                            if (!Tools.isShuldFiled(temp.packageName + "")) {
                                // The system software
                                isok = true;
                            }
                        }
                    }
                    if (isok) {
                        // System has been installed
                        sysPackageInfos.add(temp);
                    }
                } else {
                    // The user has installed the software
                    userPackageInfos.add(temp);
                }
            }
            sys_read_over = true;
            Log.i(TAG, "--The number of system installation:" + sysPackageInfos.size());
            Log.i(TAG, "--Number of users to install:" + userPackageInfos.size());
            if (UninstallActivity.sysall_reading_to_over) {
                UninstallActivity.sysall_reading_to_over = false;
                UninstallActivity.updata_sys_all = true;
            }
        }
    };

    public String getSDPath() {
        String sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            // Access to the root directory of the sd
            sdDir = Environment.getExternalStorageDirectory().toString();
            Log.d(TAG, "--getSDPath-------->>System sd path" + sdDir);
        }
        return sdDir;
    }

    private class SeachSysApksThread extends Thread {
        @SuppressLint("SdCardPath")
        @Override
        public void run() {
            sys_read_flag = true;
            myApksSys = new ArrayList<ApkInfo>();
            try {
                apkSearchTools_sys = new ApkSearchTools(cvContext);

                File file = new File(getSDPath());
                apkSearchTools_sys.findAllApkFile(file);
            } catch (Exception e) {
                e.printStackTrace();
            }
            myApksSys = apkSearchTools_sys.getApkFiles();
            if (myApksSys.size() == 0) {
                // No system application
                /*Message msg = new Message();
                msg.what = SYS_NO_APK;
                mHandler.sendMessage(msg);*/
            } else {
                Log.d(TAG, "--SeachSysApksThread----sys The number of the Apk:" + myApksSys.size());
            }
            sys_read_flag = false;
            if (InstallActivity.sys_reading_to_over) {
                InstallActivity.sys_reading_to_over = false;
                InstallActivity.updata_sys = true;
            }
        }
    };

    private void exitBy2Click() {
        Timer tExit = null;
        if (isExit == false) {
            isExit = true;
            Toast.makeText(this, R.string.MainActivity_exitBy2Click, Toast.LENGTH_SHORT).show();
            tExit = new Timer();
            tExit.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);

        } else {
            finish();
            System.exit(0);
        }
    }

    class UninstallOrInstallReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // dataString==package:packageName
            String dataString = intent.getDataString();
            Log.i(TAG, "========dataString==" + dataString);
            PackageInfo currentPackageInfo = null;
            if (intent.getAction().equals(Constant.UNINSTALL_STRING)) {
                for (PackageInfo packageInfo : userPackageInfos) {
                    if (dataString.endsWith(packageInfo.packageName)) {
                        currentPackageInfo = packageInfo;
                        break;
                    }
                }
                userPackageInfos.remove(currentPackageInfo);
                UninstallActivity.unInstallReturn = true;
            } else if (intent.getAction().equals(Constant.INSTALL_STRING)) {
                PackageManager pm = cvContext.getPackageManager();
                List<PackageInfo> all_package_infos = pm.getInstalledPackages(0);
                for (PackageInfo packageInfo : all_package_infos) {
                    if (dataString.endsWith(packageInfo.packageName)) {
                        currentPackageInfo = packageInfo;
                        break;
                    }
                }
                userPackageInfos.add(currentPackageInfo);
            }
        }
    }

    @Override
    protected void onDestroy() {
        this.unregisterReceiver(broadcastRec);
        this.unregisterReceiver(uninstallOrInstallReceiver);
        super.onDestroy();
    }

}
