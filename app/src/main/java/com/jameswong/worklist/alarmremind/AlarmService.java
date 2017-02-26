package com.jameswong.worklist.alarmremind;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.jameswong.worklist.AlarmBean;
import com.jameswong.worklist.database.AlarmDBSupport;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class AlarmService extends Service {

    private AlarmDBSupport support;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    /**
     * 得到下一条提醒的日程信息
     *
     * @return
     */
    private AlarmBean getNextStart() {

        support = new AlarmDBSupport(getApplicationContext());
        List<AlarmBean> all = support.getAll();
        support.deactivate();
        if (all.size() == 0) {
            return null;
        }

        Collections.sort(all, new Comparator<AlarmBean>() {
            @Override
            public int compare(AlarmBean lhs, AlarmBean rhs) {
                if (lhs.getRealStartAlarmTime().getTimeInMillis() > rhs.getRealStartAlarmTime().getTimeInMillis()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getRealStartAlarmTime().getTimeInMillis() - all.get(i).getCurrentTime().getTimeInMillis() > 0) {
                return all.get(i);
            }
        }

        return null;
    }

    /**
     * 得到下一条提醒的日程信息
     *
     * @return
     */
    private AlarmBean getNextEnd() {

        support = new AlarmDBSupport(getApplicationContext());
        List<AlarmBean> all = support.getAll();
        support.deactivate();
        if (all.size() == 0) {
            return null;
        }

        Collections.sort(all, new Comparator<AlarmBean>() {
            @Override
            public int compare(AlarmBean lhs, AlarmBean rhs) {
                if (lhs.getRealEndAlarmTime().getTimeInMillis() > rhs.getRealEndAlarmTime().getTimeInMillis()) {
                    return 1;
                } else {
                    return -1;
                }
            }
        });

        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getRealEndAlarmTime().getTimeInMillis() - all.get(i).getCurrentTime().getTimeInMillis() > 0) {
                return all.get(i);
            }
        }

        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            public void run() {
                AlarmBean alarmBean = getNextStart();
                if (alarmBean != null) {
                    alarmBean.schedule(getApplicationContext(),"start");
                }
                AlarmBean nextEnd = getNextEnd();
                if (nextEnd != null) {
                    nextEnd.schedule(getApplicationContext(),"end");
                }
            }
        }).start();
        return START_NOT_STICKY;
    }
}
