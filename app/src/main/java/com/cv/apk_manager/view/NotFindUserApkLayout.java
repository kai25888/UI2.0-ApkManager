
package com.cv.apk_manager.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

import com.cv.apk_manager.R;

/**
 * <b>NotFindUserApkLayout</b> TODO Could not find the user application fills
 * the page
 * <p>
 * 
 * <pre>
 *  <b>入口</b>(入口参数):
 *  参数一:  <b>Context</b> context :For a activity passed in the context of the object, can the activity itself
 * such as MainActivity in this
 * </pre>
 * 
 * </p>
 * <p>
 * Date: 2015-4-27 下午2:53:40
 * </p>
 * <p>
 * Package: com.cv.apk_manager.view
 * <p>
 * Copyright: (C), 2015-4-27, CultraView
 * </p>
 * 
 * @author Design: Marco.Song (songhong@cultraview.com)
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 1.0.0
 */
public class NotFindUserApkLayout extends FrameLayout {

    /**
     * @param context Context
     */
    public NotFindUserApkLayout(Context context) {
        super(context);
        initView();
    }

    public void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.not_find_userapk, this);
    }

}
