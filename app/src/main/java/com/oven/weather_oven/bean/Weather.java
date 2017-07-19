package com.oven.weather_oven.bean;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * 如果必须使用JSONObject,不需要与json结构一致
 * p.s.如果使用GSON这里可以先一键生成javaBean
 * Created by oven on 2017/7/11.
 */

public class Weather {


    /**
     * aqi : {"city":{"aqi":"17","co":"1","no2":"38","o3":"17","pm10":"17","pm25":"11","qlty":"优","so2":"8"}}
     * basic : {"city":"广州","cnty":"中国","id":"CN101280101","lat":"23.12517738","lon":"113.28063965","update":{"loc":"2017-07-17 19:49","utc":"2017-07-17 11:49"}}
     * daily_forecast : [{"astro":{"mr":"00:10","ms":"12:55","sr":"05:51","ss":"19:15"},"cond":{"code_d":"301","code_n":"307","txt_d":"强阵雨","txt_n":"大雨"},"date":"2017-07-17","hum":"78","pcpn":"9.2","pop":"100","pres":"1009","tmp":{"max":"32","min":"25"},"uv":"12","vis":"13","wind":{"deg":"97","dir":"无持续风向","sc":"微风","spd":"7"}},{"astro":{"mr":"00:54","ms":"13:56","sr":"05:51","ss":"19:14"},"cond":{"code_d":"307","code_n":"302","txt_d":"大雨","txt_n":"雷阵雨"},"date":"2017-07-18","hum":"88","pcpn":"10.3","pop":"100","pres":"1011","tmp":{"max":"32","min":"25"},"uv":"10","vis":"13","wind":{"deg":"113","dir":"无持续风向","sc":"微风","spd":"3"}},{"astro":{"mr":"01:40","ms":"14:58","sr":"05:51","ss":"19:14"},"cond":{"code_d":"302","code_n":"101","txt_d":"雷阵雨","txt_n":"多云"},"date":"2017-07-19","hum":"81","pcpn":"12.3","pop":"100","pres":"1010","tmp":{"max":"32","min":"26"},"uv":"8","vis":"17","wind":{"deg":"132","dir":"无持续风向","sc":"微风","spd":"5"}}]
     * hourly_forecast : [{"cond":{"code":"305","txt":"小雨"},"date":"2017-07-17 22:00","hum":"87","pop":"43","pres":"1011","tmp":"25","wind":{"deg":"128","dir":"东南风","sc":"微风","spd":"11"}}]
     * now : {"cond":{"code":"300","txt":"阵雨"},"fl":"29","hum":"92","pcpn":"1.8","pres":"1008","tmp":"24","vis":"10","wind":{"deg":"140","dir":"东南风","sc":"微风","spd":"10"}}
     * status : ok
     * suggestion : {"comf":{"brf":"较舒适","txt":"白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。"},"cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},"drsg":{"brf":"较舒适","txt":"建议着薄外套或牛仔衫裤等服装。年老体弱者宜着夹克衫、薄毛衣等。昼夜温差较大，注意适当增减衣服。"},"flu":{"brf":"极易发","txt":"昼夜温差极大，极易发生感冒，请特别注意增减衣服保暖防寒。"},"sport":{"brf":"适宜","txt":"天气较好，赶快投身大自然参与户外运动，尽情感受运动的快乐吧。"},"trav":{"brf":"较适宜","txt":"天气较好，同时又有微风伴您一路同行。稍冷，较适宜旅游，您仍可陶醉于大自然的美丽风光中。"},"uv":{"brf":"中等","txt":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}}
     */

    @SerializedName("aqi")
    public AqiBean aqi;
    @SerializedName("basic")
    public BasicBean basic;
    @SerializedName("now")
    public NowBean now;
    @SerializedName("status")
    public String status;
    @SerializedName("suggestion")
    public SuggestionBean suggestion;
    @SerializedName("daily_forecast")
    public List<DailyForecastBean> dailyForecast;
    @SerializedName("hourly_forecast")
    public List<HourlyForecastBean> hourlyForecast;

    public static Weather objectFromData(String str) {

        return new Gson().fromJson(str, Weather.class);
    }

    public static class AqiBean {
        /**
         * city : {"aqi":"17","co":"1","no2":"38","o3":"17","pm10":"17","pm25":"11","qlty":"优","so2":"8"}
         */

        @SerializedName("city")
        public CityBean city;

        public static AqiBean objectFromData(String str) {

            return new Gson().fromJson(str, AqiBean.class);
        }

