package com.mndt.ghost.doll.Image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.mndt.ghost.doll.RoundImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ghost on 2017/11/3.
 */

public class RImageUrlAsyncs extends AsyncTask<String, Void, Bitmap> {
    private final String TAG = "RImageUrlAsyncs";
    private RoundImageView g_imgUser = null;

    public RImageUrlAsyncs(final RoundImageView imgUser) {
        g_imgUser = imgUser;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        return ImageAction.fnGetBitmapFromURL(params[0]);
    }

    @Override
    protected void onPostExecute(Bitmap btData) {
        super.onPostExecute(btData);
        if (btData != null) {
            g_imgUser.setImageBitmap(btData);
        }
    }
}