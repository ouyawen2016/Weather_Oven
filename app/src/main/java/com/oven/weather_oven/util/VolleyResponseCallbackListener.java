package com.oven.weather_oven.util;

/**
 *监听volley数据的接口
 *
 * Created by oven on 2017/7/18.
 */

public interface VolleyResponseCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
