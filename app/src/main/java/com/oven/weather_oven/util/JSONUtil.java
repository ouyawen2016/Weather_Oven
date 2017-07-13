package com.oven.weather_oven.util;

import android.text.TextUtils;

import com.oven.weather_oven.bean.City;
import com.oven.weather_oven.bean.County;
import com.oven.weather_oven.bean.Province;


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
}
//


