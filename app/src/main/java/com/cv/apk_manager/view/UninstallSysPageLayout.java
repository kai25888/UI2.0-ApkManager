
package com.cv.apk_manager.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cv.apk_manager.ApkManager;
import com.cv.apk_manager.R;
import com.cv.apk_manager.UninstallActivity;
import com.cv.apk_manager.utils.Constant;
import com.cv.apk_manager.utils.ImageReflect;

/**
 * @since 2.0.0
 */
public class UninstallSysPageLayout extends LinearLayout {

    private static final String TAG = "UninstallSysPageLayout";

    private GridView cv_GridView;

    private int pageIndex;

    private UninstallActivity context;

    private OnKeyListener mylKeyListener;

    private int iFirst;

    private int last = -1;

    private ApksAdapter adapter;

    private List<PackageInfo> my_sys_List;

    private ImageView refimgs[];

    public UninstallSysPageLayout(Context context) {
        super(context);
    }

    /**
     * @param context Context
     */
    public UninstallSysPageLayout(UninstallActivity context, int pageIndex) {
        super(context);
        this.context = context;
        this.pageIndex = pageIndex;
        initView();
    }

    /**
     * Initialize the data
     */
    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.all_apks_layout, this);
        refimgs = new ImageView[5];
        refimgs[0] = (ImageView) findViewById(R.id.login_refimg_1);
        refimgs[1] = (ImageView) findViewById(R.id.login_refimg_2);
        refimgs[2] = (ImageView) findViewById(R.id.login_refimg_3);
        refimgs[3] = (ImageView) findViewById(R.id.login_refimg_4);
        refimgs[4] = (ImageView) findViewById(R.id.login_refimg_5);
        my_sys_List = new ArrayList<PackageInfo>();
        iFirst = pageIndex * Constant.APK_PAGE_SIZE;
        int iEnd = iFirst + Constant.APK_PAGE_SIZE;
        while ((iFirst < ApkManager.sysPackageInfos.size()) && (iFirst < iEnd)) {
            // Add PackageInfo each
            my_sys_List.add(ApkManager.sysPackageInfos.get(iFirst));
            iFirst++;
        }
        if (iFirst < ApkManager.sysPackageInfos.size()) {
            last = -1;
        } else {
            last = ApkManager.sysPackageInfos.size() - pageIndex * Constant.APK_PAGE_SIZE;
        }
        Log.i(TAG, "--last:" + last + " -iFirst:" + iFirst);
        cv_GridView = (GridView) findViewById(R.id.gridView_all_apk);
        adapter = new ApksAdapter(context);
        cv_GridView.setAdapter(adapter);
        cv_GridView.setOnItemClickListener(adapter);
        // When on the first page Intercept the key events on the left
        mylKeyListener = new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    int select = cv_GridView.getSelectedItemPosition() + 1;
                    Log.d(TAG, "onKey--select: " + select);
                    if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
                        if (last == select) {
                            cv_GridView.setSelection(select - 1);
                            return true;
                        } else if (select == 10) {// next page
                            context.changedPage(pageIndex, false);
                            return true;
                        } else if (select == 5) {// next line
                            cv_GridView.setSelection(select);
                            return true;
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
                        if (select == 1) {
                            if (pageIndex == 0) {
                                cv_GridView.setSelection(0);
                            } else {// before page
                                context.changedPage(pageIndex, true);
                            }
                            return true;
                        } else if (select == 6) {// before line
                            cv_GridView.setSelection(select - 2);
                            return true;
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (select > 5) {
                            if (last == -1) {// next page
                                context.changedPage(pageIndex, false);
                                return true;
                            }
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if (select <= 5) {
                            if (pageIndex != 0) {// before page
                                context.changedPage(pageIndex, true);
                                return true;
                            }
                        }
                    }
                }
                return false;
            }
        };
        cv_GridView.setOnKeyListener(mylKeyListener);
    }

    private class ApksAdapter extends BaseAdapter implements OnItemClickListener {
        private PackageInfo apk_packageInfo;

        private Context mContext;

        private PackageManager pManager;

        public ApksAdapter(Context context) {
            mContext = context;
            pManager = mContext.getPackageManager();
        }

        @Override
        public int getCount() {
            return my_sys_List.size();
        }

        @Override
        public Object getItem(int position) {
            return my_sys_List.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View contentview, ViewGroup parent) {
            if (contentview == null) {
                // For a single View through the inflater fill a layout file
                contentview = LayoutInflater.from(mContext).inflate(R.layout.all_apk_item, parent,
                        false);
            }
            PackageInfo packageInfo = my_sys_List.get(position);
            // From the layout file find ImageView and TextView id
            ImageView apkIcon = (ImageView) contentview.findViewById(R.id.iv_all_apk_icon);
            TextView apkName = (TextView) contentview.findViewById(R.id.tv_all_apk_label);
            FrameLayout ll_all_apk = (FrameLayout) contentview.findViewById(R.id.ll_all_apk);
            // Give the corresponding controls the load icon and label
            apkIcon.setImageDrawable(pManager.getApplicationIcon(packageInfo.applicationInfo));
            apkName.setText(pManager.getApplicationLabel(packageInfo.applicationInfo).toString());
            if (position % 8 == 0) {
                // ll_all_apk.setBackgroundColor(0xff11995E);
                ll_all_apk.setBackgroundResource(R.drawable.apk_bg_0);
            } else if (position % 8 == 1) {
                ll_all_apk.setBackgroundResource(R.drawable.apk_bg_1);
            } else if (position % 8 == 2) {
                ll_all_apk.setBackgroundResource(R.drawable.apk_bg_2);
            } else if (position % 8 == 3) {
                ll_all_apk.setBackgroundResource(R.drawable.apk_bg_3);
            } else if (position % 8 == 4) {
                ll_all_apk.setBackgroundResource(R.drawable.apk_bg_4);
            } else if (position % 8 == 5) {
                ll_all_apk.setBackgroundResource(R.drawable.apk_bg_5);
            } else if (position % 8 == 6) {
                ll_all_apk.setBackgroundResource(R.drawable.apk_bg_6);
            } else {
                ll_all_apk.setBackgroundResource(R.drawable.apk_bg_7);
            }
            if (position > 4 && position < 10) {
                Bitmap localBitmap = ImageReflect.convertViewToBitmap(ll_all_apk);
                Bitmap localBitmap1 = ImageReflect.toConformStr(localBitmap, apkName.getText()
                        .toString());
                Bitmap localBitmap2 = ImageReflect.createCutReflectedImage(localBitmap1, 0);
                refimgs[position - 5].setImageBitmap(localBitmap2);
            }
            return contentview;
        }

        /**
         * Set the click event
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            UninstallSysDialog dialog = new UninstallSysDialog(context, position, apk_packageInfo,
                    my_sys_List, 0);
            dialog.show();
        }
    }
}
