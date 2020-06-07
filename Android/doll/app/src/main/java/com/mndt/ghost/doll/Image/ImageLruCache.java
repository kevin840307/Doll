package com.mndt.ghost.doll.Image;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

/**
 * Created by Ghost on 2017/5/17.
 */
public class ImageLruCache {
    private LruCache<String, Bitmap> g_lruSaveBitmap;
    private final int g_iMaxMemory = (int) Runtime.getRuntime().maxMemory();
    private final int g_iCacheSizes = g_iMaxMemory / 5;

    public ImageLruCache() {
        g_lruSaveBitmap = new LruCache<String, Bitmap>(g_iCacheSizes) {
            @SuppressLint("NewApi")
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return value.getByteCount();
            }
        };
    }

    public final Bitmap fnGetBitmap(final String url){
        return g_lruSaveBitmap.get(url);
    }

    public final void fnAddBitmap(final String url, final Bitmap bitmap){
        if(fnGetBitmap(url) == null && bitmap != null){
            g_lruSaveBitmap.put(url, bitmap);
        }
    }

}
