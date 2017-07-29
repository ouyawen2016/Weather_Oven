package com.oven.weather_oven.activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.oven.weather_oven.adapter.AreaAdapter;
import com.oven.weather_oven.adapter.AreaDividerItemDecoration;
import com.oven.weather_oven.R;
import com.oven.weather_oven.base.MyApplication;
import com.oven.weather_oven.bean.City;
import com.oven.weather_oven.bean.County;
import com.oven.weather_oven.bean.Province;
import com.oven.weather_oven.db.DBDao;
import com.oven.weather_oven.ui.LoadingFragment;
import com.oven.weather_oven.util.HttpUtil;
import com.oven.weather_oven.util.JSONUtil;
import com.oven.weather_oven.util.VolleyResponseCallbackListener;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
public class ChooseArea extends Fragment {
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
    private LoadingFragment mLoading;

    private static final String PROVINCE = "http://guolin.tech/api/china";
    private static final String BACK_SLASH = "/";

    private static class MyHandler extends Handler {
        private WeakReference<ChooseArea> mReference;

        private MyHandler(ChooseArea chooseArea) {
            mReference = new WeakReference<>(chooseArea);
        }

        public void handleMessage(Message msg) {
            ChooseArea mActivity = mReference.get();
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
                    Toast.makeText(MyApplication.getContext(), "加载失败，请检查网络", Toast.LENGTH_SHORT).show();
                default:
                    break;
            }

        }

    }

    private void showCountyList() {
        closePressDialog();

        mBackBtn.setVisibility(View.VISIBLE);
        mAreaList.clear();
        for (County county : mCountyList) {
            mAreaList.add(county.getCountyName());
        }
        mTitleTxt.setText(mSelectCity.getCityName());
        mAreaAdapter.notifyDataSetChanged();

        mSelectLevel = LEVEL_COUNTY;
    }

    private void showCItyList() {
        closePressDialog();

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
        closePressDialog();
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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,  Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        mTitleTxt = (TextView) view.findViewById(R.id.title_tv_areaChoose);
        mBackBtn = (Button) view.findViewById(R.id.back_btn_areaChoose);
        /*
         * RecyclerView实现地区列表
         */
        RecyclerView mAreaRecyclerView = (RecyclerView) view.findViewById(R.id.areaList_rv_areaChoose);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mAreaRecyclerView.setLayoutManager(layoutManager);
        mAreaAdapter = new AreaAdapter(mAreaList);
        mAreaRecyclerView.setAdapter(mAreaAdapter);
        mAreaRecyclerView.addItemDecoration(new AreaDividerItemDecoration
                (getActivity(), LinearLayoutManager.VERTICAL));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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
                    //判断一下在哪里，在chooseArea就直接跳转新页面，在天气页面就关闭菜单刷新请求
                    if(getActivity() instanceof MainActivity) {
                        Intent intent = new Intent(getActivity(),WeatherViewActivity.class);
                        intent.putExtra("weather_id", weatherId);
                        startActivity(intent);
                        getActivity().finish();
                    }else if (getActivity() instanceof WeatherViewActivity) {
                        WeatherViewActivity activity = (WeatherViewActivity) getActivity();
                        activity.mDrawerLayout.closeDrawers();

                        activity.refresh(weatherId);
                        //TODO:与activity通信，传递weatherID
                        //activity.mSwipeRefresh.setRefreshing(true);
                        //activity.requestWeather(weatherId)}
                    }
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
         * 载入省份列表
         */

        queryProvince();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);


    }

    private void getCityFromServer(final int ProvinceId) {
        showPressDialog();
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
        showPressDialog();
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
        showPressDialog();

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
        mCountyList = mAreaDao.queryCountry(provinceId, cityId);
        if (mCountyList.size() > 0)
            showCountyList();
        else {
            getCountyFromServer(provinceId, cityId);

        }
    }



    /*
     *  LoadingFragment 实现进度条
     */

    private void showPressDialog() {
        mLoading = new LoadingFragment();
        FragmentTransaction pressDialog = getActivity().getSupportFragmentManager().beginTransaction();
        mLoading.show(pressDialog, "loading");


    }

    private void closePressDialog() {
        if (mLoading != null){
            mLoading.dismiss();
            mLoading = null;
        }

    }


}





