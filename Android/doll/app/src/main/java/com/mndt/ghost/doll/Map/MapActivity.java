package com.mndt.ghost.doll.Map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.transition.Slide;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.SQLHandler;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.SqlDB.DOLLshop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class MapActivity extends FragmentActivity implements OnMapReadyCallback,
        android.location.LocationListener {

    private GoogleMap mMap;
    private LocationManager g_locMan;
    private float g_fZoom = 17;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.map_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitMap();
        fnInitAdMob();
        fnInitData();
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }


    private final void fnInitMap() {
        final SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private final void fnInitData() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        g_locMan = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        g_locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0, this);
        g_locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
    }

    private final boolean fnCheckAPI() {
        if (Build.VERSION.SDK_INT >= 23) {
            final int iLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (iLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(MapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    private final boolean fnCheckGPS() {
        if (fnCheckAPI()) {
            if (!g_locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    && !g_locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Toast.makeText(this, "請開啟GPS功能", Toast.LENGTH_LONG).show();
                return true;
            }
            Toast.makeText(this, "若訊號不夠會無法取得位置", Toast.LENGTH_LONG).show();
            return true;
        } else {
            Toast.makeText(this, "請開啟權限, 請等待5秒左右", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private final void fnUpdateMarker(final LatLng latPlace, final String sArea) {
        final int iType = getIntent().getExtras().getInt("type");
        if (iType == 0) {
            new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_MAP_MARKERS, latPlace);
        } else if (iType == 1) {
            fnGetShopLocation(sArea);
        }
        new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.MAP_MOVE_POS, latPlace);
    }

    private final void fnGetShopLocation(final String sArea) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String sFixArea = sArea;
                if (sFixArea.length() > 2) {
                    sFixArea = sFixArea.substring(0, 2);
                    final SQLHandler sqlHandler = new SQLHandler(getApplicationContext());
                    final ArrayList<ArrayList<String>> alDatas = DOLLshop.fnSelectListLocation(sqlHandler, sFixArea);
                    if (alDatas != null) {
                        for (int iIndex = 0; iIndex < alDatas.size(); iIndex++) {
                            double dLat = Double.valueOf(alDatas.get(iIndex).get(2).toString());
                            double dLco = Double.valueOf(alDatas.get(iIndex).get(3).toString());
                            final LatLng latPlace = new LatLng(dLat, dLco);
                            final MapShopData msdData = new MapShopData(latPlace, alDatas.get(iIndex).get(0), alDatas.get(iIndex).get(1));
                            new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_MAP_MARKER, msdData);
                        }
                    }
                } else {
                    new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.ERROR_MESSAGE);
                }
            }
        }).start();
    }

    private final void fnMapEvent() {
        mMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(final CameraPosition cameraPosition) {
                g_fZoom = cameraPosition.zoom;
            }
        });
    }

    private final void fnSetLocation() {
        if (!fnCheckGPS()
                || (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            finish();
            return;
        }
        final LatLng latPlace = new LatLng(25.033108, 121.564099);
        fnMoveMap(latPlace);
    }

    private final void fnMoveMap(final LatLng latPlace) {
        final CameraPosition cameraPosition = new CameraPosition.Builder().target(latPlace).zoom(g_fZoom).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private final void fnMoveMap(final LatLng latPlace, final int iZoomSize) {
        final CameraPosition cameraPosition = new CameraPosition.Builder().target(latPlace).zoom(iZoomSize).build();
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    private final Marker fnAddMarker(final LatLng latPlace, final String sTitle, final String sSnippet) {
        final MapItem mapiShow = new MapItem(this);
        final BitmapDescriptor icon = BitmapDescriptorFactory.fromResource(R.drawable.gps_mark);
        final MarkerOptions markerOptions = new MarkerOptions();
        mMap.setInfoWindowAdapter(mapiShow);
        markerOptions.position(latPlace).title(sTitle).snippet(sSnippet).icon(icon);
        return mMap.addMarker(markerOptions);
    }

    private final Marker fnAddMarker(final LatLng latPlace, final String sTitle, final String sSnippet, final BitmapDescriptor icon) {
        final MapItem mapiShow = new MapItem(this);
        final MarkerOptions markerOptions = new MarkerOptions();
        mMap.setInfoWindowAdapter(mapiShow);
        markerOptions.position(latPlace).title(sTitle).snippet(sSnippet).icon(icon);
        return mMap.addMarker(markerOptions);
    }

    private final void fnSetMarkers(final LatLng latPlace) {
        final Bundle dbData = this.getIntent().getExtras();
        final String[] sShopName = dbData.getStringArray("shop_name");
        final String[] sAdd = dbData.getStringArray("add");
        for (int iPos = 0; iPos < sShopName.length; iPos++) {
            final MapShopData[] msdData = new MapShopData[2];
            msdData[0] = new MapShopData(latPlace, "", "");
            msdData[1] = new MapShopData(null, sShopName[iPos], sAdd[iPos]);
            fnSetMarker(msdData);
        }
    }

    private final void fnSetMarker(final MapShopData msdData) {
        try {
            if (msdData.fnGetLatLng().latitude > 0 && msdData.fnGetLatLng().longitude > 0) {
                fnAddMarker(msdData.fnGetLatLng(), "店家：" + msdData.fnGetShopName(), "地址：" + msdData.fnGetAddName());
            }
        } catch (IndexOutOfBoundsException ex) {
            Log.e("超出範圍：", "無法取得地址經緯度");
        }
    }

    private final void fnSetMarker(final MapShopData[] msdData) {
        try {
            final LatLng latData = fnGetLatLng(msdData[1].fnGetAddName());
            final LatLng latPlace = msdData[0].fnGetLatLng();
            final float[] fDistance = new float[1];
            Location.distanceBetween(latPlace.latitude, latPlace.longitude, latData.latitude, latData.longitude, fDistance);
            fnAddMarker(latData, "店家：" + msdData[1].fnGetShopName(), "地址：" + msdData[1].fnGetAddName() + "\n" + "距離：" + fDistance[0] / 1000 + "公里");
        } catch (IndexOutOfBoundsException ex) {
            Log.e("超出範圍：", "無法取得地址經緯度");
        }
    }

    private final LatLng fnGetLatLng(final String sAdd) {
        LatLng latPlace = null;
        final Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        try {
            final List<Address> listAdd = geoCoder.getFromLocationName(sAdd, 1);
            latPlace = new LatLng(listAdd.get(0).getLatitude(), listAdd.get(0).getLongitude());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latPlace;
    }

    public final String fnGetArea(final double dLat, final double dLng) {
        final Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
        String sAdd = "";
        try {
            final List<Address> listAdd = geoCoder.getFromLocation(dLat, dLng, 1);
            sAdd = listAdd.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sAdd;
    }

    public final static String fnGetArea(final Activity acContext, final double dLat, final double dLng) {
        final Geocoder geoCoder = new Geocoder(acContext, Locale.getDefault());
        String sAdd = "";
        try {
            final List<Address> listAdd = geoCoder.getFromLocation(dLat, dLng, 1);
            sAdd = listAdd.get(0).getAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sAdd;
    }

    public final static String fnGetArea(final Activity acContext) { //將定位資訊顯示在畫面中
        if (ActivityCompat.checkSelfPermission(acContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(acContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "台南市";
        }
        final LocationManager locmManager = (LocationManager) acContext.getSystemService(LOCATION_SERVICE); //取得系統定位服務
        final Location locPlace1 = locmManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        final Location locPlace2 = locmManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER); //使用GPS定位座標GPS_PROVIDER
        if (locPlace1 != null) {
            return fnGetArea(acContext, locPlace1.getLatitude(), locPlace1.getLongitude()).substring(0, 2);
        } else if (locPlace2 != null) {
            return fnGetArea(acContext, locPlace2.getLatitude(), locPlace2.getLongitude()).substring(0, 2);
        } else {
            Toast.makeText(acContext, "無法定位座標", Toast.LENGTH_LONG).show();
            return "台南市";
        }
    }

    private final void fnErrorMessge() {
        Toast.makeText(this, "定位錯誤請重新嘗試", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        mMap = googleMap;
        fnMapEvent();
        fnSetLocation();
    }

    @Override
    public void onLocationChanged(final Location location) {
        if (location != null) {
           location.getLatitude();
           location.getLongitude();
            final String sArea = fnGetArea(latPlace.latitude, latPlace.longitude);
            fnUpdateMarker(latPlace, sArea);
            fnAddMarker(latPlace, "您的位置", "位置：" + sArea, icon);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            g_locMan.removeUpdates(this);
            Toast.makeText(this, "定位完成", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (g_locMan != null) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            g_locMan.removeUpdates(this);
            g_locMan = null;
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Slide());
            getWindow().setExitTransition(new Slide());
        }
    }

    final Handler g_hdMessage = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case HandlerMessage.SET_MAP_MARKER:
                    fnSetMarker((MapShopData) msg.obj);
                    break;
                case HandlerMessage.SET_MAP_MARKERS:
                    fnSetMarkers((LatLng) msg.obj);
                    break;
                case HandlerMessage.MAP_MOVE_POS:
                    fnMoveMap((LatLng) msg.obj);
                    break;
                case HandlerMessage.ERROR_MESSAGE:
                    fnErrorMessge();
                    break;
            }
        }
    };
}
