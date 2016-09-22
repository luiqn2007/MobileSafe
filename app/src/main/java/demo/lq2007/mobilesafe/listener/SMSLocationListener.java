package demo.lq2007.mobilesafe.listener;

import android.app.Service;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;

import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;

import static demo.lq2007.mobilesafe.utils.KeyUtil.TAG;

/**
 * Created by lq200 on 2016/9/14.
 */
public class SMSLocationListener implements LocationListener {
    Context mContext;
    Service mLocationService;

    public SMSLocationListener(Context context, Service location){
        mContext = context;
        mLocationService = location;
    }

    //定位位置改变
    @Override
    public void onLocationChanged(Location location) {
        Log.i(TAG, "onLocationChanged: 收到短信");
        String phone = SpUtil.getString(mContext, KeyUtil.SAFE_NUMBER, KeyUtil.default_SAFE_NUMBER);
        if(TextUtils.isEmpty(phone)){
            return;
        }
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        double altitude = location.getAltitude();
        SmsManager sms = SmsManager.getDefault();
        String smsText = "手机所在位置:" +
                "\n经度 : " + longitude +
                "\n纬度 : " + latitude +
                "\n海拔 : " + altitude;
        sms.sendTextMessage(phone, null, smsText, null, null);
        mLocationService.stopSelf();
    }
    //定位方式改变
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    //定位可用
    @Override
    public void onProviderEnabled(String provider) {

    }
    //定位不可用
    @Override
    public void onProviderDisabled(String provider) {

    }
}
