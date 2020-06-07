package com.mndt.ghost.doll.Forum;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.Image.IFImageAsyncs;
import com.mndt.ghost.doll.Image.ImageLruCache;
import com.mndt.ghost.doll.Image.ImageSDAsyncs;
import com.mndt.ghost.doll.Image.ImageUrlAsyncs;
import com.mndt.ghost.doll.Image.RImageUrlAsyncs;
import com.mndt.ghost.doll.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ghost on 2017/11/5.
 */

public class ImageAdater extends BaseAdapter {

    public final static String URL = "URL";
    public final static String SD = "SD";

    private final String TAG = "ImageParentAdapter";
    private Context g_conText = null;
    private LayoutInflater g_InflaterLayout = null;
    private ArrayList<String> g_alDatas = null;
    private ImageLruCache g_ilruSaveBitmap = null;
    private String g_sType = "";
    private Set<IFImageAsyncs> g_setImgAsync = null;


    public ImageAdater(final Context conText, final ArrayList<String> linkPath) {
        g_setImgAsync = new HashSet<>();
        g_conText = conText;
        g_InflaterLayout = LayoutInflater.from(conText);
        g_alDatas = linkPath;
        g_ilruSaveBitmap = new ImageLruCache();
        g_sType = SD;
    }

    public ImageAdater(final Context conText, final ArrayList<String> linkPath, final String sType) {
        g_setImgAsync = new HashSet<>();
        g_conText = conText;
        g_InflaterLayout = LayoutInflater.from(conText);
        g_alDatas = linkPath;
        g_ilruSaveBitmap = new ImageLruCache();
        g_sType = sType;
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
        convertView = g_InflaterLayout.inflate(R.layout.select_image_item3, null);
        final ImageView imgView = (ImageView)convertView.findViewById(R.id.img_select_img_show);
        fnSetImage(imgView, g_alDatas.get(position));
        return convertView;
    }

    public final void fnCancelAsync() {
        for (final IFImageAsyncs imgAsyn : g_setImgAsync) {
            imgAsyn.fnCancel(false);
            Log.d(TAG, "Close IFImageAsyncs");
        }
    }

    private final void fnSetImage(final ImageView imagView, final String sUrl) {
        if(g_ilruSaveBitmap.fnGetBitmap(sUrl) == null) {
            if(g_sType.equals(URL)) {
                fnRunUrlSDImage(imagView, sUrl);
            } else if(g_sType.equals(SD)) {
                fnRunGetSDImage(imagView, sUrl);
            }
        } else {
            imagView.setImageBitmap(g_ilruSaveBitmap.fnGetBitmap(sUrl));
        }
    }

    private final void fnRunGetSDImage(final ImageView imagView, final String sUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ImageSDAsyncs imgSetAsyncs = new ImageSDAsyncs(imagView, Data.WIDTH_PIXELS / 4);
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

    private final void fnRunUrlSDImage(final ImageView imagView, final String sUrl) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final ImageUrlAsyncs imgSetAsyncs = new ImageUrlAsyncs(imagView, Data.WIDTH_PIXELS / 4);
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
}

