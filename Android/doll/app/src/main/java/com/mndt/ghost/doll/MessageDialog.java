package com.mndt.ghost.doll;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.widget.ImageButton;

import android.widget.TextView;

import com.mndt.ghost.doll.Handler.HandlerMessage;

import java.lang.reflect.Field;


/**
 * Created by Ghost on 2017/5/11.
 */
public class MessageDialog {
    private final String TAG = "ProductDialog";
    private Dialog g_diaProduct = null;
    private Activity g_objContext = null;
    private TextView g_textMsg = null;

    public MessageDialog(final Activity objContext) {
        g_objContext = objContext;
        fnInit();
    }

    private final void fnInit() {
        fnInitUIControl();
    }


    private final void fnInitUIControl() {
        fnInitDiaLog();
        fnInitImageButton();
        fnInitTextView();
    }

    private final void fnInitDiaLog() {
        g_diaProduct = new Dialog(g_objContext, R.style.Dialog);
        g_diaProduct.setContentView(R.layout.message_dialog);
        final Window dialogWindow = g_diaProduct.getWindow();
        final WindowManager.LayoutParams layParams = dialogWindow.getAttributes();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        dialogWindow.setGravity(Gravity.CENTER);
        layParams.width = (int) ((double) Data.WIDTH_PIXELS / 1.4); // 寬度
        layParams.height = (int) ((double) Data.HEIGHT_PIXELS / 1.6); // 高度
        layParams.alpha = 1f; // 透明度
        g_diaProduct.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
    }

    private final void fnInitImageButton() {
        final ImageButton ibtnClose = (ImageButton) g_diaProduct.findViewById(R.id.ibtn_close);
        ibtnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                g_diaProduct.cancel();
            }
        });
    }

    private final void fnInitTextView() {
        g_textMsg = (TextView) g_diaProduct.findViewById(R.id.text_message);
    }


    public final void fnShow(final String sMsg) {
        g_textMsg.setText(sMsg);
        g_diaProduct.show();
    }



    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.GET_VERTION_MSG:
                    fnShow(msg.obj.toString());
                    break;
            }
        }
    };

}
