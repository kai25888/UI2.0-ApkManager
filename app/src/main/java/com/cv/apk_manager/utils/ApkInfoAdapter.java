
package com.cv.apk_manager.utils;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.cv.apk_manager.R;
import com.cv.apk_manager.view.AppStopPageLayout;

/**
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0
 */

public class ApkInfoAdapter extends BaseAdapter implements OnItemClickListener {
    private Context context;

    private List<ApkInfo> list;

    private ImageView[] refimgs;

    private int pageIndex;

    // 0:stop , 1:clean
    private int type;

    public ApkInfoAdapter(Context context, List<ApkInfo> stop_list, ImageView[] refimgs,
            int pageIndex, int type) {
        this.context = context;
        this.list = stop_list;
        this.refimgs = refimgs;
        this.pageIndex = pageIndex;
        this.type = type;
    }

    public void setList(List<ApkInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void notifyChanged() {
        notifyDataSetChanged();
    }

    public List<ApkInfo> getList() {
        return list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("NewApi")
    @Override
    public View getView(int position, View contentview, ViewGroup parent) {
        if (contentview == null) {
            contentview = LayoutInflater.from(context).inflate(R.layout.cleancache_app_item,
                    parent, false);
        }
        Log.i("top_tag", "---getView:" + position);
        ApkInfo apkInfo = list.get(position);
        ImageView apkIcon = (ImageView) contentview.findViewById(R.id.iv_clean_apk_icon);
        ImageView iv_all_apk_select = (ImageView) contentview.findViewById(R.id.iv_selector_apk);
        TextView apkName = (TextView) contentview.findViewById(R.id.tv_clean_apk_label);
        FrameLayout fl_clean_apk = (FrameLayout) contentview.findViewById(R.id.fl_clean_apk);
        apkIcon.setImageDrawable(apkInfo.getApk_icon());
        apkName.setText(apkInfo.getLabel());
        if (apkInfo.isSelect()) {
            iv_all_apk_select.setBackground(context.getResources().getDrawable(
                    R.drawable.iv_selector_apk_focus));
        } else {
            iv_all_apk_select.setBackground(context.getResources().getDrawable(
                    R.drawable.iv_selector_apk));
        }
        if (position % 8 == 0) {
            fl_clean_apk.setBackgroundResource(R.drawable.apk_bg_0);
        } else if (position % 8 == 1) {
            fl_clean_apk.setBackgroundResource(R.drawable.apk_bg_1);
        } else if (position % 8 == 2) {
            fl_clean_apk.setBackgroundResource(R.drawable.apk_bg_2);
        } else if (position % 8 == 3) {
            fl_clean_apk.setBackgroundResource(R.drawable.apk_bg_3);
        } else if (position % 8 == 4) {
            fl_clean_apk.setBackgroundResource(R.drawable.apk_bg_4);
        } else if (position % 8 == 5) {
            fl_clean_apk.setBackgroundResource(R.drawable.apk_bg_5);
        } else if (position % 8 == 6) {
            fl_clean_apk.setBackgroundResource(R.drawable.apk_bg_6);
        } else {
            fl_clean_apk.setBackgroundResource(R.drawable.apk_bg_7);
        }
        if (position > 3 && position < 8) {// 4567
            Bitmap localBitmap = ImageReflect.convertViewToBitmap(fl_clean_apk);
            Bitmap localBitmap1 = ImageReflect.toConformStr(localBitmap, apkName.getText()
                    .toString());
            Bitmap localBitmap2 = ImageReflect.createCutReflectedImage(localBitmap1, 0);
            refimgs[position - 4].setImageBitmap(localBitmap2);
        }
        return contentview;
    }

    @SuppressLint("NewApi")
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Log.i("top_tag", "---onItemClick--position:" + position);
        ApkInfo apkInfo = list.get(position);
        if (apkInfo.isSelect()) {
            apkInfo.setSelect(false);
        } else {
            apkInfo.setSelect(true);
        }
        list.set(position, apkInfo);
        if (type == Constant.TYPE_STOP) {
            AppStopPageLayout.my_stop_list.set(position + pageIndex * Constant.STOP_PAGE_SIZE,
                    apkInfo);
        }
        notifyDataSetChanged();
    }
}
