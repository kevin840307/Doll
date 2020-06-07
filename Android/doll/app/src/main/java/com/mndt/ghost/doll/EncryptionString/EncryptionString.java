package com.mndt.ghost.doll.EncryptionString;

/**
 * Created by Ghost on 2017/10/29.
 */
public class EncryptionString {
    private String g_sData = "";
    // 讀取函式庫
    static {
        System.loadLibrary("jniLib");
    }
    private native int fnNumberEncry(String sData);

    public EncryptionString(final String sData) {
        g_sData = sData;
    }

    public final String fnGetEncryptionString() {
        String sData = "";
        try {
            sData = String.valueOf(fnNumberEncry(g_sData));
        } catch (Exception ex) {
            sData = "";
        }
        return sData;
    }
}
