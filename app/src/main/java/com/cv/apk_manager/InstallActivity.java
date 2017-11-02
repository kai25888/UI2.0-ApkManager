
package com.cv.apk_manager;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cv.apk_manager.utils.AllPagesAdapter;
import com.cv.apk_manager.utils.Constant;
import com.cv.apk_manager.utils.PageControl;
import com.cv.apk_manager.utils.Tools;
import com.cv.apk_manager.view.InstallSysPageLayout;
import com.cv.apk_manager.view.InstallUsbPageLayout;
import com.cv.apk_manager.view.LoadingPageLayout;
import com.cv.apk_manager.view.NotFindApkLayout;
import com.cv.apk_manager.view.NotFindUSBLayout;
import com.cv.apk_manager.view.VerticalViewPager;
import com.cv.apk_manager.view.VerticalViewPager.OnPageChangeListener;

/**
 * @Company: com.cultraview
 * @date: 2016
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0
 */
public class InstallActivity extends Activity implements OnClickListener, OnPageChangeListener {

    protected static final String TAG = "InstallActivity";

    private static final int UPDATA_USB = 0x01;

    private static final int UPDATA_SYS = 0x02;

    public static boolean sys_reading_to_over = false;

    public static boolean usb_reading_to_over = false;

    private VerticalViewPager usb_centerPager;

    private VerticalViewPager sys_centerPager;

    private VerticalViewPager centerPager;

    private LinearLayout ll_install_sys_select;

    private LinearLayout ll_install_usb_select;

    private ImageView iv_install_top_sys;

    private ImageView iv_install_top_usb;

    private TextView tv_install_top_usb;

    private TextView tv_install_top_sys;

    private InstallSysPageLayout installSysPage;

    private InstallUsbPageLayout installUsbPage;

    private LinearLayout group;

    private LinearLayout layout;

    private ArrayList<View> pages_user;

    private ArrayList<View> pages_sys;

    private PageControl pageControl;

    /** Used to mark the top 0:system ; 1:usb */
    private int top_install_flag;

    /** true: the need to jump to ,false: no need */
    public static boolean jump_usb = false;

    public static boolean mount_usb = false;

    public static boolean updata_usb = false;

    public static boolean updata_sys = false;

    private boolean thread_flag = true;

    /** No apk file system */
    private boolean sysNotApkFile = true;

