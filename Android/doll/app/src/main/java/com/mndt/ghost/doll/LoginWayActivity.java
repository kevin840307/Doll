package com.mndt.ghost.doll;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Ghost on 2017/10/29.
 */
public class LoginWayActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.login_way_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitData();
        fnInitButton();
    }

    private final void fnInitData() {
        fnInitAdMob();
        fnInitButton();
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnInitButton();
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnInitButton() {
        final Button btnVisitors = (Button) findViewById(R.id.btn_way_visitors);
        final Button btnLogin = (Button) findViewById(R.id.btn_way_login);
        final Button btnRegister = (Button) findViewById(R.id.btn_way_register);

        btnVisitors.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                fnOpenMainActivity();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                final Intent itStart = new Intent(LoginWayActivity.this, RegisterActivity.class);
                startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(LoginWayActivity.this).toBundle());
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                final Intent itStart = new Intent(LoginWayActivity.this, LoginActivity.class);
                startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(LoginWayActivity.this).toBundle());
                finish();
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenMainActivity() {
        final Intent itStart = new Intent(LoginWayActivity.this, MainActivity.class);
        itStart.putExtra("flag", "slide");
        itStart.putExtra("type", "0");
        new UserSharedPreferences("0").fnWritData(this);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(LoginWayActivity.this).toBundle());
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
}
