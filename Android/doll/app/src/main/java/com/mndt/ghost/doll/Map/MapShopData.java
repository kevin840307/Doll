package com.mndt.ghost.doll.Map;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by user on 2017/2/16.
 */
public class MapShopData {
    private LatLng g_latPlace = null;
    private String g_sShopName = null;
    private String g_sAddName = null;

    public MapShopData(final LatLng latPlace, final String sShopName, final String sAddName) {
        this.g_latPlace = latPlace;
        this.g_sShopName = sShopName;
        this.g_sAddName = sAddName;
    }

    public LatLng fnGetLatLng() {
        return g_latPlace;
    }

    public String fnGetShopName() {
        return g_sShopName;
    }

    public String fnGetAddName() {
        return g_sAddName;
    }
}
