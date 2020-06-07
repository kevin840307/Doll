package com.mndt.ghost.doll.Image;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.RoundImageView;

/**
 * Created by Ghost on 2017/11/2.
 */

public class RImageUrlDowloadAsyncs extends AsyncTask<URLImageData, Void, Bitmap> {
    private final String TAG = "GetImageAsync";
    private RoundImageView g_imgUser = null;
    private boolean g_bDelete = false;
    private float g_fSchedule = 0.0f;

    public RImageUrlDowloadAsyncs(final RoundImageView imgUser) {
        g_imgUser = imgUser;
        g_bDelete = false;
    }

    public RImageUrlDowloadAsyncs(final RoundImageView imgUser, final boolean bDelete) {
        g_imgUser = imgUser;
        g_bDelete = bDelete;
    }

    @Override
    protected Bitmap doInBackground(URLImageData... params) {
        return ImageAction.fnDownloadImage(params[0], g_bDelete);
    }

    @Override
    protected void onPostExecute(Bitmap btData) {
        super.onPostExecute(btData);
        if (btData != null) {
            g_imgUser.setImageBitmap(btData);
        }
    }

    public final static URLImageData fnGetUrlImageData(final String sAccount) {
        final String sDirName = "UserImage";
        final String sDataName = sAccount + ".jpg";
        final String sURL = Data.SERRVICE_URL + sDirName + "/" + sDataName;
        return new URLImageData(sURL, sDirName, sDataName);
    }
}

