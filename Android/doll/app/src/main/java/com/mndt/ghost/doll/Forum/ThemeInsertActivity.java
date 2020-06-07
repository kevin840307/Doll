package com.mndt.ghost.doll.Forum;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Handler.HandlerAction;
import com.mndt.ghost.doll.Handler.HandlerData;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.WebHandler;
import com.mndt.ghost.doll.Image.ImageAction;
import com.mndt.ghost.doll.Image.RImageUrlAsyncs;
import com.mndt.ghost.doll.Image.ShowImageActivity;
import com.mndt.ghost.doll.Image.URLImageData;
import com.mndt.ghost.doll.ImageSelect.ImageSelectActivity;
import com.mndt.ghost.doll.LodingDialog;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.RoundImageView;
import com.mndt.ghost.doll.UserSharedPreferences;

import java.util.ArrayList;

/**
 * Created by Ghost on 2017/11/5.
 */

public class ThemeInsertActivity extends AppCompatActivity {
    private LodingDialog g_loadDialog = null;
    private int g_iUpdateCount = 0;
    private final String TAG = "ThemeInsertActivity";
    public final static String INSERT = "INSERT";
    public final static String EDIT = "EDIT";
    private String g_sType = "INSERT";
    private int g_iPlateId = 0;
    private ThemeData g_thData = null;
    private ImageAdater g_imgAdater = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.theme_insert_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitData();
        fnInitControl();
    }

    private final void fnInitData() {
        fnStartLoding();
        g_sType = getIntent().getStringExtra("type");
        g_iPlateId =Integer.valueOf(getIntent().getStringExtra("plate_id"));
        if (g_sType.equals(EDIT)) {
            g_thData = (ThemeData) getIntent().getSerializableExtra("ThemeData");
            fnDowloadImage();
        }
    }

    private final void fnDowloadImage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ArrayList<String> alDatas = new ArrayList<>();
                final String sDirName = "Theme";
                final String sParentDir = String.valueOf(g_thData.fnGetPlateId()) + String.valueOf(g_thData.fnGetThemeId());
                for (int iIndex = 0; iIndex < g_thData.fnGetPicAmount(); iIndex++) {
                    final String sDataName = String.valueOf(iIndex) + ".jpg";
                    final String sURL = Data.SERRVICE_URL + sDirName + "/" + sParentDir + "/" + sDataName;
                    Log.d(TAG, sURL);
                    final URLImageData urlImageData = new URLImageData(sURL, sDirName, sDataName);
                    ImageAction.fnDownloadImage(urlImageData, true);
                    alDatas.add(urlImageData.fnGetSDDataPath());
                }
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_UI1, alDatas);
            }
        }).start();
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnInitButton();
        fnInitSpinner();
        fnInitImageButton();
        fnImageView();
        fnInitTextView();
        fnInitEditText();
        if (g_sType.equals(INSERT)) {
            fnStopLoding();
        }
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnInitButton() {
        final Button btnSelectImg = (Button) findViewById(R.id.btn_theme_i_select_img);
        final Button brnTrue = (Button) findViewById(R.id.btn_theme_i_true);
        btnSelectImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOpenSelectImage();
            }
        });

        brnTrue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOpenSendDialog();
            }
        });
    }

    private final void fnInitGridView(final ArrayList<String> alPaths) {
        final GridView gvDatas = (GridView) findViewById(R.id.gv_theme_i_show_img);
        final LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(Data.WIDTH_PIXELS / 4 * alPaths.size(), LinearLayout.LayoutParams.MATCH_PARENT);
        g_imgAdater = new ImageAdater(this, alPaths);
        gvDatas.setLayoutParams(params);
        gvDatas.setNumColumns(alPaths.size());
        gvDatas.setAdapter(g_imgAdater);
        gvDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Intent itStart = new Intent(ThemeInsertActivity.this, ShowImageActivity.class);
                itStart.putExtra("type", 1);
                itStart.putExtra("index", position);
                itStart.putExtra("paths", alPaths);
                itStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplication().startActivity(itStart);
            }
        });
        fnStopLoding();
    }

    private final void fnInitSpinner() {
        final Spinner spirArea = (Spinner) findViewById(R.id.spir_area);
        final Spinner spirPlate = (Spinner) findViewById(R.id.spir_plate);
        final Spinner spirFormat = (Spinner)findViewById(R.id.spir_theme_format);
        ArrayAdapter<String> adpAdapter1 = null;
        final ArrayAdapter<String> adpAdapter0 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ThemeData.g_sFormat);
        if (g_sType.equals(EDIT)) {
            adpAdapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ThemeData.g_sPlate);
        } else {
            adpAdapter1 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ThemeData.g_sUserPlate);
        }
        final ArrayAdapter<String> adpAdapter2 = new ArrayAdapter(this, android.R.layout.simple_list_item_1, ThemeData.g_sArea);
        spirPlate.setAdapter(adpAdapter1);
        spirPlate.setSelection(g_iPlateId - 1);
        spirArea.setAdapter(adpAdapter2);
        if (g_sType.equals(EDIT)) {
            spirPlate.setSelection(g_thData.fnGetPlateId());
            spirPlate.setEnabled(false);
            spirArea.setSelection(fnGetTitleArea(g_thData.fnGetTitle().substring(0, 4)));
        }

        spirFormat.setAdapter(adpAdapter0);
        spirFormat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                final EditText editContent = (EditText) findViewById(R.id.edit_theme_i_content);
                editContent.setText(ThemeData.g_sFormatContent[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private final void fnInitImageButton() {
        final ImageButton imgBack = (ImageButton) findViewById(R.id.ibtn_theme_i_back);
        imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOpenFinishDialog();
            }
        });
    }

    private final void fnImageView() {
        final RoundImageView rimgUser = (RoundImageView) findViewById(R.id.rimg_theme_i_user);
        new RImageUrlAsyncs(rimgUser).execute(ImageAction.fnGetUserURL(UserSharedPreferences.g_sAccount));
    }

    private final void fnInitTextView() {
        final TextView textName = (TextView) findViewById(R.id.text_theme_i_name);
        textName.setText(UserSharedPreferences.g_sName);
    }

    private final void fnInitEditText() {
        final EditText editTitle = (EditText) findViewById(R.id.edit_theme_i_title);
        final EditText editContent = (EditText) findViewById(R.id.edit_theme_i_content);

        if (g_sType.equals(EDIT)) {
            editTitle.setText(g_thData.fnGetTitle().substring(4));
            editContent.setText(g_thData.fnGetContent());
        }
    }

    private final void fnRunInsertTheme() {
        final EditText editTitle = (EditText) findViewById(R.id.edit_theme_i_title);
        final EditText editContent = (EditText) findViewById(R.id.edit_theme_i_content);
        final Spinner spirArea = (Spinner) findViewById(R.id.spir_area);
        final Spinner spirPlate = (Spinner) findViewById(R.id.spir_plate);
        String sTitle = editTitle.getText().toString();
        String sContent = editContent.getText().toString();
        if (sTitle.length() == 0) {
            Toast.makeText(this, "請輸入標題", Toast.LENGTH_SHORT).show();
            return;
        } else if (sContent.length() == 0) {
            Toast.makeText(this, "請輸入內容", Toast.LENGTH_SHORT).show();
            return;
        }
        fnStartLoding();
        final String sPlateId = String.valueOf(spirPlate.getSelectedItemPosition() + 1);
        String sPicAmount = "0";
        if (g_imgAdater != null) sPicAmount = String.valueOf(g_imgAdater.getCount());
        sTitle = ThemeData.g_sArea[spirArea.getSelectedItemPosition()] + sTitle;
        final String sKeys[] = {"sIMEI", "sAccount", "sPwd", "sPlateId", "sTitle", "sContent", "sPicAmount"};
        final String sData[] = {UserSharedPreferences.g_sIMEI, UserSharedPreferences.g_sAccount, UserSharedPreferences.g_sPwd, sPlateId, sTitle, sContent, sPicAmount};
        new HandlerAction(g_hdMessage).fnRunThread(WebHandler.INSERT_THEME_DATE_FUNTION
                , HandlerMessage.INSERT_UPDATE_THEME_DATE
                , HandlerData.VALUE_MESSAGE
                , sKeys, sData);

    }

    private final void fnRunUpdateTheme() {
        final EditText editTitle = (EditText) findViewById(R.id.edit_theme_i_title);
        final EditText editContent = (EditText) findViewById(R.id.edit_theme_i_content);
        final Spinner spirArea = (Spinner) findViewById(R.id.spir_area);
        final Spinner spirPlate = (Spinner) findViewById(R.id.spir_plate);
        String sTitle = editTitle.getText().toString();
        String sContent = editContent.getText().toString();
        if (sTitle.length() == 0) {
            Toast.makeText(this, "請輸入標題", Toast.LENGTH_SHORT).show();
            return;
        } else if (sContent.length() == 0) {
            Toast.makeText(this, "請輸入內容", Toast.LENGTH_SHORT).show();
            return;
        }
        fnStartLoding();
        final String sKeys[] = {"sIMEI", "sAccount", "sPwd", "sPlateId", "sThemeId", "sTitle", "sContent", "sPicAmount"};
        final String sPlateId = String.valueOf(g_thData.fnGetPlateId());
        final String sThemeId = String.valueOf(g_thData.fnGetThemeId());
        final String sPicAmount = String.valueOf(g_imgAdater.getCount());
        sTitle = ThemeData.g_sArea[spirArea.getSelectedItemPosition()] + sTitle;
        final String sData[] = {UserSharedPreferences.g_sIMEI, UserSharedPreferences.g_sAccount, UserSharedPreferences.g_sPwd, sPlateId, sThemeId, sTitle, sContent, sPicAmount};
        new HandlerAction(g_hdMessage).fnRunThread(WebHandler.UPDATE_THEME_DATE_FUNTION
                , HandlerMessage.INSERT_UPDATE_THEME_DATE
                , HandlerData.VALUE_MESSAGE
                , sKeys, sData);

    }

    private final void fnCheckInsert(final Object objData) {
        final HandlerData msgData = (HandlerData) objData;
        if (msgData != null) {
            final String sTheme = msgData.fnGetValue();
            if (!sTheme.equals("N")) {
                if (g_imgAdater != null && g_imgAdater.getCount() > 0) {
                    int iOffset = 1;
                    if(g_sType.equals(EDIT)) {
                        iOffset = 0;
                    }
                    final Spinner spirPlate = (Spinner) findViewById(R.id.spir_plate);
                    final String sPlate = String.valueOf(spirPlate.getSelectedItemPosition() + iOffset);
                    fnUpdateImage(sPlate, sTheme);
                } else {
                    fnStopLoding();
                    Toast.makeText(this, "發佈成功", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    }

    private final void fnCheckImage(final Object objData) {
        final HandlerData msgData = (HandlerData) objData;
        if (msgData != null) {
            final Boolean bCheck = Boolean.valueOf(msgData.fnGetValue());
            if (bCheck) {
                g_iUpdateCount++;
            }
            if (g_iUpdateCount == g_imgAdater.getCount()) {
                fnStopLoding();
                Toast.makeText(this, "發佈成功", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    private final void fnUpdateImage(final String sPlate, final String sTheme) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                g_iUpdateCount = 0;
                for (int iIndex = 0; iIndex < g_imgAdater.getCount(); iIndex++) {
                    final String sSDPath = g_imgAdater.getItem(iIndex).toString();
                    final String sBase64 = ImageAction.fnGetBase64(ImageAction.fnGetBitmap(sSDPath, true), 17, true);
                    final String sKeys[] = {"sPlateId", "sThemeId", "sStrBase64", "sIndex"};
                    final String sData[] = {sPlate, sTheme, sBase64, String.valueOf(iIndex)};
                    new HandlerAction(g_hdMessage).fnRunThread(WebHandler.UPDATE_THEME_IMAGE_FUNTION
                            , HandlerMessage.UPDATE_THEME_IMAGE
                            , HandlerData.VALUE_MESSAGE
                            , sKeys, sData);
                }
            }
        }).start();
    }

    private final int fnGetTitleArea(final String sTitle) {
        Log.d(TAG, sTitle);
        if (sTitle.equals("【北部】")) {
            return 0;
        } else if (sTitle.equals("【中部】")) {
            return 1;
        } else if (sTitle.equals("【南部】")) {
            return 2;
        } else if (sTitle.equals("【東部】")) {
            return 3;
        } else {
            return 0;
        }

    }

    private final void fnOpenSelectImage() {
        final Intent itStart = new Intent(ThemeInsertActivity.this, ImageSelectActivity.class);
        itStart.putExtra("type", "1");
        startActivityForResult(itStart, ImageSelectActivity.SELECT_IMAGE);
    }

    private final void fnOpenFinishDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ThemeInsertActivity.this)
                .setTitle("離開")
                .setMessage("確定離開嗎?")
                .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    private final void fnOpenSendDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ThemeInsertActivity.this)
                .setTitle("發布")
                .setMessage("確定要發布嗎?")
                .setPositiveButton("發布", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (g_sType.equals(INSERT)) {
                            fnRunInsertTheme();
                        } else if (g_sType.equals(EDIT)) {
                            fnRunUpdateTheme();
                        }
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != ImageSelectActivity.OK) return;
        switch (requestCode) {
            case ImageSelectActivity.SELECT_IMAGE:
                fnStartLoding();
                final ArrayList<String> alPaths = data.getStringArrayListExtra("select");
                fnInitGridView(alPaths);
                Log.d(TAG, "已選數量:" + alPaths.size());
                break;
        }
    }

    private final void fnStartLoding() {
        g_loadDialog = new LodingDialog(this);
        g_loadDialog.fnShow();
    }

    private final void fnStopLoding() {
        g_loadDialog.fnClose();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            fnOpenFinishDialog();
        }
        return true;
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
                case HandlerMessage.INSERT_UPDATE_THEME_DATE:
                    fnCheckInsert(msg.obj);
                    break;
                case HandlerMessage.UPDATE_THEME_IMAGE:
                    fnCheckImage(msg.obj);
                    break;
                case HandlerMessage.SET_UI1:
                    fnInitGridView((ArrayList<String>) msg.obj);
                    break;
            }
        }
    };
}
