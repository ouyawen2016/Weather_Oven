package com.oven.weather_oven.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.oven.weather_oven.adapter.AreaAdapter;
import com.oven.weather_oven.adapter.AreaDividerItemDecoration;
import com.oven.weather_oven.base.ActivityCollector;
import com.oven.weather_oven.base.BaseActivity;
import com.oven.weather_oven.R;
import com.oven.weather_oven.bean.City;
import com.oven.weather_oven.bean.County;
import com.oven.weather_oven.bean.Province;
import com.oven.weather_oven.db.DBDao;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;
import com.oven.weather_oven.util.VolleyResponseCallbackListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO:做下切换按钮
public class ChooseAreaActivity extends BaseActivity {
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;
    private static final int FAILURE = 3;
    //定义各种控件

    private TextView titleTxt;
    private Button backBtn;

    private RecyclerView mareaRecyclerview;
    private AreaAdapter areaAdapter;
    private List<String> areaList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectProvince;
    private City selectCity;
    public int selectLevel;
    private String response;
    private DBDao areaDao;
    private static final String PROVINCE = "http://guolin.tech/api/china";

    private static final String BACK_SLASH = "/";

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LEVEL_PROVINCE:
                    showProvinceList();

                    break;
                case LEVEL_CITY:
                    showCItyList();

                    break;
                case LEVEL_COUNTY:
                    showCountyList();

                    break;
                case FAILURE:
                    //closePressDialog();

                    Toast.makeText(ChooseAreaActivity.this, "加载失败，请检查网络", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }

        }

    };

    private void showCountyList() {
        //closePressDialog();

        backBtn.setVisibility(View.VISIBLE);
        areaList.clear();
        for (County county : countyList) {
            areaList.add(county.getCountyName());
        }
        titleTxt.setText(selectCity.getCityName());
        areaAdapter.notifyDataSetChanged();

        selectLevel = LEVEL_COUNTY;
    }

    private void showCItyList() {
        // closePressDialog();

        backBtn.setVisibility(View.VISIBLE);
        areaList.clear();
        for (City city : cityList) {
            areaList.add(city.getCityName());
        }
        titleTxt.setText(selectProvince.getProvinceName());
        areaAdapter.notifyDataSetChanged();

        selectLevel = LEVEL_CITY;
    }

    private void showProvinceList() {
        //closePressDialog();
        backBtn.setVisibility(View.GONE);
        areaList.clear();
        for (Province province : provinceList) {
            areaList.add(province.getProvinceName());
        }
        titleTxt.setText("中国");
        areaAdapter.notifyDataSetChanged();

        selectLevel = LEVEL_PROVINCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        titleTxt = (TextView) findViewById(R.id.title_tv_areaChoose);
        backBtn = (Button) findViewById(R.id.back_btn_areaChoose);

        /*
         * RecyclerView实现地区列表
         */

        mareaRecyclerview = (RecyclerView) findViewById(R.id.areaList_rv_areaChoose);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mareaRecyclerview.setLayoutManager(layoutManager);
        areaAdapter = new AreaAdapter(areaList);
        mareaRecyclerview.setAdapter(areaAdapter);
        mareaRecyclerview.addItemDecoration(new AreaDividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        /*
         * 为通用地区列表设置监听
         */

        areaAdapter.setOnItemClickListener(new AreaAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                //判断当前所在列表级别
                if (selectLevel == LEVEL_PROVINCE) {
                    selectProvince = provinceList.get(position);
                    queryCity(selectProvince.getProvinceCode());
                } else if (selectLevel == LEVEL_CITY) {
                    selectCity = cityList.get(position);
                    queryCounty(selectCity.getProvinceId(),
                            selectCity.getCityCode());
                } else if (selectLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                }

            }
        });

        /*
         * 为返回按钮设置监听
         */

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectLevel == LEVEL_COUNTY) {
                    queryCity(selectCity.getProvinceId());
                } else if (selectLevel == LEVEL_CITY) {
                    queryProvince();
                }
            }
        });

        //初始化数据库设置

        areaDao = new DBDao(this);

        /*
         *
         * 载入省份列表
         */
        queryProvince();
    }

    @Override
    public void onBackPressed() {

        ActivityCollector.finishAll();
        Toast.makeText(ChooseAreaActivity.this, "再按一次退出Weather_Oven", Toast.LENGTH_SHORT).show();


    }

    private void getCitysFromServer(final int ProvinceId) {
        //showPressDialog();
        String CityAddress = PROVINCE + BACK_SLASH + ProvinceId;
        HttpUtil.sendHttpRequest(CityAddress, new VolleyResponseCallbackListener() {
            @Override
            public void onFinish(String response) {
                cityList = JSONUtil.handleCityResponse(response, ProvinceId);
                areaDao.initCityTable(cityList);
                Message msg = new Message();
                if (response == null)
                    msg.what = FAILURE;
                else
                    msg.what = LEVEL_CITY;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void getCountysFromServer(final int provinceId, final int cityId) {
        //showPressDialog();
        String CountyAddress = PROVINCE + BACK_SLASH + provinceId + BACK_SLASH + cityId;
        HttpUtil.sendHttpRequest(CountyAddress, new VolleyResponseCallbackListener() {
            @Override
            public void onFinish(String response) {
                countyList = JSONUtil.handleCountyResponse(response, selectCity.getId());
                areaDao.initCountyTable(countyList, provinceId);
                Message msg = new Message();
                if (response == null)
                    msg.what = FAILURE;
                else
                    msg.what = LEVEL_COUNTY;
                handler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void getProincesFromServer() {
        //showPressDialog();

        HttpUtil.sendHttpRequest(PROVINCE, new VolleyResponseCallbackListener() {
            @Override
            public void onFinish(String response) {
                provinceList = JSONUtil.handleProvinceResponse(response);
                areaDao.initProvinceTable(provinceList);
                Message msg = new Message();
                if (response == null)
                    msg.what = FAILURE;
                else
                    msg.what = LEVEL_PROVINCE;
                handler.sendMessage(msg);
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void queryProvince() {
        provinceList = areaDao.queryProvince();
        if (provinceList.size() > 0)
            showProvinceList();
        else {
            getProincesFromServer();

        }

    }

    public void queryCity(int provinceId) {
        cityList = areaDao.queryCity(provinceId);
        if (cityList.size() > 0)
            showCItyList();
        else {
            getCitysFromServer(provinceId);

        }


    }

    public void queryCounty(int provinceid, int cityid) {
        countyList = areaDao.querycountry(provinceid,cityid);
        if (countyList.size() > 0)
            showCountyList();
        else {
            getCountysFromServer(provinceid, cityid);

        }
    }


    //TODO:进度条效果不好，一闪而过
    /*
     *  进度条
     */
/*
    private  void showPressDialog(){
        if(progressBar.getVisibility() == View.GONE)
            progressBar.setVisibility(View.VISIBLE);
    }



    private void closePressDialog(){
        if(progressBar.getVisibility() != View.GONE)
            progressBar.setVisibility(View.GONE);

    }
*/
}





