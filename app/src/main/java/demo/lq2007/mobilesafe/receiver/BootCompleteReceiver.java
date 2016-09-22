package demo.lq2007.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;

public class BootCompleteReceiver extends BroadcastReceiver {
    public BootCompleteReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        if(SpUtil.getBoolean(context, KeyUtil.OPEN_FIND, KeyUtil.default_OPEN_FIND)) {
            TelephonyManager tel = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String sim = tel.getSimSerialNumber();
            String savedSim = SpUtil.getString(context, KeyUtil.SIM_ID, KeyUtil.default_SIM_ID);

            if (!TextUtils.isEmpty(savedSim) && !TextUtils.isEmpty(sim)) {
                if (!savedSim.equals(sim)) {
                    sendMessage(context);
                }
            }
        }
    }

    private void sendMessage(Context context) {
        SmsManager sms = SmsManager.getDefault();
        String address = SpUtil.getString(context, KeyUtil.SAFE_NUMBER, KeyUtil.default_SAFE_NUMBER);
        if(TextUtils.isEmpty(address)){
            return;
        }
        sms.sendTextMessage(address, null, KeyUtil.MESSAGE, null, null);
    }
}
