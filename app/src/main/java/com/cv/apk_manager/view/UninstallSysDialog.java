
package com.cv.apk_manager.view;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.cv.apk_manager.R;


/**
 * @author cultraview
 * @time 2015-12-15
 */
public class UninstallSysDialog extends Dialog implements android.view.View.OnClickListener {

    private PackageManager pManager;

    private PackageInfo apk_packageInfo;

    private List<PackageInfo> my_sys_List;

    private Context mContext;

    private int position;

    public UninstallSysDialog(Context c, int position, PackageInfo apk_packageInfo,
            List<PackageInfo> my_sys_List, int theme) {

        super(c, theme);
        this.mContext = c;
        this.position = position;
        this.apk_packageInfo = apk_packageInfo;
        this.my_sys_List = my_sys_List;
    }

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv_dialog_sys_uninstall);
        setWindowStyle();
        initViews();
    }

    // 设置窗口属性
    private void setWindowStyle() {
        Window w = getWindow();
        w.setGravity(Gravity.TOP);
        w.setBackgroundDrawableResource(android.R.color.transparent);

    }

    // 给各个控件写入内容
    public void initViews() {

        pManager = mContext.getPackageManager();
        apk_packageInfo = (PackageInfo) my_sys_List.get(position);
        TextView textView_dialog1 = (TextView) findViewById(R.id.textView_dialog1);
        TextView textView_dialog2 = (TextView) findViewById(R.id.textView_dialog2);
        TextView textView_dialog3 = (TextView) findViewById(R.id.textView_dialog3);
        TextView textView_dialog4 = (TextView) findViewById(R.id.textView_dialog4);
        TextView textView_dialog5 = (TextView) findViewById(R.id.textView_dialog5);
        ImageView imageView_dialog6 = (ImageView) findViewById(R.id.imageView_dialog6);
        TextView textView_filesize = (TextView) findViewById(R.id.textView_filesize);
        textView_filesize.setText(mContext.getString(R.string.UninstallUserPageLayout_sys_apks));
        textView_dialog1
                .setText(pManager.getApplicationLabel(apk_packageInfo.applicationInfo) + "");
        textView_dialog2.setText(mContext.getString(R.string.UninstallUserPageLayout_installed));
        textView_dialog3.setText(apk_packageInfo.versionName + "");
        textView_dialog4.setText(apk_packageInfo.versionCode + "");
        textView_dialog5.setText(apk_packageInfo.packageName + "");
        imageView_dialog6.setImageDrawable(pManager
                .getApplicationIcon(apk_packageInfo.applicationInfo));

    }

    public void onClick(View v) {

    }
}
