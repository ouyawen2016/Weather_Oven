package com.oven.weather_oven.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.oven.weather_oven.bean.City;
import com.oven.weather_oven.bean.County;
import com.oven.weather_oven.bean.Province;

import java.util.ArrayList;
import java.util.List;

/**
 * 处理与数据库有关的数据操作
 * Created by Oven on 2017/7/21.
 */

public class DBDao {
    private Context mcontext;
    private AreaDBHelper areaDBHelper;
    /*
    *传入如context
     */
    public DBDao(Context context){
        this.mcontext = context;
        areaDBHelper = new AreaDBHelper(context);
    }

    /**
     * 将省级列表存入数据库
     * @param provinceList
     */
    public void initProvinceTable(List<Province> provinceList){
        SQLiteDatabase db = areaDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(Province province:provinceList){
            values.put("ProvinceName",province.getProvinceName());
            values.put("ProvinceId",province.getProvinceCode());
            db.insert("province",null,values);
            values.clear();
        }
        db.close();
    }

    /**
     * 将市级列表存入数据库
     * @param citylist
     */
    public void initCityTable(List<City> citylist){
        SQLiteDatabase db = areaDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(City city:citylist){
            values.put("CityName",city.getCityName());
            values.put("ProvinceId",city.getProvinceId());
            values.put("CityId",city.getCityCode());
            db.insert("city",null,values);
            values.clear();
        }
        db.close();
    }

    /**
     * 将县级列表存入数据库
     * @param countyList
     */

    public void initCountyTable(List<County>countyList,int ProvinceId){
        SQLiteDatabase db = areaDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(County county:countyList){
            values.put("CountyName",county.getCountyName());
            values.put("CityId",county.getCityId());
            values.put("WeatherId",county.getWeatherId());
            values.put("ProvinceId",ProvinceId);
            db.insert("county",null,values);
            values.clear();
        }
        db.close();
    }
   public List<Province> queryProvince(){
       List<Province> provinceList = new ArrayList<>();
       SQLiteDatabase db = areaDBHelper.getWritableDatabase();
       Cursor cursor = db.query("province",null,null,null,null,null,null);
       if(cursor.moveToFirst()){
           do{
               String provinceName = cursor.getString(cursor.getColumnIndex("ProvinceName"));
               int provinceCode = cursor.getInt(cursor.getColumnIndex("ProvinceId"));
               Province province = new Province();
               province.setProvinceCode(provinceCode);
               province.setProvinceName(provinceName);
               provinceList.add(province);
           }while (cursor.moveToNext());
       }
        cursor.close();
       db.close();
       return provinceList;
   }
   public List<City> queryCity(int provinceId){


       List<City> cityList = new ArrayList<>();
       SQLiteDatabase db = areaDBHelper.getWritableDatabase();
       Cursor cursor = db.query("city",null,"ProvinceId = "+ provinceId,null,null,null,null);
       if(cursor.moveToFirst()){
           do{
           String cityName = cursor.getString(cursor.getColumnIndex("CityName"));
               int cityId = cursor.getInt(cursor.getColumnIndex("CityId"));
          City city = new City();
           city.setCityCode(cityId);
           city.setCityName(cityName);
           city.setProvinceId(provinceId);
           cityList.add(city);
            }while (cursor.moveToNext());
       }
       cursor.close();
       db.close();
       return cityList;
   }
    public List<County> querycountry(int provinceId,int cityId){
        List<County> countylist = new ArrayList<>();
        SQLiteDatabase db = areaDBHelper.getWritableDatabase();
        Cursor cursor = db.query("county",null,"CityId = "+ cityId + " and ProvinceId = "+ provinceId ,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                String countyName = cursor.getString(cursor.getColumnIndex("CountyName"));
                String weatherId = cursor.getString(cursor.getColumnIndex("WeatherId"));
                County county = new County();
                county.setCityId(cityId);
                county.setCountyName(countyName);
                county.setWeatherId(weatherId);
                countylist.add(county);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return countylist;
    }



}