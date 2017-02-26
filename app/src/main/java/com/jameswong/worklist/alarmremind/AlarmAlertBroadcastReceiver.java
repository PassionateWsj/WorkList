package com.jameswong.worklist.alarmremind;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.WindowManager;

import com.jameswong.worklist.AlarmBean;
import com.jameswong.worklist.database.AlarmDBSupport;

import java.io.IOException;


public class AlarmAlertBroadcastReceiver extends BroadcastReceiver {

    private static final String TAG = "hjjzz";

    private MediaPlayer mediaPlayer;
    private Vibrator vibrator;
    private boolean isVibrator = false;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent alarmServiceIntent = new Intent(
                context,
                AlarmServiceBroadcastReceiver.class);
        context.sendBroadcast(alarmServiceIntent, null);

        Bundle bundle = intent.getExtras();
        AlarmBean alarm = (AlarmBean) bundle.getSerializable("alarm");
        showStartAlarmDialog(context, alarm);

    }

    /**
     * 显示任务开始的dialog
     *
     * @param context
     * @param bean
     */
    private void showStartAlarmDialog(Context context, AlarmBean bean) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
//        playMusicAndVibrate(context, bean);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("距离您定的日期 " + bean.getTitle() + " 已经到了哦！")
                .setMessage(bean.getDescription())
                .setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        cancel();
                    }
                });

        AlertDialog dialog = builder.create();
        dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        dialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                new AlarmDBSupport(context).updateStatus(bean.getId(), 1);
//                BusProvider.getInstance().send(new Events.UpdateEvent());
//                Intent intent = new Intent();
//                intent.putExtra("update","update");
//                intent.setAction("com.jameswong.update");//action与接收器相同
//                context.sendBroadcast(intent);
                Log.i(TAG, "正在进行任务的广播发送啦~~~~~~~~");
            }
        }).start();
    }

    /**
     * 暂停音乐
     * 停止震动
     */
    private void cancel() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
            if (isVibrator) {
                vibrator.cancel();
                isVibrator = false;
            }
        }
    }

    /**
     * 播放音乐
     *
     * @param context
     */
    private void playMusicAndVibrate(Context context, AlarmBean bean) {
        Uri ringtoneUri = Uri.parse(bean.getAlarmTonePath());
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
        } else {
            if (mediaPlayer.isPlaying())
                mediaPlayer.stop();
            mediaPlayer.reset();
        }
        try {
            mediaPlayer.setVolume(100f, 100f);
            mediaPlayer.setDataSource(context, ringtoneUri);
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
            mediaPlayer.setLooping(true);
            mediaPlayer.prepare();
            mediaPlayer.start();
            isVibrator = bean.getIsVibrate();
            if (isVibrator) {
                vibrator.vibrate(new long[]{1000, 50, 1000, 50}, 0);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
