package com.oven.weather_oven.service;


import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;

import android.graphics.BitmapFactory;

import android.os.IBinder;


import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.oven.weather_oven.R;
import com.oven.weather_oven.activity.WeatherActivity;


public class AutoUpdateService extends Service {
    //TODO:搞定以后做个切换选择区域的Button

    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*
        *设置系统提示，睡眠状态下也唤醒,6小时发送一次广播
        */
        AlarmManager manager =(AlarmManager)getSystemService(ALARM_SERVICE);
        int updateTime = 6 * 60 * 60 * 1000 ;//毫秒
        long triggerArtTime = SystemClock.elapsedRealtime() + updateTime;

        PendingIntent pi = PendingIntent.getBroadcast(this,0,intent,0);
        manager.cancel(pi);
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerArtTime,pi);
        Intent weatherIntent = new Intent();
        weatherIntent.setAction("WEATHER_UPDATE_ACTION");// 自定义Action
        sendBroadcast(weatherIntent);


        /*
         *前台服务，显示实时天气，activity消失一起消失
         */
        Intent intent1 = new Intent(this, WeatherActivity.class);
        PendingIntent pi1 = PendingIntent.getActivity(this,0,intent1,0);
        Notification notification = new NotificationCompat.Builder(this)
                //.setContentTitle(bundle.getString("temperature"))
                .setContentTitle(intent.getStringExtra("degree"))
                .setContentText(intent.getStringExtra("city"))
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_cloud)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.ic_cloud_white))
                .setContentIntent(pi1)
                .setAutoCancel(true)
                .build();
        startForeground(1,notification);
        return super.onStartCommand(intent, flags, startId);
    }

}
