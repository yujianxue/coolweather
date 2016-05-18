package com.coolweather.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.coolweather.app.service.AutoUpdateService;

/**
 * Created by Administrator on 2016/5/18.
 */
public class AutoUpdateReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("tag","AutoUpdateReceiver onReceive");
        //再次启动AutoUpdateService
        Intent i=new Intent(context,AutoUpdateService.class);
        context.startService(i);
    }
}