    /** USB no APK file */
    private boolean usbNotApkFile = true;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATA_USB:
                    System.gc();
                    jumpSysToUsb();
                    break;
                case UPDATA_SYS:
                    initSysPages();
                    break;
            }
        }
    };

    final BroadcastReceiver broadcastRec_install = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (top_install_flag == 1) {
                mHandler.sendEmptyMessage(UPDATA_USB);
            }
            if (intent.getAction().equals("android.intent.action.MEDIA_MOUNTED")) {
                mount_usb = true;
            } else if (intent.getAction().equals("android.intent.action.MEDIA_REMOVED")
                    || intent.getAction().equals("android.intent.action.ACTION_MEDIA_UNMOUNTED")
                    || intent.getAction().equals("android.intent.action.ACTION_MEDIA_BAD_REMOVAL")) {
                if (jump_usb == false && mount_usb == true) {
                    mount_usb = false;
                } else {
                    updata_usb = true;
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_install);
        initView();
        Intent intent = getIntent();
        boolean isJumpUsb = intent.getBooleanExtra("jump_usb", false);
        if (isJumpUsb) {
            loadUsbPage();
            ll_install_usb_select.requestFocus();
            top_install_flag = 1;
        } else {
            loadSysPage();
        }
        new Thread(new UpdateInstallThread()).start();
        initFilter();
    }

    @SuppressLint("CutPasteId")
    public void initView() {
        group = (LinearLayout) findViewById(R.id.install_viewGroup);
        layout = (LinearLayout) findViewById(R.id.install_viewGroup);
        centerPager = (VerticalViewPager) findViewById(R.id.install_apk_content);
        sys_centerPager = (VerticalViewPager) findViewById(R.id.install_apk_content);
        usb_centerPager = (VerticalViewPager) findViewById(R.id.install_apk_content);
        ll_install_sys_select = (LinearLayout) findViewById(R.id.ll_install_sys_select);
        ll_install_usb_select = (LinearLayout) findViewById(R.id.ll_install_usb_select);
        iv_install_top_sys = (ImageView) findViewById(R.id.iv_install_top_sys);
        iv_install_top_usb = (ImageView) findViewById(R.id.iv_install_top_usb);
        tv_install_top_usb = (TextView) findViewById(R.id.tv_install_top_usb);
        tv_install_top_sys = (TextView) findViewById(R.id.tv_install_top_sys);
        tv_install_top_usb.setTextColor(0x50ffffff);
        ll_install_usb_select.setOnClickListener(this);
        ll_install_sys_select.setOnClickListener(this);
    }

    private void loadSysPage() {
        sysNotApkFile = true;
        if (ApkManager.sys_read_flag) {
            sys_reading_to_over = true;
            ArrayList<View> pages = new ArrayList<View>();
            LoadingPageLayout loadingPageLayout = new LoadingPageLayout(this);
            pages.add(loadingPageLayout);
            centerPager.setAdapter(new AllPagesAdapter(pages));
        } else if (ApkManager.myApksSys.size() == 0) {
            notFindApkFiles();
        } else {
            sysNotApkFile = false;
            initSysPages();
        }
    }

    private void loadUsbPage() {
        usbNotApkFile = true;
        // if no usb
        if (!Tools.detectSDCardAvailability()) {
            layout.removeAllViews();
            ArrayList<View> pages = new ArrayList<View>();
            NotFindUSBLayout notFindUSBLayout = new NotFindUSBLayout(this);
            pages.add(notFindUSBLayout);
            centerPager.setAdapter(new AllPagesAdapter(pages));
        } else
        // Reading
        if (ApkManager.sdread_flag) {
            usb_reading_to_over = true;
            ArrayList<View> pages = new ArrayList<View>();
            LoadingPageLayout loadingPageLayout = new LoadingPageLayout(this);
            pages.add(loadingPageLayout);
            centerPager.setAdapter(new AllPagesAdapter(pages));
        }
        // no apk file
        else if (ApkManager.myApksUsb.size() == 0) {
            notFindApkFiles();
        } else {
            usbNotApkFile = false;
            initUsbPages();
        }
    }

    private void initFilter() {
        IntentFilter intentFilter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
        intentFilter.addAction(Intent.ACTION_MEDIA_REMOVED);
        intentFilter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
        intentFilter.addDataScheme("file");
        try {
            registerReceiver(this.broadcastRec_install, intentFilter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View view) {
        if (ll_install_sys_select == view) {
            jumpUsbToSys();
        } else if (ll_install_usb_select == view) {
            jumpSysToUsb();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            // If there is no system apk file
            if ((top_install_flag == 0) && sysNotApkFile) {
                ll_install_sys_select.requestFocus();
                return true;
            }
            if (top_install_flag == 1) {
                ll_install_usb_select.requestFocus();
                return true;
            } else {
                ll_install_sys_select.requestFocus();
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            if (ll_install_usb_select.isFocused()) {
                if (usb_reading_to_over || usbNotApkFile) {
                    Log.d(TAG, "-Scan file -the key switch has been intercepted->>");
                    return true;
                }
            }
            if (top_install_flag == 0) {
                if (sysNotApkFile) {
                    return true;
                } else {
                    sys_centerPager.requestFocus();
                    return true;
                }
            } else if (top_install_flag == 1) {
                if (!usbNotApkFile) {
                    usb_centerPager.requestFocus();
                }
            }
            return super.onKeyDown(keyCode, event);
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            if (ll_install_usb_select.isFocused()) {
                if (usb_reading_to_over) {
                    Log.d(TAG, "-- scan file left key switch has been intercepted->>");
                    return true;
                }
                jumpUsbToSys();
                jump_usb = false;
                return true;
            }
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            if (ll_install_sys_select.isFocused()) {
                jump_usb = true;
                jumpSysToUsb();
                return true;
            } else if (ll_install_usb_select.isFocused()) {
                return true;
            }
            return super.onKeyDown(keyCode, event);
        }
        return super.onKeyDown(keyCode, event);
    }

    public void changedPage(int page, boolean isUP) {
        if (top_install_flag == 1) {
            if (isUP) {// page up
                usb_centerPager.setCurrentItem(page - 1);
            } else {// page down
                usb_centerPager.setCurrentItem(page + 1);
            }
        } else if (top_install_flag == 0) {
            if (isUP) {
                sys_centerPager.setCurrentItem(page - 1);
            } else {
                sys_centerPager.setCurrentItem(page + 1);
            }
        }
    }

    public void initSysPages() {
        pages_sys = new ArrayList<View>();
        int sysPageCount = (int) Math.ceil(ApkManager.myApksSys.size() / Constant.PAGE_SIZE);
        for (int i = 0; i < sysPageCount; i++) {
            installSysPage = new InstallSysPageLayout(this, i);
            pages_sys.add(installSysPage);
        }
        pageControl = new PageControl(this, (LinearLayout) group, sysPageCount);
        sys_centerPager.setAdapter(new AllPagesAdapter(pages_sys));
        sys_centerPager.setOnPageChangeListener(this);
    }

    public void initUsbPages() {
        pages_user = new ArrayList<View>();
        int userPageCount = (int) Math.ceil(ApkManager.myApksUsb.size() / Constant.PAGE_SIZE);
        for (int i = 0; i < userPageCount; i++) {
            installUsbPage = new InstallUsbPageLayout(this, i);
            pages_user.add(installUsbPage);
        }
        pageControl = new PageControl(this, (LinearLayout) group, userPageCount);
        usb_centerPager.setAdapter(new AllPagesAdapter(pages_user));
        usb_centerPager.setOnPageChangeListener(this);
    }

    private void jumpUsbToSys() {
        // usb-->system
        top_install_flag = 0;
        loadSysPage();
        ll_install_sys_select.requestFocus();
        iv_install_top_usb.setBackgroundResource(R.drawable.install_title_usb);
        tv_install_top_usb.setTextColor(0x50ffffff);
        iv_install_top_sys.setBackgroundResource(R.drawable.uninstall_title_system_focus);
        tv_install_top_sys.setTextColor(0xffffffff);
    }

    private void jumpSysToUsb() {
        // from system-->Usb
        top_install_flag = 1;
        loadUsbPage();
        iv_install_top_sys.setBackgroundResource(R.drawable.uninstall_title_system);
        tv_install_top_sys.setTextColor(0x50ffffff);
        iv_install_top_usb.setBackgroundResource(R.drawable.install_title_usb_focus);
        tv_install_top_usb.setTextColor(0xffffffff);
        ll_install_usb_select.requestFocus();
    }

    /**
     * Apk file is not found when loading layout
     */
    private void notFindApkFiles() {
        layout.removeAllViews();
        ArrayList<View> pages = new ArrayList<View>();
        NotFindApkLayout notFindApkLayout = new NotFindApkLayout(this);
        pages.add(notFindApkLayout);
        centerPager.setAdapter(new AllPagesAdapter(pages));
    }

    private class UpdateInstallThread extends Thread {
        @Override
        public void run() {
            try {
                while (thread_flag) {
                    if (updata_usb) {
                        Message msg = new Message();
                        msg.what = UPDATA_USB;
                        mHandler.sendMessage(msg);
                        updata_usb = false;
                    }
                    if (updata_sys) {
                        Message msg = new Message();
                        msg.what = UPDATA_SYS;
                        mHandler.sendMessage(msg);
                        updata_sys = false;
                    }
                    if (mount_usb && jump_usb) {
                        mount_usb = false;
                        Message msg = new Message();
                        msg.what = UPDATA_USB;
                        mHandler.sendMessage(msg);
                    }
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    public void onPageSelected(int page) {
        pageControl.selectPage(page);
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {

    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    protected void onDestroy() {
        thread_flag = false;
        unregisterReceiver(broadcastRec_install);
        super.onDestroy();
    }
}
