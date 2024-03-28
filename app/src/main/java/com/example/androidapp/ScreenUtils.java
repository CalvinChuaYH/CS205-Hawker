package com.example.androidapp;

import android.content.Context;
import android.view.WindowManager;
import android.view.WindowMetrics;

public class ScreenUtils {
    //Getting width and height of screen for limiting map
    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics wm = windowManager.getCurrentWindowMetrics();
        return (int) (wm.getBounds().width());
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        WindowMetrics wm = windowManager.getCurrentWindowMetrics();
        return (int) (wm.getBounds().height());
    }
}
