package com.mndt.ghost.doll.Handler;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mndt.ghost.doll.SqlDB.DOLL_evaluation;
import com.mndt.ghost.doll.SqlDB.DOLLdata_update;
import com.mndt.ghost.doll.SqlDB.DOLLshop;

/**
 * Created by Ghost on 2017/5/14.
 */
public class SQLHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "doll.db";

    public SQLHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String sShopSql = DOLLshop.fnCreateSql();
        final String sDataUpdateSql = DOLLdata_update.fnCreateSql();
        final String sEvaluationSql = DOLL_evaluation.fnCreateSql();
        db.execSQL(sShopSql);
        db.execSQL(sDataUpdateSql);
        db.execSQL(sEvaluationSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        final String sShopSql = DOLLshop.fnUpgradeSql();
        final String sDataUpdateSql = DOLLdata_update.fnUpgradeSql();
        final String sEvaluationSql = DOLL_evaluation.fnUpgradeSql();
        db.execSQL(sShopSql);
        db.execSQL(sDataUpdateSql);
        db.execSQL(sEvaluationSql);
        onCreate(db);
    }
}
