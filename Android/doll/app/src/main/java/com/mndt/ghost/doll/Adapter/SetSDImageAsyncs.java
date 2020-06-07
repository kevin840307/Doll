package com.mndt.ghost.doll.Adapter;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.mndt.ghost.doll.Image.ImageAction;

/**
 * Created by Ghost on 2017/10/27.
 */
public class SetSDImageAsyncs extends AsyncTask<String, Void, Void> {
    private final String TAG = "GetImageAsync";
    private ImageView g_imgIcon = null;
    Bitmap g_btData = null;

    public SetSDImageAsyncs(final ImageView imgIcon) {
        g_imgIcon = imgIcon;
    }

    @Override
    protected Void doInBackground(String... params) {
        g_btData = ImageAction.fnGetSDImage(params[0]);
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        if(g_btData != null) {
            g_imgIcon.setImageBitmap(g_btData);
        }
    }
}
