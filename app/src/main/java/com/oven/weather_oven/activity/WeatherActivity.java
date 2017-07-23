package com.oven.weather_oven.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.oven.weather_oven.adapter.AreaDividerItemDecoration;
import com.oven.weather_oven.adapter.ForecastAdapter;
import com.oven.weather_oven.base.ActivityCollector;
import com.oven.weather_oven.base.BaseActivity;
import com.oven.weather_oven.R;
import com.oven.weather_oven.bean.Weather;
import com.oven.weather_oven.service.AutoUpdateService;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;
import com.oven.weather_oven.util.VolleyResponseCallbackListener;


/**
 *
 * Created by oven on 2017/7/13.
 */


public class WeatherActivity extends BaseActivity {
    private ScrollView mweatherLayout;
    private TextView mtitle;
    private TextView mdegree;
    private LinearLayout mforecastLayout;
    public String mresponse;
    private Weather preWeather;
    public SwipeRefreshLayout mswipeRefresh;
    private WeatherReceiver mWeatherBroadcast;
    private Button mbutton;
    private RecyclerView mForecastview;
    private ForecastAdapter mForecastadapter;
    private static final String WEATHER_API = "https://free-api.heweather.com/v5/weather?city=";
    private static final String KEY = "&key=4da8587ec7104e7a94a6d623607b334f";

    public static final int SUCESS = 0;
    private Handler handler = new Handler() {
        public void handleMessage(Message meg) {
            switch (meg.what) {
                case SUCESS:
                    mswipeRefresh.setRefreshing(false);
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
        mweatherLayout = (ScrollView) findViewById(R.id.weather_sv);
        mtitle = (TextView) findViewById(R.id.title_tv_);
        mdegree = (TextView) findViewById(R.id.degree_tv_weatherTitle);
        mforecastLayout = (LinearLayout) findViewById(R.id.forecast_ll);
        mswipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);
        mswipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        mbutton = (Button)findViewById(R.id.choose_area_btn_title);

         final String weatherID = getIntent().getStringExtra("weather_id");

        mweatherLayout.setVisibility(View.INVISIBLE);

        getWeather(weatherID);

        mswipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherID);
            }
        });




    }
    public void  getWeather  (final String weatherId){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        mresponse = pref.getString("weather",null);
        if(mresponse == null){
            requestWeather(weatherId);
        }
        else {
            preWeather = JSONUtil.handleWeatherResponse(mresponse);
            showWeather(preWeather);
        }
    }

    public void requestWeather(final String weatherId) {
        String WeatherAddress = WEATHER_API + weatherId + KEY;
        HttpUtil.sendHttpRequest(WeatherAddress, new VolleyResponseCallbackListener() {
            @Override
            public void onFinish(String response) {
                SharedPreferences.Editor editor =PreferenceManager.
                        getDefaultSharedPreferences(WeatherActivity.this).edit();
                editor.putString("weather",response);
                editor.apply();
                preWeather = JSONUtil.handleWeatherResponse(response);
                Message meg = new Message();
                meg.what = SUCESS;
                handler.sendMessage(meg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showWeather(Weather weather) {
        String cityName = weather.basic.city;
        String weatherId = weather.basic.id;
        String degeree = weather.now.tmp+ "℃";
        mtitle.setText(cityName);
        mdegree.setText(degeree);

        // mforecastLayout.removeAllViews();

        mForecastview = (RecyclerView)findViewById(R.id.forecast_rv_forecast_list) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mForecastview.setLayoutManager(layoutManager);
        mForecastadapter = new ForecastAdapter(weather.dailyForecast);
        mForecastview.setAdapter(mForecastadapter);
        mForecastview.addItemDecoration(new AreaDividerItemDecoration(this,LinearLayoutManager.VERTICAL));

        mweatherLayout.setVisibility(View.VISIBLE);

        /*
         * 更新一次天气状况后开启AutoUpdateService
         */

        Intent intent = new Intent(this, AutoUpdateService.class);
        intent.putExtra("weather_id", weatherId);
        //startService(intent);
    }


    @Override
    protected void onStart() {
       /*
        * 创建并动态注册广播接收器
        */
       /* mWeatherBroadcast = new WeatherReceiver();
        IntentFilter filter = new IntentFilter();// 过滤出更新天气的广播
        filter.addAction("WEATHER_UPDATE_ACTION");
        registerReceiver(mWeatherBroadcast, filter);// 注册广播*/
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        //unregisterReceiver(mWeatherBroadcast);//注销广播接收器
        stopService(new Intent(WeatherActivity.this, AutoUpdateService.class));//停止前台服务
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        ActivityCollector.finishAll();
        Toast.makeText(WeatherActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
    }


    class WeatherReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播后开启weatherActivity的getWeather,

              getWeather(intent.getStringExtra("weather_id"));
                Weather mWeather =  preWeather;

                intent.putExtra("degree",mWeather.now.tmp);
                intent.putExtra("city",mWeather.basic.city);



        }
    }
}



