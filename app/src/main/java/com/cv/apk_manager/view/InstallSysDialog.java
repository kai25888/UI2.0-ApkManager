
package com.cv.apk_manager.view;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
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


public class InstallSysDialog extends Dialog implements android.view.View.OnClickListener {

    private List<ApkInfo> my_sys_List;

    private MyFile myFile;

    private Context mContext;

    private int position;

    public InstallSysDialog(Context c, int position, List<ApkInfo> my_sys_List, MyFile myFile,
            int theme) {
        super(c, theme);
        this.mContext = c;
        this.position = position;
        this.my_sys_List = my_sys_List;
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
        // TODO Auto-generated constructor stub

        final String apkPath = my_sys_List.get(position).getApk_path();
        try {
            myFile = SearchMyFileInfoTool.searchMyFileInfo(apkPath, mContext);
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView textView_dialog_install_1 = (TextView) findViewById(R.id.textView_dialog_install_1);
        TextView textView_dialog_install_2 = (TextView) findViewById(R.id.textView_dialog_install_2);
        // FrameLayout install_state_bg = (FrameLayout)
        // findViewById(R.id.install_state_bg);
        ImageView install_state_bg = (ImageView) findViewById(R.id.install_state_bg);

        TextView textView_dialog_install_3 = (TextView) findViewById(R.id.textView_dialog_install_3);
        TextView textView_dialog_install_4 = (TextView) findViewById(R.id.textView_dialog_install_4);
        TextView textView_dialog_install_5 = (TextView) findViewById(R.id.textView_dialog_install_5);
        TextView textView_dialog_install_6 = (TextView) findViewById(R.id.textView_dialog_install_6);
        ImageView imageView_dialog7 = (ImageView) findViewById(R.id.imageView_dialog7);
        if (textView_dialog_install_2.getText().equals(
                mContext.getString(R.string.UninstallUserPageLayout_installed))) {
            install_state_bg.setBackgroundResource(R.drawable.state_bg_didnot);
        } else {
            install_state_bg.setBackgroundResource(R.drawable.state_bg_have);
        }
        final Button btn_apk_ok = (Button) findViewById(R.id.btn_apk_ok);
        final Button btn_apk_cancell = (Button) findViewById(R.id.btn_apk_cancell);

        // Set the label
        textView_dialog_install_1.setText(myFile.getLabel());

        // sdCurrentLableName = myFile.getLabel();

        // Set up the installation state
        if (myFile.getInstallStatus() == 0) {
            textView_dialog_install_2.setText(mContext
                    .getString(R.string.UninstallUserPageLayout_installed));
        } else if (myFile.getInstallStatus() == 1) {
            textView_dialog_install_2.setText(mContext
                    .getString(R.string.InstallSysPageLayout_can_updata));
        } else if (myFile.getInstallStatus() == 2) {
            textView_dialog_install_2.setText(mContext
                    .getString(R.string.InstallSysPageLayout_uninstall));

        }

        textView_dialog_install_3.setText(myFile.getFileSize());
        textView_dialog_install_4.setText(myFile.getVersionName());
        textView_dialog_install_5.setText(myFile.getVersionCode() + "");
        textView_dialog_install_6.setText(myFile.getFilePath());
        imageView_dialog7.setImageDrawable(myFile.getApk_icon());

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
                // The installation
                AutoInstall.setUrl(apkPath);
                AutoInstall.install(mContext);
                dismiss();// Hide the dialog box
            }
        });
        btn_apk_cancell.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    @Override
    public void onClick(View arg0) {
        // TODO Auto-generated method stub

    }

}
