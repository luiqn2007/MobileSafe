package demo.lq2007.mobilesafe.engine;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import demo.lq2007.mobilesafe.utils.KeyUtil;

/**
 * Created by lq200 on 2016/9/13.
 */
public class ContactEngine {

    /**
     * 获取联系人
     * @param ctx 上下文,用于获得内容解析者
     * @return
     */
    public static List<HashMap<String, String>> getAllContactInfo(Context ctx){
        ContentResolver resolver = ctx.getContentResolver();
        List<HashMap<String, String>> list = new ArrayList<>();
        /*
        获取内容提供者地址--com.android.contacts
        raw_contacts    存放联系人名称及id  raw_contacts
        view_data       存放联系人数据及id  data
         */
        Uri raw_uri = Uri.parse("content://com.android.contacts/raw_contacts");
        Uri data_uri = Uri.parse("content://com.android.contacts/data");

        //查询
        Cursor cursor = resolver.query(raw_uri, new String[]{"contact_id"}, null, null, null);
        while(cursor.moveToNext()){
            String id = cursor.getString(0);
            if(TextUtils.isEmpty(id)){
                continue;
            }
            Cursor contact = resolver.query(data_uri, new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);
            //获取数据
            HashMap<String, String> data = new HashMap<>();
            while(contact.moveToNext()){
                String data1 = contact.getString(0);
                String mimetype = contact.getString(1);
                if("vnd.android.cursor.item/phone_v2".equals(mimetype)){
                    data1.replace(" ","");
                    data1.replace("-","");
                    data.put(KeyUtil.CONTACT_PHONE, data1);
                } else if("vnd.android.cursor.item/name".equals(mimetype)){
                    data.put(KeyUtil.CONTACT_NAME, data1);
                }
            }
            list.add(data);
            contact.close();
        }
        cursor.close();

        return list;
    }
}
