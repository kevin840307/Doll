package com.mndt.ghost.doll.Map;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.mndt.ghost.doll.R;
/**
 * Created by user on 2017/2/15.
 */
public class MapItem implements GoogleMap.InfoWindowAdapter {

    private Activity g_caContext = null;

    public MapItem(final Activity context) {
        g_caContext = context;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        return null;
    }

    @Override
    public View getInfoContents(final Marker marker) {
        // 依指定layout檔，建立地標訊息視窗View物件
        View infoWindow = g_caContext.getLayoutInflater().inflate(R.layout.map_item, null);
        // 顯示地標title
        TextView textTitle = ((TextView)infoWindow.findViewById(R.id.text_map_title));
        textTitle.setText(marker.getTitle());
        // 顯示地標snippet
        TextView textData = ((TextView)infoWindow.findViewById(R.id.text_map_data));
        textData.setText(marker.getSnippet());
        return infoWindow;
    }
}
