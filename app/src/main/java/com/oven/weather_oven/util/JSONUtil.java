package com.oven.weather_oven.util;

import android.text.TextUtils;

import com.oven.weather_oven.bean.City;
import com.oven.weather_oven.bean.County;
import com.oven.weather_oven.bean.Province;
import com.oven.weather_oven.bean.Weather;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * 解析服务器返回的json数据并处理
 * Created by oven on 2017/7/11.
 */

public class JSONUtil {

    /**
     * 将返回的省级数据用jsonArray与jsonObject解析,并返回provinceList
     */

    public static List<Province> handleProvinceResponse(String response) {
        List<Province> provincesList = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            JSONArray allProvinces;
            try {
                allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provincesObject;
                    provincesObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provincesObject.getString("name"));
                    province.setProvinceCode(provincesObject.getInt("id"));
                    provincesList.add(province);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return provincesList;
    }

    /**
     * 将返回的城市级数据用jsonArray与jsonObject解析,并返回cityList
     */

    public static  List<City> handleCityResponse(String response, int provinceId) {
        List<City> cityList = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i = 0; i < allCities.length(); i++) {
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(provinceId);
                    cityList.add(city);
                }
                return cityList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 将返回的县级数据用jsonArray与jsonObject解析,并返回countyList
     */

    public static  List<County> handleCountyResponse(String response, int cityId) {
        List<County> countyList = new ArrayList<>();
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setWeatherId(countyObject.getString("weather_id"));
                    county.setCityId(cityId);
                    countyList.add(county);
                }
                return countyList;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
    //如果使用GSON这里可以先一键生成javaBean
    public static Weather handleWeatherResponse(String response) {
        try {
            JSONObject mHeWeather = new JSONObject(response);
            JSONArray HeWeather = mHeWeather.getJSONArray("HeWeather5");
            JSONObject weatherContent = HeWeather.getJSONObject(0);
            Weather weather = new Weather();
            weather.setStatus(weatherContent.getString("status"));
            //Basic
            JSONObject JSONbasic = weatherContent.getJSONObject("basic");
            Weather.Basic basic = weather.new Basic();
            basic.setCity(JSONbasic.getString("city"));
            basic.setId(JSONbasic.getString("id"));
            weather.setBasics(basic);

            //TODO:AQI,SUGGESTION,NOW 


            /*
            JSONObject aqi = weatherContent.getJSONObject("aqi");
            Weather.aqi aqi1 = weather.new aqi();
            aqi1.(JSONbasic.getString("city"));
            aqi1.setId(JSONbasic.getString("id"));
            weather.setBasics(basic);
            JSONObject basic = weatherContent.getJSONObject("basic");
            JSONObject basic = weatherContent.getJSONObject("basic");
            JSONObject basic = weatherContent.getJSONObject("basic");
            */
            JSONObject nTep = weatherContent.getJSONObject("now");
            weather.temperature = nTep.getString("tmp");

            //Forecast
            List<Weather.DailyForecast> forecasts = new ArrayList<>();
            JSONArray dailyForecast = weatherContent.getJSONArray("daily_forecast");
            for(int i = 0;i < dailyForecast.length();i ++){
                JSONObject daily = dailyForecast.getJSONObject(i);
                Weather.DailyForecast dailyForecast1 =  weather.new DailyForecast();
                dailyForecast1.setDate(daily.getString("date"));
                JSONObject cond = daily.getJSONObject("cond");
                dailyForecast1.setMore(cond.getString("txt_d"));
                JSONObject tmp = daily.getJSONObject("tmp");
                dailyForecast1.setTmp(tmp.getString("max"),tmp.getString("min"));
               forecasts.add(dailyForecast1);
            }
            weather.setDailyForecasts(forecasts);

            return weather;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}



