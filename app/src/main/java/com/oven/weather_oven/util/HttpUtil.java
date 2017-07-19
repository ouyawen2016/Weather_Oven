package com.oven.weather_oven.util;

/*
 * 向服务器发送网络请求，并返回需要的数据
 * Created by oven 2017/7/11.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.oven.weather_oven.base.ContextProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {
    private static final String PROVINCE = "http://guolin.tech/api/china";
    private static final String WEATHER_API = "https://free-api.heweather.com/v5/weather?city=";
    private static final String KEY = "&key=4da8587ec7104e7a94a6d623607b334f";
    private static final String BACK_SLASH = "/";



    public static String getProvince()throws IOException{
        return sendHttpRequest(PROVINCE);
    }
    public static String getCity(int provinceCode) throws IOException {
        String CITY = PROVINCE + BACK_SLASH + provinceCode;
        return sendHttpRequest(CITY);
    }
    public static String getCounty(int provinceCode,int CityCode)throws IOException{
        String COUNTY = PROVINCE + BACK_SLASH + provinceCode + BACK_SLASH +CityCode ;
        return sendHttpRequest(COUNTY);
    }
    public static String getWeather(String weatherId) throws IOException{
        String WEATHER = WEATHER_API + weatherId + KEY;
        return sendHttpRequest(WEATHER);
    }
    private static String sendHttpRequest(final String address) throws IOException {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(8000);
            connection.setReadTimeout(8000);
            connection.setDoInput(true);
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                InputStream in = connection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();
                return response.toString();
            } else {
                throw new IOException("Network Error - response code:" + connection.getResponseCode());
            }
        }finally {
            if (connection != null) {
                connection.disconnect();
            }
        }


    }
/* String WEATHER = WEATHER_API + weatherId + KEY;
        return sendHttpRequest(WEATHER);
    }
    private static String sendHttpRequest(final String address)  {
        RequestQueue mQueue = Volley.newRequestQueue(ContextProvider.getContext());

        final StringRequest stringRequest = new StringRequest(address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        final String mWeatherContext = response;
                        //TODO：存入shareperfence或写个接口
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG",error.getLocalizedMessage());
            }
        });
        */




    public static Bitmap getImageBitmap(String url){
        Bitmap bitmap = null;
        InputStream is = null;
        try {
            URL imgUrl = new URL(url);
             HttpURLConnection conn = (HttpURLConnection)imgUrl.openConnection();
             conn.setDoInput(true);
             conn.connect();
             is = conn.getInputStream();
             bitmap = BitmapFactory.decodeStream(is);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return bitmap;
    }

}




