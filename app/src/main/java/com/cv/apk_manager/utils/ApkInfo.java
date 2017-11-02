
package com.cv.apk_manager.utils;

import android.graphics.drawable.Drawable;

/**
 * @Company: com.cultraview
 * @date: 2014
 * @author sh
 * @Description:Apk文件实体
 */
@SuppressWarnings("rawtypes")
public class ApkInfo implements Comparable {
    // 应用label
    private String label;

    // apk文件名 aa.apk
    private String fileName;

    // 获取apk的图标
    private Drawable apk_icon;

    // 绝对路径
    private String apk_path;

    private boolean select;

    private String packageName;

    private int pid;

    private String processName;

    public ApkInfo() {
        super();
    }

    public String getLabel() {
        return label;
    }

    public String getApk_path() {
        return apk_path;
    }

    public void setApk_path(String apk_path) {
        this.apk_path = apk_path;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }

    public String getFileName() {
        return fileName;
    }

    /**
     * @return the select
     * @date 2015-4-2
     * @author songhong
     */
    public boolean isSelect() {
        return select;
    }

    /**
     * @param select the select to set
     * @date 2015-4-2
     * @author songhong
     */
    public void setSelect(boolean select) {
        this.select = select;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Drawable getApk_icon() {
        return apk_icon;
    }

    public int getPid() {
        return pid;
    }

    public String getProcessName() {
        return processName;
    }

    public void setApk_icon(Drawable apk_icon) {
        this.apk_icon = apk_icon;
    }

    /**
     * @return the packageName
     * @date 2015-4-1
     * @author songhong
     */
    public String getPackageName() {
        return packageName;
    }

    /**
     * @param packageName the packageName to set
     * @date 2015-4-1
     * @author songhong
     */
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public int compareTo(Object another) {
        // 判断文件名大小 返回1表示大于，返回-1表示小于，返回0表示相等。
        int result = fileName.compareTo(((ApkInfo) another).fileName);
        return result;
    }
}
