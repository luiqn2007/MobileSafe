package demo.lq2007.mobilesafe.utils;

import android.content.Context;
import android.view.Gravity;

/**
 * Created by lq200 on 2016/9/10.
 */
public class KeyUtil {
    /*
    系统变量
     */
    //SharedPreferences名称
    public static String CONFIG = "config";
    //TAG
    public static String TAG = "MobileSafe";

    /*
    UI相关
     */
    //自定义UI控件命名空间
    public static String UI = "http://schemas.android.com/apk/res-auto";

    /*
    启动界面 & 主界面
     */
    //手机更新
    public static String CHECK_UPDATE = "checkUpdate";
    //更新信息文件
    public static String UPDATA_MSG = "http://192.168.1.100:8080/json.html";

    /*
    手机防盗
     */
    //手机防盗设置向导
    public static String NEED_SET_FIND = "set_find";
    //开启手机防盗
    public static String OPEN_FIND = "open_find";
    //安全号码
    public static String SAFE_NUMBER = "safe_number";
    //SIM卡绑定id
    public static String SIM_ID = "SIM_id";
    //联系人列表相关
    public static final String CONTACT_NAME = "name";
    public static final String CONTACT_PHONE = "phone";
    //手机防盗密码
    public static String PASSWORD = "password";
    public static String HAS_PWD = "hasPsd";
    //手机短信控制
    public static String MESSAGE = "SIM卡已被更换，请注意手机安全";
    public static String SAFE_GPS = "#*location*#";
    public static String SAFE_ALARM = "#*alarm*#";
    public static String SAFE_WIPE = "#*wipedata*#";
    public static String SAFE_LOCK = "#*lockscreen*#";

    /*
    高级工具
     */
    //号码归属地前缀
    public static String LOCATION_HEAD = "查询结果:\n\n    ";
    //Toast
    public static String TOAST_THEME = "toast_theme";
    public static String TOAST_SHOW_TIME = "toast_showTime";
    public static String TOAST_POSITION = "toast_position";
    public static String TOAST_X = "toast_x";
    public static String TOAST_Y = "toast_y";

    /*
    默认值集合
     */
    public static boolean default_CHECK_UPDATE = true;
    public static boolean default_NEED_SET_FIND = true;
    public static boolean default_OPEN_FIND = false;
    public static boolean default_HAS_PWD = false;
    public static int default_TOAST_THEME = ToastUtil.THEME_BLUE;
    public static int default_TOAST_POSITION = Gravity.CENTER;
    public static int default_TOAST_SET_POSITION = Gravity.LEFT | Gravity.TOP;
    public static int default_TOAST_X = 0;
    public static int default_TOAST_Y = 0;
    public static long default_TOAST_SHOW_TIME = 3000;
    public static String default_SIM_ID = "";
    public static String default_SAFE_NUMBER = "";
    public static String default_PASSWORD = "";

    /*
    键值转换器器
     */
    //Toast风格
    public static String getThemeName(int themeCode){
        switch (themeCode){
            case ToastUtil.THEME_GRAY:
                return "灰色";
            case ToastUtil.THEME_GREEN:
                return "绿色";
            case ToastUtil.THEME_ORANGE:
                return "橙色";
            case ToastUtil.THEME_WHITE:
                return "半透明";
            case ToastUtil.THEME_BLUE:
                return "蓝色";
        }
        return "未选择";
    }
    public static int getThemeCode(String themeName){
        switch (themeName){
            case "灰色":
                return ToastUtil.THEME_GRAY;
            case "绿色":
                return ToastUtil.THEME_GREEN;
            case "橙色":
                return ToastUtil.THEME_ORANGE;
            case "半透明":
                return ToastUtil.THEME_WHITE;
            case "蓝色":
                return ToastUtil.THEME_BLUE;
        }
        return ToastUtil.THEME_NULL;
    }
}
