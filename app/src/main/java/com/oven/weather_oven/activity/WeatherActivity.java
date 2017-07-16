package com.oven.weather_oven.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.oven.weather_oven.Base.ActivityCollector;
import com.oven.weather_oven.Base.BaseActivity;

import com.oven.weather_oven.R;
import com.oven.weather_oven.bean.Weather;
import com.oven.weather_oven.service.AutoUpdateService;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;

import java.io.IOException;



/**
 *
 * Created by oven on 2017/7/13.
 */


public class WeatherActivity extends BaseActivity {
    private ScrollView weatherLayout;
    private TextView title;
    private TextView degree;
    private LinearLayout forecastLayout;
    public String response;
    private Weather preWeather;
    public SwipeRefreshLayout swipeRefresh;
    private WeatherReceiver mWeatherBroadcast;

    public static final int SUCESS = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message meg) {
            switch (meg.what) {
                case SUCESS:
                    swipeRefresh.setRefreshing(false);
                    showWeather(preWeather);
                    break;
                default:
                    break;

            }
        }

    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        weatherLayout = (ScrollView) findViewById(R.id.weather_sv);
        title = (TextView) findViewById(R.id.title_tv_);
        degree = (TextView) findViewById(R.id.degree_tv_weatherTitle);
        forecastLayout = (LinearLayout) findViewById(R.id.forecast_ll);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        final String weatherID = getIntent().getStringExtra("weather_id");
        weatherLayout.setVisibility(View.INVISIBLE);
        getWeather(weatherID);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getWeather(weatherID);
            }
        });


    }

    public void getWeather(final String weatherId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = HttpUtil.getWeather(weatherId);
                    preWeather = JSONUtil.handleWeatherResponse(response);

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Message meg = new Message();
                meg.what = SUCESS;
                handler.sendMessage(meg);

            }
        }).start();


    }

    public void showWeather(Weather weather) {
        String cityName = weather.basics.getCity();
        String weatherId = weather.basics.getId();
        String degeree = weather.temperature + "℃";
        title.setText(cityName);
        degree.setText(degeree);
        forecastLayout.removeAllViews();
        for (Weather.DailyForecast dailyForecast : weather.dailyForecasts) {
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item, forecastLayout, false);
            TextView dateText = (TextView) view.findViewById(R.id.day1_tv_forecastItem);
            TextView max = (TextView) view.findViewById(R.id.day2_tv_forecastItemtextView2);
            TextView min = (TextView) view.findViewById(R.id.day3_tv_forecastItemtextView3);
            TextView info = (TextView) view.findViewById(R.id.day4_tv_forecastItemtextView4);
            dateText.setText(dailyForecast.getDate());
            max.setText(dailyForecast.getMax());
            min.setText(dailyForecast.getMin());
            info.setText(dailyForecast.getMore());
            forecastLayout.addView(view);

        }
        weatherLayout.setVisibility(View.VISIBLE);

        /*
         * 更新一次天气状况后开启AutoUpdateService
         */

        Intent intent = new Intent(this, AutoUpdateService.class);
        intent.putExtra("weather_id", weatherId);
        startService(intent);
    }


    @Override
    protected void onStart() {
       /*
        * 创建并动态注册广播接收器
        */
        mWeatherBroadcast = new WeatherReceiver();
        IntentFilter filter = new IntentFilter();// 过滤出更新天气的广播
        filter.addAction("WEATHER_UPDATE_ACTION");
        registerReceiver(mWeatherBroadcast, filter);// 注册广播
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mWeatherBroadcast);//注销广播接收器
        stopService(new Intent(WeatherActivity.this, AutoUpdateService.class));//停止前台服务
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        ActivityCollector.finishAll();
        Toast.makeText(WeatherActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
    }


    class WeatherReceiver extends BroadcastReceiver {
        public WeatherReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播后开启weatherActivity的getWeather,
            try {
                Weather mweather =  JSONUtil.handleWeatherResponse(HttpUtil.getWeather(intent.getStringExtra("weather_id")));

                intent.putExtra("degree",mweather.temperature);
                intent.putExtra("city",mweather.new Basic().getCity());
            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
}



