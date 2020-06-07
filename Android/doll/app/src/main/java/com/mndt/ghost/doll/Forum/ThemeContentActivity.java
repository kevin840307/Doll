package com.mndt.ghost.doll.Forum;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.transition.Explode;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Image.ImageAction;
import com.mndt.ghost.doll.Image.RImageUrlAsyncs;
import com.mndt.ghost.doll.Image.ShowImageActivity;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.RoundImageView;
import com.mndt.ghost.doll.UserSharedPreferences;

import java.util.ArrayList;

/**
 * Created by Ghost on 2017/11/6.
 */

public class ThemeContentActivity extends AppCompatActivity {
    private String TAG = "ThemeContentActivity";
    private ThemeData g_thData = null;
    private ImageAdater g_imgAdater = null;
    private RespondDialog g_respondDialog = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.theme_content_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitData();
        fnInitControl();
    }

    private final void fnInitData() {
        g_thData = (ThemeData) getIntent().getSerializableExtra("ThemeData");
        fnInitfnCheckUrl();
    }

    private final void fnInitfnCheckUrl() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> alPaths = new ArrayList<>();
                final String sPalte = String.valueOf(g_thData.fnGetPlateId());
                final String sTheme = String.valueOf(g_thData.fnGetThemeId());
                for (int iIndex = 0; iIndex < g_thData.fnGetPicAmount(); iIndex++) {
                    final String sUrl = ImageAction.fnGetThemeURL(sPalte + sTheme, iIndex);
                    alPaths.add(sUrl);
                }
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_UI1, alPaths);
            }
        }).start();
    }

    private final void fnInitControl() {
        g_respondDialog = new RespondDialog(this, g_thData);
        fnInitAdMob();
        fnInitButton();
        fnInitTextView();
        fnInitImageView();
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnInitButton() {
        final Button btnRespond = (Button) findViewById(R.id.btn_theme_c_respond);
        btnRespond.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(new UserSharedPreferences().fnCheckLogin(ThemeContentActivity.this)) {
                    g_respondDialog.fnShow();
                }
            }
        });
    }

    private final void fnInitTextView() {
        final TextView textType = (TextView) findViewById(R.id.text_theme_c_type);
        final TextView textTitle = (TextView) findViewById(R.id.text_theme_c_title);
        final TextView textName = (TextView) findViewById(R.id.text_theme_c_name);
        final TextView textDate = (TextView) findViewById(R.id.text_theme_c_date);
        final TextView textContent = (TextView) findViewById(R.id.text_theme_c_content);
        textContent.setMovementMethod(ScrollingMovementMethod.getInstance());
        textType.setText(ThemeData.g_sPlate[g_thData.fnGetPlateId()]);
        textTitle.setText(Html.fromHtml(g_thData.fnGetTitle()));
        textName.setText(g_thData.fnGetName());
        textDate.setText(g_thData.fnDate());
        textContent.setText(g_thData.fnGetContent());
    }

    private final void fnInitImageView() {
        final RoundImageView rimgUser = (RoundImageView) findViewById(R.id.rimg_theme_c_user);
        new RImageUrlAsyncs(rimgUser).execute(ImageAction.fnGetUserURL(g_thData.fnGetAccount()));
    }

    private final void fnInitGridView(final ArrayList<String> alPaths) {
        final GridView gvDatas = (GridView) findViewById(R.id.gv_theme_c_show_img);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Data.WIDTH_PIXELS / 4 * alPaths.size(), LinearLayout.LayoutParams.MATCH_PARENT);
        g_imgAdater = new ImageAdater(this, alPaths, ImageAdater.URL);
        gvDatas.setLayoutParams(params);
        gvDatas.setNumColumns(alPaths.size());
        gvDatas.setAdapter(g_imgAdater);
        gvDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent itStart = new Intent(ThemeContentActivity.this, ShowImageActivity.class);
                itStart.putExtra("type", 2);
                itStart.putExtra("index", position);
                itStart.putExtra("paths", alPaths);
                itStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(itStart);
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(g_imgAdater != null) {
            g_imgAdater.fnCancelAsync();
        }
    }


    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.SET_UI1:
                    fnInitGridView((ArrayList<String>) msg.obj);
                    break;
            }
        }
    };
}
