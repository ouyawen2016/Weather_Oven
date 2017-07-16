package com.oven.weather_oven.bean;

import java.util.List;

/**
 * 如果必须使用JSONObject,不需要与json结构一致
 * p.s.如果使用GSON这里可以先一键生成javaBean
 * Created by oven on 2017/7/11.
 */

public class Weather {
    private String status;
    public Basic basics;
    private aqi aqis;
    public String temperature;
    public now nows;
    public List<DailyForecast> dailyForecasts;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public now getNows() {
        return nows;
    }

    public void setNows(now nows) {
        this.nows = nows;
    }

    public List<DailyForecast> getDailyForecasts() {
        return dailyForecasts;
    }

    public void setDailyForecasts(List<DailyForecast> dailyForecasts) {
        this.dailyForecasts = dailyForecasts;
    }

    public Basic getBasics() {
        return basics;
    }

    public void setBasics(Basic basics) {
        this.basics = basics;
    }

    public aqi getAqis() {
        return aqis;
    }

    public void setAqis(aqi aqis) {
        this.aqis = aqis;
    }

    public class Basic{
        private String city;
        public String id;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }
    }
   public class aqi{
        private AQICity aqicity;
        private class AQICity{
            private String aqi;
            private String pm25;
        }

    }
    public class now{

        private More more;
        private class More{
            private String info;
        }
        private class suggestion{
            private Comfort comfort;
            private Sport sport;
            private class Comfort{
                private String info;
            }
            private class Sport{
                private String sportInfo;
            }
        }
    }

    public class DailyForecast{
        private String date;
        private String tem_max;
        private String tem_min;
        private String more;

            public void setTmp(String max,String min){
                this.tem_max = max;
                this.tem_min= min;
            }
            public String getMax(){
                return tem_max;

            }
            public String getMin(){
                return tem_min;
            }

        public class more{
            public String infoDaily;

            public String getInfoDaily() {
                return infoDaily;
            }

            public void setInfoDaily(String infoDaily) {
                this.infoDaily = infoDaily;
            }
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getMore() {
            return more;
        }

        public void setMore(String mmore) {
            this.more = mmore;
        }



    }
}
