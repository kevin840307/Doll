package com.mndt.ghost.doll;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.transition.Explode;
import android.util.Base64;
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
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.WebHandler;
import com.mndt.ghost.doll.SoapCall.SoapFunctions;

import org.ksoap2.serialization.SoapObject;
import java.io.ByteArrayOutputStream;

/**
 * Created by Ghost on 2017/10/29.
 */
public class RegisterActivity extends AppCompatActivity {

    private final int GET_IMAGE = 0x001;
    private final int GET_CROP = 0x002;
    private String g_sImageBase64String = "";
    private String g_sIMEI = "";
    private String g_sCheckIMEI = "";
    private String g_sName = "";
    private String g_sAccount = "";
    private String g_sPwd = "";
    private String g_sSex = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.register_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitControl();
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnInitButton();
        fnInitRadioButton();
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnInitButton() {
        final Button btnRegister = (Button) findViewById(R.id.btn_register_register);
        final Button btnCancel = (Button) findViewById(R.id.btn_register_cancel);
        final Button btnOpenPic = (Button) findViewById(R.id.btn_register_open_pic);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (fnCheckAPI() && fnCheckData()) {
                    fnRunRegister();
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

        btnOpenPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent itGetImage = new Intent(Intent.ACTION_GET_CONTENT);
                itGetImage.addCategory(Intent.CATEGORY_OPENABLE);
                itGetImage.setType("image/*");
                startActivityForResult(itGetImage, GET_IMAGE);
            }
        });
    }

    private final void fnInitRadioButton() {
        final RadioButton radbtnMan = (RadioButton) findViewById(R.id.radbtn_register_man);
        radbtnMan.setChecked(true);
    }


    private final boolean fnCheckAPI() {
        if (Build.VERSION.SDK_INT >= 23) {
            final String sPermissions[] = {Manifest.permission.READ_PHONE_STATE};
            final int iPhone = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
            if (iPhone != PackageManager.PERMISSION_GRANTED) {
                //Toast.makeText(this, "請開啟權限, 無權限無法註冊", Toast.LENGTH_SHORT).show();
                ActivityCompat.requestPermissions(RegisterActivity.this, sPermissions, 1);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    private final boolean fnCheckData() {
        final EditText editPwd = (EditText) findViewById(R.id.edit_register_pwd);
        final EditText editCheckPwd = (EditText) findViewById(R.id.edit_register_check_pwd);
        final EditText editAccount = (EditText) findViewById(R.id.edit_register_acount);
        final EditText editName = (EditText) findViewById(R.id.edit_register_name);
        final RadioButton radbtnWoman = (RadioButton) findViewById(R.id.radbtn_register_woman);
        if (!editPwd.getText().toString().equals(editCheckPwd.getText().toString())) {
            Toast.makeText(this, "密碼輸入錯誤", Toast.LENGTH_SHORT).show();
            return false;
        } else if(editPwd.getText().toString().length() < 6
                || editPwd.getText().toString().length() > 20){
            Toast.makeText(this, "密碼長度錯誤", Toast.LENGTH_SHORT).show();
            return false;
        } else if(editAccount.getText().toString().length() < 6
                || editAccount.getText().toString().length() > 20){
            Toast.makeText(this, "帳號長度錯誤", Toast.LENGTH_SHORT).show();
            return false;
        } else if(editName.getText().toString().length() < 1
                || editName.getText().toString().length() > 10){
            Toast.makeText(this, "名稱長度錯誤", Toast.LENGTH_SHORT).show();
            return false;
        }
        final TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        final String sIMEI = telephonyManager.getDeviceId();
        final String sEncryNumber = new EncryptionString(sIMEI).fnGetEncryptionString();
        g_sIMEI = sIMEI;
        g_sCheckIMEI = sEncryNumber;
        g_sName = editName.getText().toString();
        g_sAccount = editAccount.getText().toString();
        g_sPwd = MD5.MD5(editPwd.getText().toString());
        g_sSex = "男";
        if (radbtnWoman.isChecked()) {
            g_sSex = "女";
        }
        return true;
    }


    private final void fnRunRegister() {
        final Thread thRun = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SoapObject soapRequest = fnGetSoapData();
                    final Object soapObject = (Object) SoapFunctions.fnGetData(Data.SERRVICE_API, Data.NAMESPACE + WebHandler.REGISTER_MESSAGE_FUNTION, soapRequest);
                    new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.REGISTER_MESSAGE, soapObject);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thRun.start();
    }

    private final SoapObject fnGetSoapData() {
        final SoapObject soapRequest = new SoapObject(Data.NAMESPACE, WebHandler.REGISTER_MESSAGE_FUNTION);
        soapRequest.addProperty("sIMEI", g_sIMEI);
        soapRequest.addProperty("sCheckIMEI", g_sCheckIMEI);
        soapRequest.addProperty("sStrBase64", g_sImageBase64String);
        soapRequest.addProperty("sName", g_sName);
        soapRequest.addProperty("sAccount", g_sAccount);
        soapRequest.addProperty("sPwd", g_sPwd);
        soapRequest.addProperty("sSex", g_sSex);
        return soapRequest;
    }

    private final void fnCheckRegister(final Object objData) {
        if (objData != null) {
            final Boolean bCheck = Boolean.valueOf(objData.toString());
            if (bCheck) {
                Toast.makeText(this, "註冊成功", Toast.LENGTH_SHORT).show();
                new UserSharedPreferences(g_sName, g_sSex, g_sPwd, g_sIMEI, g_sAccount).fnWritData(getApplication());
                fnOpenMainActivity();
            } else {
                Toast.makeText(this, "註冊失敗", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "無法連接伺服器", Toast.LENGTH_SHORT).show();
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenMainActivity() {
        final Intent itStart = new Intent(RegisterActivity.this, MainActivity.class);
        itStart.putExtra("flag", "slide");
        itStart.putExtra("type", "1");
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenWayLogin() {
        final Intent itStart = new Intent(RegisterActivity.this, LoginWayActivity.class);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_IMAGE) {
            if (data == null) {
                return;
            } else {
                final Uri urlImage = data.getData();
                fnStartImageZoom(urlImage);
            }
        } else if (requestCode == GET_CROP) {
            if (data == null) {
                return;
            } else {
                final Bundle extras = data.getExtras();
                if (extras != null) {
                    Bitmap btData = extras.getParcelable("data");
                    final RoundImageView imgShow = (RoundImageView) findViewById(R.id.img_register_pic);
                    imgShow.setImageBitmap(btData);
                    g_sImageBase64String = fnGetBase64(btData);
                    Toast.makeText(this, "上傳成功", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private final void fnStartImageZoom(Uri uri) {
        final Intent itOpenCROP = new Intent("com.android.camera.action.CROP");
        itOpenCROP.setDataAndType(uri, "image/*");         //设置数据uri和类型为图片类型
        itOpenCROP.putExtra("crop", true);                 //显示View为可裁剪的

        itOpenCROP.putExtra("aspectX", 1);                //裁剪的宽高的比例为1:1
        itOpenCROP.putExtra("aspectY", 1);

        itOpenCROP.putExtra("outputX", 150);             //输出图片的宽高均为150
        itOpenCROP.putExtra("outputY", 150);
        itOpenCROP.putExtra("return-data", true);         //裁剪之後返回data
        startActivityForResult(itOpenCROP, GET_CROP);
    }

    public final static String fnGetBase64(final Bitmap btData) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        btData.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] b = baos.toByteArray();
        //btData.recycle();
        return Base64.encodeToString(b, Base64.DEFAULT);
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
                case HandlerMessage.REGISTER_MESSAGE:
                    fnCheckRegister(msg.obj);
                    break;
            }
        }
    };
}
