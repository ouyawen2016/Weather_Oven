package com.oven.weather_oven.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 *建表建库
 * Created by oven on 2017/7/21.
 */

 class AreaDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "area.db";
    private static final String PROVINCE_NAME = "province";
    private static final String CITY_NAME = "city";
    private static final String COUNTY_NAME = "county";

     AreaDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        //构造方法需要提供四个参数，context, 库名，cursor，版本号

    }

    //建表建库
    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql1 = "create table if not exists "
                + PROVINCE_NAME
                + " (Id integer primary key autoincrement,"
                + "ProvinceName text,"
                + "ProvinceId integer)";
        String sql2 = "create table if not exists "
                + CITY_NAME
                + " (Id integer primary key autoincrement,"
                + "CityName text,"
                +"CityId integer,"
                +"ProvinceId integer)";
         String sql3 = "create table if not exists "
                 +COUNTY_NAME
                 + " (Id integer primary key autoincrement,"
                 + "ProvinceId integer,"
                 + "CityId integer,"
                 + "CountyName text,"
                 + "WeatherId text)";
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql1 = "DROP TABLE IF EXISTS" + PROVINCE_NAME;
        String sql2 = "DROP TABLE IF EXISTS" + CITY_NAME;
        String sql3 = "DROP TABLE IF EXISTS" + COUNTY_NAME;
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
        onCreate(db);
    }

}
