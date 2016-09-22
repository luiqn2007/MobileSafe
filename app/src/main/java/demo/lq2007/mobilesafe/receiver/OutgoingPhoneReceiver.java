package demo.lq2007.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import demo.lq2007.mobilesafe.db.dao.AddressDao;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;
import demo.lq2007.mobilesafe.utils.ToastUtil;

/**
 * 外拨电话接受者
 */
public class OutgoingPhoneReceiver extends BroadcastReceiver {

    public OutgoingPhoneReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving
        // an Intent broadcast.
        String phone = getResultData();
        String location = AddressDao.queryAddressByNumber(context, phone);
        ToastUtil.showToastWithoutDismiss(context, location,
                SpUtil.getInt(context, KeyUtil.TOAST_THEME, KeyUtil.default_TOAST_THEME),
                SpUtil.getInt(context, KeyUtil.TOAST_POSITION, KeyUtil.default_TOAST_POSITION),
                SpUtil.getInt(context, KeyUtil.TOAST_X, KeyUtil.default_TOAST_X),
                SpUtil.getInt(context, KeyUtil.TOAST_Y, KeyUtil.default_TOAST_Y));
    }
}
