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


public class DOLLshop {
    private final static String TAG = "DOLLshop";
    public final static String TABLE_NAME = "DOLLshop";
    public final static String ADDRESS_NO = "address_no";
    public final static String AREA = "area";
    public final static String LOCATION = "location";
    public final static String SHOP_NAME = "shop_name";
    public final static String ADDRESS = "address";
    public final static String POPULAR = "popular";
    public final static String MACHINE_AMOUNT = "machine_amount";
    public final static String MACHINE_TYPE = "machine_type";
    public final static String REMARKS = "remarks";
    public final static String MYLOVE = "mylove";
    public final static String LATITUDE = "latitude";
    public final static String LONGITUDE = "longitude";
    public final static String STAR = "star";
    public final static String CREATE_DATETIME = "create_datetime";
    public final static String MODIFY_DATETIME = "modify_datetime";

    public String g_sAddress_no = "";
    public String g_sArea = "";
    public String g_sLocation = "";
    public String g_sShopName = "";
    public String g_sAddress = "";
    public String g_sPopular = "";
    public String g_sMachineAmount = "";
    public String g_sMachineType = "";
    public String g_sMylove = "";
    public String g_sRemarks = "";
    public String g_sLatitude = "";
    public String g_sLongitude = "";
    public String g_sStar = "";
    public String g_sCreateDatetime = "";
    public String g_sModifyDatetime = "";

    public DOLLshop(final String[] sDatas) {
        g_sAddress_no = sDatas[0];
        g_sArea = sDatas[1];
        g_sLocation = sDatas[2];
        g_sShopName = sDatas[3];
        g_sAddress = sDatas[4];
        g_sPopular = sDatas[5];
        g_sMachineAmount = sDatas[6];
        g_sMachineType = sDatas[7];
        g_sRemarks = sDatas[8];
        g_sLatitude = sDatas[9];
        g_sLongitude = sDatas[10];
        g_sMylove = "0";
        g_sStar = sDatas[11];
        g_sCreateDatetime = sDatas[12];
        g_sModifyDatetime = sDatas[13];
    }


    public final static String fnCreateSql() {
        final String sSql = "CREATE TABLE " + TABLE_NAME + "("
                + _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + ADDRESS_NO + " TEXT, "
                + AREA + " TEXT, "
                + LOCATION + " TEXT, "
                + SHOP_NAME + " TEXT, "
                + POPULAR + " TEXT, "
                + ADDRESS + " TEXT, "
                + MACHINE_AMOUNT + " TEXT, "
                + MACHINE_TYPE + " TEXT, "
                + REMARKS + " TEXT, "
                + LATITUDE + " TEXT, "
                + LONGITUDE + " TEXT, "
                + MYLOVE + " TEXT, "
                + STAR + " INTEGER, "
                + CREATE_DATETIME + " TEXT, "
                + MODIFY_DATETIME + " TEXT) ";
        return sSql;
    }

    public final static String fnUpgradeSql() {
        final String sSql = "DROP TABLE IF EXISTS " + TABLE_NAME;
        return sSql;
    }


    public final static void fnInsert(final SQLHandler sqlHandler, final DOLLshop shopData) {
        final String sID = fnGet_ID(sqlHandler, shopData);
        Log.i(TAG, "sID: " + sID);
        if (sID == "-1") {
            final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
            final ContentValues cvalueData = new ContentValues();
            cvalueData.put(ADDRESS_NO, shopData.g_sAddress_no);
            cvalueData.put(AREA, shopData.g_sArea);
            cvalueData.put(LOCATION, shopData.g_sLocation);
            cvalueData.put(SHOP_NAME, shopData.g_sShopName);
            cvalueData.put(ADDRESS, shopData.g_sAddress);
            cvalueData.put(POPULAR, shopData.g_sPopular);
            cvalueData.put(MACHINE_AMOUNT, shopData.g_sMachineAmount);
            cvalueData.put(MACHINE_TYPE, shopData.g_sMachineType);
            cvalueData.put(REMARKS, shopData.g_sRemarks);
            cvalueData.put(LATITUDE, shopData.g_sLatitude);
            cvalueData.put(LONGITUDE, shopData.g_sLongitude);
            cvalueData.put(MYLOVE, shopData.g_sMylove);
            cvalueData.put(STAR, shopData.g_sStar);
            cvalueData.put(CREATE_DATETIME, shopData.g_sCreateDatetime);
            cvalueData.put(MODIFY_DATETIME, shopData.g_sModifyDatetime);
            long lId = sqlDB.insert(TABLE_NAME, null, cvalueData);
            sqlDB.close(); // Closing database connection
            Log.i(TAG, "新增:" + shopData.g_sAddress_no + " " + lId);
        } else {
            fnUpdate(sqlHandler, shopData);
        }
    }

