package com.mndt.ghost.doll.Forum;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Handler.HandlerAction;
import com.mndt.ghost.doll.Handler.HandlerData;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.WebHandler;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.UserSharedPreferences;

import java.util.ArrayList;

/**
 * Created by Ghost on 2017/11/6.
 */

public class RespondDialog {
    private final String TAG = "RespondDialog";
    private Dialog g_diaProduct = null;
    private Context g_objContext = null;
    private RespondAdater g_respondAdater = null;
    private ThemeData g_thData = null;
    private int GET_DATA_SIZE = 20;
    private int g_iStart = 0;
    private int g_iEnd = 20;
    private int iNowDataIndex = 0;
    private boolean g_bHasData = true;
    private boolean g_bScrollFoot = true;

    public RespondDialog(final Context objContext, final ThemeData thDat) {
        g_objContext = objContext;
        g_thData = thDat;
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
        g_diaProduct.setContentView(R.layout.respond_dialog);
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
                if(g_respondAdater != null) {
                    g_respondAdater.fnCancelAsync();
                }
            }
        });
    }

    private final void fnInitControl() {
        fnInitRefresh();
        fnImageButton();
        fnInitListView();
    }

    private final void fnImageButton() {
        final ImageButton ibtnSend = (ImageButton) g_diaProduct.findViewById(R.id.ibtn_respond_send);
        ibtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnRunInsertResponese();
            }
        });
    }

    private final void fnInitListView() {
        final ListView g_lvDatas = (ListView) g_diaProduct.findViewById(R.id.lv_respond_data);
        g_respondAdater = new RespondAdater(g_diaProduct.getContext());
        g_lvDatas.setAdapter(g_respondAdater);
        fnLoadListData(g_iStart, g_iEnd);
        g_lvDatas.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState != SCROLL_STATE_IDLE) {
                    g_respondAdater.fnCancelAsync();
                }

                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE && g_bScrollFoot) {
                    g_iStart = g_iEnd;
                    g_iEnd += GET_DATA_SIZE;
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

    private final void fnLoadListData(final int iStart, final int iEnd) {
        if (g_bHasData) {
            final String sKeys[] = {"sPlateId", "sThemeId", "sStart", "sEnd"};
            final String sData[] = {g_thData.fnGetSPlateId(), g_thData.fnGetSThemeId(), String.valueOf(iStart), String.valueOf(iEnd)};
            new HandlerAction(g_hdMessage).fnRunThread(WebHandler.GET_RESPONSE_DATA_FUNTION
                    , HandlerMessage.GET_RESPONSE_DATA
                    , HandlerData.V_LIST_MESSAGE
                    , sKeys, sData, 6);
        }
    }

    private final void fnSetResponseDatas(final Object objData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String[][] sDatas = ((HandlerData) objData).fnGetList();
                if (sDatas != null) {
                    if (sDatas.length + 1 < GET_DATA_SIZE) {
                        g_bHasData = false;
                    }
                    for (int iIndex = 0; iIndex < sDatas.length; iIndex++) {
                        final RespondData thData = new RespondData(g_thData.fnGetPlateId(), g_thData.fnGetThemeId(), sDatas[iIndex]);
                        g_respondAdater.fnAdd(thData);
                    }
                    iNowDataIndex += sDatas.length;
                    Log.d(TAG, "載入成功");
                    new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_UI1);
                }
            }
        }).start();
    }

    private final void fnRunInsertResponese() {
        final EditText editContent = (EditText) g_diaProduct.findViewById(R.id.edit_respond_input);
        String sContent = editContent.getText().toString();
        if (sContent.length() == 0) {
            //Toast.makeText(g_diaProduct.getContext(), "請輸入內容", Toast.LENGTH_SHORT).show();
            return;
        }
        //fnStartLoding();
        editContent.setText("");
        final String sKeys[] = {"sIMEI", "sAccount", "sPwd", "sPlateId", "sThemeId", "sContent"};
        final String sData[] = {UserSharedPreferences.g_sIMEI, UserSharedPreferences.g_sAccount, UserSharedPreferences.g_sPwd, g_thData.fnGetSPlateId()
                , g_thData.fnGetSThemeId(), sContent};
        new HandlerAction(g_hdMessage).fnRunThread(WebHandler.INSERT_RESPONSE_DATA_FUNTION
                , HandlerMessage.INSERT_RESPONSE_DATA
                , HandlerData.VALUE_MESSAGE
                , sKeys, sData);

    }

    private final void fnCheckAction(final Object objData) {
        final HandlerData msgData = (HandlerData) objData;
        if (msgData != null) {
            final String sTheme = msgData.fnGetValue();
            if (sTheme != null && !sTheme.equals("N")) {
                //Toast.makeText(g_diaProduct.getContext(), "留言成功", Toast.LENGTH_SHORT).show();
                fnRefresh();
            }
        }
    }

    private final void fnUpdateListView() {
        final SwipeRefreshLayout srlRefresh = (SwipeRefreshLayout)g_diaProduct.findViewById(R.id.srl_refresh);
        g_respondAdater.notifyDataSetChanged();
        final ListView g_lvDatas = (ListView) g_diaProduct.findViewById(R.id.lv_respond_data);
        srlRefresh.setRefreshing(false);
        //g_lvDatas.setSelection(g_respondAdater.getCount() - 1);
    }

    private final void fnInitRefresh() {
        final SwipeRefreshLayout srlRefresh = (SwipeRefreshLayout)g_diaProduct.findViewById(R.id.srl_refresh);
        srlRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fnRefresh();
            }
        });
    }

    public final void fnRefresh() {
        final SwipeRefreshLayout srlRefresh = (SwipeRefreshLayout)g_diaProduct.findViewById(R.id.srl_refresh);
        srlRefresh.setRefreshing(true);
        g_bHasData = true;
        g_iStart = iNowDataIndex;
        g_iEnd = g_iStart + GET_DATA_SIZE;
        fnLoadListData(g_iStart, g_iEnd);
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
                case HandlerMessage.GET_RESPONSE_DATA:
                    fnSetResponseDatas(msg.obj);
                    break;
                case HandlerMessage.SET_UI1:
                    fnUpdateListView();
                    break;
                case HandlerMessage.INSERT_RESPONSE_DATA:
                    fnCheckAction(msg.obj);
                    break;
            }
        }
    };
}

