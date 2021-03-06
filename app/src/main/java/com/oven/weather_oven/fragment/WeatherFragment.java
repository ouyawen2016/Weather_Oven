package com.oven.weather_oven.fragment;

//import android.content.BroadcastReceiver;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.oven.weather_oven.R;
import com.oven.weather_oven.adapter.AreaDividerItemDecoration;
import com.oven.weather_oven.adapter.ForecastAdapter;
import com.oven.weather_oven.base.MyApplication;
import com.oven.weather_oven.bean.Weather;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;
import com.oven.weather_oven.util.VolleyResponseCallbackListener;
import java.lang.ref.WeakReference;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 天气详情碎片
 * <p>
 * Created by oven on 2017/7/13.
 */
public class WeatherFragment extends Fragment {

<<<<<<< HEAD:app/src/main/java/com/oven/weather_oven/activity/WeatherFragment.java
    @BindView(R.id.degree_tv_now)
    TextView mTVDegree;
    @BindView(R.id.city_tv_now)
    TextView mTvCity;
    @BindView(R.id.weather_brf_tv_now)
    TextView mTVWeatherBrief;
    @BindView(R.id.weather_icon_iv_now)
    ImageView mIVWeatherIcon;
    @BindView(R.id.feelingTmp_tv_now)
    TextView mTVFeelingTmp;
    @BindView(R.id.wind)
    TextView mTVWind;
    @BindView(R.id.aqi)
    TextView mTVAqi;
    @BindView(R.id.weather_update_time_now)
    TextView mTVWeatherUpdateTime;
    @BindView(R.id.forecast_tv_forecast)
    TextView mTVForecastForecast;
    @BindView(R.id.forecast_rv_forecast_list)
    RecyclerView mRVForecastForecastList;
    @BindView(R.id.forecast_ll)
    LinearLayout mLLForecastList;
    @BindView(R.id.sport_title_tv_suggestions)
    TextView mTVSportTitleSuggestions;
    @BindView(R.id.sport_txt_tv_suggestions)
    TextView mTVSportTxtSuggestions;
    @BindView(R.id.dress_title_tv_suggestions)
    TextView mTVDressTitleSuggestions;
    @BindView(R.id.dress_txt_tv_suggestions)
    TextView mTVDressTxtSuggestions;
    @BindView(R.id.feeling_title_tv_suggestions)
    TextView mTVFeelingTitleSuggestions;
    @BindView(R.id.feeling_txt_tv_suggestions)
    TextView mTVFeelingTxtSuggestions;
    @BindView(R.id.swipe_refresh)
    SwipeRefreshLayout mSwipeRefresh;
    Unbinder unbinder;

    private Weather preWeather;
    private String mWeatherId;
    private Bitmap mWeatherPic;
    private WeatherHandler handler;

    private static final String WEATHER_API = "https://free-api.heweather.com/v5/weather?city=";
    private static final String KEY = "&key=4da8587ec7104e7a94a6d623607b334f";
    public static final int SUCCESS = 0;

    /*
     *  新建有参构造器
     *  @pram:weatherId
     *  碎片所需的天气id
     */
    public static WeatherFragment newInstance(String weatherId) {
        WeatherFragment fragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("weather_id", weatherId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    private static class WeatherHandler extends Handler {
        private WeakReference<WeatherFragment> mReference;

        private WeatherHandler(WeatherFragment fragment) {
            mReference = new WeakReference<>(fragment);
        }

        public void handleMessage(Message meg) {
            WeatherFragment fragment= mReference.get();

            switch (meg.what) {
                case SUCCESS:
                    fragment.mSwipeRefresh.setRefreshing(false);
                    fragment.showWeather(fragment.preWeather);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_weather, container, false);
        unbinder = ButterKnife.bind(this, view);
        mSwipeRefresh.setColorSchemeResources(R.color.Primary);
        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                requestWeather(mWeatherId);
            }
        });
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            mWeatherId = getArguments().getString("weather_id");
        }

