package com.mndt.ghost.doll.Message;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;

public class FloatWindowManager {


    private static FloatWindowView g_floatWindowView;
    private static android.view.WindowManager g_windowManager;
    private static android.view.WindowManager.LayoutParams g_layoutSmallWindowParams;


    public static void fnCreateSmallWindow(Context context) {
        android.view.WindowManager windowManager = getWindowManager(context);
        if (g_floatWindowView == null) {
            int screenWidth = windowManager.getDefaultDisplay().getWidth();
            int screenHeight = windowManager.getDefaultDisplay().getHeight();
            g_floatWindowView = new FloatWindowView(context);
            if(g_layoutSmallWindowParams == null) {
                g_layoutSmallWindowParams = new android.view.WindowManager.LayoutParams();
                g_layoutSmallWindowParams.type = android.view.WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                g_layoutSmallWindowParams.format = PixelFormat.RGBA_8888;
                g_layoutSmallWindowParams.flags = android.view.WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
                        android.view.WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE; //FloatWindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL   | FloatWindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
                g_layoutSmallWindowParams.gravity = Gravity.LEFT | Gravity.TOP;
                g_layoutSmallWindowParams.width = FloatWindowView.g_iViewWidth;
                g_layoutSmallWindowParams.height = FloatWindowView.g_iViewHeight;
                g_layoutSmallWindowParams.x = screenWidth;
                g_layoutSmallWindowParams.y = screenHeight / 2;
                g_floatWindowView.setParams(g_layoutSmallWindowParams);
            }
            windowManager.addView(g_floatWindowView, g_layoutSmallWindowParams);
        }
    }

    public static void fnRemoveSmallWindow(Context context) {
        if (g_floatWindowView != null) {
            android.view.WindowManager windowManager = getWindowManager(context);
            windowManager.removeView(g_floatWindowView);
            g_floatWindowView = null;
            g_layoutSmallWindowParams = null;
        }
    }

    private static android.view.WindowManager getWindowManager(Context context) {
        if (g_windowManager == null) {
            g_windowManager = (android.view.WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        }
        return g_windowManager;
    }
}