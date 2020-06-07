package com.mndt.ghost.doll;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mndt.ghost.doll.Adapter.ListViewStyle1Adapter;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.SQLHandler;
import com.mndt.ghost.doll.SqlDB.DOLLshop;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by Ghost on 2017/10/21.
 */
public class SearchActivity extends AppCompatActivity {
    private SQLHandler g_sqlHandler = null;
    private String g_sArea = "";
    private String g_sLocation = "全部";
    private String g_KeyWord = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SysApplication.getInstance().addActivity(this);
        fnTransitionAnimation();
        setContentView(R.layout.search_list_activity);
        fnInit();
    }

    private final void fnInit() {
        g_sqlHandler = new SQLHandler(this);
        fnInitData();
        fnInitControl();
    }

    private final void fnInitData() {
        g_sArea = getIntent().getStringExtra("area");
        g_sLocation = getIntent().getStringExtra("location");
        g_KeyWord = getIntent().getStringExtra("key_word");
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnInitList();
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }


    private final void fnInitList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<ArrayList<String>> alDatas = DOLLshop.fnSelectListView(g_sqlHandler, g_sArea, g_sLocation, g_KeyWord);
                ShopDataActivity.ADDRESS_NO = alDatas.get(0);
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.GET_SQL_DATE1, alDatas);
            }
        }).start();

    }

    private final void fnUpdateListView(final ArrayList<ArrayList<String>> alDatas) {
        final ListView lvData = (ListView) findViewById(R.id.ls_search_list);
        if (alDatas.size() > 0) {
            final ListViewStyle1Adapter searchAdapter = new ListViewStyle1Adapter(this, alDatas);
            lvData.setAdapter(searchAdapter);
            lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    fnOpenShopData(alDatas.get(0).get(position));
                    ShopDataActivity.g_iPos = position;
                }
            });
        }
        fnStopLoding();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenShopData(final String sAddressNo) {
        final Intent itStart = new Intent(SearchActivity.this, ShopDataActivity.class);
        itStart.putExtra("flag", "fade");
        itStart.putExtra("address_no", sAddressNo);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    private final void fnStartLoding() {
        final ProgressBar proBar = (ProgressBar) findViewById(R.id.pro_load);
        proBar.setVisibility(View.VISIBLE);
    }

    private final void fnStopLoding() {
        final ProgressBar proBar = (ProgressBar) findViewById(R.id.pro_load);
        proBar.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            switch (getIntent().getStringExtra("flag")) {
                case "explode":
                    getWindow().setEnterTransition(new Explode());
                    getWindow().setExitTransition(new Explode());
                    break;
                case "slide":
                    getWindow().setEnterTransition(new Slide());
                    getWindow().setExitTransition(new Slide());
                    break;
                case "fade":
                    getWindow().setEnterTransition(new Fade());
                    getWindow().setExitTransition(new Fade());
                    break;
            }
        }
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.GET_SQL_DATE1:
                    fnUpdateListView((ArrayList<ArrayList<String>>) msg.obj);
                    break;
            }
        }
    };
}
