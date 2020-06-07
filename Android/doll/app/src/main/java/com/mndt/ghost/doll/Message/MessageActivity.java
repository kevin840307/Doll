package com.mndt.ghost.doll.Message;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mndt.ghost.doll.Adapter.MessageAdapter1;
import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Handler.HandlerMessage;

import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.UserSharedPreferences;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ghost on 2017/10/31.
 */
public class MessageActivity extends AppCompatActivity {
    private final String TAG = "MessageActivity";
    private MessageAdapter1 g_msgAdapterl = null;
    private ListView g_lvMessageView = null;
    private static int g_iNowIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.message_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitData();
        fnInitControl();
    }

    private final void fnInitData() {
        FloatWindowManager.fnRemoveSmallWindow(this);
        fnInitService();
    }

    private final void fnInitControl() {
        fnInitListView();
        fnInitButton();
        fnInitTitle();
    }

    private final void fnInitTitle() {
        final Button btnLeave = (Button) findViewById(R.id.btn_messge_leave);
        btnLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnCloseFloatServer();
                fnCloseMessagerServer();
                finish();
            }
        });
    }

    private final void fnCloseFloatServer() {
        FloatWindowManager.fnRemoveSmallWindow(getApplicationContext());
        final Intent itStart = new Intent(this, WindowServer.class);
        stopService(itStart);
    }

    private final void fnCloseMessagerServer() {
        ChatService.fnClose();
        final Intent itStart = new Intent(this, ChatService.class);
        stopService(itStart);
    }


    private final void fnInitListView() {
        if (ChatService.g_alDatas == null) {
            ChatService.g_alDatas = new ArrayList<>();
        }
        g_msgAdapterl = new MessageAdapter1(this, ChatService.g_alDatas);
        g_lvMessageView = (ListView) findViewById(R.id.lv_message_data);
        g_lvMessageView.setAdapter(g_msgAdapterl);
        g_lvMessageView.setSelection(g_iNowIndex);
        g_msgAdapterl.notifyDataSetChanged();
    }

    private final void fnInitButton() {
        final Button btnSend = (Button) findViewById(R.id.btn_msg_send);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showListViewRight();
            }
        });

    }

    private final void fnInitService() {
        ChatService.fnSetContent(this);
        LocalBroadcastManager.getInstance(this).registerReceiver(g_brBroadcastMessage, fnGetUpdateFilter());
    }

    private final static IntentFilter fnGetUpdateFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastMessages.GET_ALL_MESSAGE);
        intentFilter.addAction(BroadcastMessages.SEND_ALL_MESSAGE);
        return intentFilter;
    }

//    private ServiceConnection g_serviceConnection = new ServiceConnection() {
//        public void onServiceConnected(ComponentName className, IBinder rawBinder) {
//            g_chatService = ((ChatService.ChatServiceBinder) rawBinder).fnGetService();
//            Log.d(TAG, "啟動ChatService服務= " + g_chatService);
//            g_chatService.fnConnnection();
//        }
//
//        public void onServiceDisconnected(ComponentName classname) {
//            g_chatService = null;
//        }
//    };


    private final void showListViewRight() {
        final EditText editMsg = (EditText) findViewById(R.id.edit_msg_massege);
        final String sMsg = editMsg.getText().toString();
        if (sMsg.length() > 0) {
            final MessageData dataLeft = new MessageData(UserSharedPreferences.g_sAccount, UserSharedPreferences.g_sName, sMsg, MessageData.RIGHT_MSG);
            if (ChatService.fnWrite(ChatService.SET_ALL_MESSAGE, dataLeft.fnGetSocketMsg())) {
                ChatService.g_alDatas.add(dataLeft);
                g_msgAdapterl.notifyDataSetChanged();
                g_lvMessageView.setSelection(g_msgAdapterl.getCount() - 1);
                editMsg.setText("");
            } else {
                Toast.makeText(this, "無法與伺服器連接", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final void showListViewShow() {
        g_msgAdapterl.notifyDataSetChanged();
        g_lvMessageView.setSelection(g_msgAdapterl.getCount() - 1);
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());

        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) { // 攔截返回鍵
            FloatWindowManager.fnCreateSmallWindow(this);
            g_iNowIndex = g_msgAdapterl.getCount() > 0 ? g_msgAdapterl.getCount() - 1 : 0;
            finish();
        }
        Log.d(TAG, "按鈕" + keyCode);
        return super.onKeyDown(keyCode, event);
    }


    private final BroadcastReceiver g_brBroadcastMessage = new BroadcastReceiver() {

        public void onReceive(Context context, final Intent intent) {
            final String sAction = intent.getAction();

            if (sAction.equals(BroadcastMessages.GET_ALL_MESSAGE)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        final String sHasData = intent.getStringExtra(ChatService.MSG);
                        if(sHasData.equals("Y")) {
                            showListViewShow();
                        }
                    }
                });
            } else if (sAction.equals(BroadcastMessages.SEND_ALL_MESSAGE)) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        showListViewRight();
                    }
                });
            }
        }
    };

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.GET_SQL_DATE1:
                    break;
            }
        }
    };
}
