package demo.lq2007.mobilesafe.utils;

import android.content.Context;

/**
 * Created by lq200 on 2016/9/10.
 */
public class SpUtil {

    public static void putBoolean(Context ctt, String key, boolean value){
        ctt.getSharedPreferences(KeyUtil.CONFIG, Context.MODE_PRIVATE).edit().putBoolean(key,value).commit();
    }

    public static boolean getBoolean(Context ctt, String key, boolean defValue){
        return ctt.getSharedPreferences(KeyUtil.CONFIG, Context.MODE_PRIVATE).getBoolean(key, defValue);
    }

    public static void putString(Context ctt, String key, String value){
        ctt.getSharedPreferences(KeyUtil.CONFIG, Context.MODE_PRIVATE).edit().putString(key,value).commit();
    }

    public static String getString(Context ctt, String key, String defValue){
        return ctt.getSharedPreferences(KeyUtil.CONFIG, Context.MODE_PRIVATE).getString(key, defValue);
    }

    public static void putInt(Context ctt, String key, int value){
        ctt.getSharedPreferences(KeyUtil.CONFIG, Context.MODE_PRIVATE).edit().putInt(key,value).commit();
    }

    public static int getInt(Context ctt, String key, int defValue){
        return ctt.getSharedPreferences(KeyUtil.CONFIG, Context.MODE_PRIVATE).getInt(key, defValue);
    }

    public static void putLong(Context ctt, String key, long value){
        ctt.getSharedPreferences(KeyUtil.CONFIG, Context.MODE_PRIVATE).edit().putLong(key,value).commit();
    }

    public static long getLong(Context ctt, String key, long defValue){
        return ctt.getSharedPreferences(KeyUtil.CONFIG, Context.MODE_PRIVATE).getLong(key, defValue);
    }

    public void remove(Context ctt, String key){
        ctt.getSharedPreferences(KeyUtil.CONFIG, Context.MODE_PRIVATE).edit().remove(key).commit();
    }
}
