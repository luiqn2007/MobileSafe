package demo.lq2007.mobilesafe.receiver;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.text.TextUtils;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.service.GPSService;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;

public class SMSReceiver extends BroadcastReceiver {
    private static MediaPlayer mPlayer;
    private static ComponentName mAdminComponentName;
    private static DevicePolicyManager mDevicePolicyManager;

    public SMSReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        //超级管理员
        mAdminComponentName = new ComponentName(context, SafeDeviceAdminReceiver.class);
        mDevicePolicyManager = (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
        //短信识别
        String safePhone = SpUtil.getString(context, KeyUtil.SAFE_NUMBER, KeyUtil.default_SAFE_NUMBER);
        if (SpUtil.getBoolean(context, KeyUtil.OPEN_FIND, KeyUtil.default_OPEN_FIND) && !TextUtils.isEmpty(safePhone)) {
            Object[] objs = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objs) {
                SmsMessage sms = SmsMessage.createFromPdu((byte[]) obj);
                String body = sms.getMessageBody();
                String number = sms.getOriginatingAddress();
                number.replace(" ","");
                number.replace("-","");

                if (safePhone.equals(number)) {
                    if (KeyUtil.SAFE_ALARM.equals(body)) {
                        alarmPhone(context);
                    } else if (KeyUtil.SAFE_GPS.equals(body)) {
                        getLocation(context);
                    } else if (KeyUtil.SAFE_LOCK.equals(body)) {
                        lockPhone(context);
                    } else if (KeyUtil.SAFE_WIPE.equals(body)) {
                        wipePhone(context);
                    }
                }
            }
        }
    }

    private void wipePhone(Context context) {
        if(mDevicePolicyManager.isAdminActive(mAdminComponentName)){
            mDevicePolicyManager.wipeData(0);
        }
        abortBroadcast();
    }

    private void lockPhone(Context context) {
        if(mDevicePolicyManager.isAdminActive(mAdminComponentName)){
            mDevicePolicyManager.lockNow();
        }
        abortBroadcast();
    }

    private void alarmPhone(Context context) {
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audio.setStreamVolume(AudioManager.STREAM_MUSIC, audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC), 0);
        if(mPlayer != null){
            mPlayer.release();
        }
        mPlayer = MediaPlayer.create(context, R.raw.alarm);
        mPlayer.setLooping(true);
        mPlayer.start();
        abortBroadcast();
    }

    public void getLocation(Context context) {
        Intent intent = new Intent(context, GPSService.class);
        context.startService(intent);
    }
}
