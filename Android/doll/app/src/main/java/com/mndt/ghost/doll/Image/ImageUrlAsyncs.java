package com.mndt.ghost.doll.Image;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.mndt.ghost.doll.RoundImageView;

/**
 * Created by Ghost on 2017/11/3.
 */

public class ImageUrlAsyncs extends AsyncTask<String, Void, Bitmap> implements IFImageAsyncs {
    private final String TAG = "RImageUrlAsyncs";
    private ImageView g_imgData = null;
    private int g_iWidth = 0;
    private int g_iType = 1;

    public ImageUrlAsyncs(final ImageView imgData, final int iWidth) {
        g_imgData = imgData;
        g_iWidth = iWidth;
        g_iType = 2;
    }

    public ImageUrlAsyncs(final ImageView imgData) {
        g_imgData = imgData;
        g_iType = 1;
    }

    public final ImageUrlAsyncs fnSetType(final int iType) {
        g_iType = iType;
        return this;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        switch (g_iType) {
            case 1:
                return ImageAction.fnGetBitmapFromURL(params[0]);
            case 2:
                return ImageAction.fnGetBitmapFromURL(params[0], g_iWidth);
            default:
                return ImageAction.fnGetBitmapFromURL(params[0]);
        }
    }

    @Override
    protected void onPostExecute(Bitmap btData) {
        super.onPostExecute(btData);
        if (btData != null) {
            g_imgData.setImageBitmap(btData);
        }
    }

    @Override
    public void fnCancel(final boolean bCancel) {
        cancel(bCancel);
    }
}