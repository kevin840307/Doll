package com.mndt.ghost.doll.Handler;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.mndt.ghost.doll.Data;
import com.mndt.ghost.doll.SoapCall.SoapFunctions;
import com.mndt.ghost.doll.UserSharedPreferences;

import org.ksoap2.serialization.SoapObject;


/**
 * Created by Ghost on 2017/10/30.
 */
public class HandlerAction {
    private final String TAG = "HandlerAction";
    private final Handler g_hdMessage;

    public HandlerAction(final Handler hdMessage) {
        g_hdMessage = hdMessage;
    }

    public final void fnRunThread(final String sFunction, final int iMsg, final int iDataType) {
        fnRunThread(sFunction, iMsg, iDataType, null, null, 0);
    }

    public final void fnRunThread(final String sFunction, final int iMsg, final int iDataType, final String[] sKeys, final String[] sDatas) {
        final Thread thRun = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SoapObject soapRequest = fnGetSoapData(sKeys, sDatas, sFunction);
                    final Object soapObject = (Object) SoapFunctions.fnGetData(Data.SERRVICE_API, Data.NAMESPACE + sFunction, soapRequest);
                    final HandlerData hdData = new HandlerData(iDataType).fnChangeDatas(soapObject);
                    new HandlerMessage().fnSendMessage(g_hdMessage, iMsg, ((Object)hdData));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        thRun.start();
    }


    public final void fnRunThread(final String sFunction, final int iMsg, final int iDataType, final String[] sKeys, final String[] sDatas, final int iSize) {
        final Thread thRun = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final SoapObject soapRequest = fnGetSoapData(sKeys, sDatas, sFunction);
                    final Object soapObject = (Object) SoapFunctions.fnGetData(Data.SERRVICE_API, Data.NAMESPACE + sFunction, soapRequest);
                    final HandlerData hdData = new HandlerData(iDataType, iSize).fnChangeDatas(soapObject);
                    new HandlerMessage().fnSendMessage(g_hdMessage, iMsg, ((Object)hdData));
                } catch (Exception e) {
                    Log.e(TAG, e.getMessage());
                }
            }
        });
        thRun.start();
    }

    public final SoapObject fnGetSoapData(final String[] sKeys, final String[] sDatas, final String sFunction) {
        final SoapObject soapRequest = new SoapObject(Data.NAMESPACE, sFunction);
        if(sKeys != null) {
            for (int iIndex = 0; iIndex < sKeys.length; iIndex++) {
                soapRequest.addProperty(sKeys[iIndex], sDatas[iIndex]);
            }
        }
        return soapRequest;
    }


}