        public static List<AqiBean> arrayAqiBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<AqiBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static class CityBean {
            /**
             * aqi : 17
             * co : 1
             * no2 : 38
             * o3 : 17
             * pm10 : 17
             * pm25 : 11
             * qlty : 优
             * so2 : 8
             */

            @SerializedName("aqi")
            public String aqi;
            @SerializedName("co")
            public String co;
            @SerializedName("no2")
            public String no2;
            @SerializedName("o3")
            public String o3;
            @SerializedName("pm10")
            public String pm10;
            @SerializedName("pm25")
            public String pm25;
            @SerializedName("qlty")
            public String qlty;
            @SerializedName("so2")
            public String so2;

            public static CityBean objectFromData(String str) {

                return new Gson().fromJson(str, CityBean.class);
            }

            public static List<CityBean> arrayCityBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<CityBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }
    }

    public static class BasicBean {
        /**
         * city : 广州
         * cnty : 中国
         * id : CN101280101
         * lat : 23.12517738
         * lon : 113.28063965
         * update : {"loc":"2017-07-17 19:49","utc":"2017-07-17 11:49"}
         */

        @SerializedName("city")
        public String city;
        @SerializedName("cnty")
        public String cnty;
        @SerializedName("id")
        public String id;
        @SerializedName("lat")
        public String lat;
        @SerializedName("lon")
        public String lon;
        @SerializedName("update")
        public UpdateBean update;

        public static BasicBean objectFromData(String str) {

            return new Gson().fromJson(str, BasicBean.class);
        }

        public static List<BasicBean> arrayBasicBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<BasicBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static class UpdateBean {
            /**
             * loc : 2017-07-17 19:49
             * utc : 2017-07-17 11:49
             */

            @SerializedName("loc")
            public String loc;
            @SerializedName("utc")
            public String utc;

            public static UpdateBean objectFromData(String str) {

                return new Gson().fromJson(str, UpdateBean.class);
            }

            public static List<UpdateBean> arrayUpdateBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<UpdateBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }
    }

    public static class NowBean {
        /**
         * cond : {"code":"300","txt":"阵雨"}
         * fl : 29
         * hum : 92
         * pcpn : 1.8
         * pres : 1008
         * tmp : 24
         * vis : 10
         * wind : {"deg":"140","dir":"东南风","sc":"微风","spd":"10"}
         */

        @SerializedName("cond")
        public CondBean cond;
        @SerializedName("fl")
        public String fl;
        @SerializedName("hum")
        public String hum;
        @SerializedName("pcpn")
        public String pcpn;
        @SerializedName("pres")
        public String pres;
        @SerializedName("tmp")
        public String tmp;
        @SerializedName("vis")
        public String vis;
        @SerializedName("wind")
        public WindBean wind;

        public static NowBean objectFromData(String str) {

            return new Gson().fromJson(str, NowBean.class);
        }

        public static List<NowBean> arrayNowBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<NowBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static class CondBean {
            /**
             * code : 300
             * txt : 阵雨
             */

            @SerializedName("code")
            public String code;
            @SerializedName("txt")
            public String txt;

            public static CondBean objectFromData(String str) {

                return new Gson().fromJson(str, CondBean.class);
            }

            public static List<CondBean> arrayCondBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<CondBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class WindBean {
            /**
             * deg : 140
             * dir : 东南风
             * sc : 微风
             * spd : 10
             */

            @SerializedName("deg")
            public String deg;
            @SerializedName("dir")
            public String dir;
            @SerializedName("sc")
            public String sc;
            @SerializedName("spd")
            public String spd;

            public static WindBean objectFromData(String str) {

                return new Gson().fromJson(str, WindBean.class);
            }

            public static List<WindBean> arrayWindBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<WindBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }
    }

    public static class SuggestionBean {
        /**
         * comf : {"brf":"较舒适","txt":"白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。"}
         * cw : {"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"}
         * drsg : {"brf":"较舒适","txt":"建议着薄外套或牛仔衫裤等服装。年老体弱者宜着夹克衫、薄毛衣等。昼夜温差较大，注意适当增减衣服。"}
         * flu : {"brf":"极易发","txt":"昼夜温差极大，极易发生感冒，请特别注意增减衣服保暖防寒。"}
         * sport : {"brf":"适宜","txt":"天气较好，赶快投身大自然参与户外运动，尽情感受运动的快乐吧。"}
         * trav : {"brf":"较适宜","txt":"天气较好，同时又有微风伴您一路同行。稍冷，较适宜旅游，您仍可陶醉于大自然的美丽风光中。"}
         * uv : {"brf":"中等","txt":"属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。"}
         */

