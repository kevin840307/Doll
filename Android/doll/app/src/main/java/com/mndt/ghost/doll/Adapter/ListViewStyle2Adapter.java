package com.mndt.ghost.doll.Adapter;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import com.mndt.ghost.doll.Map.WebMapActivity;
import com.mndt.ghost.doll.R;

import java.util.ArrayList;

/**
 * Created by Ghost on 2017/10/23.
 */
public class ListViewStyle2Adapter extends BaseAdapter {
    private final String TAG = "ListViewStyle2Adapter";
    private Context g_conText = null;
    private int g_iImageId = 0;
    private LayoutInflater g_InflaterLayout = null;
    private ArrayList<ArrayList<String>> g_alDatas= null;


    public ListViewStyle2Adapter(final Context conText, final int iImageId, final ArrayList<ArrayList<String>> alDatas) {
        g_conText = conText;
        g_InflaterLayout = LayoutInflater.from(conText);
        g_iImageId = iImageId;
        g_alDatas= alDatas;
    }



    @Override
    public int getCount() {
        return g_alDatas.get(0).size();
    }

    @Override
    public Object getItem(int position) {
        return g_alDatas.get(0).get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        convertView = g_InflaterLayout.inflate(R.layout.list_item_style2, null);

        final ImageView imgIcon1 = (ImageView) convertView.findViewById(R.id.img_list_icon1);
        final ImageView imgIcon2 = (ImageView) convertView.findViewById(R.id.img_list_icon2);
        final TextView textName = (TextView) convertView.findViewById(R.id.text_list_name);
        final TextView textAdd = (TextView) convertView.findViewById(R.id.text_list_add);
        final ImageButton ibtnGPS = (ImageButton) convertView.findViewById(R.id.ibtn_gps);
        final String sUrl = "https://www.google.com.tw/maps/place/" + g_alDatas.get(4).get(position);
        textName.setText(g_alDatas.get(3).get(position));
        textAdd.setText(g_alDatas.get(4).get(position));
        imgIcon1.setImageResource(g_iImageId);
        new SetSDImageAsyncs(imgIcon2).execute(g_alDatas.get(0).get(position));
//        final String sImageName = "shop_" + g_alDatas.get(0).get(position);
//        final int iImageId = g_conText.getResources().getIdentifier(sImageName, "drawable", g_conText.getPackageName()) ;
//        if(iImageId > 0) {
//            imgIcon2.setImageResource(iImageId);
//        }

        ibtnGPS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOpenGPSMap(g_alDatas.get(3).get(position), g_alDatas.get(4).get(position), g_alDatas.get(6).get(position), g_alDatas.get(7).get(position));
            }
        });
        return convertView;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private final void fnOpenGPSMap(final String sShopName, final String sShopAdd, final String sLat, final String sLon) {
        final Intent itStart = new Intent(g_conText, WebMapActivity.class);
        final Bundle bdData = new Bundle();
        bdData.putString("shop", sShopName);
        bdData.putString("add", sShopAdd);
        bdData.putString("lat", sLat);
        bdData.putString("lon", sLon);
        bdData.putInt("type", 0);
        itStart.putExtras(bdData);
        g_conText.startActivity(itStart, ActivityOptionsCompat.makeSceneTransitionAnimation((Activity) g_conText).toBundle());
    }
}
