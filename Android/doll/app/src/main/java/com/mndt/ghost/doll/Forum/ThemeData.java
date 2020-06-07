package com.mndt.ghost.doll.Forum;

import com.mndt.ghost.doll.Image.ImageAction;

import java.io.Serializable;

/**
 * Created by Ghost on 2017/11/3.
 */

public class ThemeData implements Serializable {
    public final static String g_sUserPlate[] = {"娃娃機出租區", "閒聊區", "夾物交易區", "問題回報區", "夾點分享區"};
    public final static String g_sPlate[] = {"全版", "娃娃機出租區", "閒聊區", "夾物交易區", "問題回報區", "夾點分享區"};
    public final static String g_sFormat[] = {"無格式", "格式1", "格式2"};
    public final static String g_sFormatContent[] = {""
                                                    ,"【地點】：\n"+
                                                    "【機台數量】：\n"+
                                                    "【租金】：\n"+
                                                    "【壓金】：\n"+
                                                    "【場內特色,規定】：\n"+
                                                    "【預計開場營業時間】：\n"+
                                                    "【備註】：\n"
                                                    ,"【賣】：\n"+
                                                    "【價格】：\n"+
                                                    "【交易方式】：\n"+
                                                    "【備註】：\n"};

    public final static String g_sArea[] = {"【北部】", "【中部】", "【南部】", "【東部】"};

    private short g_stPlateId = 0;
    private int g_stThemeId = 0;
    private short g_stArticleType = 0;
    private String g_sTitle = "";
    private String g_sContent = "";
    private int g_iResponseCount = 0;
    private String g_sAccount = "";
    private String g_sName = "";
    private byte g_bPicAmount = 0;
    private String g_sDate = "";


    public ThemeData(final String[] sDatas) {
        g_stPlateId = Short.valueOf(sDatas[0]);
        g_stThemeId = Integer.valueOf(sDatas[1]);
        g_stArticleType = Short.valueOf(sDatas[2]);
        g_sTitle = sDatas[3];
        g_sContent = sDatas[4];
        g_iResponseCount = Integer.valueOf(sDatas[5]);
        g_sAccount = sDatas[6];
        g_sName = sDatas[7];
        g_bPicAmount = Byte.valueOf(sDatas[8]);
        g_sDate = sDatas[9];
    }

    public ThemeData(final String sPlateId, final String[] sDatas) {
        g_stPlateId = Short.valueOf(sPlateId);
        g_stThemeId = Integer.valueOf(sDatas[0]);
        g_stArticleType = Short.valueOf(sDatas[1]);
        g_sTitle = sDatas[2];
        g_sContent = sDatas[3];
        g_iResponseCount = Integer.valueOf(sDatas[4]);
        g_sAccount = sDatas[5];
        g_sName = sDatas[6];
        g_bPicAmount = Byte.valueOf(sDatas[7]);
        g_sDate = sDatas[8];
    }

       public final short fnGetPlateId() {
        return g_stPlateId;
    }

    public final String fnGetSThemeId() {
        return String.valueOf(g_stThemeId);
    }

    public final String fnGetSPlateId() {
        return String.valueOf(g_stPlateId);
    }

    public final int fnGetThemeId() {
        return g_stThemeId;
    }


    public final short fnGetArticleType() {
        return g_stArticleType;
    }

    public final String fnGetTitle() {
        return g_sTitle;
    }

    public final String fnGetContent() {
        return g_sContent;
    }

    public final int fnGetResponseCount() {
        return g_iResponseCount;
    }

    public final String fnGetAccount() {
        return g_sAccount;
    }

    public final String fnGetName() {
        return g_sName;
    }

    public final byte fnGetPicAmount() {
        return g_bPicAmount;
    }

    public final String fnDate() {
        return g_sDate;
    }

    public final String fnGetURL() {
        return ImageAction.fnGetUserURL(g_sAccount);
    }

}
