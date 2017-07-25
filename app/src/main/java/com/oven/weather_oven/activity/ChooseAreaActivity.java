package com.oven.weather_oven.activity;


import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.oven.weather_oven.adapter.AreaAdapter;
import com.oven.weather_oven.adapter.AreaDividerItemDecoration;
import com.oven.weather_oven.base.ActivityCollector;
import com.oven.weather_oven.base.BaseActivity;
import com.oven.weather_oven.R;
import com.oven.weather_oven.base.MyApplication;
import com.oven.weather_oven.bean.City;
import com.oven.weather_oven.bean.County;
import com.oven.weather_oven.bean.Province;
import com.oven.weather_oven.db.DBDao;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;
import com.oven.weather_oven.util.VolleyResponseCallbackListener;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

//TODO:做下切换按钮
public class ChooseAreaActivity extends BaseActivity {
    private static final int LEVEL_PROVINCE = 0;
    private static final int LEVEL_CITY = 1;
    private static final int LEVEL_COUNTY = 2;
    private static final int FAILURE = 3;

    //声明各种成员变量
    private TextView mTitleTxt;
    private Button mBackBtn;
    private AreaAdapter mAreaAdapter;
    private List<String> mAreaList = new ArrayList<>();
    private List<Province> mProvinceList;
    private List<City> mCityList;
    private List<County> mCountyList;
    private Province mSelectProvince;
    private City mSelectCity;
    public int mSelectLevel;
    private DBDao mAreaDao;
    private MyHandler mHandler;

    private static final String PROVINCE = "http://guolin.tech/api/china";
    private static final String BACK_SLASH = "/";

    private static class MyHandler extends Handler {
        private WeakReference<ChooseAreaActivity> mReference;
        private MyHandler(ChooseAreaActivity activity){
            mReference= new WeakReference<>(activity);
        }
        public void handleMessage(Message msg) {
            ChooseAreaActivity mActivity = mReference.get();
            switch (msg.what) {
                case LEVEL_PROVINCE:
                    mActivity.showProvinceList();

                    break;
                case LEVEL_CITY:
                    mActivity.showCItyList();

                    break;
                case LEVEL_COUNTY:
                    mActivity.showCountyList();

                    break;
                case FAILURE:
                    //closePressDialog();
                    Toast.makeText(MyApplication.getContext(), "加载失败，请检查网络", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }

        }

    }

    private void showCountyList() {
        //closePressDialog();

        mBackBtn.setVisibility(View.VISIBLE);
        mAreaList.clear();
        for (County county : mCountyList){
            mAreaList.add(county.getCountyName());
        }
        mTitleTxt.setText(mSelectCity.getCityName());
        mAreaAdapter.notifyDataSetChanged();

        mSelectLevel = LEVEL_COUNTY;
    }

    private void showCItyList() {
        // closePressDialog();

        mBackBtn.setVisibility(View.VISIBLE);
       mAreaList.clear();
        for (City city : mCityList) {
            mAreaList.add(city.getCityName());
        }
        mTitleTxt.setText(mSelectProvince.getProvinceName());
        mAreaAdapter.notifyDataSetChanged();

        mSelectLevel = LEVEL_CITY;
    }

