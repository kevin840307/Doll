package com.mndt.ghost.doll.ImageSelect;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Image.ImageLruCache;
import com.mndt.ghost.doll.Image.ImageSDAsyncs;
import com.mndt.ghost.doll.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ghost on 2017/11/5.
 */

public class ImageSelectAdapter extends BaseAdapter {
    private final String TAG = "ImageSelectAdapter";
    private Context g_conText = null;
    private LayoutInflater g_InflaterLayout = null;
    private LinkedList<String> g_linkDatas = null;
    private ImageLruCache g_ilruSaveBitmap = null;


    public ImageSelectAdapter(final Context conText, final LinkedList<String> linkPath) {
        g_conText = conText;
        g_InflaterLayout = LayoutInflater.from(conText);
        g_linkDatas = linkPath;
        g_ilruSaveBitmap = new ImageLruCache();
    }

    @Override
    public int getCount() {
        return g_linkDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return g_linkDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        convertView = g_InflaterLayout.inflate(R.layout.select_image_item2, null);
        final ImageView imgView = (ImageView)convertView.findViewById(R.id.img_select_img_show);
        final CheckBox chSelected = (CheckBox)convertView.findViewById(R.id.ch_image_select);
        if(ImageSelectActivity.g_alSelectedPaths.indexOf(g_linkDatas.get(position).toString()) >= 0) {
            chSelected.setChecked(true);
        }
        imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!chSelected.isChecked()) {
                    ImageSelectActivity.g_alSelectedPaths.add(g_linkDatas.get(position).toString());
                    Log.d(TAG, "新增");
                } else {
                    ImageSelectActivity.g_alSelectedPaths.remove(g_linkDatas.get(position).toString());
                    Log.d(TAG, "移除");
                }
                chSelected.setChecked(!chSelected.isChecked());
            }
        });
        fnSetImage(imgView, g_linkDatas.get(position));
        return convertView;
    }

    private final void fnSetImage(final ImageView imagView, final String sUrl) {
        if(g_ilruSaveBitmap.fnGetBitmap(sUrl) == null) {
            fnRunGetImage(imagView, sUrl);
        } else {
            imagView.setImageBitmap(g_ilruSaveBitmap.fnGetBitmap(sUrl));
        }
    }

    private final void fnRunGetImage(final ImageView imagView, final String sUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ImageSDAsyncs imgSetAsyncs = new ImageSDAsyncs(imagView, Data.WIDTH_PIXELS / 4);
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
}
