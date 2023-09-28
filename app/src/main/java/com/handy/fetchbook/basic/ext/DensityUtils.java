package com.handy.fetchbook.basic.ext;

import android.content.Context;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.WindowManager;

/**
 * - Author: Charles
 * - Date: 2023/8/18
 * - Description:
 */
public class DensityUtils {
    private static Context mContext;
    public static int screenW;
    public static int screenH;
    private static boolean hasInit = false;
    private static float density;//屏幕密度，dip/160
    private static Display display;
    private static int densityDpi;//dpi，像素密度
    private static DisplayMetrics displayMetrics;

    public static int dip2px(float dipValue) {
        if (!checkInit()) {
            return 0;
        }
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue, displayMetrics);
    }

    /**
     * dip转像素
     *
     * @param dipValue 指定dip值
     * @return 转换后的像素，会向上取整
     */
    public static int dp2px(float dipValue) {
        if (!checkInit()) {
            return 0;
        }
        float value = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                dipValue, displayMetrics);
        return (int) ((value >= 0) ? (value + 0.5f) : (value - 0.5f));
    }

    public static void init(Context context) {
        if (context == null) {
            return;
        }
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);

        if (dm.heightPixels > dm.widthPixels) {
            screenW = dm.widthPixels;
            screenH = dm.heightPixels;
        } else {
            screenW = dm.heightPixels;
            screenH = dm.widthPixels;
        }
        mContext = context;
        density = context.getResources().getDisplayMetrics().density;
        densityDpi = dm.densityDpi;
        displayMetrics = context.getResources().getDisplayMetrics();
        hasInit = true;
    }

    private static boolean checkInit() {
        return hasInit && mContext != null;
    }
}
