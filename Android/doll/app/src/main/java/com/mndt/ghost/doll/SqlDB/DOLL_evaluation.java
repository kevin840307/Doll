package com.mndt.ghost.doll.SqlDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mndt.ghost.doll.Handler.SQLHandler;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.provider.BaseColumns._ID;

/**
 * Created by Ghost on 2017/10/30.
 */
public class DOLL_evaluation {
    private final static String TAG = "DOLL_evaluation";
    public final static String TABLE_NAME = "DOLL_evaluation";
    public final static String ACCOUNT = "account";
    public final static String ADDRESS_NO = "address_no";
    public final static String STAR = "star";
    public final static String MESSAGE = "message";
    public final static String CREATE_DATETIME = "create_datetime";
    public final static String MODIFY_DATETIME = "modify_datetime";

    public String g_sAccount = "";
    public String g_sAddressNo = "";
    public String g_sStar = "";
    public String g_sMessage = "";
    public String g_sCreateDatetime = "";
    public String g_sModifyDatetime = "";

    public DOLL_evaluation(final String[] sDatas) {
        g_sAccount = sDatas[0];
        g_sAddressNo = sDatas[1];
        g_sStar = sDatas[2];
        g_sMessage = sDatas[3];
        g_sCreateDatetime = sDatas[4];
        g_sModifyDatetime = sDatas[5];
    }

    public DOLL_evaluation(final String sAccount, final String sAddressNo, final String sStar, final String sMessage) {
        g_sAccount = sAccount;
        g_sAddressNo = sAddressNo;
        g_sStar = sStar;
        g_sMessage = sMessage;
        final Calendar mCal = Calendar.getInstance();
        final SimpleDateFormat dfFormat = new SimpleDateFormat("yyyy/MM/dd");
        g_sCreateDatetime = dfFormat.format(mCal.getTime());
        g_sModifyDatetime = dfFormat.format(mCal.getTime());
    }

    public final static String fnCreateSql() {
        final String sSql = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ACCOUNT + " TEXT, "
                + ADDRESS_NO + " TEXT, "
                + STAR + " INTEGER,"
                + MESSAGE + " TEXT, "
                + CREATE_DATETIME + " TEXT,"
                + MODIFY_DATETIME + " TEXT ) ";
        return sSql;
    }

    public final static String fnUpgradeSql() {
        final String sSql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        return sSql;
    }


    public final static void fnInsert(final SQLHandler sqlHandler, final DOLL_evaluation dataData) {
        final String sID = fnGet_ID(sqlHandler, dataData);
        if (sID == "-1") {
            final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
            final ContentValues cvalueData = new ContentValues();
            cvalueData.put(ACCOUNT, dataData.g_sAccount);
            cvalueData.put(ADDRESS_NO, dataData.g_sAddressNo);
            cvalueData.put(STAR, dataData.g_sStar);
            cvalueData.put(MESSAGE, dataData.g_sMessage);
            cvalueData.put(CREATE_DATETIME, dataData.g_sCreateDatetime);
            cvalueData.put(MODIFY_DATETIME, dataData.g_sModifyDatetime);
            sqlDB.insert(TABLE_NAME, null, cvalueData);
            sqlDB.close(); // Closing database connection
            Log.i(TAG, "新增:" + dataData.g_sAddressNo);
        } else {
            fnUpdate(sqlHandler, dataData);
        }
    }

    public final static void fnUpdate(final SQLHandler sqlHandler, final DOLL_evaluation dataData) {
        final ContentValues cvalueData = new ContentValues();
        cvalueData.put(STAR, dataData.g_sStar);
        cvalueData.put(MESSAGE, dataData.g_sMessage);
        final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
        final String sWhere = " 1 = 1 "
                + " AND " + ACCOUNT + "= '" + dataData.g_sAccount + "' "
                + " AND " + ADDRESS_NO + "= '" + dataData.g_sAddressNo + "' ";
        sqlDB.update(TABLE_NAME, cvalueData, sWhere, null);
        Log.i(TAG, "更新:" + dataData.g_sAddressNo);
    }

    public final static void fnDelete(final SQLHandler sqlHandler, final String sKeyAccount, final String sAddressNo) {
        final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
        final ContentValues cvalueData = new ContentValues();
        cvalueData.put("seq", "0");
        sqlDB.update("sqlite_sequence", cvalueData, "name = '" + TABLE_NAME + "' ", null);
        final String sWhere = " 1 = 1 "
                + " AND " + ACCOUNT + "= '" + sKeyAccount + "' "
                + " AND " + ADDRESS_NO + "= '" + sAddressNo + "' ";
        sqlDB.delete(TABLE_NAME, sWhere, null);
    }

    public final static String[] fnSelectMyEvaluation(final SQLHandler sqlHandler, final String sKeyAccount, final String sAddressNo) {
        final String[] sCol = {STAR, MESSAGE};
        String sConnd = "";
        sConnd += " AND " + ACCOUNT + " =  '" + sKeyAccount + "' ";
        sConnd += " AND " + ADDRESS_NO + " =  '" + sAddressNo + "' ";
        final ArrayList<ArrayList<String>> alsData = fnSelectCol(sqlHandler, sCol, sConnd);
        if (alsData.size() > 0) {
            return alsData.get(0).toArray(new String[0]);
        } else {
            return new String[]{"", ""};
        }
    }

    private static ArrayList<ArrayList<String>> fnSelectCol(final SQLHandler sqlHandler, final String[] Col, final String sConnd) {
        final SQLiteDatabase sqlDB = sqlHandler.getReadableDatabase();
        final String selectQuery = " SELECT DISTINCT  "
                + fnFormatCol(Col)
                + " FROM " + TABLE_NAME
                + " WHERE 1 = 1 "
                + sConnd;
        final ArrayList<ArrayList<String>> alDataList = new ArrayList<>();
        final Cursor cursorData = sqlDB.rawQuery(selectQuery, null);
        if (cursorData.moveToFirst()) {
            do {
                final ArrayList<String> alData = new ArrayList<>();
                for (int iIndex = 0; iIndex < Col.length; iIndex++) {
                    alData.add(cursorData.getString(cursorData.getColumnIndex(Col[iIndex])));
                }
                alDataList.add(alData);
            } while (cursorData.moveToNext());
        }
        cursorData.close();
        sqlDB.close();
        return alDataList;
    }

    private static String fnFormatCol(final String[] sDatas) {
        String sData = "";
        if (sDatas.length > 0) {
            sData += sDatas[0];
            for (int iIndex = 1; iIndex < sDatas.length; iIndex++) {
                sData += "," + sDatas[iIndex];
            }
        }
        return sData;
    }


    public final static String fnGet_ID(final SQLHandler sqlHandler, final DOLL_evaluation dataData) {
        final SQLiteDatabase sqlDB = sqlHandler.getReadableDatabase();
        final String sSql = " SELECT  " + _ID
                + " FROM " + TABLE_NAME
                + " WHERE 1 = 1 "
                + " AND " + ACCOUNT + " = '" + dataData.g_sAccount + "' "
                + " AND " + ADDRESS_NO + " = '" + dataData.g_sAddressNo + "' "
                + " LIMIT 1 ";
        String s_Id = "-1";
        final Cursor cursorData = sqlDB.rawQuery(sSql, null);
        if (cursorData.moveToFirst()) {
            s_Id = cursorData.getString(cursorData.getColumnIndex(_ID));
        }
        cursorData.close();
        sqlDB.close();
        return s_Id;
    }
}