    private void showProvinceList() {
        //closePressDialog();
       mBackBtn.setVisibility(View.GONE);
        mAreaList.clear();
        for (Province province : mProvinceList) {
            mAreaList.add(province.getProvinceName());
        }
        mTitleTxt.setText("中国");
        mAreaAdapter.notifyDataSetChanged();

        mSelectLevel = LEVEL_PROVINCE;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        mTitleTxt = (TextView) findViewById(R.id.title_tv_areaChoose);
        mBackBtn = (Button) findViewById(R.id.back_btn_areaChoose);

        /*
         * RecyclerView实现地区列表
         */
        RecyclerView mAreaRecyclerView = (RecyclerView) findViewById(R.id.areaList_rv_areaChoose);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mAreaRecyclerView.setLayoutManager(layoutManager);
        mAreaAdapter = new AreaAdapter(mAreaList);
        mAreaRecyclerView.setAdapter(mAreaAdapter);
        mAreaRecyclerView.addItemDecoration(new AreaDividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        /*
         * 为通用地区列表设置监听
         */

        mAreaAdapter.setOnItemClickListener(new AreaAdapter.OnItemClickListener() {

            @Override
            public void onItemClick(View view, int position) {
                //判断当前所在列表级别
                if (mSelectLevel == LEVEL_PROVINCE) {
                    mSelectProvince = mProvinceList.get(position);
                    queryCity(mSelectProvince.getProvinceCode());
                } else if (mSelectLevel == LEVEL_CITY) {
                    mSelectCity = mCityList.get(position);
                    queryCounty(mSelectCity.getProvinceId(),
                            mSelectCity.getCityCode());
                } else if (mSelectLevel == LEVEL_COUNTY) {
                    String weatherId = mCountyList.get(position).getWeatherId();
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                }

            }
        });

        /*
         * 为返回按钮设置监听
         */

       mBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSelectLevel == LEVEL_COUNTY) {
                    queryCity(mSelectCity.getProvinceId());
                } else if (mSelectLevel == LEVEL_CITY) {
                    queryProvince();
                }
            }
        });

        //初始化数据库设置

        mAreaDao = new DBDao();
        //初始化 Handler
        mHandler = new MyHandler(this);

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

    private void getCityFromServer(final int ProvinceId) {
        //showPressDialog();
        String CityAddress = PROVINCE + BACK_SLASH + ProvinceId;
        HttpUtil.sendHttpRequest(CityAddress, new VolleyResponseCallbackListener() {
            @Override
            public void onFinish(String response) {
                mCityList = JSONUtil.handleCityResponse(response, ProvinceId);
                mAreaDao.initCityTable(mCityList);
                Message msg = new Message();
                if (response == null)
                    msg.what = FAILURE;
                else
                    msg.what = LEVEL_CITY;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }

    private void getCountyFromServer(final int provinceId, final int cityId) {
        //showPressDialog();
        String CountyAddress = PROVINCE + BACK_SLASH + provinceId + BACK_SLASH + cityId;
        HttpUtil.sendHttpRequest(CountyAddress, new VolleyResponseCallbackListener() {
            @Override
            public void onFinish(String response) {
                mCountyList = JSONUtil.handleCountyResponse(response, mSelectCity.getId());
                mAreaDao.initCountyTable(mCountyList, provinceId);
                Message msg = new Message();
                if (response == null)
                    msg.what = FAILURE;
                else
                    msg.what = LEVEL_COUNTY;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void getProvincesFromServer() {
        //showPressDialog();

        HttpUtil.sendHttpRequest(PROVINCE, new VolleyResponseCallbackListener() {
            @Override
            public void onFinish(String response) {
                mProvinceList = JSONUtil.handleProvinceResponse(response);
                mAreaDao.initProvinceTable(mProvinceList);
                Message msg = new Message();
                if (response == null)
                    msg.what = FAILURE;
                else
                    msg.what = LEVEL_PROVINCE;
                mHandler.sendMessage(msg);
            }
            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }
        });

    }

    public void queryProvince() {
        mProvinceList = mAreaDao.queryProvince();
        if (mProvinceList.size() > 0)
            showProvinceList();
        else {
            getProvincesFromServer();

        }

    }

    public void queryCity(int provinceId) {
        mCityList = mAreaDao.queryCity(provinceId);
        if (mCityList.size() > 0)
            showCItyList();
        else {
            getCityFromServer(provinceId);

        }


    }

    public void queryCounty(int provinceId, int cityId) {
        mCountyList = mAreaDao.queryCountry(provinceId,cityId);
        if (mCountyList.size() > 0)
            showCountyList();
        else {
            getCountyFromServer(provinceId, cityId);

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





