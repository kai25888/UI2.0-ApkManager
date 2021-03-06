
package com.cv.apk_manager.view;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cv.apk_manager.R;
import com.cv.apk_manager.utils.AutoUninstall;


/**
 * User卸载界面弹出对话框 cultraview 2015.12.16
 */
public class UninstallUserDialog extends Dialog implements android.view.View.OnClickListener {

    private PackageManager pManager;

    private PackageInfo apk_packageInfo;

    private List<PackageInfo> my_user_List;

    private Context mContext;

    private int position;

    public UninstallUserDialog(Context c, int position, PackageInfo apk_packageInfo,
            List<PackageInfo> my_user_List, int theme) {

        super(c, theme);
        this.mContext = c;
        this.position = position;
        this.apk_packageInfo = apk_packageInfo;
        this.my_user_List = my_user_List;
    }

    /*
     * (non-Javadoc)
     * @see android.app.Dialog#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv_dialog_user_uninstall);
        setWindowStyle();
        initViews();
    }

    // 设置窗口属性
    private void setWindowStyle() {
        Window w = getWindow();
        w.setGravity(Gravity.TOP);
        w.setBackgroundDrawableResource(android.R.color.transparent);

    }

    public void initViews() {

        pManager = mContext.getPackageManager();
        apk_packageInfo = (PackageInfo) my_user_List.get(position);

        //
        TextView textView_dialog1 = (TextView) findViewById(R.id.textView_dialog1);
        TextView textView_dialog2 = (TextView) findViewById(R.id.textView_dialog2);
        TextView textView_dialog3 = (TextView) findViewById(R.id.textView_dialog3);
        TextView textView_dialog4 = (TextView) findViewById(R.id.textView_dialog4);
        TextView textView_dialog5 = (TextView) findViewById(R.id.textView_dialog5);
        ImageView imageView_dialog6 = (ImageView) findViewById(R.id.imageView_dialog6);

        textView_dialog1
                .setText(pManager.getApplicationLabel(apk_packageInfo.applicationInfo) + "");
        textView_dialog2.setText(mContext.getString(R.string.UninstallUserPageLayout_installed));
        textView_dialog3.setText(apk_packageInfo.versionName + "");
        textView_dialog4.setText(apk_packageInfo.versionCode + "");
        textView_dialog5.setText(apk_packageInfo.packageName + "");
        imageView_dialog6.setImageDrawable(pManager
                .getApplicationIcon(apk_packageInfo.applicationInfo));

        // unInstallPackageName = apk_packageInfo.packageName + "";

        TextView textView_filesize = (TextView) findViewById(R.id.textView_filesize);
        textView_filesize.setText(mContext.getString(R.string.UninstallUserPageLayout_user_apks));
        final Button btn_apk_ok = (Button) findViewById(R.id.btn_apk_ok);
        final Button btn_apk_cancell = (Button) findViewById(R.id.btn_apk_cancell);

        // Button 字体高亮
        btn_apk_ok.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    btn_apk_ok.setTextColor(0xffffffff);
                } else {
                    btn_apk_ok.setTextColor(0x66ffffff);
                }

            }
        });
        btn_apk_cancell.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View arg0, boolean arg1) {
                // TODO Auto-generated method stub
                if (arg1) {
                    btn_apk_cancell.setTextColor(0xffffffff);

                } else {
                    btn_apk_cancell.setTextColor(0x66ffffff);
                }
            }
        });

        btn_apk_ok.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get the package name, and set up the package name
                AutoUninstall.setUrl(apk_packageInfo.packageName);
                AutoUninstall.uninstall(mContext);
                dismiss();

            }
        });
        btn_apk_cancell.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
    }
}
