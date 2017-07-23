package com.oven.weather_oven.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.oven.weather_oven.R;
import com.oven.weather_oven.bean.Weather;

import java.util.List;

/**
 *
 * Created by oven on 2017/7/20.
 */

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder>{
        private List<Weather.DailyForecastBean> mForecastList;

    static class ViewHolder extends RecyclerView.ViewHolder{
            TextView mCityName;
            TextView mWeather;
            TextView mDegreeMax;
            TextView mDegreeMin;
            ImageView mWeatherImg;
            public ViewHolder(View view){
                super(view);
                mCityName = (TextView)view.findViewById(R.id.item_forecast_tv_cityName);
                mDegreeMax = (TextView)view.findViewById(R.id.item_forecast_tv_max_degree);
                mDegreeMin = (TextView)view.findViewById(R.id.item_forecast_tv_min_degree);
                mWeather = (TextView)view.findViewById(R.id.item_forecast_tv_weather);
                mWeatherImg = (ImageView)view.findViewById(R.id.item_forecast_iv_weather);
            }
        }
        public ForecastAdapter(List<Weather.DailyForecastBean> forecastList){
            mForecastList = forecastList;
            notifyDataSetChanged();
        }
        @Override
        public ForecastAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.forecast_item,parent,false);
            return new ForecastAdapter.ViewHolder(view);
        }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mWeather.setText(mForecastList.get(position).cond.txtD);
        holder.mDegreeMin.setText(mForecastList.get(position).tmp.min);
        holder.mDegreeMax.setText(mForecastList.get(position).tmp.max);
        holder.mCityName.setText(mForecastList.get(position).date);

    }

        @Override
        public int getItemCount() {

            return mForecastList == null ? 0 : mForecastList.size();
        }


    }


