package com.oven.weather_oven.activity;

//import android.content.BroadcastReceiver;
//import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
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

import java.lang.ref.WeakReference;


/**
 *
 * Created by oven on 2017/7/13.
 */


public class WeatherActivity extends BaseActivity {
    private ScrollView mWeatherLayout;

    private TextView mUpdate;
    private TextView mSport;
    private TextView mDress;
    private TextView mComfortable;
    private TextView mSportTip;
    private TextView mDressTip;
    private TextView mComfortableTip;
    private TextView mDegree;
    private TextView mAqi;
    private TextView mWind;
    private TextView mFeeling;
    private TextView mCity;
    private TextView mWeatherBrf;
    private DrawerLayout mDrawerLayout;


    private Weather preWeather;
    public SwipeRefreshLayout mSwipeRefresh;
    // private WeatherReceiver mWeatherBroadcast;
    private WeatherHandler handler;

    private static final String WEATHER_API = "https://free-api.heweather.com/v5/weather?city=";
    private static final String KEY = "&key=4da8587ec7104e7a94a6d623607b334f";

    public static final int SUCCESS = 0;
    private static class WeatherHandler extends Handler{
        private WeakReference<WeatherActivity> mReference;
        private WeatherHandler(WeatherActivity activity){
            mReference = new WeakReference<>(activity);
        }

        public void handleMessage(Message meg) {
            WeatherActivity mActivity = mReference.get();
            switch (meg.what) {
                case SUCCESS:
                    mActivity.mSwipeRefresh.setRefreshing(false);
                    mActivity.showWeather(mActivity.preWeather);
                    break;
                default:
                    break;

            }
        }

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather);
        mWeatherLayout = (ScrollView) findViewById(R.id.weather_sv);
        mUpdate = (TextView) findViewById(R.id.update_title_tv_title);
        mAqi = (TextView)findViewById(R.id.aqi);
        mCity = (TextView)findViewById(R.id.city_tv_now);
        mDegree =(TextView)findViewById(R.id.degree_tv_now);
        mFeeling =(TextView)findViewById(R.id.feelingTmp_tv_now);
        mWind =(TextView)findViewById(R.id.wind);
        mWeatherBrf = (TextView)findViewById(R.id.weather_brf_tv_now);

        mComfortable = (TextView)findViewById(R.id.feeling_txt_tv_suggestions);
        mSport = (TextView)findViewById(R.id.sport_txt_tv_suggestions);
        mComfortableTip = (TextView)findViewById(R.id.feeling_title_tv_suggestions);
        mSportTip = (TextView)findViewById(R.id.sport_title_tv_suggestions);
        mDress = (TextView)findViewById(R.id.dress_txt_tv_suggestions);
        mDressTip = (TextView)findViewById(R.id.dress_title_tv_suggestions);
        mSwipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh);

        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

       Button mChooseButton = (Button)findViewById(R.id.choose_area_btn_title);
        mChooseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        handler = new WeatherHandler(this);

        final String weatherId = getIntent().getStringExtra("weather_id");

        mWeatherLayout.setVisibility(View.INVISIBLE);

        getWeather(weatherId);

        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(weatherId);
            }
        });




    }
    public void  getWeather  (final String weatherId){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this);
        String mResponse = pref.getString("weather",null);
        if(mResponse == null){
            requestWeather(weatherId);
        }
        else {
            preWeather = JSONUtil.handleWeatherResponse(mResponse);
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
                meg.what = SUCCESS;
                handler.sendMessage(meg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void showWeather(Weather weather) {

        String weatherId = weather.basic.id;
        String degree = weather.now.tmp+ "℃";
        String feeling = weather.suggestion.comf.txt;
        String feelingTip = weather.suggestion.comf.brf;
        String sport = weather.suggestion.sport.txt;
        String sportTip = weather.suggestion.sport.brf;
        String dress = weather.suggestion.drsg.txt;
        String dressTip = weather.suggestion.drsg.brf;
        String update = weather.basic.update.loc;
        String feelingTmp = "体感温度: " + weather.now.fl + "℃";
        String wind = weather.now.wind.dir +", " +weather.now.wind.sc+", "+ weather.now.wind.spd +" km/h";
        String aqi = "空气质量： "+ weather.aqi.city.qlty + ", 相对湿度： "+ weather.now.hum+"%";
        String city = weather.basic.city+", "+weather.basic.cnty;
        String weatherBrf = weather.now.cond.txt;

        mDegree.setText(degree);
        mComfortable.setText(feeling);
        mComfortableTip.setText(feelingTip);
        mDress.setText(dress);
        mDressTip.setText(dressTip);
        mSport.setText(sport);
        mSportTip.setText(sportTip);
        mUpdate.setText(update);
        mCity.setText(city);
        mWeatherBrf.setText(weatherBrf);
        mAqi.setText(aqi);
        mFeeling.setText(feelingTmp);
        mWind.setText(wind);




        RecyclerView mForecastView = (RecyclerView)findViewById(R.id.forecast_rv_forecast_list) ;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mForecastView.setLayoutManager(layoutManager);
        ForecastAdapter mForecastAdapter = new ForecastAdapter(weather.dailyForecast);
        mForecastView.setAdapter(mForecastAdapter);
        mForecastView.addItemDecoration(new AreaDividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        mWeatherLayout.setVisibility(View.VISIBLE);

        /*
         * 更新一次天气状况后开启AutoUpdateService
         */

        Intent intent = new Intent(this, AutoUpdateService.class);
        intent.putExtra("weather_id", weatherId);
        //startService(intent);
    }

/*
    @Override
    protected void onStart() {
       /*
        * 创建并动态注册广播接收器

       /* mWeatherBroadcast = new WeatherReceiver();
        IntentFilter filter = new IntentFilter();// 过滤出更新天气的广播
        filter.addAction("WEATHER_UPDATE_ACTION");
        registerReceiver(mWeatherBroadcast, filter);// 注册广播
        super.onStart();


    }
    */
/*
    @Override
    protected void onDestroy() {
        //unregisterReceiver(mWeatherBroadcast);//注销广播接收器
        stopService(new Intent(WeatherActivity.this, AutoUpdateService.class));//停止前台服务
        super.onDestroy();
    }*/

    @Override
    public void onBackPressed() {
        ActivityCollector.finishAll();
        Toast.makeText(WeatherActivity.this, "再按一次退出", Toast.LENGTH_SHORT).show();
    }
/*

    class WeatherReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            //收到广播后开启weatherActivity的getWeather,

              getWeather(intent.getStringExtra("weather_id"));
                Weather mWeather =  preWeather;

                intent.putExtra("degree",mWeather.now.tmp);
                intent.putExtra("city",mWeather.basic.city);
*/


        }





