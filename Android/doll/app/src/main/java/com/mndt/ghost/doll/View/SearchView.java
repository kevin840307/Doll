package com.mndt.ghost.doll.View;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.SQLHandler;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.SearchActivity;
import com.mndt.ghost.doll.SqlDB.DOLLshop;

import java.util.ArrayList;

/**
 * Created by Ghost on 2017/10/23.
 */
public class SearchView {
    private EditText g_editKeyWord = null;
    private Spinner g_spiArea = null;
    private Spinner g_spiLocation = null;
    private String g_sArea = "";
    private String g_sLocation = "全部";
    private View g_view = null;


    public final void fnInit(final View view) {
        g_view = view;
        if (g_view != null) {
            fnInitControl();
        }
    }

    private final void fnInitControl() {
        fnInitSpinner();
        fnInitEdit();
        fnInitButton();
        //fnInitImageButton();
    }



    private final void fnInitSpinner() {
        g_spiArea = (Spinner) g_view.findViewById(R.id.spir_area);
        g_spiLocation = (Spinner) g_view.findViewById(R.id.spir_location);
        final ArrayAdapter<String> adpAdapter2 = new ArrayAdapter(g_view.getContext(), android.R.layout.simple_list_item_1, Data.AREA_NAME);
        g_spiArea.setAdapter(adpAdapter2);
        g_spiArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                g_sArea = Data.AREA_NAME[position];
                fnSetLocation(Data.AREA_NAME[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private final void fnInitEdit() {
        g_editKeyWord = (EditText)g_view.findViewById(R.id.edit_key_word);
    }

    private final void fnInitButton() {
        final Button btnSearch = (Button) g_view.findViewById(R.id.btn_search_location);
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOpenSearch();
            }
        });
    }

//    private final void fnInitImageButton() {
//        final ImageButton ibtnFacebook = (ImageButton) g_view.findViewById(R.id.ibtn_facebook);
//        ibtnFacebook.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String sUrl = "";
//                try {
//                    g_view.getContext().getPackageManager().getPackageInfo("com.facebook.katana", 0);
//                    sUrl = "fb://page/120707168626004";
//                } catch (PackageManager.NameNotFoundException e) {
//                    sUrl="https://www.facebook.com/%E6%8A%93%E5%AF%B6%E9%BE%8D-120707168626004/";
//                }
//                final Intent itStart = new Intent(Intent.ACTION_VIEW, Uri.parse(sUrl));
//                g_view.getContext().startActivity(itStart);
//            }
//        });
//    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenSearch() {
        final Intent itStart = new Intent(g_view.getContext(), SearchActivity.class);
        itStart.putExtra("flag", "explode");
        itStart.putExtra("key_word", g_editKeyWord.getText().toString());
        itStart.putExtra("area", g_sArea);
        itStart.putExtra("location", g_sLocation);
        g_view.getContext().startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) g_view.getContext()).toBundle());
    }

    private final void fnSetLocation(final String sArea) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final SQLHandler sqlHandler = new SQLHandler(g_view.getContext());
                final ArrayList<String> alData = DOLLshop.fnSelectLocation(sqlHandler, sArea);
                alData.add(0, "全部");
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.GET_SQL_DATE1, alData);
            }
        }).start();
    }

    private final void fnUpdateListView(final ArrayList<String> alData) {
        if (alData.size() > 0) {
            final ArrayAdapter<String> adpAdapter = new ArrayAdapter(g_view.getContext(), android.R.layout.simple_list_item_1, alData.toArray());
            g_spiLocation.setAdapter(adpAdapter);

            g_spiLocation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    g_sLocation = alData.get(position);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.GET_SQL_DATE1:
                    fnUpdateListView((ArrayList<String>) msg.obj);
                    break;
            }
        }
    };
}
