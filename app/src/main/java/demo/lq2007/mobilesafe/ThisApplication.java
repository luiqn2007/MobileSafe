package demo.lq2007.mobilesafe;

import android.app.Application;

import org.xutils.x;

/**
 * 初始化xUtils3.0
 * Created by lq200 on 2016/9/9.
 */
public class ThisApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
    }
}
