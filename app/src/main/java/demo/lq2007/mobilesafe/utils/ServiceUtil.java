package demo.lq2007.mobilesafe.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;

import demo.lq2007.mobilesafe.service.PhoneService;

import static demo.lq2007.mobilesafe.utils.KeyUtil.TAG;

/**
 * Created by lq200 on 2016/9/16.
 */
public class ServiceUtil {

    public static boolean isRunningService(Context context, String className) {
        //进程管理者
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = manager.getRunningServices(Integer.MAX_VALUE);
        if(runningServices.size() == 0){
            return false;
        }
        //遍历检查
        for (ActivityManager.RunningServiceInfo runningService : runningServices) {
            ComponentName service = runningService.service;
            String name = service.getClassName();
            if(name.equals(className)){
                return true;
            }
        }
        return false;
    }

    public static void runService(Context context, Class serviceClass) {
        Intent intent = new Intent(context, serviceClass);
        context.startService(intent);
    }

    public static void stopService(Context context, Class serviceClass) {
        Intent intent = new Intent(context, serviceClass);
        context.stopService(intent);
    }
}