    public final static void fnUpdateLove(final SQLHandler sqlHandler, final String sAddressNo, final boolean bLove) {
        String sLove = "0";
        if (bLove) {
            sLove = "1";
        }
        final ContentValues cvalueData = new ContentValues();
        cvalueData.put(MYLOVE, sLove);
        final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
        sqlDB.update(TABLE_NAME, cvalueData, ADDRESS_NO + "=" + sAddressNo, null);
        Log.i(TAG, "更新:" + sAddressNo);
    }

    public final static void fnUpdate(final SQLHandler sqlHandler, final DOLLshop shopData) {
        final ContentValues cvalueData = new ContentValues();
        cvalueData.put(AREA, shopData.g_sArea);
        cvalueData.put(LOCATION, shopData.g_sLocation);
        cvalueData.put(SHOP_NAME, shopData.g_sShopName);
        cvalueData.put(ADDRESS, shopData.g_sAddress);
        cvalueData.put(POPULAR, shopData.g_sPopular);
        cvalueData.put(MACHINE_AMOUNT, shopData.g_sMachineAmount);
        cvalueData.put(MACHINE_TYPE, shopData.g_sMachineType);
        cvalueData.put(REMARKS, shopData.g_sRemarks);
        cvalueData.put(LATITUDE, shopData.g_sLatitude);
        cvalueData.put(LONGITUDE, shopData.g_sLongitude);
        cvalueData.put(STAR, shopData.g_sStar);
        cvalueData.put(CREATE_DATETIME, shopData.g_sCreateDatetime);
        cvalueData.put(MODIFY_DATETIME, shopData.g_sModifyDatetime);
        final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
        sqlDB.update(TABLE_NAME, cvalueData, ADDRESS_NO + "=" + shopData.g_sAddress_no, null);
        Log.i(TAG, "更新:" + shopData.g_sAddress_no);
    }

    public final static void fnDelete(final SQLHandler sqlHandler, final String sIndex) {
        final SQLiteDatabase sqlDB = sqlHandler.getWritableDatabase();
        final ContentValues cvalueData = new ContentValues();
        cvalueData.put("seq", "0");
        sqlDB.update("sqlite_sequence", cvalueData, "name = '" + TABLE_NAME + "' ", null);
        sqlDB.delete(TABLE_NAME, ADDRESS_NO + "=" + sIndex, null);
    }


    public final static ArrayList<ArrayList<String>> fnSelectListView(final SQLHandler sqlHandler, final String sArea, final String sLocation, final String sKeyWord) {
        final SQLiteDatabase sqlDB = sqlHandler.getReadableDatabase();
        final String[] sCol = {ADDRESS_NO, AREA, LOCATION, SHOP_NAME, ADDRESS, POPULAR, LATITUDE, LONGITUDE };
        String sConnd = "";
        sConnd += " AND " + AREA + " = '" + sArea + "' ";
        if (!sLocation.equals("全部")) {
            sConnd += " AND " + LOCATION + " = '" + sLocation + "' ";
        }
        if(sKeyWord.length() > 0) {
            sConnd += " AND " + REMARKS + " LIKE '%" + sKeyWord + "%' ";
        }
        return fnSelectRow(sqlHandler, sCol, sConnd);
    }

    public final static ArrayList<ArrayList<String>> fnSelectListPopular(final SQLHandler sqlHandler) {
        final String[] sCol = {ADDRESS_NO, AREA, LOCATION, SHOP_NAME, ADDRESS, POPULAR, LATITUDE, LONGITUDE};
        String sConnd = "";
        sConnd += " AND " + POPULAR + " = 'Y' ";
        return fnSelectRow(sqlHandler, sCol, sConnd);
    }