        @SerializedName("comf")
        public ComfBean comf;
        @SerializedName("cw")
        public CwBean cw;
        @SerializedName("drsg")
        public DrsgBean drsg;
        @SerializedName("flu")
        public FluBean flu;
        @SerializedName("sport")
        public SportBean sport;
        @SerializedName("trav")
        public TravBean trav;
        @SerializedName("uv")
        public UvBean uv;

        public static SuggestionBean objectFromData(String str) {

            return new Gson().fromJson(str, SuggestionBean.class);
        }

        public static List<SuggestionBean> arraySuggestionBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<SuggestionBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static class ComfBean {
            /**
             * brf : 较舒适
             * txt : 白天天气晴好，您在这种天气条件下，会感觉早晚凉爽、舒适，午后偏热。
             */

            @SerializedName("brf")
            public String brf;
            @SerializedName("txt")
            public String txt;

            public static ComfBean objectFromData(String str) {

                return new Gson().fromJson(str, ComfBean.class);
            }

            public static List<ComfBean> arrayComfBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<ComfBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class CwBean {
            /**
             * brf : 不宜
             * txt : 不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。
             */

            @SerializedName("brf")
            public String brf;
            @SerializedName("txt")
            public String txt;

            public static CwBean objectFromData(String str) {

                return new Gson().fromJson(str, CwBean.class);
            }

            public static List<CwBean> arrayCwBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<CwBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class DrsgBean {
            /**
             * brf : 较舒适
             * txt : 建议着薄外套或牛仔衫裤等服装。年老体弱者宜着夹克衫、薄毛衣等。昼夜温差较大，注意适当增减衣服。
             */

            @SerializedName("brf")
            public String brf;
            @SerializedName("txt")
            public String txt;

            public static DrsgBean objectFromData(String str) {

                return new Gson().fromJson(str, DrsgBean.class);
            }

            public static List<DrsgBean> arrayDrsgBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<DrsgBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class FluBean {
            /**
             * brf : 极易发
             * txt : 昼夜温差极大，极易发生感冒，请特别注意增减衣服保暖防寒。
             */

            @SerializedName("brf")
            public String brf;
            @SerializedName("txt")
            public String txt;

            public static FluBean objectFromData(String str) {

                return new Gson().fromJson(str, FluBean.class);
            }

            public static List<FluBean> arrayFluBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<FluBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class SportBean {
            /**
             * brf : 适宜
             * txt : 天气较好，赶快投身大自然参与户外运动，尽情感受运动的快乐吧。
             */

            @SerializedName("brf")
            public String brf;
            @SerializedName("txt")
            public String txt;

            public static SportBean objectFromData(String str) {

                return new Gson().fromJson(str, SportBean.class);
            }

            public static List<SportBean> arraySportBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<SportBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class TravBean {
            /**
             * brf : 较适宜
             * txt : 天气较好，同时又有微风伴您一路同行。稍冷，较适宜旅游，您仍可陶醉于大自然的美丽风光中。
             */

            @SerializedName("brf")
            public String brf;
            @SerializedName("txt")
            public String txt;

            public static TravBean objectFromData(String str) {

                return new Gson().fromJson(str, TravBean.class);
            }

            public static List<TravBean> arrayTravBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<TravBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class UvBean {
            /**
             * brf : 中等
             * txt : 属中等强度紫外线辐射天气，外出时建议涂擦SPF高于15、PA+的防晒护肤品，戴帽子、太阳镜。
             */

            @SerializedName("brf")
            public String brf;
            @SerializedName("txt")
            public String txt;

            public static UvBean objectFromData(String str) {

                return new Gson().fromJson(str, UvBean.class);
            }

            public static List<UvBean> arrayUvBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<UvBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }
    }

    public static class DailyForecastBean {
        /**
         * astro : {"mr":"00:10","ms":"12:55","sr":"05:51","ss":"19:15"}
         * cond : {"code_d":"301","code_n":"307","txt_d":"强阵雨","txt_n":"大雨"}
         * date : 2017-07-17
         * hum : 78
         * pcpn : 9.2
         * pop : 100
         * pres : 1009
         * tmp : {"max":"32","min":"25"}
         * uv : 12
         * vis : 13
         * wind : {"deg":"97","dir":"无持续风向","sc":"微风","spd":"7"}
         */

        @SerializedName("astro")
        public AstroBean astro;
        @SerializedName("cond")
        public CondBeanX cond;
        @SerializedName("date")
        public String date;
        @SerializedName("hum")
        public String hum;
        @SerializedName("pcpn")
        public String pcpn;
        @SerializedName("pop")
        public String pop;
        @SerializedName("pres")
        public String pres;
        @SerializedName("tmp")
        public TmpBean tmp;
        @SerializedName("uv")
        public String uv;
        @SerializedName("vis")
        public String vis;
        @SerializedName("wind")
        public WindBeanX wind;

        public static DailyForecastBean objectFromData(String str) {

            return new Gson().fromJson(str, DailyForecastBean.class);
        }

        public static List<DailyForecastBean> arrayDailyForecastBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<DailyForecastBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static class AstroBean {
            /**
             * mr : 00:10
             * ms : 12:55
             * sr : 05:51
             * ss : 19:15
             */

            @SerializedName("mr")
            public String mr;
            @SerializedName("ms")
            public String ms;
            @SerializedName("sr")
            public String sr;
            @SerializedName("ss")
            public String ss;

            public static AstroBean objectFromData(String str) {

                return new Gson().fromJson(str, AstroBean.class);
            }

            public static List<AstroBean> arrayAstroBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<AstroBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class CondBeanX {
            /**
             * code_d : 301
             * code_n : 307
             * txt_d : 强阵雨
             * txt_n : 大雨
             */

            @SerializedName("code_d")
            public String codeD;
            @SerializedName("code_n")
            public String codeN;
            @SerializedName("txt_d")
            public String txtD;
            @SerializedName("txt_n")
            public String txtN;

            public static CondBeanX objectFromData(String str) {

                return new Gson().fromJson(str, CondBeanX.class);
            }

            public static List<CondBeanX> arrayCondBeanXFromData(String str) {

                Type listType = new TypeToken<ArrayList<CondBeanX>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class TmpBean {
            /**
             * max : 32
             * min : 25
             */

            @SerializedName("max")
            public String max;
            @SerializedName("min")
            public String min;

            public static TmpBean objectFromData(String str) {

                return new Gson().fromJson(str, TmpBean.class);
            }

            public static List<TmpBean> arrayTmpBeanFromData(String str) {

                Type listType = new TypeToken<ArrayList<TmpBean>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class WindBeanX {
            /**
             * deg : 97
             * dir : 无持续风向
             * sc : 微风
             * spd : 7
             */

            @SerializedName("deg")
            public String deg;
            @SerializedName("dir")
            public String dir;
            @SerializedName("sc")
            public String sc;
            @SerializedName("spd")
            public String spd;

            public static WindBeanX objectFromData(String str) {

                return new Gson().fromJson(str, WindBeanX.class);
            }

            public static List<WindBeanX> arrayWindBeanXFromData(String str) {

                Type listType = new TypeToken<ArrayList<WindBeanX>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }
    }

    public static class HourlyForecastBean {
        /**
         * cond : {"code":"305","txt":"小雨"}
         * date : 2017-07-17 22:00
         * hum : 87
         * pop : 43
         * pres : 1011
         * tmp : 25
         * wind : {"deg":"128","dir":"东南风","sc":"微风","spd":"11"}
         */

        @SerializedName("cond")
        public CondBeanXX cond;
        @SerializedName("date")
        public String date;
        @SerializedName("hum")
        public String hum;
        @SerializedName("pop")
        public String pop;
        @SerializedName("pres")
        public String pres;
        @SerializedName("tmp")
        public String tmp;
        @SerializedName("wind")
        public WindBeanXX wind;

        public static HourlyForecastBean objectFromData(String str) {

            return new Gson().fromJson(str, HourlyForecastBean.class);
        }

        public static List<HourlyForecastBean> arrayHourlyForecastBeanFromData(String str) {

            Type listType = new TypeToken<ArrayList<HourlyForecastBean>>() {
            }.getType();

            return new Gson().fromJson(str, listType);
        }

        public static class CondBeanXX {
            /**
             * code : 305
             * txt : 小雨
             */

            @SerializedName("code")
            public String code;
            @SerializedName("txt")
            public String txt;

            public static CondBeanXX objectFromData(String str) {

                return new Gson().fromJson(str, CondBeanXX.class);
            }

            public static List<CondBeanXX> arrayCondBeanXXFromData(String str) {

                Type listType = new TypeToken<ArrayList<CondBeanXX>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }

        public static class WindBeanXX {
            /**
             * deg : 128
             * dir : 东南风
             * sc : 微风
             * spd : 11
             */

            @SerializedName("deg")
            public String deg;
            @SerializedName("dir")
            public String dir;
            @SerializedName("sc")
            public String sc;
            @SerializedName("spd")
            public String spd;

            public static WindBeanXX objectFromData(String str) {

                return new Gson().fromJson(str, WindBeanXX.class);
            }

            public static List<WindBeanXX> arrayWindBeanXXFromData(String str) {

                Type listType = new TypeToken<ArrayList<WindBeanXX>>() {
                }.getType();

                return new Gson().fromJson(str, listType);
            }
        }
    }
}
