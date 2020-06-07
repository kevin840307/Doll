package com.mndt.ghost.doll.Evaluation;

import com.mndt.ghost.doll.Image.ImageAction;

/**
 * Created by Ghost on 2017/11/7.
 */

public class EvaluationData {

    private String g_sAddressNo = "";
    private String g_sAccount = "";
    private String g_sName = "";
    private byte g_bStar = 0;
    private String g_sMessage = "";
    private String g_sDate = "";



    public EvaluationData(final String sAddressNo, final String[] sDatas) {
        g_sAddressNo = sAddressNo;
        g_sAccount = sDatas[0];
        g_sName = sDatas[1];
        g_bStar = Byte.valueOf(sDatas[2]);;
        g_sMessage = sDatas[3];
        g_sDate = sDatas[4];
    }

    public final String fnGetAddressNo() {
        return g_sAddressNo;
    }

    public final String fnGetAccount() {
        return g_sAccount;
    }

    public final String fnGetName() {
        return g_sName;
    }

    public final int fnGetStar() {
        return g_bStar;
    }

    public final String fnGetDate() {
        return g_sDate;
    }

    public final String fnGetMessage() {
        return g_sMessage;
    }

    public final String fnGetURL() {
        return ImageAction.fnGetUserURL(g_sAccount);
    }
}
