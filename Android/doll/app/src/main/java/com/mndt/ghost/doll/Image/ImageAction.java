package com.mndt.ghost.doll.Image;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Base64;
import android.util.Log;

import com.android.internal.util.ImageUtils;
import com.mndt.ghost.doll.Data;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Ghost on 2017/10/27.
 */
public class ImageAction {

    public final static Bitmap fnGetSDImage(final String sNo) {
        final String sPath = "/sdcard/DollImage/shop_" + sNo + ".jpg";
        return fnGetBitmap(sPath);
    }

    public final static Bitmap fnGetUserImage(final String sAccount) {
        final String sPath = "/sdcard/UserImage/" + sAccount + ".jpg";
        return fnGetBitmap(sPath);
    }

    public final static String fnGetBase64(final Bitmap btData) {
        return fnGetBase64(btData, 100);
    }

    public final static String fnGetBase64(final Bitmap btData, final int iRatio) {
       return fnGetBase64(btData, iRatio, false);
    }

    public final static String fnGetBase64(final Bitmap btData, final int iRatio, final boolean bRecycle) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        btData.compress(Bitmap.CompressFormat.JPEG, iRatio, baos);
        byte[] b = baos.toByteArray();
        if(bRecycle) {
            btData.recycle();
        }
        return Base64.encodeToString(b, Base64.DEFAULT);
    }

    public final static Bitmap fnGetBitmap(final String sPath) {
        try {
            if (fnSDExist(sPath)) {
                final Bitmap bitmap = BitmapFactory.decodeFile(sPath);
                return bitmap;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    public final static Bitmap fnGetBitmap(final String sFile, final boolean bRGB) {
        final BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        option.inPurgeable = true;
        Bitmap bitmap =BitmapFactory.decodeFile(sFile, option);
        if(bRGB) {
            option.inPreferredConfig = Bitmap.Config.RGB_565;
        }
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(sFile, option);
    }

    public final static Bitmap fnGetBitmap(final String sFile, final int iMaxW) {
        final BitmapFactory.Options option = new BitmapFactory.Options();
        option.inJustDecodeBounds = true;
        option.inPurgeable = true;
        Bitmap bitmap =BitmapFactory.decodeFile(sFile, option);
        option.inPreferredConfig = Bitmap.Config.RGB_565;
        Log.i("寬", String.valueOf(option.outWidth));
        Log.i("高", String.valueOf(option.outHeight));
        int iSize = 1;
        while(option.outWidth / iSize >= iMaxW) {
            iSize++;
        }
        option.inSampleSize = iSize;
        option.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(sFile, option);
    }

    public static Bitmap fnGetStreamBitmap(final InputStream inputStream, final int iMaxW) {
        BufferedInputStream bufferedInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;
        try {
            final BitmapFactory.Options option = new BitmapFactory.Options();
            option.inJustDecodeBounds = true;
            option.inPreferredConfig = Bitmap.Config.RGB_565;
            bufferedInputStream = new BufferedInputStream(inputStream);
            byteArrayOutputStream = new ByteArrayOutputStream();

            byte bBuffer[] = new byte[1024];
            int iLen;
            while ((iLen = bufferedInputStream.read(bBuffer, 0, bBuffer.length)) > 0) {
                byteArrayOutputStream.write(bBuffer, 0, iLen);
            }
            byte[] imageData = byteArrayOutputStream.toByteArray();
            BitmapFactory.decodeByteArray(imageData, 0, imageData.length, option);
            Log.i("寬", String.valueOf(option.outWidth));
            Log.i("高", String.valueOf(option.outHeight));
            int iSize = 1;
            while(option.outWidth / iSize >= iMaxW) {
                iSize++;
            }
            option.inSampleSize = iSize;
            option.inJustDecodeBounds = false;
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length, option);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedInputStream.close();
                byteArrayOutputStream.close();
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


//    public final static Bitmap fnGetStreamBitmap(final InputStream inputStream, final int iMaxW) {
//        final BitmapFactory.Options option = new BitmapFactory.Options();
//        option.inJustDecodeBounds = true;
//        option.inPurgeable = true;
//        Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, option);
//        try {
//            inputStream.reset();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        Log.i("寬", String.valueOf(option.outWidth));
//        Log.i("高", String.valueOf(option.outHeight));
//        int iSize = 1;
//        while(option.outWidth / iSize >= iMaxW) {
//            iSize++;
//        }
//        option.inSampleSize = iSize;
//        option.inJustDecodeBounds = false;
//        return BitmapFactory.decodeStream(inputStream, null, option);
//    }


    public final static Bitmap fnGetBitmapFromSD(final String imageSD, int iMaxW) {
        if (fnSDExist(imageSD)) {
            return fnGetBitmap(imageSD, iMaxW);
        }
        return null;
    }

    public final static Bitmap fnGetBitmapFromURL(final String imageUrl, int iMaxW) {
        try {
            final URL url = new URL(imageUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final int iStatus = connection.getResponseCode();
            if (fnCheckStatus(iStatus)) {
                final InputStream input = connection.getInputStream();
                final Bitmap btData = fnGetStreamBitmap(input, iMaxW);
                input.close();
                return btData;
            } else {
                return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public final static Bitmap fnGetBitmapFromURL(final String imageUrl) {
        try {
            final URL url = new URL(imageUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            final int iStatus = connection.getResponseCode();
            if (fnCheckStatus(iStatus)) {
                final InputStream input = connection.getInputStream();
                final Bitmap btData = BitmapFactory.decodeStream(input);
                return btData;
            } else {
                return null;
            }
        } catch (IOException e) {
            return null;
        }
    }

    public final static boolean fnCheckUrl(final String sUrl) {
        try {
            final URL url = new URL(sUrl);
            final HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            final int iStatus = connection.getResponseCode();
            return fnCheckStatus(iStatus);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public final static boolean fnCheckInternet(final Context conText) {
        final ConnectivityManager cManager=(ConnectivityManager)conText.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = cManager.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isAvailable()){
            return true;
        }else{
            return false;
        }
    }

    public final static boolean fnCheckStatus(final int iStatus) {
        try {
            switch (iStatus) {
                case java.net.HttpURLConnection.HTTP_NOT_FOUND: //404 網址不存在
                    return false;
                case java.net.HttpURLConnection.HTTP_OK:
                    return true;
            }
            return false;
        }
        catch (Exception ex) {
            return false;
        }
    }

    public final static Bitmap fnDownloadImage(final URLImageData imageData, boolean bDelete) {
        if (bDelete) {
            fnSDDelete(imageData);
        }
        if (!fnSDExist(imageData)) {
            final String imageUrl = imageData.fnGetURL();
            final Bitmap btData = fnGetBitmapFromURL(imageUrl);
            if(btData != null) {
                fnSaveSD(imageData, btData);
                return btData;
            } else {
                return null;
            }
        }
        return fnGetBitmap(imageData.fnGetSDDataPath());
    }


    public final static Bitmap fnDownloadImage(final URLImageData imageData) {
        return fnDownloadImage(imageData, false);
    }

    private final static void fnSaveSD(final URLImageData imageData, final Bitmap btData) {
        final FileOutputStream foutStream;
        try {
            final File fileDir = new File(imageData.fnGetSDDirPath());
            if (!fileDir.exists()) {
                fileDir.mkdir();
            }
            foutStream = new FileOutputStream(imageData.fnGetSDDataPath());
            btData.compress(Bitmap.CompressFormat.JPEG, 75, foutStream);
            foutStream.flush();
            foutStream.close();
        } catch (IOException e) {
        } catch (Exception e) {

        }
    }

    private final static void fnSDDelete(final URLImageData imageData) {
        final File fileData = new File(imageData.fnGetSDDataPath());
        if (fileData.exists()) {
            fileData.delete();
        }
    }

    private final static boolean fnSDExist(final URLImageData imageData) {
        final File fileData = new File(imageData.fnGetSDDataPath());
        return fileData.exists();
    }

    private final static boolean fnSDExist(final String sPath) {
        final File fileData = new File(sPath);
        return fileData.exists();
    }

    public final static URLImageData fnGetShopImageData(final String sAdd) {
        final String sDirName = "DollImage";
        final String sDataName = "shop_" + sAdd + ".jpg";
        final String sURL = Data.SERRVICE_URL + sDirName + "/" + sDataName;
        return new URLImageData(sURL, sDirName, sDataName);
    }

    public final static String fnGetShopURL(final String sAdd) {
        final String sDirName = "DollImage";
        final String sDataName = "shop_" + sAdd + ".jpg";
        return Data.SERRVICE_URL + sDirName + "/" + sDataName;
    }

    public final static String fnGetUserURL(final String sAccount) {
        final String sDirName = "UserImage";
        final String sDataName = sAccount + ".jpg";
        return Data.SERRVICE_URL + sDirName + "/" + sDataName;
    }

    public final static String fnGetThemeURL(final String sPath, int iIndex) {
        final String sDirName = "Theme/" + sPath;
        final String sDataName = String.valueOf(iIndex) + ".jpg";
        return Data.SERRVICE_URL + sDirName + "/" + sDataName;
    }
}
