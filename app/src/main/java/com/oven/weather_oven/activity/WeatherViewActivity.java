package com.oven.weather_oven.activity;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.oven.weather_oven.R;
import com.oven.weather_oven.base.MyApplication;
import com.oven.weather_oven.bean.Weather;
import com.oven.weather_oven.fragment.WeatherFragment;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;
import com.oven.weather_oven.util.VolleyResponseCallbackListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

//把请求的逻辑放入 activity 内， fragments 用于展示

public class WeatherViewActivity extends FragmentActivity {
    public DrawerLayout mDrawerLayout;
    public Button mChooseButton;
    public ViewPager mViewPager;
    public String mWeatherId;
    public SwipeRefreshLayout mSwipeRefresh;
    private WeatherHandler handler;
    public List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fragmentManager;
    private Weather preWeather;

    private static final String WEATHER_API = "https://free-api.heweather.com/v5/weather?city=";
    private static final String KEY = "&key = 4da8587ec7104e7a94a6d623607b334f";
    public static final int UPDATE = 0;

    private static class WeatherHandler extends Handler {
        private WeakReference<WeatherViewActivity> mReference;

        private WeatherHandler(WeatherViewActivity activity) {
            mReference = new WeakReference<>(activity);
        }

        public void handleMessage(Message meg) {
            WeatherViewActivity mActivity = mReference.get();
            switch (meg.what) {
                case UPDATE:
                   // mActivity.mSwipeRefresh.setRefreshing(false);

                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_view);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mChooseButton = (Button) findViewById(R.id.choose_area_btn_title);
        mViewPager = (ViewPager) findViewById(R.id.pager_weather_vp_weather_view);

        fragmentManager = getSupportFragmentManager();

        //点击按钮打开侧栏
        mChooseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(GravityCompat.START);
            }
        });

        //从 intent 中取出 weatherId 判空，初次由选择界面跳转
        if (getIntent() != null) {
            mWeatherId = getIntent().getStringExtra("weather_id");
            getWeather(mWeatherId);
            refresh(mWeatherId);
        }
        //TODO:更新操作
        // mSwipeRefresh.setColorSchemeResources(R.color.Primary);
//
//        mSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                requestWeather(mWeatherId);
//            }
//        });
//        getWeather(mWeatherId);
//    }
    }

    public void refresh(String idFromFragment) {
        Toast.makeText(this, idFromFragment, Toast.LENGTH_SHORT).show();

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new Fragment1());
        fragments.add(new Fragment2());
        fragments.add(new WeatherFragment());
        FragAdapter adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        mViewPager.setAdapter(adapter);
    }


//            //TODO:此处以上没问题
//             WeatherFragment fragment = WeatherFragment.newInstance(idFromFragment);
////            fragments.add(fragment);
////            WeatherViewAdapter weatherViewAdapter = new WeatherViewAdapter(fragmentManager,fragments);
////            mViewPager.setAdapter(weatherViewAdapter);
//            FragmentTransaction transaction = fragmentManager.beginTransaction();
//            transaction.add(fragment,"__");
//            transaction.commit();


    public void getWeather(final String weatherId) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(MyApplication.getContext());
        String mResponse = pref.getString("weather", null);
        if (mResponse == null) {
            requestWeather(weatherId);
        } else {
            preWeather = JSONUtil.handleWeatherResponse(mResponse);

        }
    }
    //请求对应 id 的信息并存入 SP
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
                meg.what = UPDATE;
                handler.sendMessage(meg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }
}





