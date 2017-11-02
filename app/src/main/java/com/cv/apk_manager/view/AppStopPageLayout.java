
package com.cv.apk_manager.view;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cv.apk_manager.CleanOrStopAppActivity;
import com.cv.apk_manager.R;
import com.cv.apk_manager.utils.AllPagesAdapter;
import com.cv.apk_manager.utils.ApkInfo;
import com.cv.apk_manager.utils.Constant;
import com.cv.apk_manager.utils.PageControl;
import com.cv.apk_manager.utils.Tools;
import com.cv.apk_manager.view.VerticalViewPager.OnPageChangeListener;

public class AppStopPageLayout extends LinearLayout implements OnClickListener,
        OnFocusChangeListener, OnPageChangeListener {
    private static final String TAG = "AppStopPageLayout";

    public static boolean app_stop_top_flag = false;

    public Button stop_seletor_all_btn;

    public Button stop_seletor_none_btn;

    public Button stop_btn;

    private VerticalViewPager stop_view_pager;

    private PageControl pageControl;

    private LinearLayout stop_view_group;

    private FrameLayout app_stopping;

    public static ArrayList<ApkInfo> my_stop_list;

    private StopPage stopPages[];

    protected int list_size, stopPageCount;

    private PackageManager mPm;

    private ActivityManager mActivityManager;

    private CleanOrStopAppActivity context;

    private static final int BEGIN_STOPPING = 0;

    private static final int APP_STOP = 1;

    private static final int SELECTOR_NONE = 2;

    private static final int HAVE_STOPPED = 3;

    private static final int NO_CLEAR = 4;

    private String clearMem = "";

    private long killSize = 0;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case BEGIN_STOPPING:
                    app_stopping.setVisibility(View.VISIBLE);
                    break;
                case APP_STOP:
                    initDate();
                    app_stopping.setVisibility(View.GONE);
                    stop_btn.requestFocus();
                    CtvToast.makeText(context, getCleanResult(), R.drawable.img_toast,
                            Toast.LENGTH_LONG).show();
                    break;
                case NO_CLEAR:
                    initDate();
                    app_stopping.setVisibility(View.GONE);
                    CtvToast.makeText(context, R.string.no_clear, R.drawable.img_toast,
                            Toast.LENGTH_LONG).show();
                    break;
                case SELECTOR_NONE:
                    app_stopping.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.cache_select_none_tip, Toast.LENGTH_LONG)
                            .show();
                    break;
                case HAVE_STOPPED:
                default:
                    break;
            }
        }
    };

    public AppStopPageLayout(Context context) {
        super(context);
    }

    public AppStopPageLayout(CleanOrStopAppActivity context) {
        super(context);
        this.context = context;
        mPm = context.getPackageManager();
        initView();
        getStopApps();
        initDate();
    }

    private void initView() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.stop_app_layout, this);
        stop_seletor_all_btn = (Button) v.findViewById(R.id.stop_seletor_all_btn);
        stop_seletor_none_btn = (Button) v.findViewById(R.id.stop_seletor_none_btn);
        stop_btn = (Button) v.findViewById(R.id.stop_btn);
        stop_view_group = (LinearLayout) v.findViewById(R.id.stop_view_group);
        app_stopping = (FrameLayout) v.findViewById(R.id.fl_stop_cleaner_ing);
        stop_view_pager = (VerticalViewPager) v.findViewById(R.id.stop_view_pager);
        stop_seletor_all_btn.setOnClickListener(this);
        stop_seletor_none_btn.setOnClickListener(this);
        stop_btn.setOnClickListener(this);
        stop_seletor_all_btn.setOnFocusChangeListener(this);
        stop_seletor_none_btn.setOnFocusChangeListener(this);
        stop_btn.setOnFocusChangeListener(this);
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN && list_size == 0
                        && keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                    return true;
                }
                return false;
            }
        };
        stop_seletor_all_btn.setOnKeyListener(keyListener);
        stop_seletor_none_btn.setOnKeyListener(keyListener);
        stop_btn.setOnKeyListener(keyListener);
    }

    public void initDate() {
        ArrayList<View> pages_user = new ArrayList<View>();
        stop_view_pager.removeAllViews();
        list_size = my_stop_list.size();
        stopPageCount = (int) Math.ceil(list_size / Constant.SIZE);
        stopPages = new StopPage[stopPageCount];
        for (int i = 0; i < stopPageCount; i++) {
            stopPages[i] = new StopPage(context, AppStopPageLayout.this, i);
            pages_user.add(stopPages[i]);
        }
        pageControl = new PageControl(context, stop_view_group, stopPageCount);
        stop_view_pager.setAdapter(new AllPagesAdapter(pages_user));
        stop_view_pager.setOnPageChangeListener(this);
        stop_view_pager.setCurrentItem(0);
    }

    @Override
    public void onClick(View view) {
        if (my_stop_list == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.stop_seletor_all_btn:
                setSelectAll(true);
                break;
            case R.id.stop_seletor_none_btn:
                setSelectAll(false);
                break;
            case R.id.stop_btn:
                Thread mThread = new Thread(mRunnable);
                mThread.start();
                break;
            default:
                break;
        }
    }

    private void getStopApps() {
        my_stop_list = new ArrayList<ApkInfo>();
        mActivityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> infoList = mActivityManager.getRunningAppProcesses();
        if (infoList != null) {
            for (RunningAppProcessInfo appProcessInfo : infoList) {
                if (appProcessInfo.importance > RunningAppProcessInfo.IMPORTANCE_VISIBLE) {
                    String[] pkgList = appProcessInfo.pkgList;
                    for (int i = 0; i < pkgList.length; ++i) {
                        Log.i(TAG, "==: " + pkgList[i]);
                        if (!Tools.isNeedClean(pkgList[i])) {
                            my_stop_list.add(getAppInfo(pkgList[i]));
                        }
                    }
                }
            }
        }
    }

    private ApkInfo getAppInfo(String processName) {
        PackageInfo app = null;
        try {
            app = mPm.getPackageInfo(processName, 0);
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        ApkInfo appInfo = new ApkInfo();
        ApplicationInfo applicationInfo = app.applicationInfo;
        appInfo.setLabel(applicationInfo.loadLabel(mPm) + "");
        appInfo.setApk_icon(applicationInfo.loadIcon(mPm));
        appInfo.setProcessName(processName);
        return appInfo;
    }

    private String getCleanResult() {
        return context.getString(R.string.clean_bg_process) + " " + killSize
                + context.getString(R.string.unit) + "\n"
                + context.getString(R.string.release_memory) + clearMem;
    }

    private void stopApp() {
        // 发送消息，progressbar 显示
        Message msg = new Message();
        msg.what = BEGIN_STOPPING;
        mHandler.sendMessage(msg);
        // 遍历 applist 如果被选中就干掉这个线程
        killSize = 0;
        MemoryInfo bmi = new MemoryInfo();
        mActivityManager = (ActivityManager) getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.getMemoryInfo(bmi);
        double beforeMem = bmi.availMem / (1024 * 1024.0);
        Log.i(TAG, "beforeMem: " + beforeMem + "M");
        list_size = my_stop_list.size();
        ArrayList<ApkInfo> temp_list = new ArrayList<ApkInfo>();
        for (int i = 0; i < list_size; i++) {
            if (my_stop_list.get(i).isSelect()) {
                killSize++;
                mActivityManager.killBackgroundProcesses(my_stop_list.get(i).getProcessName());
            } else {
                temp_list.add(my_stop_list.get(i));
            }
        }
        my_stop_list.clear();
        my_stop_list.addAll(temp_list);
        temp_list.clear();
        temp_list = null;
        MemoryInfo ami = new MemoryInfo();
        mActivityManager = (ActivityManager) getContext()
                .getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.getMemoryInfo(ami);
        double releaseMem = (ami.availMem / (1024 * 1024.0)) - beforeMem;
        if (releaseMem > 1.0) {
            DecimalFormat df = new DecimalFormat("#.00");
            clearMem = df.format(releaseMem) + context.getString(R.string.memory_unit_m);
        } else if (releaseMem == 0.0) {
            Message msg2 = new Message();
            msg2.what = NO_CLEAR;
            mHandler.sendMessage(msg2);
            return;
        } else {
            releaseMem = (long) (releaseMem * 1024);
            clearMem = "" + releaseMem + context.getString(R.string.memory_unit_k);
        }
        Log.i(TAG, "clearMem: " + clearMem);
        Log.i(TAG, "killSize: " + killSize + " ge");
        Message msg1 = new Message();
        msg1.what = APP_STOP;
        mHandler.sendMessage(msg1);
    }

    public Button getButtonView() {
        return stop_seletor_all_btn;
    }

    public void changedPage(int page, boolean isUP) {
        if (isUP) {
            stop_view_pager.setCurrentItem(page - 1);
        } else {
            stop_view_pager.setCurrentItem(page + 1);
        }
    }

    private void setSelectAll(boolean isSelect) {
        for (int i = 0; i < my_stop_list.size(); i++) {
            my_stop_list.get(i).setSelect(isSelect);
        }
        for (int i = 0; i < stopPageCount; i++) {
            for (ApkInfo apkInfo : stopPages[i].getStopList()) {
                apkInfo.setSelect(isSelect);
            }
            stopPages[i].notifyChanged();
        }
    }

    Runnable mRunnable = new Runnable() {
        @Override
        public void run() {
            if (isSelectNone()) {
                Message msg = new Message();
                msg.what = SELECTOR_NONE;
                mHandler.sendMessage(msg);
                return;
            }
            stopApp();
        }
    };

    private boolean isSelectNone() {
        if (my_stop_list == null) {
            return true;
        }
        for (int i = 0; i < my_stop_list.size(); i++) {
            if (my_stop_list.get(i).isSelect()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onFocusChange(View v, boolean isFocused) {
        switch (v.getId()) {
            case R.id.stop_seletor_all_btn:
                if (isFocused) {
                    stop_seletor_all_btn.setTextColor(Color.WHITE);
                } else {
                    stop_seletor_all_btn.setTextColor(getResources().getColor(R.color.white_fifty));
                }
                break;
            case R.id.stop_seletor_none_btn:
                if (isFocused) {
                    stop_seletor_none_btn.setTextColor(Color.WHITE);
                    app_stop_top_flag = false;
                } else {
                    stop_seletor_none_btn
                            .setTextColor(getResources().getColor(R.color.white_fifty));
                }
                break;
            case R.id.stop_btn:
                if (isFocused) {
                    stop_btn.setTextColor(Color.WHITE);
                    app_stop_top_flag = false;
                } else {
                    stop_btn.setTextColor(getResources().getColor(R.color.white_fifty));

                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
    }

    @Override
    public void onPageSelected(int page) {
        pageControl.selectPage(page);
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

}
