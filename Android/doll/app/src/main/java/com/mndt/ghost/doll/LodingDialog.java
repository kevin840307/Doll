package com.mndt.ghost.doll;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.mndt.ghost.doll.R;


/**
 * Created by Ghost on 2017/5/11.
 */
public class LodingDialog {
    private final String TAG = "LodingDialog";
    private Dialog g_diaProduct = null;
    private Context g_objContext = null;

    public LodingDialog(final Context objContext) {
        g_objContext = objContext;
        fnInit();
    }

    private final void fnInit() {
        fnInitUIControl();
    }


    private final void fnInitUIControl() {
        fnInitDiaLog();
    }

    private final void fnInitDiaLog() {
        g_diaProduct = new Dialog(g_objContext, R.style.Dialog_style3);
        g_diaProduct.setContentView(R.layout.loding_dialog);
        final Window dialogWindow = g_diaProduct.getWindow();
        final WindowManager.LayoutParams layParams = dialogWindow.getAttributes();
        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
        dialogWindow.setGravity(Gravity.CENTER);
//        layParams.width = (int) ((double) Data.WIDTH_PIXELS / 1.4); // 寬度
//        layParams.height = (int) ((double) Data.HEIGHT_PIXELS / 1.6); // 高度
        layParams.alpha = 1f; // 透明度
        g_diaProduct.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {

            }
        });
    }

    public final void fnClose() {
        g_diaProduct.setCancelable(true);
        g_diaProduct.cancel();
    }

    public final void fnShow() {
        g_diaProduct.setCancelable(false);
        g_diaProduct.show();
    }
}
