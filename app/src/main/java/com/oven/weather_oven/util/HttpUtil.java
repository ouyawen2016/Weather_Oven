package com.oven.weather_oven.util;

/*
 * 向服务器发送网络请求，并返回需要的数据
 * Created by oven 2017/7/11.
 */

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpUtil {

    public static void sendHttpRequest(final String address,
                                        final VolleyResponseCallbackListener listener) {
        final StringRequest stringRequest = new StringRequest(address,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        listener.onFinish(response);
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        listener.onError(error);

            }
        });

      com.oven.weather_oven.base.MyApplication.getQueue().add(stringRequest);
    }




    public static Bitmap getImageBitmap(String weatherCode){
        final String url = "https://cdn.heweather.com/cond_icon/" + weatherCode + ".png";
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

/*
    public static Bitmap getImageBitmap(String weatherCode){
        final String url = "https://cdn.heweather.com/cond_icon/" + weatherCode + ".png";
        RequestFuture<Bitmap> future = RequestFuture.newFuture();
        ImageRequest imageRequest = new ImageRequest(url,);

    }
*/
}




