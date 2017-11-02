
package com.cv.apk_manager.view;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cv.apk_manager.R;

/**
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0
 */
public class CtvToast extends Toast {

    public CtvToast(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence text, int res_iv, int duration) {
        Toast result = new Toast(context);
        Log.i("CtvToast", "makeText");
        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_toast);
        ImageView iv = (ImageView) v.findViewById(R.id.iv_toast);
        tv.setText(text);
        iv.setBackgroundResource(res_iv);
        result.setDuration(duration);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setView(v);
        return result;
    }

    public static Toast makeText(Context context, int res_tv, int res_iv, int duration) {
        Toast result = new Toast(context);
        LayoutInflater inflate = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflate.inflate(R.layout.toast, null);
        TextView tv = (TextView) v.findViewById(R.id.tv_toast);
        ImageView iv = (ImageView) v.findViewById(R.id.iv_toast);
        tv.setText(res_tv);
        iv.setBackgroundResource(res_iv);
        result.setDuration(duration);
        result.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        result.setView(v);
        return result;
    }
}
