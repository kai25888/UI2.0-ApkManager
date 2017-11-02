
package com.cv.apk_manager.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

/**
 * <b>MarqueeView</b> TODO Custom controls, and realized the effect of
 * entertaining diversions
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
 * Date: 2015-4-27 下午2:51:20
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
public class MarqueeView extends TextView {

    public MarqueeView(Context context) {
        this(context, null);
        // TODO Auto-generated constructor stub
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        this(context, attrs, android.R.attr.textViewStyle);
    }

    public MarqueeView(Context context, AttributeSet attrs, int defStyle) {

        super(context, attrs, defStyle);
    }

    @Override
    protected void onFocusChanged(boolean arg0, int arg1, Rect arg2) {
        // TODO Auto-generated method stub
        if (arg0)
            super.onFocusChanged(arg0, arg1, arg2);
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        // TODO Auto-generated method stub
        if (hasWindowFocus)
            super.onWindowFocusChanged(hasWindowFocus);
    }

    @Override
    @ExportedProperty(category = "focus")
    public boolean isFocused() {
        // TODO Auto-generated method stub
        return true;
    }
}
