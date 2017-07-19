package com.oven.weather_oven.base;
import android.app.Activity;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 活动管理器
 * Created by oven on 2017/5/14.
 */

public class ActivityCollector {
    private static long lastPressTime = 0;

    private static List<Activity> activities = new ArrayList<>();

    static void addActivity(Activity activity) {
        activities.add(activity);
    }

    static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {

        //结束程序
        if (new Date().getTime() - lastPressTime < 1000) {
            for (Activity activity : activities) {
                if (!activity.isFinishing()) {
                    activity.finish();
                }

            }
        } else {
            lastPressTime = new Date().getTime();//重置lastPressTime
        }

    }
}
