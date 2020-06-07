package com.mndt.ghost.doll.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Image.ImageLruCache;
import com.mndt.ghost.doll.Image.RImageUrlAsyncs;
import com.mndt.ghost.doll.Image.RImageUrlDowloadAsyncs;
import com.mndt.ghost.doll.Image.URLImageData;
import com.mndt.ghost.doll.Message.MessageData;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.RoundImageView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ghost on 2017/10/31.
 */
public class MessageAdapter1 extends BaseAdapter {

    private Context g_conText = null;
    private LayoutInflater g_InflaterLayout = null;
    private ArrayList<MessageData> g_alDatas = null;
    private static ImageLruCache g_ilruSaveBitmap = null;
    private Set<RImageUrlDowloadAsyncs> g_setImgAsync = null;
    private int g_iIndex = 0;

    public MessageAdapter1(final Context conText) {
        if(g_ilruSaveBitmap == null) {
            g_ilruSaveBitmap = new ImageLruCache();
        }
        g_setImgAsync = new HashSet<>();
        g_conText = conText;
        g_InflaterLayout = LayoutInflater.from(conText);
        g_alDatas = new ArrayList<MessageData>();
    }

    public MessageAdapter1(final Context conText, final ArrayList<MessageData> alDatas) {
        if(g_ilruSaveBitmap == null) {
            g_ilruSaveBitmap = new ImageLruCache();
        }
        g_iIndex = alDatas.size();
        g_setImgAsync = new HashSet<>();
        g_conText = conText;
        g_InflaterLayout = LayoutInflater.from(conText);
        g_alDatas = alDatas;
    }

    public final void fnAdd(final MessageData msgData) {
        g_alDatas.add(msgData);
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
        final MessageData msgData = g_alDatas.get(position);
        switch (msgData.fnGetType()) {
            case MessageData.LEFT_MSG:
                convertView = g_InflaterLayout.inflate(R.layout.listview_item_left, null);
                fnInitUserMsg(position, convertView);
                break;
            case MessageData.RIGHT_MSG:
                convertView = g_InflaterLayout.inflate(R.layout.listview_item_right, null);
                fnInitUserMsg(position, convertView);
                break;
            case MessageData.CENTER_MSG:
                convertView = g_InflaterLayout.inflate(R.layout.listview_item_center, null);
                fnInitServerMsg(position, convertView);
                break;
        }
        if (g_iIndex == position) {
            final Animation animation = AnimationUtils.loadAnimation(g_conText, R.anim.layout_item_anim_style2);
            convertView.setAnimation(animation);
            g_iIndex++;
        }
        return convertView;
    }

    private final void fnInitUserMsg(final int position, final View convertView) {
        final MessageData msgData = g_alDatas.get(position);
        final TextView textMassge = (TextView) convertView.findViewById(R.id.text_msg_input);
        final TextView textName = (TextView) convertView.findViewById(R.id.text_msg_name);
        final RoundImageView rimagView = (RoundImageView) convertView.findViewById(R.id.img_msg_person);
        textName.setText(msgData.fnGetName());
        textMassge.setText(msgData.fnGetMessage());
        final String sDirName = "UserImage";
        final String sDataName = g_alDatas.get(position).fnGetAccount() + ".jpg";
        final String sURL = Data.SERRVICE_URL + sDirName + "/" + sDataName;
        final URLImageData urlImageData = new URLImageData(sURL, sDirName, sDataName);
        fnSetThemeImage(rimagView, urlImageData, position);
        //new RImageUrlDowloadAsyncs(rimagView).execute(urlImageData);
    }

    private final void fnSetThemeImage(final RoundImageView rimagView, final URLImageData urlImageData, final int iPos) {
        final Bitmap btData = g_ilruSaveBitmap.fnGetBitmap(urlImageData.fnGetURL());
        if (btData == null) {
            if (g_iIndex == iPos) {
                fnRunSetImage(rimagView, urlImageData);
            }
        } else {
            rimagView.setImageBitmap(btData);
        }
    }

    private final void fnRunSetImage(final RoundImageView rimagView, final URLImageData urlImageData) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final RImageUrlDowloadAsyncs imgSetAsyncs = new RImageUrlDowloadAsyncs(rimagView);
                g_setImgAsync.add(imgSetAsyncs);
                try {
                    g_ilruSaveBitmap.fnAddBitmap(urlImageData.fnGetURL(), imgSetAsyncs.execute(urlImageData).get());
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

    private final void fnInitServerMsg(final int position, final View convertView) {
        final MessageData msgData = g_alDatas.get(position);
        final TextView textMassge = (TextView) convertView.findViewById(R.id.text_msg_server);
        textMassge.setText(msgData.fnGetMessage());
    }

    public final void fnCancelAsync() {
        for (final RImageUrlDowloadAsyncs imgAsyn : g_setImgAsync) {
            imgAsyn.cancel(false);
        }
    }
}