        handler = new WeatherHandler(this);

=======
//    public static WeatherFragment newInstance(String weatherId){
//        WeatherFragment fragment = new WeatherFragment();
//        Bundle bundle = new Bundle();
//        bundle.putString("weather_id",weatherId);
//        fragment.setArguments(bundle);
//        return  fragment;
//    }

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
    private String weatherId ;
    private Bitmap mWeatherPic;
    private ImageView mWeatherIcon;
    private View view;
    private Weather preWeather;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_weather_view,container,false);
        initView();
        //从sp中得到信息
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        String mResponse = pref.getString("weather",null);
        preWeather = JSONUtil.handleWeatherResponse(mResponse);
        showWeather(preWeather);
        return view;
    }
//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        if(getArguments()!=null){
//            weatherId = getArguments().getString("weather_id");
//        }
//
//       //handler = new WeatherHandler(this);
//
//
//
//
//       mWeatherLayout.setVisibility(View.INVISIBLE);
//
//
//
//
//    }
>>>>>>> ed23025a6f15e34ed9aeb93286a26914bcb34162:app/src/main/java/com/oven/weather_oven/fragment/WeatherFragment.java

        //mWeatherLayout.setVisibility(View.INVISIBLE);

<<<<<<< HEAD:app/src/main/java/com/oven/weather_oven/activity/WeatherFragment.java
        getWeather(mWeatherId);


    }

    public void getWeather(final String weatherId) {
        //FIXME:如果使用缓存增加地区后显示异常 oven 170731
//        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
//        String mResponse = pref.getString("weather", null);
//        if (mResponse == null) {
//            requestWeather(weatherId);
//        } else {
//            preWeather = JSONUtil.handleWeatherResponse(mResponse);
//            showWeather(preWeather);
//        }
        requestWeather(weatherId);
    }

    public void requestWeather(final String weatherId) {
        String WeatherAddress = WEATHER_API + weatherId + KEY;
        HttpUtil.sendHttpRequest(WeatherAddress, new VolleyResponseCallbackListener() {
            @Override
            public void onFinish(String response) {
                SharedPreferences.Editor editor = PreferenceManager.
                        getDefaultSharedPreferences(MyApplication.getContext()).edit();
                editor.putString("weather", response);
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
        mWeatherId = weather.basic.id;
=======



    public void showWeather(Weather weather) {
        weatherId = weather.basic.id;
>>>>>>> ed23025a6f15e34ed9aeb93286a26914bcb34162:app/src/main/java/com/oven/weather_oven/fragment/WeatherFragment.java
        String degree = weather.now.tmp + "℃";
        String feeling = weather.suggestion.comf.txt;
        String feelingTip = weather.suggestion.comf.brf;
        String sport = weather.suggestion.sport.txt;
        String sportTip = weather.suggestion.sport.brf;
        String dress = weather.suggestion.drsg.txt;
        String dressTip = weather.suggestion.drsg.brf;
        String update = weather.basic.update.loc;
        String feelingTmp = "体感温度: " + weather.now.fl + "℃";
        String wind = weather.now.wind.dir + ", " + weather.now.wind.sc + ", " + weather.now.wind.spd + " km/h";
        String aqi = "能见度: " + weather.now.vis + " m ," + "相对湿度： " + weather.now.hum + "%";
        String city = weather.basic.city + ", " + weather.basic.cnty;
        String weatherBrf = weather.now.cond.txt;
        String weatherCode = weather.now.cond.code;

<<<<<<< HEAD:app/src/main/java/com/oven/weather_oven/activity/WeatherFragment.java
        mTVDegree.setText(degree);
        mTVFeelingTxtSuggestions.setText(feeling);
        mTVFeelingTitleSuggestions.setText(feelingTip);
        mTVDressTxtSuggestions.setText(dress);
        mTVDressTitleSuggestions.setText(dressTip);
        mTVSportTxtSuggestions.setText(sport);
        mTVSportTitleSuggestions.setText(sportTip);
        mTVWeatherUpdateTime.setText(update);
        mTvCity.setText(city);
        mTVWeatherBrief.setText(weatherBrf);
        mTVAqi.setText(aqi);
        mTVFeelingTmp.setText(feelingTmp);
        mTVWind.setText(wind);
        initWeatherPic(weatherCode);
        mIVWeatherIcon.setImageBitmap(mWeatherPic);

=======
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
        //initWeatherPic(weatherCode);
        mWeatherIcon.setImageBitmap(mWeatherPic);


        RecyclerView mForecastView = (RecyclerView) view.findViewById(R.id.forecast_rv_forecast_list);
>>>>>>> ed23025a6f15e34ed9aeb93286a26914bcb34162:app/src/main/java/com/oven/weather_oven/fragment/WeatherFragment.java
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRVForecastForecastList.setLayoutManager(layoutManager);
        ForecastAdapter mForecastAdapter = new ForecastAdapter(weather.dailyForecast);
<<<<<<< HEAD:app/src/main/java/com/oven/weather_oven/activity/WeatherFragment.java
        mRVForecastForecastList.setAdapter(mForecastAdapter);
        mRVForecastForecastList.addItemDecoration(new AreaDividerItemDecoration(getActivity(),
                LinearLayoutManager.VERTICAL));

=======
        mForecastView.setAdapter(mForecastAdapter);
        mForecastView.addItemDecoration(new AreaDividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        mWeatherLayout.setVisibility(View.VISIBLE);
>>>>>>> ed23025a6f15e34ed9aeb93286a26914bcb34162:app/src/main/java/com/oven/weather_oven/fragment/WeatherFragment.java

        /*
         * 更新一次天气状况后开启AutoUpdateService
         */

        //Intent intent = new Intent(this, AutoUpdateService.class);
        //intent.putExtra("weather_id", weatherId);
        //startService(intent);
    }
<<<<<<< HEAD:app/src/main/java/com/oven/weather_oven/activity/WeatherFragment.java
=======
//    private void initWeatherPic(final String code){
//        new Thread() {
//        public void run() {
//            mWeatherPic = HttpUtil.getImageBitmap(code);
//        }}.start();

>>>>>>> ed23025a6f15e34ed9aeb93286a26914bcb34162:app/src/main/java/com/oven/weather_oven/fragment/WeatherFragment.java

    private void initWeatherPic(final String code) {
        new Thread() {
            public void run() {
                mWeatherPic = HttpUtil.getImageBitmap(code);
            }
        }.start();
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

//FIXME:广播接收器
    }
    */
/*
    @Override
    protected void onDestroy() {
        //unregisterReceiver(mWeatherBroadcast);//注销广播接收器
        stopService(new Intent(WeatherFragment.this, AutoUpdateService.class));//停止前台服务
        super.onDestroy();
    }*/


<<<<<<< HEAD:app/src/main/java/com/oven/weather_oven/activity/WeatherFragment.java
    /*
=======
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
private void initView() {
    mWeatherLayout = (ScrollView) view.findViewById(R.id.weather_sv);
    mUpdate = (TextView)view.findViewById(R.id.update_title_tv_title);
    mAqi = (TextView)view.findViewById(R.id.aqi);
    mCity = (TextView)view.findViewById(R.id.city_tv_now);
    mDegree = (TextView)view.findViewById(R.id.degree_tv_now);
    mFeeling = (TextView)view.findViewById(R.id.feelingTmp_tv_now);
    mWind = (TextView)view.findViewById(R.id.wind);
    mWeatherBrf = (TextView)view.findViewById(R.id.weather_brf_tv_now);
    mComfortable = (TextView)view.findViewById(R.id.feeling_txt_tv_suggestions);
    mSport = (TextView)view.findViewById(R.id.sport_txt_tv_suggestions);
    mComfortableTip = (TextView)view.findViewById(R.id.feeling_title_tv_suggestions);
    mSportTip = (TextView)view.findViewById(R.id.sport_title_tv_suggestions);
    mDress = (TextView)view.findViewById(R.id.dress_txt_tv_suggestions);
    mDressTip = (TextView)view.findViewById(R.id.dress_title_tv_suggestions);
    mWeatherIcon = (ImageView)view.findViewById(R.id.weather_icon_iv_now) ;
    //mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
    }

}
>>>>>>> ed23025a6f15e34ed9aeb93286a26914bcb34162:app/src/main/java/com/oven/weather_oven/fragment/WeatherFragment.java

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