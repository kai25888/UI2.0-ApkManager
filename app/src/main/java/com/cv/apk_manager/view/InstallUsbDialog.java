
package com.cv.apk_manager.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.cv.apk_manager.R;
import com.cv.apk_manager.utils.ApkInfo;
import com.cv.apk_manager.utils.AutoInstall;
import com.cv.apk_manager.utils.MyFile;
import com.cv.apk_manager.utils.SearchMyFileInfoTool;

import java.util.List;

public class InstallUsbDialog extends Dialog implements OnClickListener {

    private List<ApkInfo> my_usb_List;

    private MyFile myFile;

    private Context mContext;

    private int position;

    public InstallUsbDialog(Context c, int position, List<ApkInfo> my_usb_List, MyFile myFile,
            int theme) {
        super(c, theme);
        this.mContext = c;
        this.position = position;
        this.my_usb_List = my_usb_List;
        this.myFile = myFile;
    }

    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cv_dialog_install);
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
        final String apkPath = my_usb_List.get(position).getApk_path();
        try {
            myFile = SearchMyFileInfoTool.searchMyFileInfo(apkPath, mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
		
		final String packageName = myFile.getPackageName();
        // Initialize the common dialog box.And set up the style
        // cv_dialog = new Dialog(mContext, R.style.dialog);
        // cv_dialog.setCancelable(true);
        // cv_dialog.setContentView(R.layout.cv_dialog_install);

        TextView textView_dialog_install_1 = (TextView) findViewById(R.id.textView_dialog_install_1);
        TextView textView_dialog_install_2 = (TextView) findViewById(R.id.textView_dialog_install_2);
        // L
        ImageView install_state_bg = (ImageView) findViewById(R.id.install_state_bg);
        // l
        TextView textView_dialog_install_3 = (TextView) findViewById(R.id.textView_dialog_install_3);
        TextView textView_dialog_install_4 = (TextView) findViewById(R.id.textView_dialog_install_4);
        TextView textView_dialog_install_5 = (TextView) findViewById(R.id.textView_dialog_install_5);
        TextView textView_dialog_install_6 = (TextView) findViewById(R.id.textView_dialog_install_6);
        ImageView imageView_dialog7 = (ImageView) findViewById(R.id.imageView_dialog7);
        final Button btn_apk_ok = (Button) findViewById(R.id.btn_apk_ok);
        final Button btn_apk_cancell = (Button) findViewById(R.id.btn_apk_cancell);

        textView_dialog_install_1.setText(myFile.getLabel());

        // usbCurrentLableName = myFile.getLabel();

        // Set up the installation state
        if (myFile.getInstallStatus() == 0) {
            textView_dialog_install_2.setText(mContext
                    .getString(R.string.UninstallUserPageLayout_installed));
        } else if (myFile.getInstallStatus() == 1) {
            // Can be updated
            textView_dialog_install_2.setText(mContext
                    .getString(R.string.InstallSysPageLayout_can_updata));
        } else if (myFile.getInstallStatus() == 2) {
            // Has been installed
            textView_dialog_install_2.setText(mContext
                    .getString(R.string.InstallSysPageLayout_uninstall));

        }

        textView_dialog_install_3.setText(myFile.getFileSize());
        textView_dialog_install_4.setText(myFile.getVersionName());
        textView_dialog_install_5.setText(myFile.getVersionCode() + "");
        textView_dialog_install_6.setText(myFile.getApkRelativePath());
        imageView_dialog7.setImageDrawable(myFile.getApk_icon());
        if (textView_dialog_install_2.getText().equals(
                mContext.getString(R.string.UninstallUserPageLayout_installed))) {
            install_state_bg.setBackgroundResource(R.drawable.state_bg_have);
        } else {
            install_state_bg.setBackgroundResource(R.drawable.state_bg_didnot);
        }
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
				AutoInstall.setPackageName(packageName);
                AutoInstall.setUrl(apkPath);
                AutoInstall.install(mContext);
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

    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

}
