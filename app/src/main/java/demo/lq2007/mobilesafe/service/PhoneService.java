package demo.lq2007.mobilesafe.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import demo.lq2007.mobilesafe.db.dao.AddressDao;
import demo.lq2007.mobilesafe.receiver.OutgoingPhoneReceiver;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;
import demo.lq2007.mobilesafe.utils.ToastUtil;

public class PhoneService extends Service {

    private TelephonyManager mManager;
    private MyPhoneStateListener mListenner;
    private OutgoingPhoneReceiver mOutgoingPhoneReceiver;

    public PhoneService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        mManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        //监听
        mListenner = new MyPhoneStateListener();
        mManager.listen(mListenner, PhoneStateListener.LISTEN_CALL_STATE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //接收者
        //设置接收广播事件
        mOutgoingPhoneReceiver = new OutgoingPhoneReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.NEW_OUTGOING_CALL");
        intentFilter.setPriority(2000);
        //注册广播
        registerReceiver(mOutgoingPhoneReceiver, intentFilter);
        return super.onStartCommand(intent, flags, startId);
    }

    private class MyPhoneStateListener extends PhoneStateListener{
        @Override
        public void onCallStateChanged(int state, String incomingNumber) {
            super.onCallStateChanged(state, incomingNumber);
            if(TelephonyManager.CALL_STATE_RINGING == state){
                if(!TextUtils.isEmpty(incomingNumber)){
                    ToastUtil.showToast(getApplicationContext()
                            , AddressDao.queryAddressByNumber(getApplicationContext(), incomingNumber)
                            , SpUtil.getLong(getApplicationContext(), KeyUtil.TOAST_SHOW_TIME, KeyUtil.default_TOAST_SHOW_TIME)
                            , SpUtil.getInt(getApplicationContext(), KeyUtil.TOAST_THEME, KeyUtil.default_TOAST_THEME)
                            , SpUtil.getInt(getApplicationContext(), KeyUtil.TOAST_POSITION, KeyUtil.default_TOAST_POSITION)
                            , SpUtil.getInt(getApplicationContext(), KeyUtil.TOAST_X, KeyUtil.default_TOAST_X)
                            , SpUtil.getInt(getApplicationContext(), KeyUtil.TOAST_Y, KeyUtil.default_TOAST_Y));
                }
            } else if(TelephonyManager.CALL_STATE_IDLE == state){
                ToastUtil.closeToast(getApplicationContext());
            }
        }
    }

    @Override
    public void onDestroy() {
        unregisterReceiver(mOutgoingPhoneReceiver);
        mManager.listen(mListenner, PhoneStateListener.LISTEN_NONE);
        super.onDestroy();
    }
}
