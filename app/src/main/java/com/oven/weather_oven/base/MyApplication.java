package com.oven.weather_oven.base;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


/**
 * 提供一个全局context，进行一些全局的操作
 * Created by oven on 2017/5/7.
 */

public class MyApplication extends Application {
    //application本就是生命周期伴随整个app的类，不存在内存泄漏
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    private static RequestQueue mQueue;
    public void onCreate(){
        super.onCreate();
        mContext = getApplicationContext();
        mQueue = Volley.newRequestQueue(mContext);

    }
    public static Context getContext() {
        return mContext;
    }
    public static RequestQueue getQueue(){
        return mQueue;
    }







}
