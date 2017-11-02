
package com.cv.apk_manager.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.cv.apk_manager.ApkManager;
import com.cv.apk_manager.CleanOrStopAppActivity;
import com.cv.apk_manager.R;
import com.cv.apk_manager.utils.AllPagesAdapter;
import com.cv.apk_manager.utils.ApkInfo;
import com.cv.apk_manager.utils.Constant;
import com.cv.apk_manager.utils.PageControl;
import com.cv.apk_manager.utils.Tools;
import com.cv.apk_manager.view.VerticalViewPager.OnPageChangeListener;

public class AppCleanPageLayout extends LinearLayout implements OnClickListener,
        OnPageChangeListener, OnFocusChangeListener {

    private CleanOrStopAppActivity context;

    public Button selector_all_button;

    public Button selector_none_button;

    public Button clean;

    private FrameLayout app_cache_cleaning;

    private VerticalViewPager clean_view_pager;

    private LinearLayout clean_view_group;

    private CleanPage cleanPages[];

    List<ApkInfo> my_clean_List;

    int list_size;

    int cleanPageCount;

    int iFirst;

    private PageControl pageControl;

    public static boolean app_clean_top_flag = false;

    private PackageManager mpm;

    private ClearCacheObserver mClearCacheObserver;

    private static final int OP_SUCCESSFUL = 1;

    private static final int OP_FAILED = 2;

    private static final int CLEAR_CACHE = 3;

    private static final int CLEAR_NONE = 5;

    private final Handler cacheHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // If the fragment is gone, don't process any more messages.
            switch (msg.what) {
                case CLEAR_CACHE:
                    setSelectAll(false);
                    app_cache_cleaning.setVisibility(View.GONE);
                    Toast.makeText(context, R.string.cache_cleaner_ok, Toast.LENGTH_LONG).show();
                    break;
                case CLEAR_NONE:
                    app_cache_cleaning.setVisibility(View.GONE);
                    Toast.makeText(context,
                            context.getResources().getString(R.string.cache_select_none_tip),
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
        }
    };

    public AppCleanPageLayout(Context context) {
        super(context);
    }

    public AppCleanPageLayout(CleanOrStopAppActivity context) {
        super(context);
        this.context = context;
        mpm = context.getPackageManager();
        initData();
    }

    public void initData() {
        View v = LayoutInflater.from(getContext()).inflate(R.layout.clean_app_layout, this);
        clean_view_pager = (VerticalViewPager) v.findViewById(R.id.clean_view_pager);
        clean_view_group = (LinearLayout) v.findViewById(R.id.clean_view_group);
        selector_all_button = (Button) v.findViewById(R.id.seletor_all_btn);
        selector_none_button = (Button) v.findViewById(R.id.seletor_none_btn);
        clean = (Button) v.findViewById(R.id.close_or_clean_btn);
        app_cache_cleaning = (FrameLayout) v.findViewById(R.id.fl_cache_cleaner_ing);
        selector_all_button.setOnClickListener(this);
        selector_none_button.setOnClickListener(this);
        clean.setOnClickListener(this);
        selector_all_button.setOnFocusChangeListener(this);
        selector_none_button.setOnFocusChangeListener(this);
        clean.setOnFocusChangeListener(this);
        getAllApps();
        ArrayList<View> pages = new ArrayList<View>();
        list_size = my_clean_List.size();
        cleanPageCount = (int) Math.ceil(list_size / Constant.SIZE);
        cleanPages = new CleanPage[cleanPageCount];
        for (int i = 0; i < cleanPageCount; i++) {
            cleanPages[i] = new CleanPage(context, AppCleanPageLayout.this, i);
            pages.add(cleanPages[i]);
        }
        clean_view_pager.removeAllViews();
        pageControl = new PageControl(context, clean_view_group, cleanPageCount);
        clean_view_pager.setAdapter(new AllPagesAdapter(pages));
        clean_view_pager.setOnPageChangeListener(this);
        clean_view_pager.setCurrentItem(0);
        clean_view_pager.requestFocus();
        OnKeyListener keyListener = new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN
                        && keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                    return true;
                }
                return false;
            }
        };
        selector_all_button.setOnKeyListener(keyListener);
        selector_none_button.setOnKeyListener(keyListener);
        clean.setOnKeyListener(keyListener);
    }

    @Override
    public void onClick(View view) {
        if (my_clean_List == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.seletor_all_btn:
                setSelectAll(true);
                break;
            case R.id.seletor_none_btn:
                setSelectAll(false);
                break;
            case R.id.close_or_clean_btn:
                cacheCleaner();
                break;
            default:
                break;
        }
    }

    private void setSelectAll(boolean isSelect) {
        for (int i = 0; i < my_clean_List.size(); i++) {
            my_clean_List.get(i).setSelect(isSelect);
        }
        for (int i = 0; i < cleanPageCount; i++) {
            for (ApkInfo apkInfo : cleanPages[i].getStopList()) {
                apkInfo.setSelect(isSelect);
            }
            cleanPages[i].notifyChanged();
        }
    }

    public void getAllApps() {
        my_clean_List = new ArrayList<ApkInfo>();
        // 主activity读取的 系统所有应用
        List<PackageInfo> clean_List = new ArrayList<PackageInfo>();
        clean_List = ApkManager.cleanPackageInfos;
        if (ApkManager.cleanPackageInfos == null) {
            clean_List = context.getPackageManager().getInstalledPackages(0);
        }
        PackageManager pm = context.getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> mApps = mpm.queryIntentActivities(mainIntent, 0);
        ApkInfo apkInfo;
        ApplicationInfo applicationInfo;
        for (int i = 0; i < clean_List.size(); i++) {
            PackageInfo temp = clean_List.get(i);
            applicationInfo = temp.applicationInfo;
            apkInfo = new ApkInfo();
            apkInfo.setApk_icon(applicationInfo.loadIcon(pm));
            apkInfo.setLabel(applicationInfo.loadLabel(pm) + "");
            apkInfo.setPackageName(temp.packageName);
            apkInfo.setSelect(false);
            for (ResolveInfo resolveInfo : mApps) {
                if ((resolveInfo.loadLabel(pm) + "").equals(pm
                        .getApplicationLabel(temp.applicationInfo) + "")) {
                    if (!Tools.isShuldFiled(temp.packageName + "")) {
                        my_clean_List.add(apkInfo);
                    }
                }
            }
        }

    }

    private boolean isSelectNone() {
        if (my_clean_List == null) {
            return true;
        }
        for (int i = 0; i < my_clean_List.size(); i++) {
            if (my_clean_List.get(i).isSelect()) {
                return false;
            }
        }
        return true;
    }

    public void changedPage(int page, boolean isUP) {
        if (isUP) {
            clean_view_pager.setCurrentItem(page - 1);
        } else {
            clean_view_pager.setCurrentItem(page + 1);
        }
    }

    public Button getButtonView() {
        return selector_all_button;
    }

    private void cacheCleaner() {
        ApkInfo apkInfo;
        String pName;
        if (isSelectNone()) {
            Message msg = new Message();
            msg.what = CLEAR_NONE;
            cacheHandler.sendMessage(msg);
            return;
        }
        app_cache_cleaning.setVisibility(View.VISIBLE);
        for (int i = 0; i < my_clean_List.size(); i++) {
            apkInfo = my_clean_List.get(i);
            if (apkInfo.isSelect()) {
                pName = apkInfo.getPackageName();
                clearCache(pName);
                apkInfo.setSelect(false);
            }
        }
    }

    class ClearCacheObserver extends IPackageDataObserver.Stub {
        @Override
        public void onRemoveCompleted(final String packageName, final boolean succeeded) {
            final Message msg = cacheHandler.obtainMessage(CLEAR_CACHE);
            if (cacheHandler.hasMessages(CLEAR_CACHE)) {
                cacheHandler.removeMessages(CLEAR_CACHE);
            }
            msg.arg1 = succeeded ? OP_SUCCESSFUL : OP_FAILED;
            cacheHandler.sendMessageDelayed(msg, 100);
        }
    }

    private void clearCache(String pName) {
        if (mClearCacheObserver == null) {
            mClearCacheObserver = new ClearCacheObserver();
        }
        mpm.deleteApplicationCacheFiles(pName, mClearCacheObserver);
    }

    @Override
    public void onFocusChange(View v, boolean isFocused) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.seletor_all_btn:
                if (isFocused) {
                    selector_all_button.setTextColor(Color.WHITE);
                } else {
                    selector_all_button.setTextColor(getResources().getColor(R.color.white_fifty));
                }
                break;
            case R.id.seletor_none_btn:
                if (isFocused) {
                    selector_none_button.setTextColor(Color.WHITE);
                    app_clean_top_flag = false;
                } else {
                    selector_none_button.setTextColor(getResources().getColor(R.color.white_fifty));
                }
                break;
            case R.id.close_or_clean_btn:
                if (isFocused) {
                    clean.setTextColor(Color.WHITE);
                    app_clean_top_flag = false;
                } else {
                    clean.setTextColor(getResources().getColor(R.color.white_fifty));

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
