package com.mndt.ghost.doll.Handler;

import android.os.Handler;
import android.os.Message;

public class HandlerMessage {

    public final static int UPDATE_SCHEDULE = 0x000;
    public final static int GET_SHOP_DATA = 0x001;
    public final static int GET_VERTION_DATE = 0x002;
    public final static int UPDATA_TOP_GRID_BUTTON = 0x003;
    public final static int UPDATA_MYLOVE_SQL = 0x004;
    public final static int GET_VERTION_MSG = 0x005;
    public final static int OPEN_ACTIVITY = 0x006;
    public final static int GET_SQL_DATE1 = 0x007;
    public final static int ERROR_MESSAGE = 0x008;
    public final static int REGISTER_MESSAGE = 0x009;
    public final static int LOGIN_MESSAGE = 0x010;

    public final static int SET_UI1 = 0x011;

    public final static int UPDATE_EVALUATION = 0x012;
    public final static int GET_MY_EVALUATION = 0x013;
    public final static int GET_SHOP_EVALUATION = 0x014;

    public final static int GET_ALL_THEME_DATA = 0x015;
    public final static int GET_THEME_DATA = 0x016;

    public final static int GET_CODE_VALUE = 0x017;

    public final static int INSERT_UPDATE_THEME_DATE = 0x018;
    public final static int UPDATE_THEME_IMAGE = 0x019;
    public final static int GET_RESPONSE_DATA = 0x020;
    public final static int INSERT_RESPONSE_DATA = 0x021;
    public final static int UPDATE_USER_IMAGE = 0x022;

    public final static int SET_MAP_MARKERS = 0x027;
    public final static int SET_MAP_MARKER = 0x028;
    public final static int MAP_MOVE_POS = 0x029;

    public final void fnSendMessage(final Handler hdMessage, final int iWhat, final int iArg1, final int iArg2, final Object objData) {
        final Message msgData = new Message();
        msgData.what = iWhat;
        msgData.arg1 = iArg1;
        msgData.arg2 = iArg2;
        msgData.obj = objData;
        hdMessage.sendMessage(msgData);
    }


    public final void fnSendMessage(final Handler hdMessage, final int iWhat, final int iArg1, final Object objData) {
        final Message msgData = new Message();
        msgData.what = iWhat;
        msgData.arg1 = iArg1;
        msgData.obj = objData;
        hdMessage.sendMessage(msgData);
    }

    public final void fnSendMessage(final Handler hdMessage, final int iWhat, final int iArg1, final int iArg2) {
        final Message msgData = new Message();
        msgData.what = iWhat;
        msgData.arg1 = iArg1;
        msgData.arg2 = iArg2;
        hdMessage.sendMessage(msgData);
    }

    public final void fnSendMessage(final Handler hdMessage, final int iWhat, final Object objData) {
        final Message msgData = new Message();
        msgData.what = iWhat;
        msgData.obj = objData;
        hdMessage.sendMessage(msgData);
    }

    public final void fnSendMessage(final Handler hdMessage, final int iWhat, final int iArg1) {
        final Message msgData = new Message();
        msgData.what = iWhat;
        msgData.arg1 = iArg1;
        hdMessage.sendMessage(msgData);
    }

    public final void fnSendMessage(final Handler hdMessage, final int iWhat) {
        final Message msgData = new Message();
        msgData.what = iWhat;
        hdMessage.sendMessage(msgData);
    }
}
