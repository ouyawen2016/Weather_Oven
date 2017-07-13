package com.oven.weather_oven.ui;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.oven.weather_oven.R;
import com.oven.weather_oven.bean.City;
import com.oven.weather_oven.bean.County;
import com.oven.weather_oven.bean.Province;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 遍历省，市，县的碎片
 * Created by Oven on 2017/7/12.
 */

public class ChooseAreaFragment extends Fragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTY = 2;
    private ProgressDialog progressdialog;
    private TextView titleTxt;
    private Button backBtn;
    private ListView areaListview;
    private ArrayAdapter<String> areaAdapter ;
    private List<String> areaList = new ArrayList<>();
    private List<Province> provinceList;
    private List<City> cityList;
    private List<County> countyList;
    private  Province selectProvince;
    private County selectCounty;
    private City selectCity;
    private int selectLevel ;
    private String response;


    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case LEVEL_PROVINCE:
                    provinceList = JSONUtil.handleProvinceResponse(response);
                    for(Province province:provinceList){
                        areaList.add(province.getProvinceName());
                    }
                    areaAdapter.notifyDataSetChanged();
                    selectLevel = LEVEL_PROVINCE;
                    break;
                case LEVEL_CITY:
                    cityList = JSONUtil.handleCityResponse(response,selectProvince.getId());
                    areaList.clear();
                    for(City city:cityList){
                        areaList.add(city.getCityName());
                    }
                    areaAdapter.notifyDataSetChanged();
                    selectLevel = LEVEL_CITY;
                    break;
                case LEVEL_COUNTY:
                    countyList = JSONUtil.handleCountyResponse(response,selectCity.getId());
                    areaList.clear();
                    for(County county:countyList){
                        areaList.add(county.getCountyName());
                    }
                    areaAdapter.notifyDataSetChanged();
                    selectLevel = LEVEL_COUNTY;
                    break;

                default:
                    break;
            }

        }

    };

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleTxt = (TextView)view.findViewById(R.id.title_tv_areaChoose);
        backBtn = (Button)view.findViewById(R.id.back_btn_areaChoose);
        areaListview = (ListView)view.findViewById(R.id.areaList_lv_areaChoose);
        areaAdapter = new ArrayAdapter<>(getContext(),android.R.layout.simple_list_item_1,areaList);
        areaListview.setAdapter(areaAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        /**
         * 为通用地区列表设置监听
         */

        areaListview.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //判断当前所在列表级别

                if(selectLevel == LEVEL_PROVINCE){
                    selectProvince = provinceList.get(position);
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                response = HttpUtil.getCity(selectProvince.getProvinceCode());
                                Message msg = new Message();
                                msg.what = LEVEL_CITY;
                               handler.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }else if(selectLevel == LEVEL_CITY) {
                    selectCity = cityList.get(position);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                response = HttpUtil.getCounty(selectProvince.getProvinceCode(),
                                        selectCity.getCityCode());
                                Message msg = new Message();
                                msg.what = LEVEL_COUNTY;
                                handler.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();

                }
            }
        });
                    /**
                     * 载入省份列表
                     */
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                response = HttpUtil.getProvince();
                                Message msg = new Message();
                                msg.what = LEVEL_PROVINCE;
                                handler.sendMessage(msg);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();







    }
}

