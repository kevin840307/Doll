package com.mndt.ghost.doll.Message;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.R;
import java.lang.reflect.Field;

public class FloatWindowView extends LinearLayout {

    public static int g_iViewWidth, g_iViewHeight, g_iStatusBarHeight;
    private float g_fXInScreen, g_fYInScreen, g_fXDownInScreen, g_fYDownInScreen, g_fXInView, g_fYInView;
    private android.view.WindowManager g_windowManager;
    private android.view.WindowManager.LayoutParams g_layoutParams;
    static Context g_conText;

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public FloatWindowView(Context context) {
        super(context);
        g_windowManager = (android.view.WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        LayoutInflater.from(context).inflate(R.layout.float_window_small_layout, this);
        g_conText = context;
        final View view = findViewById(R.id.small_window_layout);
        final LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(Data.WIDTH, Data.HEIGHT);
        view.setLayoutParams(layoutParams);
        g_iViewWidth = Data.WIDTH;
        g_iViewHeight = Data.HEIGHT;
        view.setBackgroundResource(R.drawable.logo);
    }


    @Override
    public boolean onTouchEvent(final MotionEvent event) {
        g_fXInScreen = event.getRawX();
        g_fYInScreen = event.getRawY() - fnGetg_iStatusBarHeight();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                g_fXInView = event.getX();
                g_fYInView = event.getY();
                g_fXDownInScreen = event.getRawX();
                g_fYDownInScreen = event.getRawY() - fnGetg_iStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                fnUpdateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                if (g_fXDownInScreen + 10 >= g_fXInScreen && g_fYDownInScreen + 10 >= g_fYInScreen
                        && g_fXDownInScreen - 10 <= g_fXInScreen && g_fYDownInScreen - 10 <= g_fYInScreen) {
                    fnOpenMessageActivity();
                } else {
                    fnUpdateViewPosition();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private final void fnOpenMessageActivity() {
        final Intent itStart = new Intent(g_conText, MessageActivity.class);
        itStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        g_conText.startActivity(itStart);
        FloatWindowManager.fnRemoveSmallWindow(g_conText);
    }

    public void setParams(final android.view.WindowManager.LayoutParams layoutParams) {
        g_layoutParams = layoutParams;
    }

    private void fnUpdateViewPosition() {
        if (g_layoutParams != null) {
            g_layoutParams.x = (int) (g_fXInScreen - g_fXInView);
            g_layoutParams.y = (int) (g_fYInScreen - g_fYInView);
            g_windowManager.updateViewLayout(this, g_layoutParams);
        }
    }

    private int fnGetg_iStatusBarHeight() {
        if (g_iStatusBarHeight == 0) {
            try {
                Class<?> c = Class.forName("com.android.internal.R$dimen");
                Object o = c.newInstance();
                Field field = c.getField("status_bar_height");
                int x = (Integer) field.get(o);
                g_iStatusBarHeight = getResources().getDimensionPixelSize(x);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return g_iStatusBarHeight;
    }

    public static Context fnGetFloatContext() {
        return g_conText;
    }
}