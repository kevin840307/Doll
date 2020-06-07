package com.mndt.ghost.doll.SoapCall;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;


/**
 * Created by user on 2016/12/21.
 */
public class SoapFunctions {

    public final static Object fnGetData(final String sURL, final String sNameSpace, final SoapSerializationEnvelope soapSeriEnve) {
        final HttpTransportSE httpTrans = new HttpTransportSE(sURL);
        try {
            httpTrans.call(sNameSpace, soapSeriEnve);
            return soapSeriEnve.getResponse();
        } catch (IOException ioExc) {

        } catch (XmlPullParserException xmlPPExc) {
        }
        return null;
    }

    public final static Object fnGetData(final String sURL, final String sNameSpace) {
        final SoapSerializationEnvelope soapSeriEnve = fnGetSoapSerializationEnvelope();
        final HttpTransportSE httpTrans = new HttpTransportSE(sURL);
        try {
            httpTrans.call(sNameSpace, soapSeriEnve);
            return soapSeriEnve.getResponse();
        } catch (IOException ioExc) {

        } catch (XmlPullParserException xmlPPExc) {
        }
        return null;
    }

    public final static Object fnGetData(final String sURL, final String sNameSpace, final SoapObject soapRequest) {
        final SoapSerializationEnvelope soapSeriEnve = fnGetSoapSerializationEnvelope(soapRequest);
        final HttpTransportSE httpTrans = new HttpTransportSE(sURL);
        try {
            httpTrans.call(sNameSpace, soapSeriEnve);
            return soapSeriEnve.getResponse();
        } catch (IOException ioExc) {

        } catch (XmlPullParserException xmlPPExc) {
        }
        return null;
    }

    public final static SoapSerializationEnvelope fnGetSoapSerializationEnvelope(final SoapObject soapRequest) {
        final SoapSerializationEnvelope soapSeriEnve = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSeriEnve.bodyOut = soapRequest;
        soapSeriEnve.dotNet = true;
        soapSeriEnve.setOutputSoapObject(soapRequest);
        return soapSeriEnve;
    }

    public final static SoapSerializationEnvelope fnGetSoapSerializationEnvelope() {
        final SoapSerializationEnvelope soapSeriEnve = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        soapSeriEnve.dotNet = true;
        return soapSeriEnve;
    }

    public final static String[][] fnGetListData(final Object soapObject, final int iCount) {
        if(soapObject == null) return null;
        final int iAmount = ((SoapObject) soapObject).getPropertyCount();
        if(iAmount == 0) return null;
        final String[][] sData = new String[iCount][(iAmount / iCount)];
        for (int iPos = 0; iPos < iAmount; iPos += iCount) {
            for (int iOffset = 0; iOffset < iCount; iOffset++) {
                sData[iOffset][(iPos / iCount)] = ((SoapObject) soapObject).getProperty(iPos + iOffset).toString();
            }
        }
        return sData;
    }

    public final static String[][] fnGetVListData(final Object soapObject, final int iCount) {
        if(soapObject == null) return null;
        final int iAmount = ((SoapObject) soapObject).getPropertyCount();
        if(iAmount == 0) return null;
        final String[][] sData = new String[(iAmount / iCount)][iCount];
        for (int iPos = 0; iPos < iAmount; iPos += iCount) {
            for (int iOffset = 0; iOffset < iCount; iOffset++) {
                sData[(iPos / iCount)][iOffset] = ((SoapObject) soapObject).getProperty(iPos + iOffset).toString();
            }
        }
        return sData;
    }

}
