package com.mndt.ghost.doll.Forum;

import com.mndt.ghost.doll.Image.ImageAction;

/**
 * Created by Ghost on 2017/11/7.
 */

public class RespondData {
    private short g_stPlateId = 0;
    private int g_iThemeId = 0;
    private int g_iResponseId = 0;
    private short g_stResponseType = 0;
    private String g_sContent = "";
    private String g_sAccount = "";
    private String g_sName = "";
    private String g_sDate = "";

    public RespondData(final String sPlateId, final String sThemeId, final String[] sDatas) {
        g_stPlateId = Short.valueOf(sPlateId);
        g_iThemeId = Integer.valueOf(sThemeId);
        g_iResponseId = Integer.valueOf(sDatas[0]);
        g_stResponseType = Short.valueOf(sDatas[1]);
        g_sContent = sDatas[2];
        g_sAccount = sDatas[3];
        g_sName = sDatas[4];
        g_sDate = sDatas[5];
    }

    public RespondData(final short shPlateId, final int iThemeId, final String[] sDatas) {
        g_stPlateId = shPlateId;
        g_iThemeId = iThemeId;
        g_iResponseId = Integer.valueOf(sDatas[0]);
        g_stResponseType = Short.valueOf(sDatas[1]);
        g_sContent = sDatas[2];
        g_sAccount = sDatas[3];
        g_sName = sDatas[4];
        g_sDate = sDatas[5];
    }

    public final short fnGetPlateId() {
        return g_stPlateId;
    }

    public final int fnGetThemeId() {
        return g_iThemeId;
    }

    public final int fnGetResponId() {
        return g_iResponseId;
    }

    public final short fnGetResponseType() {
        return g_stResponseType;
    }

    public final String fnGetSPlateId() {
        return String.valueOf(g_stPlateId);
    }

    public final String fnGetSThemeId() {
        return String.valueOf(g_iThemeId);
    }

    public final String fnGetSResponId() {
        return String.valueOf(g_iResponseId);
    }

    public final String fnGetSResponseType() {
        return String.valueOf(g_stResponseType);
    }

    public final String fnGetContent() {
        return g_sContent;
    }

    public final String fnGetAccount() {
        return g_sAccount;
    }

    public final String fnGetName() {
        return g_sName;
    }

    public final String fnDate() {
        return g_sDate;
    }

    public final String fnGetURL() {
        return ImageAction.fnGetUserURL(g_sAccount);
    }
}
