package com.mndt.ghost.doll.Image;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Explode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Forum.ImageAdater;
import com.mndt.ghost.doll.R;

import java.util.ArrayList;

/**
 * Created by Ghost on 2017/11/5.
 */

public class ShowImageActivity  extends AppCompatActivity {
    private final String TAG = "ThemeInsertActivity";
    private int g_iType = 0;
    private int g_iIndex = -1;
    private ArrayList<String> g_alPaths = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.show_imge_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitData();
        fnInitControl();
    }

    private final void fnInitData() {
        g_iType= getIntent().getIntExtra("type", 0);
        g_alPaths = getIntent().getStringArrayListExtra("paths");
        g_iIndex = getIntent().getIntExtra("index", 0);
    }

    private final void fnInitControl() {
        fnInitImageView();
        fnInitImageButton();
    }

    private final void fnInitImageView() {
        final ImageView imgView = (ImageView)findViewById(R.id.img_show_image);
        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.layout_item_anim_style2);
        imgView.setAnimation(animation);
        switch (g_iType) {
            case 1:
                new ImageSDAsyncs(imgView).execute(g_alPaths.get(g_iIndex));
                break;
            case 2:
                new ImageUrlAsyncs(imgView).execute(g_alPaths.get(g_iIndex));
                break;
        }
    }

    private final void fnInitImageButton() {
        final ImageButton ibtnLast = (ImageButton)findViewById(R.id.ibtn_last_data);
        final ImageButton ibtnNext = (ImageButton)findViewById(R.id.ibtn_next_data);

        ibtnLast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(g_iIndex > 0) {
                    g_iIndex--;
                    fnInitImageView();
                } else {
                    Toast.makeText(getApplicationContext(), "已經是第一筆了", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(g_iIndex < g_alPaths.size() - 1) {
                    g_iIndex++;
                    fnInitImageView();
                } else {
                    Toast.makeText(getApplicationContext(), "已經是最後一筆了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }



    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().setEnterTransition(new Explode());
            getWindow().setExitTransition(new Explode());
        }
    }
}
