
package com.cv.apk_manager.utils;

import java.util.ArrayList;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0
 */
public class AllPagesAdapter extends PagerAdapter {

    protected static final String TAG = "com.cv.apk_manager.view.AllPagesAdapter";

    private ArrayList<View> mListViews;

    public AllPagesAdapter(ArrayList<View> mListViews) {
        this.mListViews = mListViews;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        try {
            container.removeView(mListViews.get(position));
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    @Override
    public float getPageWidth(int position) {
        // TODO Auto-generated method stub
        return super.getPageWidth(position);
    }

    @Override
    public int getCount() {
        return mListViews.size();
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if(mListViews.get(position).getParent() == null)
        container.addView(mListViews.get(position), 0);
        return mListViews.get(position);
    }
}
