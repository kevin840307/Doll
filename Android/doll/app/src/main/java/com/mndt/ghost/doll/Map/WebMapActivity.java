package com.mndt.ghost.doll.Map;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.maps.model.LatLng;
import com.mndt.ghost.doll.Handler.HandlerMessage;
import com.mndt.ghost.doll.Handler.SQLHandler;
import com.mndt.ghost.doll.R;
import com.mndt.ghost.doll.SqlDB.DOLLshop;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Ghost on 2017/10/25.
 */
public class WebMapActivity extends AppCompatActivity implements android.location.LocationListener {

    private String TAG = "WebMapActivity";
    private static final String MAP_URL = "file:///android_asset/googleMap.html";
    private static LatLng g_latMePlace = null;
    private LocationManager g_locMan;
    private WebView g_wvMapView = null;
    private boolean g_GPSSearch = true;
    private boolean g_Loading = true;
    private Location g_locLocation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.web_map_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitAdMob();
        fnInitControl();
        fnInitData();
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }


    private final void fnInitData() {
        fnStartLoding();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if (fnCheckGPS()) {
            fnSearchGPS();
            g_locMan = (LocationManager) getSystemService(this.LOCATION_SERVICE);
            g_locLocation = g_locMan.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (g_locLocation == null) {
                g_locLocation = g_locMan.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }
            if (g_locLocation != null) {
                g_latMePlace = new LatLng(g_locLocation.getLatitude(), g_locLocation.getLongitude());
                fnWaitWebView();
            } else if (g_latMePlace != null) {
                fnWaitWebView();
            }
            //g_locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);
            //g_locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 0, this);
        }
    }


    private final void fnInitControl() {
        fnInitWebView();
        fnInitButton();
    }

    private final void fnInitWebView() {
        g_wvMapView = (WebView) findViewById(R.id.web_map_view);
        g_wvMapView.getSettings().setJavaScriptEnabled(true);
        g_wvMapView.getSettings().setDomStorageEnabled(true);
        g_wvMapView.setWebViewClient(new WebViewClientWait());
        g_wvMapView.loadUrl(MAP_URL);
    }

    private final void fnInitButton() {
        final Button ibtnMe = (Button) findViewById(R.id.btn_gps_me);
        final Button ibtnBack = (Button) findViewById(R.id.btn_gps_back);
        final Button ibtnFind = (Button) findViewById(R.id.btn_gps_find);

        ibtnMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnSetWebViewGPSMe();
            }
        });

        ibtnFind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!g_GPSSearch) {
                    g_GPSSearch = true;
                    fnInitData();
                } else {
                    Toast.makeText(getApplicationContext(), "目前正在定位請稍後...", Toast.LENGTH_SHORT).show();
                }
            }
        });

        ibtnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fnOpenDialog();
            }
        });
    }

    private final void fnOpenDialog() {
        final AlertDialog alertDialog = new AlertDialog.Builder(WebMapActivity.this)
                .setTitle("關閉")
                .setMessage("是否要關閉GPS")
                .setPositiveButton("關閉", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                })
                .setNeutralButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private final void fnStartLoding() {
        final ProgressBar proBar = (ProgressBar) findViewById(R.id.pro_load);
        proBar.setVisibility(View.VISIBLE);
    }

    private final void fnStopLoding() {
        final ProgressBar proBar = (ProgressBar) findViewById(R.id.pro_load);
        proBar.setVisibility(View.GONE);
    }

    private final void fnSetMarker(final MapShopData msdData) {
        try {
            final LatLng latPlace = msdData.fnGetLatLng();
            final float[] fDistance = new float[1];
            Location.distanceBetween(g_latMePlace.latitude, g_latMePlace.longitude, latPlace.latitude, latPlace.longitude, fDistance);
            final String sMsg = "'店家：" + msdData.fnGetShopName() + "</br>地址：" + msdData.fnGetAddName() + "</br>距離：" + fDistance[0] / 1000 + "公里'";
            final String sMarkURL = "javascript:fnMarkShop(" + latPlace.latitude + "," + latPlace.longitude + "," + sMsg + ")";
            g_wvMapView.loadUrl(sMarkURL);
        } catch (IndexOutOfBoundsException ex) {
            Log.e("超出範圍：", "無法取得地址經緯度");
        }
    }

    private final void fnSetMarker() {
        final Bundle bdData = getIntent().getExtras();
        try {
            final String sShop = bdData.getString("shop");
            final String sAdd = bdData.getString("add");
            final String sLat = bdData.getString("lat");
            final String sLon = bdData.getString("lon");

            if (!sLat.equals("0")) {
                final double dbLat = Double.valueOf(sLat);
                final double dbLon = Double.valueOf(sLon);
                final float[] fDistance = new float[1];
                Location.distanceBetween(g_latMePlace.latitude, g_latMePlace.longitude, dbLat, dbLon, fDistance);
                final String sMsg = "'店家：" + sShop + "</br>地址：" + sAdd + "</br>距離：" + fDistance[0] / 1000 + "公里'";
                final String sMarkURL = "javascript:fnMarkShop(" + dbLat + "," + dbLon + "," + sMsg + ")";
                g_wvMapView.loadUrl(sMarkURL);
                final String sCenterURL = "javascript:fnMoveAt(" + dbLat + "," + dbLon + ")";
                g_wvMapView.loadUrl(sCenterURL);
            }
        } catch (IndexOutOfBoundsException ex) {
            Log.e("超出範圍：", "無法取得地址經緯度");
        } catch (Exception ex) {
            Log.e(TAG, "fnSetMarker()錯誤");
        }
    }

    private final void fnRunMarks(final String sArea) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final SQLHandler sqlHandler = new SQLHandler(getApplicationContext());
                final ArrayList<ArrayList<String>> alDatas = DOLLshop.fnSelectListLocation(sqlHandler, sArea);
                if (alDatas != null) {
                    for (int iIndex = 0; iIndex < alDatas.size(); iIndex++) {
                        double dLat = Double.valueOf(alDatas.get(iIndex).get(2).toString());
                        double dLco = Double.valueOf(alDatas.get(iIndex).get(3).toString());
                        final LatLng latPlace = new LatLng(dLat, dLco);
                        final MapShopData msdData = new MapShopData(latPlace, alDatas.get(iIndex).get(0), alDatas.get(iIndex).get(1));
                        new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_MAP_MARKER, msdData);
                    }
                }
            }
        }).start();
    }

    private final boolean fnCheckAPI() {
        if (Build.VERSION.SDK_INT >= 23) {
            final int iLocation = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (iLocation != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(WebMapActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                return false;
            }
            return true;
        } else {
            return true;
        }
    }

    private final boolean fnCheckGPS() {
        g_locMan = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (fnCheckAPI()) {
            if (!g_locMan.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    && !g_locMan.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                Toast.makeText(this, "請開啟GPS功能", Toast.LENGTH_SHORT).show();
                g_GPSSearch = false;
                return false;
            } else {
                fnStartLoding();
                Toast.makeText(this, "開始定位, 若訊號不夠會無法取得位置", Toast.LENGTH_SHORT).show();
                return true;
            }
        } else {
            //Toast.makeText(this, "請開啟權限, 請等待5秒左右", Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    private final void fnWaitWebView() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                int iAmount = 0;
                while(g_Loading && iAmount < 10) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    iAmount++;
                }
                new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_MAP_MARKERS);
            }
        }).start();
    }

    private final void fnStartSetMarks() {
        if (g_latMePlace != null) {
            if (!g_Loading) {
                fnSetWebViewGPSMe();
                g_GPSSearch = false;

                final String sUrl = "http://maps.google.com/maps/api/geocode/json?latlng=" + g_latMePlace.latitude + "," + g_latMePlace.longitude
                        + "&language=zh-CN&sensor=true";
                new TransTask().execute(sUrl);
                //Toast.makeText(this, "定位完成", Toast.LENGTH_SHORT).show();
            } else {
                //Toast.makeText(this, "定位錯誤請重新嘗試", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private final void fnSetWebViewGPSMe() {
        if (g_latMePlace != null) {
            final String sMsg = "'自己</br>經度：" + g_latMePlace.latitude + "</br>緯度：" + g_latMePlace.longitude + "'";
            final String sMarkURL = "javascript:fnMarkMe(" + g_latMePlace.latitude + "," + g_latMePlace.longitude + "," + sMsg + ")";
            g_wvMapView.loadUrl(sMarkURL);

            final String sCenterURL = "javascript:fnMoveAt(" + g_latMePlace.latitude + "," + g_latMePlace.longitude + ")";
            g_wvMapView.loadUrl(sCenterURL);
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

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.d(TAG, "已抓到位置");
            g_latMePlace = new LatLng(location.getLatitude(), location.getLongitude());
            new HandlerMessage().fnSendMessage(g_hdMessage, HandlerMessage.SET_MAP_MARKERS);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            g_locMan.removeUpdates(this);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        fnSearchGPS();
    }

    private final void fnSearchGPS() {
        g_locMan = (LocationManager) getSystemService(this.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        g_locMan.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1, 0, this);
        g_locMan.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1, 0, this);
    }

    private final void fnOpenGPS(final String sUrl) {
        int iLatIndex = sUrl.indexOf("Lat");
        int iLogndex = sUrl.indexOf("Log");
        if (iLatIndex > 0 && g_latMePlace != null) {
            iLatIndex += 3;
            final String sLat = sUrl.substring(iLatIndex, iLogndex);
            final String sLog = sUrl.substring(iLogndex + 3, sUrl.length());
            final String sSAddr = "saddr=" + g_latMePlace.latitude + "," + g_latMePlace.longitude;
            final String sDAddr = "daddr=" + sLat + "," + sLog;
            final String sUriString = "http://maps.google.com/maps?" + sSAddr + "&" + sDAddr;

            final Uri uriData = Uri.parse(sUriString);
            //getPackageManager().getPackageInfo("com.google.android.apps.maps", 0);
            final Intent itStart = new Intent(android.content.Intent.ACTION_VIEW, uriData);

            itStart.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(itStart);
            fnOpenWebView();
        }

    }

    private final void fnOpenWebView() {
        if (g_latMePlace != null) {
            g_wvMapView.loadUrl(MAP_URL + "?GOGPSAPP");
        }
    }

    private final void fnUpdateWebView(final String sCheckUrl) {
        if (sCheckUrl.indexOf("GOGPSAPP") > 0) {
            final String sMsg = "'自己</br>經度：" + g_latMePlace.latitude + "</br>緯度：" + g_latMePlace.longitude + "'";
            final String sMarkURL = "javascript:fnMarkMe(" + g_latMePlace.latitude + "," + g_latMePlace.longitude + "," + sMsg + ")";
            g_wvMapView.loadUrl(sMarkURL);

            final String sCenterURL = "javascript:fnMoveAt(" + g_latMePlace.latitude + "," + g_latMePlace.longitude + ")";

            g_wvMapView.loadUrl(sCenterURL);
            final String sUrl = "http://maps.google.com/maps/api/geocode/json?latlng=" + g_latMePlace.latitude + "," + g_latMePlace.longitude
                    + "&language=zh-CN&sensor=true";
            new TransTask().execute(sUrl);
        }
    }

    private class WebViewClientWait extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            fnStartLoding();
            g_Loading = true;
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            g_Loading = false;
            fnUpdateWebView(url);
            super.onPageFinished(view, url);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            fnOpenGPS(url);
            return super.shouldOverrideUrlLoading(view, url);
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
                    fnStartSetMarks();
                    break;
            }
        }
    };


    class TransTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String sArea = "";
            try {
                final URL urlData = new URL(params[0]);
                final BufferedReader bufferRead = new BufferedReader(new InputStreamReader(urlData.openStream()));
                String sData = bufferRead.readLine();
                int iIndex = 0;
                while (sData != null && iIndex < 26) {
                    sData = bufferRead.readLine();
                    iIndex++;
                }
                if (iIndex == 26) {
                    sArea = sData.replace(" ", "");
                    if (sArea.length() > 17) {
                        sArea = sArea.substring(14, sArea.length() - 2);
                    }
                }
            } catch (MalformedURLException e) {
                //e.printStackTrace();
                sArea = "";
            } catch (IOException e) {
                //e.printStackTrace();
                sArea = "";
            } catch (Exception e) {
                sArea = "";
            }
            return sArea;
        }

        @Override
        protected void onPostExecute(String sData) {
            super.onPostExecute(sData);
            Log.d(TAG + " GOOGLE ADD:", sData);
            final int iType = getIntent().getExtras().getInt("type");
            if (iType == 1) {
                fnRunMarks(sData);
            } else if (iType == 0) {
                fnSetMarker();
            }
            fnStopLoding();
        }
    }
}
