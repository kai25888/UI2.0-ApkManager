
package com.cv.apk_manager.utils;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cv.apk_manager.R;

/**
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0
 */
public class PageControl {

    private LinearLayout layout;

    private TextView[] textViews;

    private TextView textView;

    private int pageSize;

    private int currentPage = 0;

    private Context mContext;

    /**
     * @param context context
     * @param layout To hold all the dots of a linear layout
     * @param pageSize The page number (page number = / the total number of each
     *            page)
     */
    public PageControl(Context context, LinearLayout layout, int pageSize) {
        this.mContext = context;
        this.pageSize = pageSize;
        this.layout = layout;
        layout.removeAllViews();
        initDots();
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    /**
     * Initialize the dot
     */
    void initDots() {
        // When the page with a 2 page to add more
        if (pageSize > 1) {
            textViews = new TextView[pageSize];
            for (int i = 0; i < pageSize; i++) {
                textView = new TextView(mContext);

                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(22, 22);
                layoutParams.setMargins(0, 23, 0, 0);
                textView.setLayoutParams(layoutParams);
                textViews[i] = textView;
                if (i == 0) {
                    // Default into the program after the first picture is
                    // selected;
                    textViews[i].setBackgroundResource(R.drawable.ic_home_page_focus);
                } else {
                    textViews[i].setBackgroundResource(R.drawable.ic_home_page_unfocus);
                }
                layout.addView(textViews[i]);
            }
        }
    }

    // The first page
    public boolean isFirst() {
        return this.currentPage == 0;
    }

    // The last page
    public boolean isLast() {
        return this.currentPage == pageSize;
    }

    // When the page
    public void selectPage(int current) {
        for (int i = 0; i < textViews.length; i++) {
            textViews[current].setBackgroundResource(R.drawable.ic_home_page_focus);
            if (current != i) {
                textViews[i].setBackgroundResource(R.drawable.ic_home_page_unfocus);
            }
        }
    }

    // The next page
    void turnToNextPage() {
        if (!isLast()) {
            currentPage++;
            selectPage(currentPage);
        }
    }

    // The previous page
    void turnToPrePage() {
        if (!isFirst()) {
            currentPage--;
            selectPage(currentPage);
        }
    }
}
