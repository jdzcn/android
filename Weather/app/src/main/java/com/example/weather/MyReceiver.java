package com.example.weather;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class MyReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(ACTION)) {

            Intent newIntent = context.getPackageManager().getLaunchIntentForPackage(context.getPackageName());
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //2.如果自启动Activity
            context.startActivity(newIntent);
            //3.如果自启动服务
//            context.startService(newIntent);
        }
        //throw new UnsupportedOperationException("Not yet implemented");
    }
}