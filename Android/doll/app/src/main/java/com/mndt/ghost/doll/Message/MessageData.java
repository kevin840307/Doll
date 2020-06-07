package com.mndt.ghost.doll.Message;

import java.io.Serializable;

/**
 * Created by Ghost on 2017/10/31.
 */
public class MessageData implements Serializable {
    private String g_sAccount = "";
    private String g_sName = "";
    private String g_sMessage = "";
    private int g_iType = 0;

    public final static int LEFT_MSG = 1;
    public final static int RIGHT_MSG = 2;
    public final static int CENTER_MSG = 3;

    public MessageData(final String sAccount, final String sName, final String sMessage, final int iType) {
        g_sAccount = sAccount;
        g_sName = sName;
        g_sMessage = sMessage;
        g_iType = iType;
    }

    public MessageData() {
    }

    public String fnGetAccount() {
        return g_sAccount;
    }

    public String fnGetName() {
        return g_sName;
    }

    public String fnGetMessage() {
        return g_sMessage;
    }

    public int fnGetType() {
        return g_iType;
    }

    public String fnGetSocketMsg() {
        return g_sAccount + "_______" + g_sName + "_______" + g_sMessage + "_______1" ;
    }

    public final static MessageData fnSplitMsgToMessageData(final String sMsg) {
        String sDatas[] = sMsg.split("_______");
        if (sDatas.length == 4) {
            return new MessageData(sDatas[0], sDatas[1], sDatas[2], (sDatas[3].charAt(0) - 48));
        }
        return null;
    }
}
