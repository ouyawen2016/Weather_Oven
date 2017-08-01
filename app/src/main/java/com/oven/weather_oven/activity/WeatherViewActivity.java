package com.oven.weather_oven.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.Toast;
import com.oven.weather_oven.R;
import com.oven.weather_oven.adapter.WeatherViewAdapter;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
public class WeatherViewActivity extends AppCompatActivity {

    public String mWeatherId;
    public List<Fragment> fragments = new ArrayList<>();
    @BindView(R.id.choose_area_btn_title)
    Button mBtnChooseArea;
    @BindView(R.id.pager_weather_vp_weather_view)
    ViewPager mVPWeatherView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_view);
        ButterKnife.bind(this);

        //从 intent 中取出 weatherId 判空，初次由选择界面跳转
        if (getIntent() != null) {
            mWeatherId = getIntent().getStringExtra("weather_id");
            refresh(mWeatherId);
        }
    }

    public void refresh(String idFromFragment) {
        Toast.makeText(this, idFromFragment, Toast.LENGTH_SHORT).show();
        WeatherFragment fragment = WeatherFragment.newInstance(idFromFragment);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragments.add(fragment);
        WeatherViewAdapter weatherViewAdapter = new WeatherViewAdapter(fragmentManager, fragments);
        mVPWeatherView.setAdapter(weatherViewAdapter);
    }

    //点击按钮打开侧栏
    @OnClick(R.id.choose_area_btn_title)
    public void onViewClicked() {
        mDrawerLayout.openDrawer(GravityCompat.START);
    }

}






