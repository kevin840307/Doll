package com.mndt.ghost.doll.Handler;

import com.mndt.ghost.doll.SoapCall.SoapFunctions;

/**
 * Created by Ghost on 2017/10/30.
 */
public class HandlerData {
    public final static int VALUE_MESSAGE = 1;
    public final static int V_LIST_MESSAGE = 2;
    public final static int H_LIST_MESSAGE = 3;

    private int g_iDataType = 0;
    private int g_iListSize = 0;
    private String g_sValueDatas = null;
    private String[][] g_sListDatas = null;

    public HandlerData(final int iDataType) {
        g_iDataType = iDataType;

    }

    public HandlerData(final int iDataType, int iListSize) {
        g_iDataType = iDataType;
        g_iListSize = iListSize;
    }

    public final String fnGetValue() {
        return g_sValueDatas;
    }

    public final String[][] fnGetList() {
        return g_sListDatas;
    }

    public final HandlerData fnChangeDatas(final Object soapObject) {
        if(soapObject != null) {
            switch (g_iDataType) {
                case VALUE_MESSAGE:
                    g_sValueDatas = soapObject.toString();
                    break;
                case V_LIST_MESSAGE:
                    g_sListDatas = SoapFunctions.fnGetVListData(soapObject, g_iListSize);
                    break;
                case H_LIST_MESSAGE:
                    g_sListDatas = SoapFunctions.fnGetListData(soapObject, g_iListSize);
                    break;
            }
        }
        return this;
    }
}
