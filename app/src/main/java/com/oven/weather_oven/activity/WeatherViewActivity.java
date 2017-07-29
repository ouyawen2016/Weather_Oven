package com.oven.weather_oven.activity;

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
import com.oven.weather_oven.adapter.WeatherViewAdapter;

import java.util.ArrayList;
import java.util.List;

public class WeatherViewActivity extends FragmentActivity {
    public DrawerLayout mDrawerLayout;
    public Button mChooseButton;
    public ViewPager mViewPager;
    public String mWeatherId;
    public List<Fragment> fragments = new ArrayList<>();
    private FragmentManager fragmentManager;


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
            refresh(mWeatherId);
        }
    }

        public void refresh(String idFromFragment){
            Toast.makeText(this, idFromFragment, Toast.LENGTH_SHORT).show();

            WeatherFragment fragment = WeatherFragment.newInstance(idFromFragment);

            //TODO:此处以上没问题

//            fragments.add(fragment);
//            WeatherViewAdapter weatherViewAdapter = new WeatherViewAdapter(fragmentManager,fragments);
//            mViewPager.setAdapter(weatherViewAdapter);
            Fragment

        }
}






