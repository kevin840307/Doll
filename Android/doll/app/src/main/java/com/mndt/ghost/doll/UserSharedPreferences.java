package com.mndt.ghost.doll;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by Ghost on 2017/10/29.
 */
public class UserSharedPreferences {
    private SharedPreferences g_sharedPreferencesSetting;
    private static final String DATA = "DATA";
    private static final String NAME = "NAME";
    private static final String SEX = "SEX";
    private static final String PASSWORD = "PASSWORD";
    private static final String IMEI = "IMEI";
    private static final String ACCOUNT = "ACCOUNT";
    private static final String TYPE = "TYPE";
    private static final String EVALUATION = "EVALUATION";

    public static String g_sName = "";
    public static String g_sSex = "";
    public static String g_sPwd = "";
    public static String g_sIMEI = "";
    public static String g_sAccount = "";
    public static String g_sType = "";

    public UserSharedPreferences() {

    }

    public UserSharedPreferences(final String sType) {
        g_sType = sType;
    }

    public UserSharedPreferences(final String sName, final String sSex, final String sPwd
            , final String sIMEI, final String sAccount) {
        g_sName = sName;
        g_sSex = sSex;
        g_sPwd = sPwd;
        g_sIMEI = sIMEI;
        g_sAccount = sAccount;
        g_sType = "1";
    }

    public final void fnReadData(final Context conText) {
        g_sharedPreferencesSetting = conText.getSharedPreferences(DATA, 0);
        g_sName = g_sharedPreferencesSetting.getString(NAME, "");
        g_sSex = g_sharedPreferencesSetting.getString(SEX, "");
        g_sPwd = g_sharedPreferencesSetting.getString(PASSWORD, "");
        g_sIMEI = g_sharedPreferencesSetting.getString(IMEI, "");
        g_sAccount = g_sharedPreferencesSetting.getString(ACCOUNT, "");
        g_sType = g_sharedPreferencesSetting.getString(TYPE, "");
    }

    public final String fnGetEvaluation(final Context conText) {
        g_sharedPreferencesSetting = conText.getSharedPreferences(DATA, 0);
        return g_sharedPreferencesSetting.getString(EVALUATION, "");
    }

    public final void fnWriteEvaluation(final Context conText) {
        g_sharedPreferencesSetting = conText.getSharedPreferences(DATA, 0);
        g_sharedPreferencesSetting.edit()
                .putString(EVALUATION, "1")
                .commit();
    }

    public final void fnWritData(final Context conText) {
        g_sharedPreferencesSetting = conText.getSharedPreferences(DATA, 0);
        g_sharedPreferencesSetting.edit()
                .putString(NAME, g_sName)
                .putString(SEX, g_sSex)
                .putString(PASSWORD, g_sPwd)
                .putString(IMEI, g_sIMEI)
                .putString(ACCOUNT, g_sAccount)
                .putString(TYPE, g_sType)
                .commit();
    }

    public final void fnClearData(final Context conText) {
        g_sharedPreferencesSetting = conText.getSharedPreferences(DATA, 0);
        g_sharedPreferencesSetting.edit()
                .putString(NAME, "")
                .putString(SEX, "")
                .putString(PASSWORD, "")
                .putString(IMEI, "")
                .putString(ACCOUNT, "")
                .putString(TYPE, "")
                .commit();
    }


    public final boolean fnCheckLogin(final Context conText) {
        if(g_sType.equals("1")) {
            return true;
        } else {
            Toast.makeText(conText, "登入後才可使用", Toast.LENGTH_SHORT).show();
            fnOpenLoginDialog(conText);
            return false;
        }
    }

    private final void fnOpenLoginDialog(final Context conText) {
        new AlertDialog.Builder(conText)
                .setTitle("登入")
                .setMessage("是否要進行登入")
                .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SysApplication.getInstance().fnClose();
                        fnOpenLogin(conText);
                    }
                })
                .setNegativeButton("註冊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SysApplication.getInstance().fnClose();
                        fnOpenRegister(conText);
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenLogin(final Context conText) {
        final Intent itStart = new Intent(conText, LoginActivity.class);
        conText.startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation((AppCompatActivity)conText).toBundle());
        ((AppCompatActivity) conText).finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenRegister(final Context conText) {
        final Intent itStart = new Intent(conText, RegisterActivity.class);
        conText.startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation((AppCompatActivity)conText).toBundle());
        ((AppCompatActivity) conText).finish();
    }


}
