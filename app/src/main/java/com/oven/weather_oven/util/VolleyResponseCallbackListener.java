package com.oven.weather_oven.util;

/**
 *
 *
 * Created by oven on 2017/7/18.
 */

public interface VolleyResponseCallbackListener {
    void onFinish(String response);
    void onError(Exception e);
}
