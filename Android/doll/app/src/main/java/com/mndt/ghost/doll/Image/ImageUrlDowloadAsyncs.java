package com.mndt.ghost.doll.Image;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.mndt.ghost.doll.RoundImageView;

/**
 * Created by Ghost on 2017/11/2.
 */

public class ImageUrlDowloadAsyncs extends AsyncTask<URLImageData, Void, Bitmap> {
    private final String TAG = "GetImageAsync";
    private ImageView g_imgData = null;
    private boolean g_iDelete = false;

    public ImageUrlDowloadAsyncs(final ImageView imgData) {
        g_imgData = imgData;
    }

    public ImageUrlDowloadAsyncs(final ImageView imgData, final boolean iDelete) {
        g_imgData = imgData;
        g_iDelete = iDelete;
    }

    public ImageUrlDowloadAsyncs(final boolean iDelete) {
        g_imgData = null;
        g_iDelete = iDelete;
    }


    @Override
    protected Bitmap doInBackground(URLImageData... params) {
        return ImageAction.fnDownloadImage(params[0], g_iDelete);
    }

    @Override
    protected void onPostExecute(Bitmap btData) {
        super.onPostExecute(btData);
        if (g_imgData != null && btData != null) {
            g_imgData.setImageBitmap(btData);
        }
    }
}

