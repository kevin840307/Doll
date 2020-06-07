package com.mndt.ghost.doll.Image;

/**
 * Created by Ghost on 2017/5/17.
 */
public class URLImageData {
    private String g_sURL;
    private String g_sDirName;
    private String g_sDataName;
    private String g_sSDDirPath;
    private String g_sSDDataPath;

    public URLImageData(final String sURL, final String sDirName, final String sDataName) {
        g_sURL = sURL;
        g_sDirName = sDirName;
        g_sDataName = sDataName;

        g_sSDDirPath = "/sdcard/" + g_sDirName;
        g_sSDDataPath = "/sdcard/" + g_sDirName + "/" + sDataName;
    }

    public final String fnGetURL() {
        return g_sURL;
    }

    public final String fnGetDirName() {
        return g_sDirName;
    }

    public final String fnGetDataName() {
        return g_sDataName;
    }

    public final String fnGetSDDirPath() {
        return g_sSDDirPath;
    }

    public final String fnGetSDDataPath() {
        return g_sSDDataPath;
    }
}
