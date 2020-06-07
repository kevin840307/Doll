package com.mndt.ghost.doll.SqlDB;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.mndt.ghost.doll.Handler.SQLHandler;

import java.util.ArrayList;

import static android.provider.BaseColumns._ID;

/**
 * Created by Ghost on 2017/5/14.
 */
public class DOLLdata_update {
    private final static String TAG = "DOLLdata_update";
    public final static String TABLE_NAME = "DOLLdata_update";
    public final static String TYPE = "type";
    public final static String DATE = "date";
    public final static String STATUS = "status";
    public final static String MESSAGE = "message";

    public String g_sType = "";
    public String g_sDate = "";
    public String g_sStatus = "";
    public String g_sMessage = "";

    public final static String fnCreateSql() {
        final String sSql = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TYPE + " TEXT, "
                + DATE + " TEXT, "
                + STATUS + " INTEGER,"
                + MESSAGE + " TEXT ) ";
        return sSql;
    }

    public final static String fnUpgradeSql() {
        final String sSql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        return sSql;
    }


    public final static void fnInsert(final SQLHandler sqlHandler, final DOLLdata_update dataData) {
        final String sID = fnGet_ID(sqlHandler, dataData);
        if (sID == "-1") {
            final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
            final ContentValues cvalueData = new ContentValues();
            cvalueData.put(TYPE, dataData.g_sType);
            cvalueData.put(DATE, dataData.g_sDate);
            cvalueData.put(STATUS, dataData.g_sStatus);
            cvalueData.put(MESSAGE, dataData.g_sMessage);
            sqlDB.insert(TABLE_NAME, null, cvalueData);
            sqlDB.close(); // Closing database connection
            Log.i(TAG, "新增:" + dataData.g_sType);
        } else {
            fnUpdate(sqlHandler, dataData);
        }
    }

    public final static void fnUpdate(final SQLHandler sqlHandler, final DOLLdata_update dataData) {
        final ContentValues cvalueData = new ContentValues();
        cvalueData.put(DATE, dataData.g_sDate);
        cvalueData.put(STATUS, dataData.g_sStatus);
        cvalueData.put(MESSAGE, dataData.g_sMessage);
        final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
        sqlDB.update(TABLE_NAME, cvalueData, TYPE + "= '" + dataData.g_sType + "' ", null);
        Log.i(TAG, "更新:" + dataData.g_sType);
    }

    public final static void fnDelete(final SQLHandler sqlHandler, final String sKeyName) {
        final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
        final ContentValues cvalueData = new ContentValues();
        cvalueData.put("seq", "0");
        sqlDB.update("sqlite_sequence", cvalueData, "name = '" + TABLE_NAME + "' ", null);
        sqlDB.delete(TABLE_NAME, TYPE + "= '" + sKeyName + "' ", null);
    }


    public final static String[] fnSelectStatus(final SQLHandler sqlHandler, final String sKeyName) {
        final String[] sCol = {STATUS, DATE, MESSAGE};
        String sConnd = "";
        sConnd += " AND " + TYPE + " =  '" + sKeyName + "' ";
        final ArrayList<ArrayList<String>> alsData = fnSelectCol(sqlHandler, sCol, sConnd);
        if (alsData.size() > 0) {
            return alsData.get(0).toArray(new String[0]);
        } else {
            return new String[]{"0", ""};
        }
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


    public final static String fnGet_ID(final SQLHandler sqlHandler, final DOLLdata_update dataData) {
        final SQLiteDatabase sqlDB = sqlHandler.getReadableDatabase();
        final String sSql = " SELECT  " + _ID +
                " FROM " + TABLE_NAME +
                " WHERE " + TYPE + " ='" + dataData.g_sType + "' " +
                " LIMIT 1 ";
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
