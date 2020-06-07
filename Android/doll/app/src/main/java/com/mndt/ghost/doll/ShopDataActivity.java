package com.mndt.ghost.doll;

import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mndt.ghost.doll.Adapter.SetSDImageAsyncs;
import com.mndt.ghost.doll.Evaluation.EvaluationDialog;
import com.mndt.ghost.doll.Evaluation.EvaluationListDialog;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.SQLHandler;
import com.mndt.ghost.doll.SqlDB.DOLLshop;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.ArrayList;

/**
 * Created by Ghost on 2017/10/23.
 */
public class ShopDataActivity extends AppCompatActivity {
    private String g_sAddressNo = "";
    public static int g_iPos = 0;
    public static ArrayList<String> ADDRESS_NO = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.shop_data_activity);
        fnInit();
    }

    private final void fnInit() {
        InitData();
        fnInitControl();
    }

    private final void InitData() {
        g_sAddressNo = getIntent().getStringExtra("address_no");
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnInitImage();
        fnInitButton();
        fnInitImageButton();
        fnInitTextView();
    }


    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnInitImage() {
        final ImageView imgShow = (ImageView) findViewById(R.id.img_product_show);
        new SetSDImageAsyncs(imgShow).execute(g_sAddressNo);
//        final String sImageName = "shop_" + g_sAddressNo;
//        final int iImageId = getResources().getIdentifier(sImageName, "drawable", getPackageName());
//        if (iImageId > 0) {
//            imgShow.setImageResource(iImageId);
//        }
    }


    private final void fnInitTextView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final SQLHandler sqlHandler = new SQLHandler(getApplication());
                final ArrayList<String> alData = DOLLshop.fnSelectListShopData(sqlHandler, g_sAddressNo);
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.GET_SQL_DATE1, alData);
            }
        }).start();
    }

    private final void fnUpdateShowUI(final ArrayList<String> alData) {
        if (alData.size() > 0) {
            final TextView textShopName = (TextView) findViewById(R.id.text_shop_name);
            final TextView textShopAdd = (TextView) findViewById(R.id.text_shop_add);
            final TextView textMachineAmount = (TextView) findViewById(R.id.text_shop_machine_amount);
            final TextView textMachineType = (TextView) findViewById(R.id.text_shop_machine_type);
            final TextView textRemarks = (TextView) findViewById(R.id.text_remarks);
            final RatingBar ratingStar = (RatingBar) findViewById(R.id.rating_shop_data_start);
            textShopName.setText(alData.get(1));
            textShopAdd.setText(alData.get(2));
            textMachineAmount.setText(alData.get(3));
            textMachineType.setText(alData.get(4));
            textRemarks.setText(alData.get(5));
            final Button btnMylove = (Button) findViewById(R.id.btn_shop_mylove);
            if (alData.get(6).toString().equals("1")) {
                btnMylove.setText("取消最愛");
            }

            int iNum = 0;
            try {
                iNum = Integer.valueOf(alData.get(7));
            } catch (NumberFormatException ex) {
                iNum = 0;
            }
            ratingStar.setProgress(iNum);
        }
        fnStopLoding();
    }

    private final void fnInitButton() {
        final Button btnMylove = (Button) findViewById(R.id.btn_shop_mylove);
        final Button btnShopEvaluation = (Button) findViewById(R.id.btn_shop_evaluation);
        final Button btnShopOther = (Button) findViewById(R.id.btn_shop_other);
        final Button btnCheckEvaluation = (Button)findViewById(R.id.btn_shop_check_evaluation);

        btnMylove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.UPDATA_MYLOVE_SQL);
            }
        });

        btnShopEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOpenEvaluationDialog();
                //Toast.makeText(getApplicationContext(), "功能未開放", Toast.LENGTH_SHORT).show();
            }
        });

        btnShopOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "功能未開放", Toast.LENGTH_SHORT).show();
            }
        });

        btnCheckEvaluation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOpenEvaluationListDialog();
//                Toast.makeText(getApplicationContext(), "功能未開放", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private final void fnOpenEvaluationListDialog() {
        if(UserSharedPreferences.g_sType.equals("1")) {
            new EvaluationListDialog(this, g_sAddressNo).fnShow();
        } else {
            Toast.makeText(getApplicationContext(), "登入後才可使用", Toast.LENGTH_SHORT).show();
            fnOpenLoginDialog();
        }
    }

    private final void fnOpenEvaluationDialog() {
        if(UserSharedPreferences.g_sType.equals("1")) {
            new EvaluationDialog(this, g_sAddressNo).fnShow();
        } else {
            Toast.makeText(getApplicationContext(), "登入後才可使用", Toast.LENGTH_SHORT).show();
            fnOpenLoginDialog();
        }
    }

    private final void fnOpenLoginDialog() {
        new AlertDialog.Builder(ShopDataActivity.this)
                .setTitle("登入")
                .setMessage("是否要進行登入")
                .setPositiveButton("登入", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SysApplication.getInstance().fnClose();
                        fnOpenLogin();
                    }
                })
                .setNegativeButton("註冊", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SysApplication.getInstance().fnClose();
                        fnOpenRegister();
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
    private final void fnOpenLogin() {
        final Intent itStart = new Intent(ShopDataActivity.this, LoginActivity.class);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(ShopDataActivity.this).toBundle());
        finish();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenRegister() {
        final Intent itStart = new Intent(ShopDataActivity.this, RegisterActivity.class);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(ShopDataActivity.this).toBundle());
        finish();
    }


    private final void fnInitImageButton() {
        final ImageButton ibtnLast = (ImageButton) findViewById(R.id.ibtn_last_data);
        final ImageButton ibtnNext = (ImageButton) findViewById(R.id.ibtn_next_data);

        ibtnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (g_iPos != 0) {
                    g_iPos -= 1;
                    fnOpenShopData(ADDRESS_NO.get(g_iPos));
                } else {
                    Toast.makeText(getApplicationContext(), "已經是第一筆了", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (g_iPos < ADDRESS_NO.size() - 1) {
                    g_iPos += 1;
                    fnOpenShopData(ADDRESS_NO.get(g_iPos));
                } else {
                    Toast.makeText(getApplicationContext(), "已經是最後一筆了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenShopData(final String sAddressNo) {
        final Intent itStart = new Intent(ShopDataActivity.this, ShopDataActivity.class);
        itStart.putExtra("flag", "explode");
        itStart.putExtra("address_no", sAddressNo);
        startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
        finish();
    }

    private final void fnUpdateMyLove() {
        final Button btnMylove = (Button) findViewById(R.id.btn_shop_mylove);
        final SQLHandler sqlHandler = new SQLHandler(this);
        if (btnMylove.getText().equals("取消最愛")) {
            DOLLshop.fnUpdateLove(sqlHandler, g_sAddressNo, false);
            btnMylove.setText("加入最愛");
            Toast.makeText(getApplicationContext(), "已取消最愛", Toast.LENGTH_SHORT).show();
        } else if (btnMylove.getText().equals("加入最愛")) {
            DOLLshop.fnUpdateLove(sqlHandler, g_sAddressNo, true);
            btnMylove.setText("取消最愛");
            Toast.makeText(getApplicationContext(), "已加入最愛", Toast.LENGTH_SHORT).show();
        }
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
                case HandlerMessage.UPDATA_MYLOVE_SQL:
                    fnUpdateMyLove();
                    break;
                case HandlerMessage.GET_SQL_DATE1:
                    fnUpdateShowUI((ArrayList<String>) msg.obj);
                    break;
            }
        }
    };

}
