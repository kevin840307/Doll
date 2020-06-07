package com.mndt.ghost.doll.Forum;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mndt.ghost.doll.Handler.HandlerAction;
import com.mndt.ghost.doll.Handler.HandlerData;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.WebHandler;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.UserSharedPreferences;

/**
 * Created by Ghost on 2017/11/3.
 */

public class ThemeActivity extends AppCompatActivity {
    public final static String VIEW = "VIEW";
    public final static String MYVIEW = "MYVIEW";
    private String sType = "";
    private String TAG = "ThemeActivity";
    private ListView g_lvDatas = null;
    private ThemeAdater g_thetmeAdater = null;
    private int GET_DATA_SIZE = 20;
    private int g_iStart = 0;
    private int g_iEnd = 0;
    private boolean g_bHasData = true;
    private boolean g_bScrollFoot = true;
    private String g_sPlateId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.theme_activity);
        fnInit();
    }

    //----------------------------------------------------------------初始化UI.Data--------START-------------------------------------------------------------------------------------------------------------------------

    private final void fnInit() {
        InitData();
        fnInitControl();
    }

    private final void InitData() {
        g_iStart = 0;
        g_iEnd = 0;
        g_bHasData = true;
        g_bScrollFoot = true;
        g_sPlateId = getIntent().getStringExtra("plate_id");
        sType = getIntent().getStringExtra("type");
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnInitRefresh();
        fnInitTitle();
        if (sType.equals(VIEW)) {
            fnInitViewListView();
            fnInitViewButton();
        } else if (sType.equals(MYVIEW)) {
            fnInitMyListView();
            fnInitMyButton();
        }
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnInitTitle() {
      final TextView textTitle = (TextView) findViewById(R.id.text_title_name);
        textTitle.setText(ThemeData.g_sUserPlate[Integer.valueOf(g_sPlateId) - 1]);
    }

    private final void fnInitViewListView() {
        g_lvDatas = (ListView) findViewById(R.id.lv_theme_data);
        g_thetmeAdater = new ThemeAdater(this);
        g_lvDatas.setAdapter(g_thetmeAdater);
        fnRunGetFirstTheme();
        fnRunNowMaxTheme();

        g_lvDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                fnOpenThemeView((ThemeData) g_thetmeAdater.getItem(position));
            }
        });

        g_lvDatas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != SCROLL_STATE_IDLE) {
                    g_thetmeAdater.fnCancelAsync();
                }

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && g_bScrollFoot) {
                    g_iEnd = g_iStart;
                    g_iStart = g_iStart - GET_DATA_SIZE;
                    fnLoadListData(g_iStart, g_iEnd);
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
                    g_bScrollFoot = true;
                } else {
                    g_bScrollFoot = false;
                }
            }
        });
    }

    private final void fnInitViewButton() {
        final Button btnThemeAdd = (Button) findViewById(R.id.btn_theme_action1);
        final Button btnThemeShowMy = (Button) findViewById(R.id.btn_theme_action2);
        btnThemeShowMy.setBackgroundResource(R.drawable.green_button);
        btnThemeAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(new UserSharedPreferences().fnCheckLogin(ThemeActivity.this)) {
                    fnOpenThemeInsert();
                }
            }
        });
        btnThemeShowMy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if(new UserSharedPreferences().fnCheckLogin(ThemeActivity.this)) {
                    fnOpenMyTheme();
                }
            }
        });
    }

    private final void fnInitMyListView() {
        g_lvDatas = (ListView) findViewById(R.id.lv_theme_data);
        final Button btnAction = (Button) findViewById(R.id.btn_theme_action2);
        g_thetmeAdater = new ThemeAdater(this);
        g_lvDatas.setAdapter(g_thetmeAdater);
        fnRunGeMyTheme();

        g_lvDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (btnAction.getText().toString()) {
                    case "檢視模式":
                        fnOpenThemeView((ThemeData) g_thetmeAdater.getItem(position));
                        break;
                    case "編輯模式":
                        fnOpenThemeInsert((ThemeData) g_thetmeAdater.getItem(position));
                        break;
                }
            }
        });
    }

    private final void fnInitMyButton() {
        final Button btnBack = (Button) findViewById(R.id.btn_theme_action1);
        final Button btnAction = (Button) findViewById(R.id.btn_theme_action2);
        btnBack.setText("返回");
        btnAction.setText("檢視模式");
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (btnAction.getText().toString()) {
                    case "檢視模式":
                        btnAction.setText("編輯模式");
                        btnAction.setBackgroundResource(R.drawable.orange_button);
                        break;
                    case "編輯模式":
                        btnAction.setText("檢視模式");
                        btnAction.setBackgroundResource(R.drawable.red_button);
                        break;
                }
            }
        });
    }


    //----------------------------------------------------------------初始化UI.Data--------END-------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------抓置自己的資料--------START-------------------------------------------------------------------------------------------------------------------------

    private final void fnRunGeMyTheme() {
        if (g_bHasData) {
            final String sKeys[] = {"sIMEI", "sAccount", "sPwd"};
            final String sData[] = {UserSharedPreferences.g_sIMEI, UserSharedPreferences.g_sAccount, UserSharedPreferences.g_sPwd};
            new HandlerAction(g_hdMessage).fnRunThread(WebHandler.GET_MY_THEME_DATA_FUNTION
                    , HandlerMessage.GET_ALL_THEME_DATA
                    , HandlerData.V_LIST_MESSAGE
                    , sKeys, sData, 10);
        }
    }

    //----------------------------------------------------------------抓置自己的資料--------END-------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------抓置頂熱門資料--------START-------------------------------------------------------------------------------------------------------------------------

    private final void fnRunGetFirstTheme() {
        if (g_bHasData) {
            final String sKeys[] = {"sPlateId"};
            final String sData[] = {g_sPlateId};
            new HandlerAction(g_hdMessage).fnRunThread(WebHandler.GET_FIRST_THEME_DATA_FUNTION
                    , HandlerMessage.GET_ALL_THEME_DATA
                    , HandlerData.V_LIST_MESSAGE
                    , sKeys, sData, 10);
        }
    }


    private final void fnSetAllThemeDatas(final Object objData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[][] sDatas = ((HandlerData) objData).fnGetList();
                if (sDatas != null) {
                    for (int iIndex = 0; iIndex < sDatas.length; iIndex++) {
                        final ThemeData thData = new ThemeData(sDatas[iIndex]);
                        g_thetmeAdater.fnAdd(thData);
                    }
                    new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_UI1);
                }
            }
        }).start();
    }

    //----------------------------------------------------------------抓置頂熱門資料--------END-------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------抓主題最大數--------START-------------------------------------------------------------------------------------------------------------------------

    private final void fnRunNowMaxTheme() {
        final String sKeys[] = {"sCodeKind", "sCode"};
        final String sData[] = {"1", g_sPlateId};
        new HandlerAction(g_hdMessage).fnRunThread(WebHandler.GET_CODE_VALUE_FUNTION
                , HandlerMessage.GET_CODE_VALUE
                , HandlerData.VALUE_MESSAGE
                , sKeys, sData);
    }

    private final void fnSetNowMaxTheme(final Object objData) {
        final String sDatas = ((HandlerData) objData).fnGetValue();
        if (sDatas != null && !sDatas.equals(" ")) {
            final int iMaxNum = Integer.valueOf(sDatas);
            g_iEnd = iMaxNum;
            g_iStart = g_iEnd - GET_DATA_SIZE;
            fnLoadListData(g_iStart, g_iEnd);
        }
    }

    //----------------------------------------------------------------抓主題最大數--------END-------------------------------------------------------------------------------------------------------------------------


    //----------------------------------------------------------------抓主題資料--------START-------------------------------------------------------------------------------------------------------------------------

    private final void fnLoadListData(final int iStart, final int iEnd) {
        if (g_bHasData) {
            final String sKeys[] = {"sPlateId", "sStart", "sEnd"};
            final String sData[] = {g_sPlateId, String.valueOf(iStart), String.valueOf(iEnd)};
            new HandlerAction(g_hdMessage).fnRunThread(WebHandler.GET_THEME_DATA_FUNTION
                    , HandlerMessage.GET_THEME_DATA
                    , HandlerData.V_LIST_MESSAGE
                    , sKeys, sData, 9);
        }
    }


    private final void fnSetThemeDatas(final Object objData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[][] sDatas = ((HandlerData) objData).fnGetList();
                if (sDatas != null) {
                    if (sDatas.length + 1 < GET_DATA_SIZE) {
                        g_bHasData = false;
                    }
                    for (int iIndex = 0; iIndex < sDatas.length; iIndex++) {
                        final ThemeData thData = new ThemeData(g_sPlateId, sDatas[iIndex]);
                        g_thetmeAdater.fnAdd(thData);
                    }
                    //g_thetmeAdater.notifyDataSetChanged();
                    new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_UI1);
                }
            }
        }).start();
    }

    //----------------------------------------------------------------抓主題資料--------END-------------------------------------------------------------------------------------------------------------------------

    private final void fnUpdateListView() {
        g_thetmeAdater.notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenMyTheme() {
        final Intent itStart = new Intent(ThemeActivity.this, ThemeActivity.class);
        itStart.putExtra("plate_id", g_sPlateId);
        itStart.putExtra("type", ThemeActivity.MYVIEW);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenThemeInsert() {
        final Intent itStart = new Intent(ThemeActivity.this, ThemeInsertActivity.class);
        itStart.putExtra("type", ThemeInsertActivity.INSERT);
        itStart.putExtra("plate_id", g_sPlateId);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenThemeInsert(final ThemeData thData) {
        final Intent itStart = new Intent(ThemeActivity.this, ThemeInsertActivity.class);
        itStart.putExtra("type", ThemeInsertActivity.EDIT);
        itStart.putExtra("ThemeData", thData);
        itStart.putExtra("plate_id", g_sPlateId);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenThemeView(final ThemeData thData) {
        final Intent itStart = new Intent(ThemeActivity.this, ThemeContentActivity.class);
        itStart.putExtra("ThemeData", thData);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    private final void fnInitRefresh() {
        final SwipeRefreshLayout srlRefresh = (SwipeRefreshLayout)findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fnRefresh();
            }
        });
    }

    public final void fnRefresh() {
        final SwipeRefreshLayout srlRefresh = (SwipeRefreshLayout)findViewById(R.id.srl_refresh);
        srlRefresh.setRefreshing(true);
        fnInit();
        srlRefresh.setRefreshing(false);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        g_thetmeAdater.fnCancelAsync();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.GET_THEME_DATA:
                    fnSetThemeDatas(msg.obj);
                    break;
                case HandlerMessage.GET_ALL_THEME_DATA:
                    fnSetAllThemeDatas(msg.obj);
                    break;
                case HandlerMessage.SET_UI1:
                    fnUpdateListView();
                    break;
                case HandlerMessage.GET_CODE_VALUE:
                    fnSetNowMaxTheme(msg.obj);
                    break;
            }
        }
    };
}
