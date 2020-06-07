package com.mndt.ghost.doll.Forum;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mndt.ghost.doll.Image.ImageLruCache;
import com.mndt.ghost.doll.Image.RImageUrlAsyncs;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.RoundImageView;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.ExecutionException;

/**
 * Created by Ghost on 2017/11/3.
 */

public class ThemeAdater extends BaseAdapter {
    private int g_iProcess = -1;
    private final String TAG = "ThemeAdater";
    private ImageLruCache g_ilruSaveBitmap = null;
    private Set<RImageUrlAsyncs> g_setImgAsync = null;
    private Context g_conText = null;
    private LayoutInflater g_InflaterLayout = null;
    private LinkedList<ThemeData> g_alDatas = null;
    public boolean g_bInit = true;

    public ThemeAdater(final Context conText) {
        g_ilruSaveBitmap = new ImageLruCache();
        g_setImgAsync = new HashSet<>();
        g_conText = conText;
        g_InflaterLayout = LayoutInflater.from(conText);
        g_alDatas = new LinkedList<ThemeData>();
        g_bInit = true;
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

        convertView = g_InflaterLayout.inflate(R.layout.listview_item_forum1, null);
        final ThemeData thData = (ThemeData)getItem(position);
        final RoundImageView rimagIcon1 = (RoundImageView) convertView.findViewById(R.id.img_forum_icon1);
        final ImageView imgIcon2 = (ImageView) convertView.findViewById(R.id.img_forum_icon2);
        final TextView textContent = (TextView) convertView.findViewById(R.id.text_forum_content);
        final TextView textName = (TextView) convertView.findViewById(R.id.text_forum_name);
        final TextView textDate = (TextView) convertView.findViewById(R.id.text_forum_date);
        final TextView textTitle = (TextView)convertView.findViewById(R.id.text_forum_title);
        final TextView textCount = (TextView)convertView.findViewById(R.id.text_forum_count);
        textContent.setText(thData.fnGetContent());
        textName.setText(thData.fnGetName());
        textDate.setText(thData.fnDate());
        textTitle.setText(Html.fromHtml(thData.fnGetTitle()));
        textCount.setText(String.valueOf(thData.fnGetResponseCount()));
        rimagIcon1.setTag(thData.fnGetURL());
        fnSetIcon2(imgIcon2, thData.fnGetArticleType());
        fnSetThemeImage(rimagIcon1, g_alDatas.get(position).fnGetURL(), position);
//        if(getCount() == position + 1) {
//            final Animation animation = AnimationUtils.loadAnimation(g_conText, R.anim.layout_item_anim_style2);
//            convertView.setAnimation(animation);
//        }
        return convertView;
    }

    private final void fnSetThemeImage(final RoundImageView rimagView, final String sUrl, final int iPos) {
        final Bitmap btData = g_ilruSaveBitmap.fnGetBitmap(sUrl);
        if (btData == null) {
            if(iPos > g_iProcess) {
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

    private final void fnSetIcon2(final ImageView imgIcon2, final short shType) {
        switch (shType) {
            case 2:
                imgIcon2.setImageResource(R.drawable.crown);
                break;
            case 3:
                imgIcon2.setImageResource(R.drawable.ic_whatshot_50dp);
                break;
        }
    }


    public final void fnAdd(final ThemeData thData) {
        g_alDatas.add(thData);
    }

    public final void fnCancelAsync() {
        for (final RImageUrlAsyncs imgAsyn : g_setImgAsync) {
            imgAsyn.cancel(false);
        }
    }
}
