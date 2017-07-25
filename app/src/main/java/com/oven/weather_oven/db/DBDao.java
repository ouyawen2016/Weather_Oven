package com.oven.weather_oven.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.oven.weather_oven.base.MyApplication;
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

    private AreaDBHelper areaDBHelper = new AreaDBHelper(MyApplication.getContext());
    /**
     * 将省级列表存入数据库
     * @param provinceList
     * 传入省级列表
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
     * @param cityList
     * 传入城市列表
     */
    public void initCityTable(List<City> cityList){
        SQLiteDatabase db = areaDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        for(City city:cityList){
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
     * 传入县级列表
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

    /**
     * 从数据库中查询省份
     * @return List<Province>
     */
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

    /**
     * @param provinceId
     * 传入的省份ID
     * @return List<City> 城市列表
     */


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

    /**
     *
     * @param provinceId
     * 省份ID
     * @param cityId
     * 城市ID
     *@return list<County><
     */
    public List<County> queryCountry(int provinceId,int cityId){
        List<County> countyList = new ArrayList<>();
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
                countyList.add(county);
            }while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return countyList;
    }



}