    public final static ArrayList<ArrayList<String>> fnSelectListMyLove(final SQLHandler sqlHandler) {
        final String[] sCol = {ADDRESS_NO, AREA, LOCATION, SHOP_NAME, ADDRESS, POPULAR, LATITUDE, LONGITUDE};
        String sConnd = "";
        sConnd += " AND " + MYLOVE + " = '1' ";
        return fnSelectRow(sqlHandler, sCol, sConnd);
    }

    public final static ArrayList<String> fnSelectListShopData(final SQLHandler sqlHandler, final String sAddressNo) {
        final String[] sCol = { ADDRESS_NO, SHOP_NAME, ADDRESS, MACHINE_AMOUNT, MACHINE_TYPE, REMARKS, MYLOVE, STAR };
        String sConnd = "";
        sConnd += " AND " + ADDRESS_NO + " = '" + sAddressNo + "' ";
        final ArrayList<ArrayList<String>> alData = fnSelectCol(sqlHandler, sCol, sConnd);
        if (alData.size() > 0) {
            return alData.get(0);
        } else {
            return null;
        }
    }

    public final static ArrayList<ArrayList<String>> fnSelectListLocation(final SQLHandler sqlHandler, final String sAddress) {
        final String[] sCol = { SHOP_NAME, ADDRESS, LATITUDE, LONGITUDE };
        String sConnd = "";
        sConnd += " AND " + ADDRESS + " LIKE '%" + sAddress + "%' ";
        sConnd += " AND " + LATITUDE + " <> '0' ";
        sConnd += " AND " + LONGITUDE + " <> '0' ";
        final ArrayList<ArrayList<String>> alData = fnSelectCol(sqlHandler, sCol, sConnd);
        if (alData.size() > 0) {
            return alData;
        } else {
            return null;
        }
    }

    public final static ArrayList<String> fnSelectLocation(final SQLHandler sqlHandler, final String sArea) {
        final String[] sCol = {LOCATION};
        String sConnd = "";
        sConnd += " AND " + AREA + " = '" + sArea + "' ";
        return fnSelectRow(sqlHandler, sCol, sConnd).get(0);
    }

    public final static ArrayList<String> fnSelectPopular(final SQLHandler sqlHandler) {
        final String[] sCol = {ADDRESS};
        String sConnd = "";
        sConnd += " AND " + POPULAR + " = 'Y' ";
        return fnSelectRow(sqlHandler, sCol, sConnd).get(0);
    }

    public final static ArrayList<String> fnSelectArea(final SQLHandler sqlHandler) {
        final String[] sCol = {AREA};
        String sConnd = "";
        return fnSelectRow(sqlHandler, sCol, sConnd).get(0);
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

    private static ArrayList<ArrayList<String>> fnSelectRow(final SQLHandler sqlHandler, final String[] Col, final String sConnd) {
        final SQLiteDatabase sqlDB = sqlHandler.getReadableDatabase();
        final String selectQuery = " SELECT DISTINCT  "
                + fnFormatCol(Col)
                + " FROM " + TABLE_NAME
                + " WHERE 1 = 1 "
                + sConnd;
        final ArrayList<ArrayList<String>> alDataList = new ArrayList<>();
        for (int iIndex = 0; iIndex < Col.length; iIndex++) {
            final ArrayList<String> alData = new ArrayList<>();
            alDataList.add(alData);
        }
        final Cursor cursorData = sqlDB.rawQuery(selectQuery, null);
        if (cursorData.moveToFirst()) {
            do {
                for (int iIndex = 0; iIndex < Col.length; iIndex++) {
                    alDataList.get(iIndex).add(cursorData.getString(cursorData.getColumnIndex(Col[iIndex])));
                }
            } while (cursorData.moveToNext());
        }
        cursorData.close();
        sqlDB.close();
        return alDataList;
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


    public final static String fnGet_ID(final SQLHandler sqlHandler, final DOLLshop shopData) {
        final SQLiteDatabase sqlDB = sqlHandler.getReadableDatabase();
        final String sSql = " SELECT  " + _ID +
                " FROM " + TABLE_NAME +
                " WHERE " + ADDRESS_NO + " ='" + shopData.g_sAddress_no + "' " +
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
