package com.jameswong.worklist.alarmremind;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmServiceBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent serviceIntent = new Intent(context, AlarmService.class);
        context.startService(serviceIntent);
    }
}
