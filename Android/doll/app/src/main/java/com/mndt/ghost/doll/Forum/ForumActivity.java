package com.mndt.ghost.doll.Forum;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.SysApplication;

/**
 * Created by Ghost on 2017/11/3.
 */

public class ForumActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        fnTransitionAnimation();
        setContentView(R.layout.forum_activity);
        fnInit();
    }

//----------------------------------------------------------------初始化UI.Data--------START-------------------------------------------------------------------------------------------------------------------------

    private final void fnInit() {
        InitData();
        fnInitControl();
    }

    private final void InitData() {

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
        final Button btnForumOne = (Button) findViewById(R.id.btn_forum_one);
        final Button btnForumTwo = (Button) findViewById(R.id.btn_forum_two);
        final Button btnForumThree = (Button) findViewById(R.id.btn_forum_three);
        final Button btnForumFour = (Button) findViewById(R.id.btn_forum_four);
        final Button btnForumFive = (Button) findViewById(R.id.btn_forum_five);

        btnForumOne.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                fnOpenThemeActivity("1");
            }
        });

        btnForumTwo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                fnOpenThemeActivity("2");
            }
        });

        btnForumThree.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                fnOpenThemeActivity("3");
            }
        });

        btnForumFour.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                fnOpenThemeActivity("4");
            }
        });

        btnForumFive.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                fnOpenThemeActivity("5");
            }
        });
    }

    //----------------------------------------------------------------初始化UI.Data--------END-------------------------------------------------------------------------------------------------------------------------

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenThemeActivity(final String sType) {
        final Intent itStart = new Intent(ForumActivity.this, ThemeActivity.class);
        itStart.putExtra("plate_id", sType);
        itStart.putExtra("type", ThemeActivity.VIEW);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
    }
}
