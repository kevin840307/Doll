package com.mndt.ghost.doll;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.Base64;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.mndt.ghost.doll.Forum.ThemeActivity;
import com.mndt.ghost.doll.Forum.ThemeData;
import com.mndt.ghost.doll.Handler.HandlerAction;
import com.mndt.ghost.doll.Handler.HandlerData;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.WebHandler;
import com.mndt.ghost.doll.Image.RImageUrlDowloadAsyncs;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by Ghost on 2017/11/7.
 */

public class UserViewActivity extends AppCompatActivity {
    private final int GET_IMAGE = 0x001;
    private final int GET_CROP = 0x002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.user_view_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitControl();
    }

    private final void fnInitControl() {
        fnInitTextView();
        fnInitButton();
        fnInitImageView();
    }

    private final void fnInitTextView() {
        final TextView textName = (TextView) findViewById(R.id.text_user_name);
        final TextView textAccount = (TextView) findViewById(R.id.text_user_account);
        textName.setText(UserSharedPreferences.g_sName);
        textAccount.setText(UserSharedPreferences.g_sAccount);
    }

    private final void fnInitButton() {
        final Button btnUpdateImage = (Button) findViewById(R.id.btn_user_open_pic);
        final Button btnTheme = (Button) findViewById(R.id.btn_user_theme);

        btnUpdateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Intent itGetImage = new Intent(Intent.ACTION_GET_CONTENT);
                itGetImage.addCategory(Intent.CATEGORY_OPENABLE);
                itGetImage.setType("image/*");
                startActivityForResult(itGetImage, GET_IMAGE);
            }
        });

        btnTheme.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                fnOpenMyTheme();
            }
        });
    }

    private final void fnInitImageView() {
        final RoundImageView rimgView = (RoundImageView) findViewById(R.id.rimg_user_pic);
        new RImageUrlDowloadAsyncs(rimgView, true).execute(RImageUrlDowloadAsyncs.fnGetUrlImageData(UserSharedPreferences.g_sAccount));
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
                    final Bitmap btData = extras.getParcelable("data");
                    final RoundImageView imgShow = (RoundImageView) findViewById(R.id.rimg_user_pic);
                    imgShow.setImageBitmap(btData);
                    fnUpdateImageData(btData);
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

    private final void fnUpdateImageData(final Bitmap btData) {
        final String sImageBase64String = fnGetBase64(btData);
        final String sKeys[] = {"sIMEI", "sAccount", "sPwd", "sStrBase64"};
        final String sData[] = {UserSharedPreferences.g_sIMEI, UserSharedPreferences.g_sAccount, UserSharedPreferences.g_sPwd, sImageBase64String};
        new HandlerAction(g_hdMessage).fnRunThread(WebHandler.UPDATE_USER_IMAGE_FUNTION
                , HandlerMessage.UPDATE_USER_IMAGE
                , HandlerData.VALUE_MESSAGE
                , sKeys, sData);
    }


    public final static String fnGetBase64(final Bitmap btData) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        btData.compress(Bitmap.CompressFormat.JPEG, 60, baos);
        byte[] b = baos.toByteArray();
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    private final void fnCheckUpdate(final Object objData) {
        final String sDatas = ((HandlerData) objData).fnGetValue();
        if (sDatas != null) {
            if(Boolean.valueOf(sDatas)) {
                fnInitImageView();
               Toast.makeText(this, "上傳成功", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "上傳失敗", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenMyTheme() {
        final Intent itStart = new Intent(UserViewActivity.this, ThemeActivity.class);
        itStart.putExtra("plate_id", "1");
        itStart.putExtra("type", ThemeActivity.MYVIEW);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.UPDATE_USER_IMAGE:
                    fnCheckUpdate(msg.obj);
                    break;
            }
        }
    };
}
