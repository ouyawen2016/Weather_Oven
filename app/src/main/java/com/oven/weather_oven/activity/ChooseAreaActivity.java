package com.oven.weather_oven.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import com.oven.weather_oven.Base.ActivityCollector;
import com.oven.weather_oven.Base.BaseActivity;
import com.oven.weather_oven.R;
import com.oven.weather_oven.bean.City;
import com.oven.weather_oven.bean.County;
import com.oven.weather_oven.bean.Province;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//TODO:做下切换按钮
public class ChooseAreaActivity extends BaseActivity{
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private static final int FAILURE = 3;
    private ProgressDialog progressDialog;
    private TextView titleTxt;
    private Button backBtn;
    private ListView areaListview;
    private ArrayAdapter<String> areaAdapter;
    private List<String> areaList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private Province selectProvince;
    private County selectCounty;
    private City selectCity;
    private int selectLevel;
    private String response;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LEVEL_PROVINCE:
                    //closePressDialog();
                    backBtn.setVisibility(View.GONE);
                    areaList.clear();
                    for (Province province : provinceList) {
                        areaList.add(province.getProvinceName());
                    }
                    titleTxt.setText("中国");
                    areaAdapter.notifyDataSetChanged();
                    areaListview.setSelection(0);
                    selectLevel = LEVEL_PROVINCE;
                    break;
                case LEVEL_CITY:
                   // closePressDialog();
                    backBtn.setVisibility(View.VISIBLE);
                    areaList.clear();
                    for (City city : cityList) {
                        areaList.add(city.getCityName());
                    }
                    titleTxt.setText(selectProvince.getProvinceName());
                    areaAdapter.notifyDataSetChanged();
                    areaListview.setSelection(0);
                    selectLevel = LEVEL_CITY;
                    break;
                case LEVEL_COUNTY:
                    //closePressDialog();
                    backBtn.setVisibility(View.VISIBLE);
                    areaList.clear();
                    for (County county : countyList) {
                        areaList.add(county.getCountyName());
                    }
                    titleTxt.setText(selectCity.getCityName());
                    areaAdapter.notifyDataSetChanged();
                    areaListview.setSelection(0);
                    selectLevel = LEVEL_COUNTY;
                    break;
                case FAILURE:
                    //closePressDialog();

                    Toast.makeText(ChooseAreaActivity.this, "加载失败，请检查网络", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }

        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_area);
        titleTxt = (TextView) findViewById(R.id.title_tv_areaChoose);
        backBtn = (Button) findViewById(R.id.back_btn_areaChoose);
        areaListview = (ListView) findViewById(R.id.areaList_lv_areaChoose);
        areaAdapter = new ArrayAdapter<>(ChooseAreaActivity.this, R.layout.choosearea_item, areaList);
        areaListview.setAdapter(areaAdapter);


        /**
         * 为通用地区列表设置监听
         */

        areaListview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前所在列表级别
                if (selectLevel == LEVEL_PROVINCE) {
                    selectProvince = provinceList.get(position);
                    getCitysFromServer(selectProvince.getProvinceCode());
                } else if (selectLevel == LEVEL_CITY) {
                    selectCity = cityList.get(position);
                    getCountysFromServer(selectCity.getProvinceId(), selectCity.getCityCode());

                } else if (selectLevel == LEVEL_COUNTY) {
                    String weatherId = countyList.get(position).getWeatherId();
                    Intent intent = new Intent(ChooseAreaActivity.this, WeatherActivity.class);
                    intent.putExtra("weather_id", weatherId);
                    startActivity(intent);
                }
            }
        });

        /**
         *
         * 载入省份列表
         */

        getProincesFromServer();


        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (selectLevel == LEVEL_COUNTY) {
                    getCitysFromServer(selectProvince.getId());
                } else if (selectLevel == LEVEL_CITY) {
                    getProincesFromServer();
                }
            }
        });
    }
    @Override
    public void onBackPressed() {

        ActivityCollector.finishAll();
        Toast.makeText(ChooseAreaActivity.this,"再按一次退出",Toast.LENGTH_SHORT).show();


    }

    private void getCitysFromServer(final int ProvinceId) {
        //showPressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = HttpUtil.getCity(ProvinceId);
                    cityList = JSONUtil.handleCityResponse(response, selectProvince.getId());
                    Message msg = new Message();
                    if (response == null)
                        msg.what = FAILURE;
                    else
                        msg.what = LEVEL_CITY;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getCountysFromServer (final int provinceId, final int cityId){
        //showPressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = HttpUtil.getCounty(provinceId, cityId);
                    countyList = JSONUtil.handleCountyResponse(response, selectCity.getId());
                    Message msg = new Message();
                    if (response == null)
                        msg.what = FAILURE;
                    else
                    msg.what = LEVEL_COUNTY;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void getProincesFromServer(){
        //showPressDialog();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    response = HttpUtil.getProvince();
                    provinceList = JSONUtil.handleProvinceResponse(response);
                    Message msg = new Message();
                    if (response == null)
                        msg.what = FAILURE;
                    else
                        msg.what = LEVEL_PROVINCE;
                    handler.sendMessage(msg);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }








    //TODO:进度条效果不好，一闪而过
    /*
     *  进度条
     */

    private  void showPressDialog(){
        if(progressDialog == null){
            progressDialog = new ProgressDialog(ChooseAreaActivity.this);
            progressDialog.setMessage("正在加载…");
            progressDialog.setCancelable(false);
        }
        progressDialog.show();
    }



    private void closePressDialog(){
        if(progressDialog != null){
            progressDialog.dismiss();
        }
    }




}


