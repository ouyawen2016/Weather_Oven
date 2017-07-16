package com.oven.weather_oven.Base;

import android.app.Application;
import android.content.Context;

/**
 * 提供一个全局context
 * Created by oven on 2017/5/7.
 */

public class ContextProvider extends Application {
    private  static Context mContext;
        public void onCreate(){
            super.onCreate();
            mContext= getApplicationContext();
            }
        public static Context getContext(){
            return mContext;
            }

    }
