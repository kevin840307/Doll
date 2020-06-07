package com.mndt.ghost.doll.Evaluation;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Handler.HandlerAction;
import com.mndt.ghost.doll.Handler.HandlerData;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.SQLHandler;
import com.mndt.ghost.doll.Handler.WebHandler;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.SqlDB.DOLL_evaluation;
import com.mndt.ghost.doll.UserSharedPreferences;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Ghost on 2017/5/11.
 */
public class EvaluationDialog {
    private final String TAG = "EvaluationDialog";
    private Dialog g_diaProduct = null;
    private Activity g_objContext = null;
    private String g_sAddressNo = "";
    private String g_sStar = "";
    private String g_sMessage= "";
    private boolean g_bHaveData = false;

    public EvaluationDialog(final Activity objContext, final String sAddressNo) {
        g_objContext = objContext;
        g_sAddressNo = sAddressNo;
        fnInit();
    }

    private final void fnInit() {
        if(UserSharedPreferences.g_sType.equals("1")) {
            fnInitUIControl();
            fnInitData();
        } else {
            Toast.makeText(g_objContext, "登入後才可評價", Toast.LENGTH_SHORT);
        }
    }

    private final void fnInitData() {
        final SQLHandler sqlHandler = new SQLHandler(g_objContext);
        final String sDatas[] = DOLL_evaluation.fnSelectMyEvaluation(sqlHandler, UserSharedPreferences.g_sAccount, g_sAddressNo);
        if(sDatas.length > 0 && sDatas[0].length() > 0) {
            final RatingBar raringStar = (RatingBar)g_diaProduct.findViewById(R.id.rating_eva_start);
            final EditText editMessage = (EditText)g_diaProduct.findViewById(R.id.edit_eva_message);
            raringStar.setProgress(Integer.valueOf(sDatas[0]));
            editMessage.setText(sDatas[1]);
            g_bHaveData = true;
        }
    }

    private final void fnInitUIControl() {
        fnInitDiaLog();
        fnInitButton();
    }

    private final void fnInitDiaLog() {
        g_diaProduct = new Dialog(g_objContext, R.style.Dialog_style2);
        g_diaProduct.setContentView(R.layout.evaluation_dialog);
        final Window dialogWindow = g_diaProduct.getWindow();
        final WindowManager.LayoutParams layParams = dialogWindow.getAttributes();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        dialogWindow.setGravity(Gravity.CENTER);
        layParams.width = (int) ((double) Data.WIDTH_PIXELS / 1.05); // 寬度
        layParams.height = (int) ((double) Data.HEIGHT_PIXELS / 1.3); // 高度
        layParams.alpha = 1f; // 透明度
    }

    private final void fnInitButton() {
        final Button btnCancel = (Button)g_diaProduct.findViewById(R.id.btn_eva_cancel);
        final Button btnSend = (Button)g_diaProduct.findViewById(R.id.btn_eva_send);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g_diaProduct.cancel();
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnRunInsertEvaluation();
            }
        });
    }


/*---------------------------------------------------------------------------------------------------開始新增評價-----------------------------------------------------------------------------------*/
    private final void fnRunInsertEvaluation() {
        final String sFunction = g_bHaveData ? WebHandler.UPDATE_EVALUATION_FUNTION
                                            : WebHandler.INSERT_EVALUATION_FUNTION;
        final RatingBar raringStar = (RatingBar)g_diaProduct.findViewById(R.id.rating_eva_start);
        final EditText editMessage = (EditText)g_diaProduct.findViewById(R.id.edit_eva_message);
        g_sStar = String.valueOf(raringStar.getProgress());
        g_sMessage = editMessage.getText().toString();
        if(g_sMessage.length() == 0) {
            Toast.makeText(g_objContext, "請輸入內容", Toast.LENGTH_SHORT).show();
            return;
        }
        final String sKeys[] = {"sIMEI", "sAccount", "sPwd", "sAddressNo", "star", "sMessage"};
        final String sData[] = {UserSharedPreferences.g_sIMEI, UserSharedPreferences.g_sAccount, UserSharedPreferences.g_sPwd
                        , g_sAddressNo, g_sStar, g_sMessage};
        new HandlerAction(g_hdMessage).fnRunThread(sFunction
                , HandlerMessage.UPDATE_EVALUATION
                , HandlerData.VALUE_MESSAGE
                , sKeys, sData);
    }

    private final void fnCheckSuccess(final Object objData) {
        final String sValue = ((HandlerData)objData).fnGetValue();
        if(sValue != null && sValue.length() > 0) {
            if(Boolean.valueOf(sValue)) {
                Toast.makeText(g_objContext, "評價成功", Toast.LENGTH_SHORT).show();
                fnUpdateSql();
                g_diaProduct.cancel();
            } else {
                Toast.makeText(g_objContext, "評價失敗", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(g_objContext, "評價失敗", Toast.LENGTH_SHORT).show();
        }
    }

    private final void fnUpdateSql() {
        final DOLL_evaluation dollEvaluation = new DOLL_evaluation(UserSharedPreferences.g_sAccount, g_sAddressNo, g_sStar, g_sMessage);
        final SQLHandler sqlHandler = new SQLHandler(g_objContext);
        DOLL_evaluation.fnInsert(sqlHandler, dollEvaluation);
    }

/*---------------------------------------------------------------------------------------------------結束新增評價-----------------------------------------------------------------------------------*/

    public final void fnShow() {
        g_diaProduct.show();
    }


    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.UPDATE_EVALUATION:
                    fnCheckSuccess(msg.obj);
                    break;
            }
        }
    };


}
