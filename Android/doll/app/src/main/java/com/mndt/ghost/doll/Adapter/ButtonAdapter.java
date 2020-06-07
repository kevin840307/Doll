package com.mndt.ghost.doll.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import com.mndt.ghost.doll.R;
/**
 * Created by user on 2017/2/10.
 */
public class ButtonAdapter extends BaseAdapter {
    private LayoutInflater g_InflaterLayout;
    private ButtonAdapterData g_taddData;
    private ImageView g_imgIcon;
    private TextView g_textName;
    public static int POS = 0;

    public ButtonAdapter(Context conText, final Integer[] iImage, final String[] sName) {
        g_InflaterLayout = LayoutInflater.from(conText);
        g_taddData = new ButtonAdapterData();
        g_taddData.g_alistImage =  new ArrayList(Arrays.asList(iImage));
        g_taddData.g_alistName= new ArrayList(Arrays.asList(sName));
    }
    @Override
    public int getCount() {
        return g_taddData.g_alistName.size();
    }

    @Override
    public Object getItem(int position) {
        return g_taddData.g_alistName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = g_InflaterLayout.inflate(R.layout.main_grid_item, null);
        fnInitControl(convertView);
        g_imgIcon.setImageResource(g_taddData.g_alistImage.get(position));
        g_textName.setText(g_taddData.g_alistName.get(position));
        fnSetColor(position);
        return convertView;
    }

    private final void fnSetColor(final int iPos) {
        if(POS == iPos) {
            g_textName.setTextColor(Color.WHITE);
        } else {
            g_textName.setTextColor(Color.BLACK);
        }
    }

    private final void fnInitControl(View convertView) {
        g_imgIcon = (ImageView) convertView.findViewById(R.id.img_grid_view);
        g_textName = (TextView) convertView.findViewById(R.id.text_grid_name);
    }
}
