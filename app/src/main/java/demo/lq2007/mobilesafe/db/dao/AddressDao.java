package demo.lq2007.mobilesafe.db.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

/**
 * 从数据库查询归属地
 * Created by lq200 on 2016/9/15.
 */
public class AddressDao {

    /**
     * 用于从数据库中查询归属地
     * @param context 上下文
     * @param number 要查询的手机号码
     * @return 归属地
     */
    public static String queryAddressByNumber(Context context, String number){
        String location;
        File database = new File(context.getFilesDir(), "address.db");
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(database.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        String sql = "select location from data2 where id=(select outkey from data1 where id=?)";
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{number.substring(0, 7)});
        if(cursor.moveToNext()){
            location = cursor.getString(0);
        } else {
            location = "数据库内无此数据";
        }
        cursor.close();
        sqLiteDatabase.close();
        return location;
    }

    public static String queryAddressByCode(Context context, String number){
        String location;
        File database = new File(context.getFilesDir(), "address.db");
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(database.getAbsolutePath(), null, SQLiteDatabase.OPEN_READONLY);
        String sql = "select location from data2 where id=?";
        String code = number.substring(1, 3);
        Cursor cursor = sqLiteDatabase.rawQuery(sql, new String[]{code});
        if(cursor.moveToNext()){
            location = cursor.getString(0);
        } else {
            code = number.substring(1, 4);
            cursor = sqLiteDatabase.rawQuery(sql, new String[]{code});
            if(cursor.moveToNext()){
                location = cursor.getString(0);
            } else {
                location = "数据库内无此数据";
            }
        }
        cursor.close();
        sqLiteDatabase.close();
        if(!location.equals("数据库内无此数据")){
            location = location.substring(0, location.length() - 2);
        }
        return location;
    }
}
