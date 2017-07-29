package com.oven.weather_oven.activity;

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
import android.widget.ScrollView;
import android.widget.TextView;
import com.oven.weather_oven.adapter.AreaDividerItemDecoration;
import com.oven.weather_oven.adapter.ForecastAdapter;
import com.oven.weather_oven.R;
import com.oven.weather_oven.base.MyApplication;
import com.oven.weather_oven.bean.Weather;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;
import com.oven.weather_oven.util.VolleyResponseCallbackListener;
import java.lang.ref.WeakReference;

/**
 * 天气详情碎片
 * Created by oven on 2017/7/13.
 */
public class WeatherFragment extends Fragment {
    //新建一个有参构造器

    public static WeatherFragment newInstance(String weatherId){
        WeatherFragment fragment = new WeatherFragment();
        Bundle bundle = new Bundle();
        bundle.putString("weather_id",weatherId);
        fragment.setArguments(bundle);
        return  fragment;
    }

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
    private String weatherId;
    private Bitmap mWeatherPic;
    private ImageView mWeatherIcon;
    private View view;
    private Weather preWeather;
    public SwipeRefreshLayout mSwipeRefresh;
    private WeatherHandler handler;

    private static final String WEATHER_API = "https://free-api.heweather.com/v5/weather?city=";
    private static final String KEY = "&key=4da8587ec7104e7a94a6d623607b334f";
    public static final int SUCCESS = 0;

    private static class WeatherHandler extends Handler{
        private WeakReference<WeatherFragment> mReference;
        private WeatherHandler(WeatherFragment activity){
            mReference = new WeakReference<>(activity);
        }
        public void handleMessage(Message meg) {
            WeatherFragment mActivity = mReference.get();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,  Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_weather_view,container,false);
        initView();
        //从来自 activity 的 bundle 中得到信息
       // mSwipeRefresh.setColorSchemeResources(R.color.colorPrimary);

//        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                requestWeather(weatherId);
//            }
//        });
       return view;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(getArguments()!=null){
            weatherId = getArguments().getString("weather_id");
        }

        handler = new WeatherHandler(this);




       //mWeatherLayout.setVisibility(View.INVISIBLE);

        getWeather(weatherId);


    }
    public void  getWeather  (final String weatherId){
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
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
                        getDefaultSharedPreferences(MyApplication.getContext()).edit();
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
        weatherId = weather.basic.id;
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
        String aqi = "能见度: " + weather.now.vis + " m ," + "相对湿度： " + weather.now.hum+"%";
        String city = weather.basic.city+", " + weather.basic.cnty;
        String weatherBrf = weather.now.cond.txt;
        String weatherCode = weather.now.cond.code;

       // mDegree.setText(degree);
      // mComfortable.setText(feeling);
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
        initWeatherPic(weatherCode);
        mWeatherIcon.setImageBitmap(mWeatherPic);


        RecyclerView mForecastView = (RecyclerView)view.findViewById(R.id.forecast_rv_forecast_list);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mForecastView.setLayoutManager(layoutManager);
        ForecastAdapter mForecastAdapter = new ForecastAdapter(weather.dailyForecast);
        mForecastView.setAdapter(mForecastAdapter);
        mForecastView.addItemDecoration(new AreaDividerItemDecoration(getActivity(),LinearLayoutManager.VERTICAL));
        mWeatherLayout.setVisibility(View.VISIBLE);

        /*
         * 更新一次天气状况后开启AutoUpdateService
         */

        //Intent intent = new Intent(this, AutoUpdateService.class);
        //intent.putExtra("weather_id", weatherId);
        //startService(intent);
    }
    private void initWeatherPic(final String code){
        new Thread() {
        public void run() {
            mWeatherPic = HttpUtil.getImageBitmap(code);
        }}.start();
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
        stopService(new Intent(WeatherFragment.this, AutoUpdateService.class));//停止前台服务
        super.onDestroy();
    }*/


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
    mSwipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh);
    }

}





