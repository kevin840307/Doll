package com.mndt.ghost.doll;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.transition.Explode;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mndt.ghost.doll.EncryptionString.EncryptionString;
import com.mndt.ghost.doll.EncryptionString.MD5;
import com.mndt.ghost.doll.Handler.HandlerAction;
import com.mndt.ghost.doll.Handler.HandlerData;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.SQLHandler;
import com.mndt.ghost.doll.Handler.WebHandler;
import com.mndt.ghost.doll.SoapCall.SoapFunctions;
import com.mndt.ghost.doll.SqlDB.DOLL_evaluation;
import com.mndt.ghost.doll.SqlDB.DOLLshop;

import org.ksoap2.serialization.SoapObject;

/**
 * Created by Ghost on 2017/10/28.
 */
public class LoginActivity extends AppCompatActivity {

    private String g_sIMEI = "";
    private String g_sName = "";
    private String g_sAccount = "";
    private String g_sPwd = "";
    private String g_sSex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.login_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitControl();
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnInitButton();
    }

    private final boolean fnCheckAPI() {
        if (Build.VERSION.SDK_INT >= 23) {
            final String sPermissions[] = {Manifest.permission.READ_PHONE_STATE};
            final int iPhone = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (iPhone != PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "請開啟權限, 無權限無法登入", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(LoginActivity.this, sPermissions, 1);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnInitButton() {
        final Button btnLogin = (Button) findViewById(R.id.btn_login_login);
        final Button btnCancel = (Button) findViewById(R.id.btn_login_cancel);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (fnCheckAPI() && fnCheckData()) {
                    fnRunLogin();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                fnOpenWayLogin();
            }
        });
    }

    private final boolean fnCheckData() {

        final EditText editAccount = (EditText) findViewById(R.id.edit_login_accont);
        final EditText editPwd = (EditText) findViewById(R.id.edit_login_pwd);

        if (editPwd.getText().toString().length() < 6
                || editPwd.getText().toString().length() > 20) {
            Toast.makeText(this, "密碼長度錯誤", Toast.LENGTH_SHORT).show();
            return false;
        } else if (editAccount.getText().toString().length() < 6
                || editPwd.getText().toString().length() > 20) {
            Toast.makeText(this, "帳號長度錯誤", Toast.LENGTH_SHORT).show();
            return false;
        }

        final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        @SuppressLint("MissingPermission") final String sIMEI = telephonyManager.getDeviceId();
        g_sIMEI = sIMEI;
        g_sAccount = editAccount.getText().toString();
        g_sPwd = MD5.MD5(editPwd.getText().toString());
        return true;
    }

    /*------------------------------------------------開始登入---------------------------------------------------------------------*/

    private final void fnRunLogin() {
        final String sKeys[] = {"sIMEI", "sAccount", "sPwd"};
        final String sData[] = {g_sIMEI, g_sAccount, g_sPwd};
        new HandlerAction(g_hdMessage).fnRunThread(WebHandler.LOGIN_MESSAGE_FUNTION
                , HandlerMessage.LOGIN_MESSAGE
                , HandlerData.V_LIST_MESSAGE
                , sKeys, sData, 2);
    }

    private final void fnCheckLogin(final Object objData) {
        final String[][] sDatas = ((HandlerData) objData).fnGetList();
        if (sDatas != null) {
            if (sDatas.length > 0) {
                g_sName = sDatas[0][0];
                g_sSex = sDatas[0][1];
                new UserSharedPreferences(g_sName, g_sSex, g_sPwd, g_sIMEI, g_sAccount).fnWritData(getApplication());
                Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show();
                fnRunGetEvaluation();
            } else {
                Toast.makeText(this, "登入失敗", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "登入失敗", Toast.LENGTH_SHORT).show();
        }
    }

    /*------------------------------------------------結束登入---------------------------------------------------------------------*/


    /*------------------------------------------------開始取得自己評價資料---------------------------------------------------------------------*/

    private final void fnRunGetEvaluation() {
        final String sKeys[] = {"sIMEI", "sAccount", "sPwd"};
        final String sData[] = {g_sIMEI, g_sAccount, g_sPwd};
        new HandlerAction(g_hdMessage).fnRunThread(WebHandler.GET_MY_EVALUATION_FUNTION
                , HandlerMessage.GET_MY_EVALUATION
                , HandlerData.V_LIST_MESSAGE
                , sKeys, sData, 6);
    }

    private final void fnInsertEvaluatio(final Object objData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[][] sDatas = ((HandlerData) objData).fnGetList();
                if (sDatas != null && sDatas.length > 0) {
                    final SQLHandler sqlHandler = new SQLHandler(getApplicationContext());
                    for (int iPos = 0; iPos < sDatas.length; iPos++) {
                        final DOLL_evaluation dollData = new DOLL_evaluation(sDatas[iPos]);
                        DOLL_evaluation.fnInsert(sqlHandler, dollData);
                    }
                }
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.OPEN_ACTIVITY);
            }
        }).start();
    }

    /*------------------------------------------------結束取得自己評價資料---------------------------------------------------------------------*/


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenWayLogin() {
        final Intent itStart = new Intent(LoginActivity.this, LoginWayActivity.class);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
        finish();
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenMainActivity() {
        final Intent itStart = new Intent(LoginActivity.this, MainActivity.class);
        itStart.putExtra("flag", "slide");
        itStart.putExtra("type", "1");
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            fnOpenWayLogin();
        }
        return true;
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.LOGIN_MESSAGE:
                    fnCheckLogin(msg.obj);
                    break;
                case HandlerMessage.GET_MY_EVALUATION:
                    fnInsertEvaluatio(msg.obj);
                    break;
                case HandlerMessage.OPEN_ACTIVITY:
                    fnOpenMainActivity();
                    break;
            }
        }
    };
}
