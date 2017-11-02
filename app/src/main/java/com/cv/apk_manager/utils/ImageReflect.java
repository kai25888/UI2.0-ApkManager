
package com.cv.apk_manager.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader.TileMode;
import android.graphics.Typeface;
import android.util.TypedValue;
import android.view.View;

/**
 * @author Write Macro.Song(songhong@cultraview.com)
 * @since 2.0.0
 */
public class ImageReflect {

    /** Set the height of the projection */
    private static int reflectImageHeight = 150;

    /**
     * Overloading Settings make it compatible with different density of the
     * screen
     * 
     * @param background
     * @param str
     * @param cvContext
     * @return Bitmap
     */
    static public Bitmap toConformStr(Bitmap background, String str, Context cvContext) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        cv.drawBitmap(background, 0, 0, null);
        Paint p = new Paint();
        p.setColor(Color.WHITE);

        // Set the font size to 24 sp
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, cvContext
                .getResources().getDisplayMetrics());
        p.setTextSize(size);

        // Set the font is bold
        p.setTypeface(Typeface.DEFAULT_BOLD);
        // Set up in the middle
        p.setTextAlign(Align.CENTER);
        cv.drawText(str, bgWidth / 2, bgHeight - 15, p);
        return newbmp;
    }

    static public Bitmap toConformStr(Bitmap background, String str) {
        if (background == null) {
            return null;
        }
        int bgWidth = background.getWidth();
        int bgHeight = background.getHeight();
        Bitmap newbmp = Bitmap.createBitmap(bgWidth, bgHeight, Config.ARGB_8888);
        Canvas cv = new Canvas(newbmp);
        cv.drawBitmap(background, 0, 0, null);
        Paint p = new Paint();
        p.setColor(Color.WHITE);
        p.setTextSize(36);
        p.setTextAlign(Align.CENTER);
        cv.drawText(str, (bgWidth / 2) - 2, bgHeight - 39, p);
        return newbmp;
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, 0), View.MeasureSpec.makeMeasureSpec(0, 0));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        return view.getDrawingCache();
    }

    public static Bitmap createCutReflectedImage(Bitmap bitmap, int paramInt) {
        int i = bitmap.getWidth();
        int j = bitmap.getHeight();
        Matrix localMatrix = new Matrix();
        localMatrix.preScale(1.0F, -1.0F);
        Bitmap localBitmap1 = Bitmap.createBitmap(bitmap, 0, j - reflectImageHeight - paramInt, i,
                reflectImageHeight, localMatrix, true);
        Bitmap localBitmap2 = Bitmap.createBitmap(i, reflectImageHeight, Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap2);
        localCanvas.drawBitmap(localBitmap1, 0.0F, 0.0F, null);
        LinearGradient localLinearGradient = new LinearGradient(0.0F, 0.0F, 0.0F,
                localBitmap2.getHeight(), -2130706433, 16777215, TileMode.CLAMP);
        Paint localPaint = new Paint();
        localPaint.setShader(localLinearGradient);
        localPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        localCanvas.drawRect(0.0F, 0.0F, i, localBitmap2.getHeight(), localPaint);
        if (!localBitmap1.isRecycled())
            localBitmap1.recycle();
        System.gc();
        return localBitmap2;
    }

    public static Bitmap createReflectedImage(Bitmap bitmap) {
        int i = bitmap.getWidth();
        int j = bitmap.getHeight();
        Matrix localMatrix = new Matrix();
        localMatrix.preScale(1.0F, -1.0F);
        // (Bitmap source, int x, int y, int width, int height, Matrix m,
        // boolean filter)
        Bitmap localBitmap1 = Bitmap.createBitmap(bitmap, 0, j - 70, i, 70, localMatrix, true);
        Bitmap localBitmap2 = Bitmap.createBitmap(i, 70, Bitmap.Config.ARGB_8888);
        Canvas localCanvas = new Canvas(localBitmap2);
        localCanvas.drawBitmap(localBitmap1, 0.0F, 0.0F, null);
        LinearGradient localLinearGradient = new LinearGradient(0.0F, 0.0F, 0.0F,
                localBitmap2.getHeight(), -2130706433, 16777215, TileMode.CLAMP);
        Paint localPaint = new Paint();
        localPaint.setShader(localLinearGradient);
        localPaint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        localCanvas.drawRect(0.0F, 0.0F, i, localBitmap2.getHeight(), localPaint);
        return localBitmap2;
    }
}
