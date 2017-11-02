
package com.cv.apk_manager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.cv.apk_manager.R;

/**
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 1.0.0
 */
public class LoadingPageLayout extends LinearLayout {

    /**
     * @param context Context
     */
    public LoadingPageLayout(Context context) {
        super(context);
        initView();
    }

    public void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.all_apks_reading, this);
    }

}
