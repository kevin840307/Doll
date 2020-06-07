package com.mndt.ghost.doll.Evaluation;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;

import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Handler.HandlerAction;
import com.mndt.ghost.doll.Handler.HandlerData;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.WebHandler;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.UserSharedPreferences;

/**
 * Created by Ghost on 2017/11/6.
 */

public class EvaluationListDialog {
    private final String TAG = "RespondDialog";
    private Dialog g_diaProduct = null;
    private Context g_objContext = null;
    private EvaluationAdater g_evaluationAdater = null;
    private String g_sAddressNo = "";

    public EvaluationListDialog(final Context objContext, final String sAddressNo) {
        g_objContext = objContext;
        g_sAddressNo = sAddressNo;
        fnInit();
    }

    private final void fnInit() {
        fnInitUIControl();
    }


    private final void fnInitUIControl() {
        fnInitDiaLog();
    }

    private final void fnInitDiaLog() {
        g_diaProduct = new Dialog(g_objContext, R.style.Dialog_style4);
        g_diaProduct.setContentView(R.layout.evaluation_list_dialog);
        final Window dialogWindow = g_diaProduct.getWindow();
        final WindowManager.LayoutParams layParams = dialogWindow.getAttributes();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        dialogWindow.setGravity(Gravity.CENTER);
        layParams.width = (int) ((double) Data.WIDTH_PIXELS - 20); // 寬度
        layParams.height = (int) ((double) Data.HEIGHT_PIXELS / 1.1); // 高度
        layParams.alpha = 1f; // 透明度
        fnInitControl();
        g_diaProduct.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (g_evaluationAdater != null) {
                    g_evaluationAdater.fnCancelAsync();
                }
            }
        });
    }

    private final void fnInitControl() {
        fnInitRefresh();
        fnInitListView();
    }


    private final void fnInitListView() {
        final ListView g_lvDatas = (ListView) g_diaProduct.findViewById(R.id.lv_evaluation_data);
        g_evaluationAdater = new EvaluationAdater(g_diaProduct.getContext());
        g_lvDatas.setAdapter(g_evaluationAdater);
        fnLoadListData();
        g_lvDatas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != SCROLL_STATE_IDLE) {
                    g_evaluationAdater.fnCancelAsync();
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private final void fnLoadListData() {
        final String sKeys[] = {"sIMEI", "sAccount", "sPwd", "sAddressNo"};
        final String sData[] = {UserSharedPreferences.g_sIMEI, UserSharedPreferences.g_sAccount, UserSharedPreferences.g_sPwd, g_sAddressNo};
        new HandlerAction(g_hdMessage).fnRunThread(WebHandler.GET_SHOP_EVALUATION_FUNTION
                , HandlerMessage.GET_SHOP_EVALUATION
                , HandlerData.V_LIST_MESSAGE
                , sKeys, sData, 5);
    }

    private final void fnSetEvaluationDatas(final Object objData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[][] sDatas = ((HandlerData) objData).fnGetList();
                if (sDatas != null) {
                    for (int iIndex = 0; iIndex < sDatas.length; iIndex++) {
                        final EvaluationData thData = new EvaluationData(g_sAddressNo, sDatas[iIndex]);
                        g_evaluationAdater.fnAdd(thData);
                    }
                    Log.d(TAG, "載入成功");
                    new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_UI1);
                }
            }
        }).start();
    }


    private final void fnUpdateListView() {
        final SwipeRefreshLayout srlRefresh = (SwipeRefreshLayout) g_diaProduct.findViewById(R.id.srl_refresh);
        g_evaluationAdater.notifyDataSetChanged();
        srlRefresh.setRefreshing(false);
    }

    private final void fnInitRefresh() {
        final SwipeRefreshLayout srlRefresh = (SwipeRefreshLayout) g_diaProduct.findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fnRefresh();
            }
        });
    }

    public final void fnRefresh() {
        final SwipeRefreshLayout srlRefresh = (SwipeRefreshLayout) g_diaProduct.findViewById(R.id.srl_refresh);
        srlRefresh.setRefreshing(true);
        fnInitListView();
        srlRefresh.setRefreshing(false);
    }

    public final void fnClose() {
        g_diaProduct.setCancelable(true);
        g_diaProduct.cancel();
    }

    public final void fnShow() {
        //g_diaProduct.setCancelable(false);
        g_diaProduct.show();
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.GET_SHOP_EVALUATION:
                    fnSetEvaluationDatas(msg.obj);
                    break;
                case HandlerMessage.SET_UI1:
                    fnUpdateListView();
                    break;
            }
        }
    };
}

