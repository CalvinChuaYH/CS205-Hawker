package com.example.androidapp;

import android.content.Context;
import android.view.Display;
import android.view.WindowManager;
import android.view.WindowMetrics;

public class ScreenUtils {
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = context.getDisplay();
        WindowMetrics wm = windowManager.getCurrentWindowMetrics();
        return (int) (wm.getBounds().width());
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = context.getDisplay();
        WindowMetrics wm = windowManager.getCurrentWindowMetrics();
        return (int) (wm.getBounds().height());
    }
}
