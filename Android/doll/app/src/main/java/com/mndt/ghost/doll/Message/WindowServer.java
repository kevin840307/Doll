package com.mndt.ghost.doll.Message;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.mndt.ghost.doll.R;

public class WindowServer extends Service {

    private static final String TAG = "WindowServer";
    private static Notification g_notiFication = null;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
        final Intent itStart = new Intent(this, MessageActivity.class);
        final PendingIntent contentIntent = PendingIntent.getActivity(this, 0, itStart, 0);
        FloatWindowManager.fnCreateSmallWindow(getApplicationContext());
        final Notification notiFication = new Notification.Builder(this)
                .setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.gps_me)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(false)
                .setContentTitle("抓寶龍聊天")
                .setContentText("抓寶龍聊天 運行中")
                .getNotification();
        startForeground(1, notiFication);
        Log.i(TAG, "oncreat");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent.getBooleanExtra("isStop", false)) {
            Log.e(TAG, "fnCloseNotification()");
            fnCloseNotification();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        fnCloseNotification();
    }

    private final void fnCloseNotification() { //恢复优先级
        stopForeground(true);
    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
