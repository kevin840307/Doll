package com.mndt.ghost.doll.Evaluation;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mndt.ghost.doll.Forum.RespondData;
import com.mndt.ghost.doll.Image.ImageLruCache;
import com.mndt.ghost.doll.Image.RImageUrlAsyncs;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.RoundImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ghost on 2017/11/7.
 */

public class EvaluationAdater extends BaseAdapter {
    private int g_iProcess = -1;
    private final String TAG = "EvaluationAdater";
    private ImageLruCache g_ilruSaveBitmap = null;
    private Set<RImageUrlAsyncs> g_setImgAsync = null;
    private Context g_conText = null;
    private LayoutInflater g_InflaterLayout = null;
    private ArrayList<EvaluationData> g_alDatas = null;

    public EvaluationAdater(final Context conText) {
        g_ilruSaveBitmap = new ImageLruCache();
        g_setImgAsync = new HashSet<>();
        g_conText = conText;
        g_InflaterLayout = LayoutInflater.from(conText);
        g_alDatas = new ArrayList<>();
    }

    public EvaluationAdater(final Context conText, final ArrayList<EvaluationData> alDatas) {
        g_ilruSaveBitmap = new ImageLruCache();
        g_setImgAsync = new HashSet<>();
        g_conText = conText;
        g_InflaterLayout = LayoutInflater.from(conText);
        g_alDatas = alDatas;
    }

    @Override
    public int getCount() {
        return g_alDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return g_alDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = g_InflaterLayout.inflate(R.layout.list_item_style4, null);
        final EvaluationData evaData = (EvaluationData) getItem(position);
        final RoundImageView rimagIcon1 = (RoundImageView) convertView.findViewById(R.id.img_list_icon);
        final TextView textMessage = (TextView) convertView.findViewById(R.id.text_list_content);
        final TextView textName = (TextView) convertView.findViewById(R.id.text_list_name);
        final TextView textDate = (TextView) convertView.findViewById(R.id.text_list_date);
        final RatingBar ratingStar = (RatingBar)convertView.findViewById(R.id.rating_list_star);
        textMessage.setText(evaData.fnGetMessage());
        textName.setText(evaData.fnGetName());
        textDate.setText(evaData.fnGetDate());
        ratingStar.setMax(10);
        ratingStar.setProgress(evaData.fnGetStar());
        rimagIcon1.setTag(evaData.fnGetURL());
        fnSetThemeImage(rimagIcon1, evaData.fnGetURL(), position);
        final Animation animation = AnimationUtils.loadAnimation(g_conText, R.anim.layout_item_anim_style2);
        convertView.setAnimation(animation);
        return convertView;
    }

    private final void fnSetThemeImage(final RoundImageView rimagView, final String sUrl, final int iPos) {
        final Bitmap btData = g_ilruSaveBitmap.fnGetBitmap(sUrl);
        if (btData == null) {
            if (iPos > g_iProcess) {
                fnRunThemeImage(rimagView, sUrl);
                g_iProcess += 1;
            }
        } else {
            rimagView.setImageBitmap(btData);
        }
    }

    private final void fnRunThemeImage(final RoundImageView rimagView, final String sUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RImageUrlAsyncs imgSetAsyncs = new RImageUrlAsyncs(rimagView);
                g_setImgAsync.add(imgSetAsyncs);
                try {
                    g_ilruSaveBitmap.fnAddBitmap(sUrl, imgSetAsyncs.execute(sUrl).get());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }


    public final void fnAdd(final EvaluationData evaData) {
        g_alDatas.add(evaData);
    }

    public final void fnCancelAsync() {
        for (final RImageUrlAsyncs imgAsyn : g_setImgAsync) {
            imgAsyn.cancel(false);
        }
    }
}
