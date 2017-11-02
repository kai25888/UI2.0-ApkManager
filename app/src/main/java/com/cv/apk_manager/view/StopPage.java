
package com.cv.apk_manager.view;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.cv.apk_manager.CleanOrStopAppActivity;
import com.cv.apk_manager.R;
import com.cv.apk_manager.utils.ApkInfo;
import com.cv.apk_manager.utils.ApkInfoAdapter;
import com.cv.apk_manager.utils.Constant;

/**
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0
 */
public class StopPage extends LinearLayout {

    private static final String TAG = "StopPage";

    private GridView cv_GridView;

    private int pageIndex;

    private CleanOrStopAppActivity context;

    private AppStopPageLayout stopPage;

    private OnKeyListener mylKeyListener;

    private ApkInfoAdapter adapter;

    private List<ApkInfo> stop_list;

    private int iFirst;

    private int last = -1;

    private ImageView[] refimgs = null;

    public StopPage(Context context) {
        super(context);
    }

    public StopPage(CleanOrStopAppActivity context, AppStopPageLayout stopPage, int pageIndex) {
        super(context);
        this.context = context;
        this.stopPage = stopPage;
        this.pageIndex = pageIndex;
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.all_clean_layout, this);
        refimgs = new ImageView[4];
        refimgs[0] = (ImageView) findViewById(R.id.clean_refimg_1);
        refimgs[1] = (ImageView) findViewById(R.id.clean_refimg_2);
        refimgs[2] = (ImageView) findViewById(R.id.clean_refimg_3);
        refimgs[3] = (ImageView) findViewById(R.id.clean_refimg_4);
        stop_list = new ArrayList<ApkInfo>();
        iFirst = pageIndex * Constant.STOP_PAGE_SIZE;
        int iEnd = iFirst + Constant.STOP_PAGE_SIZE;
        while ((iFirst < stopPage.list_size) && (iFirst < iEnd)) {
            // Add PackageInfo each
            stop_list.add(stopPage.my_stop_list.get(iFirst));
            iFirst++;
        }
        if (iFirst < stopPage.list_size) {
            last = -1;
        } else {
            last = stopPage.list_size - pageIndex * Constant.STOP_PAGE_SIZE;
        }
        cv_GridView = (GridView) findViewById(R.id.gv_all_clean);
        adapter = new ApkInfoAdapter(context, stop_list, refimgs, pageIndex,Constant.TYPE_STOP);
        cv_GridView.setAdapter(adapter);
        cv_GridView.setOnItemClickListener(adapter);
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
                        } else if (select == 8) {// next page
                            stopPage.changedPage(pageIndex, false);
                            return true;
                        } else if (select == 4) {// next line
                            cv_GridView.setSelection(select);
                            return true;
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
                        if (select > 4) {
                            if (last == -1) {// next page
                                stopPage.changedPage(pageIndex, false);
                                return true;
                            }
                        }
                    } else if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
                        if (select <= 4) {
                            if (pageIndex != 0) {// before page
                                stopPage.changedPage(pageIndex, true);
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

    public List<ApkInfo> getStopList() {
        return adapter.getList();
    }

    public void notifyChanged() {
        adapter.notifyChanged();
    }
}
