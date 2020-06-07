package com.mndt.ghost.doll.ImageSelect;

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.LoginWaitActivity;
import com.mndt.ghost.doll.MainActivity;
import com.mndt.ghost.doll.R;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Ghost on 2017/11/5.
 */

public class ImageSelectActivity extends AppCompatActivity {
    public static final int MAX = 10;
    public static final int OK = 11;
    public static final int SELECT_IMAGE = 1;

    private final String TAG = "ImageSelectActivity";
    private static Map g_mapImageData = null;
    public static ArrayList<String> g_alSelectedPaths = null;
    private String g_sType = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.image_select_activity);
        fnInit();
    }
//-------------------------------------------------------------------初始化資料.介面----START---------------------------------------------------------------------------------------------------------------------

    private final void fnInit() {
        fnInitData();
        fnInitControl();
    }

    private final void fnInitData() {
        g_sType = getIntent().getStringExtra("type");
        if (g_sType.equals("1")) {
            g_alSelectedPaths = new ArrayList<>();
            fnSetImages();
        } else if (g_sType.equals("2")) {
            fnInitSelectGridView();
        }
    }

    private final void fnSetImages() {
        g_mapImageData = new HashMap();
        g_mapImageData.put("first", new LinkedList<String>());
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            Toast.makeText(this, "找不道SD卡", Toast.LENGTH_SHORT).show();
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                final Uri urlPath = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                // 只查询jpeg和png的图片
                final Cursor curSor = getContentResolver().query(urlPath, null,
                        MediaStore.Images.Media.MIME_TYPE + "=? or "
                                + MediaStore.Images.Media.MIME_TYPE + "=?",
                        new String[]{"image/jpeg", "image/png"},
                        MediaStore.Images.Media.DATE_MODIFIED);

                while (curSor.moveToNext()) {
                    final String sImagePath = curSor.getString(curSor.getColumnIndex(MediaStore.Images.Media.DATA));
                    final File fileParentFile = new File(sImagePath).getParentFile();
                    final String sParentPath = fileParentFile.getAbsolutePath();
                    if (g_mapImageData.get(sParentPath) == null) {
                        final LinkedList<String> linkPaths = new LinkedList<String>();
                        linkPaths.add(sImagePath);
                        g_mapImageData.put(sParentPath, linkPaths);
                        ((LinkedList) g_mapImageData.get("first")).add(sImagePath);
                    } else {
                        ((LinkedList) g_mapImageData.get(sParentPath)).add(sImagePath);
                    }

//                    int iImageSize = fileParentFile.list(new FilenameFilter() {
//                        @Override
//                        public boolean accept(File dir, String filename) {
//                            if (filename.endsWith(".jpg")
//                                    || filename.endsWith(".png")
//                                    || filename.endsWith(".jpeg"))
//                                return true;
//                            return false;
//                        }
//                    }).length;
                }
                curSor.close();
                fnSetTitle();
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_UI1);
            }
        }).start();

    }

    private final void fnSetTitle() {
        final LinkedList<String> linkFirstData = (LinkedList<String>) g_mapImageData.get("first");
        final LinkedList<String> linkTitleData = new LinkedList<String>();

        for (final String sPath : linkFirstData) {
            final File fileParentFile = new File(sPath).getParentFile();
            final String sParentPath = fileParentFile.getAbsolutePath();
            linkTitleData.add(fileParentFile.getName() + "(" + ((LinkedList) g_mapImageData.get(sParentPath)).size() + "張)");
        }
        g_mapImageData.put("title", linkTitleData);
    }

    private final void fnInitControl() {
        fnInitButton();
        fnInitImageButton();
    }

    private final void fnInitButton() {
        final Button btnSelectSend = (Button) findViewById(R.id.btn_imgage_select_true);

        btnSelectSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (g_sType.equals("1")) {
                    final Intent itSend = new Intent();
                    final Bundle bdData = new Bundle();
                    bdData.putStringArrayList("select", g_alSelectedPaths);
                    itSend.putExtras(bdData);
                    setResult(OK, itSend);
                }
                fnClose();
                finish();
            }
        });
    }

    private final void fnInitImageButton() {
        final ImageButton btnSelectClose = (ImageButton) findViewById(R.id.ibtn_back);

        btnSelectClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (g_sType.equals("1")) {
                    fnOpenDialog();
                } else {
                    finish();
                }
            }
        });
    }

    private final void fnInitParentGridView() {
        final GridView gvDatas = (GridView) findViewById(R.id.gv_image_show);
        final LinkedList<String> linkFirst = (LinkedList<String>) g_mapImageData.get("first");
        final LinkedList<String> linkTitle = (LinkedList<String>) g_mapImageData.get("title");
        gvDatas.setNumColumns(3);
        final ImageParentAdapter imageParentAdapter = new ImageParentAdapter(this, linkTitle, linkFirst);
        gvDatas.setAdapter(imageParentAdapter);
        gvDatas.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final File fileParentFile = new File(linkFirst.get(position)).getParentFile();
                final String sParentPath = fileParentFile.getAbsolutePath();
                final Intent itStart = new Intent(ImageSelectActivity.this, ImageSelectActivity.class);
                itStart.putExtra("type", "2");
                itStart.putExtra("parent", sParentPath);
                startActivity(itStart);
            }
        });
        fnStopLoding();
    }

    private final void fnInitSelectGridView() {
        final GridView gvDatas = (GridView) findViewById(R.id.gv_image_show);
        final String sParntPath = getIntent().getStringExtra("parent");
        final LinkedList<String> linkPaths = (LinkedList<String>) g_mapImageData.get(sParntPath);
        gvDatas.setNumColumns(3);
        final ImageSelectAdapter imageSelectAdapter = new ImageSelectAdapter(this, linkPaths);
        gvDatas.setAdapter(imageSelectAdapter);
        fnStopLoding();
    }

//-------------------------------------------------------------------初始化資料.介面----END---------------------------------------------------------------------------------------------------------------------

    private final void fnOpenDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(ImageSelectActivity.this)
                .setTitle("離開")
                .setMessage("確定離開嗎? (返回=無選圖片)")
                .setPositiveButton("離開", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        fnClose();
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
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
    }

    private final void fnClose() {
        if (g_sType.equals("1")) {
            g_mapImageData = null;
            g_alSelectedPaths = null;
            Log.d(TAG, "釋放Map");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        fnClose();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub
        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            if(g_sType.equals("1")) {
                fnOpenDialog();
            } else {
                finish();
            }
        }
        return true;
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.SET_UI1:
                    fnInitParentGridView();
                    break;
            }
        }
    };

}
