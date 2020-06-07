package com.mndt.ghost.doll;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.transition.Fade;
import android.view.View;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

/**
 * Created by Ghost on 2017/10/22.
 */
public class GPSActivity extends AppCompatActivity {
    private String g_sUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fnTransitionAnimation();
        setContentView(R.layout.gps_activity);
        fnInit();
    }

    private final void fnInit() {
        fnInitData();
        fnInitControl();
    }

    private final void fnInitData() {
        g_sUrl = getIntent().getStringExtra("url");
    }

    private final void fnInitControl() {
        fnInitAdMob();
        fnWebView();
    }

    private final void fnInitAdMob() {
        final AdView mAdView = (AdView) findViewById(R.id.adView);
        final AdRequest adRequest = new AdRequest.Builder().addTestDevice("8F549F37C6A9EC634F95A50747644AA6").build();
        mAdView.loadAd(adRequest);
    }

    private final void fnWebView() {
        final WebView wvView = (WebView) findViewById(R.id.web_gps_view);
        wvView.setWebViewClient(new WebViewClientWait());
        wvView.setWebChromeClient(new WebChromeClient());
        final WebSettings websetSet = wvView.getSettings();
        websetSet.setSupportZoom(true);
        websetSet.setBuiltInZoomControls(true);
        websetSet.setJavaScriptEnabled(true);
        websetSet.setJavaScriptCanOpenWindowsAutomatically(true);
        wvView.loadUrl(g_sUrl);
        fnStopLoding();
    }

    private final void fnStartLoding() {
        final ProgressBar proBar = (ProgressBar)findViewById(R.id.pro_load);
        proBar.setVisibility(View.VISIBLE);
    }

    private final void fnStopLoding() {
        final ProgressBar proBar = (ProgressBar)findViewById(R.id.pro_load);
        proBar.setVisibility(View.GONE);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private final void fnTransitionAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
            getWindow().setEnterTransition(new Fade());
            getWindow().setExitTransition(new Fade());
        }
    }

    private class WebViewClientWait extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            fnStartLoding();
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            fnStopLoding();
            super.onPageFinished(view, url);
        }
    }
}
