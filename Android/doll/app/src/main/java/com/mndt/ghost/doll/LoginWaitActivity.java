package com.mndt.ghost.doll;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.transition.Slide;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mndt.ghost.doll.Handler.HandlerAction;
import com.mndt.ghost.doll.Handler.HandlerData;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.SQLHandler;
import com.mndt.ghost.doll.Handler.WebHandler;
import com.mndt.ghost.doll.Image.ImageAction;
import com.mndt.ghost.doll.Image.ImageUrlDowloadAsyncs;
import com.mndt.ghost.doll.Image.URLImageData;
import com.mndt.ghost.doll.SoapCall.SoapFunctions;
import com.mndt.ghost.doll.SqlDB.DOLLdata_update;
import com.mndt.ghost.doll.SqlDB.DOLLshop;

import org.ksoap2.serialization.SoapObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Ghost on 2017/5/10.
 */
public class LoginWaitActivity extends AppCompatActivity {
    private final String TAG = "LoginWaitActivity";
    private float g_fScheduble = 0;
    private ProgressBar g_probSchedule = null;
    private TextView g_textSchedule = null;
    private SQLHandler g_sqlHandler = null;
    private static boolean g_bFinish = false;
    private static boolean g_bInit = true;
    private boolean g_bDowloadImage = true;
    private final int PERMISSION_RESULT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.login_wait_layout);
        fnInit();
    }

    private final void fnInit() {
        fnInitData();
        fnInitControl();
        if (fnCheckAPI()) {
            fnCheckUpdate();
        }
    }


    private final void fnCheckUpdate() {
        final String[] sData = DOLLdata_update.fnSelectStatus(g_sqlHandler, "start");
        fnUpdateSchedule(5f);
        if (sData[0].equals("1")) {
            MainActivity.MESSAGE = sData[2];
            fnRunCheckDate(sData[1]);
        } else {
            DOLLdata_update.fnInsert(g_sqlHandler, fnGetUpDateData("0", "無"));
            fnUpdateSchedule(10f);
            fnShowDialog(true);
        }
    }

    private final boolean fnCheckAPI() {
        if (Build.VERSION.SDK_INT >= 23) {
            final String sPermissions[] = {Manifest.permission.READ_EXTERNAL_STORAGE
                    , Manifest.permission.WRITE_EXTERNAL_STORAGE
                    , Manifest.permission.ACCESS_FINE_LOCATION
                    , Manifest.permission.READ_PHONE_STATE};
            final int iRead = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            final int iWrite = ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            final int iLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            final int iPhone = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (iRead != PackageManager.PERMISSION_GRANTED
                    || iWrite != PackageManager.PERMISSION_GRANTED
                    || iLocation != PackageManager.PERMISSION_GRANTED
                    || iPhone != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(LoginWaitActivity.this, sPermissions, PERMISSION_RESULT);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_RESULT:
                fnCheckUpdate();
                return;
        }
    }

    private final void fnRunCheckDate(final String sDate) {
        final Thread thRun = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    if(fnCheckServerStatus()) {
                        final SoapObject soapRequest = fnGetSoapCheckData(sDate);
                        final Object soapObject = (Object) SoapFunctions.fnGetData(Data.SERRVICE_API, Data.NAMESPACE + WebHandler.GET_VERTION_DATE_FUNTION, soapRequest);
                        fnUpdateSchedule(10f);
                        new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.GET_VERTION_DATE, soapObject);
                    } else {
                        fnInitMainData();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thRun.start();
    }

    private final boolean fnCheckServerStatus() {
        if (!ImageAction.fnCheckInternet(this) && !ImageAction.fnCheckUrl(Data.SERRVICE_API)) {
            return false;
        }
        return true;
    }


    private final SoapObject fnGetSoapCheckData(final String sDate) {
        final SoapObject soapRequest = new SoapObject(Data.NAMESPACE, WebHandler.GET_VERTION_DATE_FUNTION);
        soapRequest.addProperty("sDate", sDate);
        return soapRequest;
    }

    private final void fnUpdate(final Object soapObject) {
        if (soapObject != null) {
            boolean bCheck = Boolean.valueOf(soapObject.toString());
            if (bCheck) {
                fnShowDialog(false);
            } else {
                fnUpdateSchedule(60f);
                fnInitMainData();
            }
        } else {
            fnShowDialog(false);
            Toast.makeText(this, "無法連接伺服器", Toast.LENGTH_SHORT).show();
        }
    }

    private final void fnInitData() {
        g_bFinish = false;
        g_bInit = true;
        g_sqlHandler = new SQLHandler(this);
        final DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        Data.WIDTH_PIXELS = displayMetrics.widthPixels;
        Data.HEIGHT_PIXELS = displayMetrics.heightPixels;
    }


    private final void fnInitBackData() {
        fnInitSql();
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnInitImageView();
        fnInitProgressBar();
        fnInitTextView();
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnInitImageView() {
        final ImageView imgLogo = (ImageView) findViewById(R.id.img_wait_logo);
        final Animation anAction = AnimationUtils.loadAnimation(LoginWaitActivity.this, R.anim.wait_anim);
        imgLogo.setAnimation(anAction);
        anAction.start();
    }

    private final void fnInitProgressBar() {
        g_probSchedule = (ProgressBar) findViewById(R.id.prob_schedule);
        g_probSchedule.setMax(100);
    }

    private final void fnInitTextView() {
        g_textSchedule = (TextView) findViewById(R.id.text_schedule);
    }

    private final void fnRunUpdate() {
        final Timer timerWait = new Timer();
        timerWait.schedule(TimerTasks, 2000, 500);
    }

    private final void fnInitSql() {
        fnRunGetShop();
    }

    private final void fnShowDialog(final boolean bNoData) {
        final AlertDialog alertDialog = new AlertDialog.Builder(LoginWaitActivity.this)
                .setTitle("更新")
                .setMessage("是否要下載新資料(無更新可能無法使用)")
                .setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fnCancel(dialog);
                        fnShowDialogGetImage();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fnCancel(dialog);
                        if (bNoData) {
                            Toast.makeText(getApplicationContext(), "無資料讀取請更新", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            fnUpdateSchedule(70f);
                            fnInitMainData();
                            Toast.makeText(getApplicationContext(), "取消更新", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .show();

        try {
            final Field fieId = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
            fieId.setAccessible(true);
            fieId.set(alertDialog, false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private final void fnShowDialogGetImage() {
        final AlertDialog alertDialog = new AlertDialog.Builder(LoginWaitActivity.this)
                .setTitle("下載")
                .setMessage("是否要下載圖片")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        g_bDowloadImage = true;
                        fnCancel(dialog);
                        Toast.makeText(getApplicationContext(), "開始更新", Toast.LENGTH_SHORT).show();
                        fnRunUpdate();
                    }
                })
                .setNeutralButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        g_bDowloadImage = false;
                        Toast.makeText(getApplicationContext(), "開始更新", Toast.LENGTH_SHORT).show();
                        fnRunUpdate();
                    }
                })
                .show();
    }

    private final void fnCancel(final DialogInterface alertDialog) {
        try {
            final Field fieId = alertDialog.getClass().getSuperclass().getDeclaredField("mShowing");
            fieId.setAccessible(true);
            fieId.set(alertDialog, true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final void fnInitMainData() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                fnUpdateSchedule(80f);
                final ArrayList<String> alPopularData = DOLLshop.fnSelectPopular(g_sqlHandler);
                fnUpdateSchedule(87f);
                final ArrayList<String> alAreaData = DOLLshop.fnSelectArea(g_sqlHandler);
                fnUpdateSchedule(95f);
                Data.POPULAR_NAME = alPopularData.toArray(new String[0]);
                Data.AREA_NAME = alAreaData.toArray(new String[0]);
                if(fnCheckServerStatus()) {
                    new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.OPEN_ACTIVITY, 1);
                } else {
                    new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.OPEN_ACTIVITY, 0);
                }
            }
        }).start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenActivity(final int iType) {
        fnUpdateSchedule(98f);
        if (iType == 1) {
            new UserSharedPreferences().fnReadData(getApplication());
            if (UserSharedPreferences.g_sType.equals("")) {
                fnOpenLoginWayActivity();
            } else if (UserSharedPreferences.g_sType.equals("0")) {
                fnOpenMainActivity();
            } else if (UserSharedPreferences.g_sType.equals("1")) {
                fnRunLogin();
            }
        } else {
            Toast.makeText(this, "伺服器維修中", Toast.LENGTH_SHORT).show();
            fnOpenLoginWayActivity();
        }
    }

    private final void fnRunLogin() {
        final String sKeys[] = {"sIMEI", "sAccount", "sPwd"};
        final String sData[] = {UserSharedPreferences.g_sIMEI, UserSharedPreferences.g_sAccount, UserSharedPreferences.g_sPwd};
        new HandlerAction(g_hdMessage).fnRunThread(WebHandler.LOGIN_MESSAGE_FUNTION
                , HandlerMessage.LOGIN_MESSAGE
                , HandlerData.V_LIST_MESSAGE
                , sKeys, sData, 2);
    }

    private final void fnCheckLogin(final Object objData) {
        final String[][] sDatas = ((HandlerData) objData).fnGetList();
        if (sDatas != null) {
            if (sDatas.length > 0) {
                Toast.makeText(this, "登入成功", Toast.LENGTH_SHORT).show();
                fnOpenMainActivity();
            } else {
                fnOpenLoginWayActivity();
                Toast.makeText(this, "登入失敗", Toast.LENGTH_SHORT).show();
            }
        } else {
            fnOpenLoginWayActivity();
            Toast.makeText(this, "登入失敗", Toast.LENGTH_SHORT).show();
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenMainActivity() {
        final Intent itStart = new Intent(LoginWaitActivity.this, MainActivity.class);
        itStart.putExtra("flag", "fade");
        itStart.putExtra("type", UserSharedPreferences.g_sType);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenLoginWayActivity() {
        final Intent itStart = new Intent(LoginWaitActivity.this, LoginWayActivity.class);
        startActivity(itStart);
        finish();
    }


    private final void fnUpdateSchedule(final float fValue) {
        g_fScheduble = fValue;
        new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.UPDATE_SCHEDULE);
    }

    private final void fnUpdateAddSchedule(final float fValue) {
        g_fScheduble += fValue;
        new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.UPDATE_SCHEDULE);
    }

    private final void fnUpdateSchedule() {
        g_probSchedule.setProgress((int) g_fScheduble);
        g_textSchedule.setText(g_probSchedule.getProgress() + " %");
    }

    /*------------------------------------------------開始取得寫入商店資料---------------------------------------------------------------------*/

    private final void fnRunGetShop() {
        final Thread thRun = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SoapObject soapRequest = new SoapObject(Data.NAMESPACE, WebHandler.GET_SHOP_DATA_FUNTION);
                    final Object soapObject = (Object) SoapFunctions.fnGetData(Data.SERRVICE_API, Data.NAMESPACE + WebHandler.GET_SHOP_DATA_FUNTION, soapRequest);
                    fnUpdateSchedule(15f);
                    new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.GET_SHOP_DATA, soapObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thRun.start();
    }

    private final void fnSetData(final Object soapObject) {
        if (soapObject != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String[][] sDatas = SoapFunctions.fnGetVListData(soapObject, 14);
                    if (sDatas.length > 0) {
                        final float fAddSch = 55.0f / (float) sDatas.length;
                        for (int iPos = 0; iPos < sDatas.length; iPos++) {
                            final DOLLshop dollData = new DOLLshop(sDatas[iPos]);
                            DOLLshop.fnInsert(g_sqlHandler, dollData);
                            if(g_bDowloadImage) {
                                final URLImageData urlImageData = ImageAction.fnGetShopImageData(sDatas[iPos][0]);
                                ImageAction.fnDownloadImage(urlImageData);
                            }
                            fnUpdateAddSchedule(fAddSch);
                        }
                        fnRunGetMsg();
                    }
                }
            }).start();
        } else {
            Toast.makeText(this, "無法與伺服器連線", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    /*------------------------------------------------結束取得寫入商店資料---------------------------------------------------------------------*/





    /*------------------------------------------------開始取得版本、訊息---------------------------------------------------------------------*/

    private final void fnRunGetMsg() {
        try {
            final SoapObject soapRequest = new SoapObject(Data.NAMESPACE, WebHandler.GET_VERTION_MSG_FUNTION);
            final Object soapObject = (Object) SoapFunctions.fnGetData(Data.SERRVICE_API, Data.NAMESPACE + WebHandler.GET_VERTION_MSG_FUNTION, soapRequest);
            fnUpdateSchedule(75f);
            new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.GET_VERTION_MSG, soapObject);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 寫入資料
    private final void fnSaveVarMsg(final Object soapObject) {
        if (soapObject != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    final String sData = soapObject.toString();
                    DOLLdata_update.fnUpdate(g_sqlHandler, fnGetUpDateData("1", sData));
                    MainActivity.MESSAGE = sData;
                    fnUpdateSchedule(70);
                    fnInitMainData();
                }
            }).start();

        } else {
            Toast.makeText(this, "無法與伺服器連線", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private final DOLLdata_update fnGetUpDateData(final String sStatus, final String sMsg) {
        final DOLLdata_update dollStartData = new DOLLdata_update();
        final Calendar mCal = Calendar.getInstance();
        final SimpleDateFormat dfFormat = new SimpleDateFormat("yyyy/MM/dd");
        dollStartData.g_sType = "start";
        dollStartData.g_sDate = dfFormat.format(mCal.getTime());
        dollStartData.g_sStatus = sStatus;
        dollStartData.g_sMessage = sMsg;
        return dollStartData;
    }

/*------------------------------------------------結束取得版本、訊息---------------------------------------------------------------------*/

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
        }
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.UPDATE_SCHEDULE:
                    fnUpdateSchedule();
                    break;
                case HandlerMessage.GET_SHOP_DATA:
                    fnSetData(msg.obj);
                    break;
                case HandlerMessage.GET_VERTION_DATE:
                    fnUpdate(msg.obj);
                    break;
                case HandlerMessage.GET_VERTION_MSG:
                    fnSaveVarMsg(msg.obj);
                    break;
                case HandlerMessage.OPEN_ACTIVITY:
                    fnOpenActivity(msg.arg1);
                    break;
                case HandlerMessage.LOGIN_MESSAGE:
                    fnCheckLogin(msg.obj);
                    break;
            }
        }
    };

    final TimerTask TimerTasks = new TimerTask() {
        public void run() {
            if (g_bInit) {
                fnInitBackData();
                g_bInit = false;
            }
            if (!g_bFinish) {
                this.cancel();
            }
        }
    };
}
