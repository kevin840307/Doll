package com.mndt.ghost.doll.View;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.mndt.ghost.doll.Adapter.ListViewStyle2Adapter;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.SQLHandler;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.ShopDataActivity;
import com.mndt.ghost.doll.SqlDB.DOLLshop;

import java.util.ArrayList;

/**
 * Created by Ghost on 2017/10/23.
 */
public class PopularView {
    private SQLHandler g_sqlHandler = null;
    private View g_view = null;

    public final void fnInit(final View view) {
        g_view = view;
        if (g_view != null) {
            fnStartLoding();
            g_sqlHandler = new SQLHandler(view.getContext());
            fnInitControl();
            fnStopLoding();
        }
    }

    private final void fnInitControl() {
        fnInitListView();
    }

    private final void fnStartLoding() {
        final ProgressBar proBar = (ProgressBar) g_view.findViewById(R.id.pro_load);
        proBar.setVisibility(View.VISIBLE);
    }

    private final void fnStopLoding() {
        final ProgressBar proBar = (ProgressBar) g_view.findViewById(R.id.pro_load);
        proBar.setVisibility(View.GONE);
    }


    private final void fnInitListView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<ArrayList<String>> alDatas = DOLLshop.fnSelectListPopular(g_sqlHandler);
                ShopDataActivity.ADDRESS_NO = alDatas.get(0);
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.GET_SQL_DATE1, alDatas);
            }
        }).start();
    }

    private final void fnUpdateListView(final ArrayList<ArrayList<String>> alDatas) {
        final ListView lvData = (ListView) g_view.findViewById(R.id.lv_popular_data);
        if (alDatas.size() > 0) {
            final ListViewStyle2Adapter lvAdapter = new ListViewStyle2Adapter(g_view.getContext(), R.drawable.crown, alDatas);
            lvData.setAdapter(lvAdapter);
            Animation animation = AnimationUtils.loadAnimation(g_view.getContext(), R.anim.layout_item_anim_set);
            LayoutAnimationController controller = new LayoutAnimationController(animation);
            controller.setDelay(0.5f);
            controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
            lvData.setLayoutAnimation(controller);
            lvData.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    ShopDataActivity.g_iPos = position;
                    fnOpenShopData(alDatas.get(0).get(position));
                }
            });
        }
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenShopData(final String sAddressNo) {
        final Intent itStart = new Intent(g_view.getContext(), ShopDataActivity.class);
        itStart.putExtra("flag", "fade");
        itStart.putExtra("address_no", sAddressNo);
        g_view.getContext().startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) g_view.getContext()).toBundle());
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.GET_SQL_DATE1:
                    fnUpdateListView((ArrayList<ArrayList<String>>)msg.obj);
                    break;
            }
        }
    };
}