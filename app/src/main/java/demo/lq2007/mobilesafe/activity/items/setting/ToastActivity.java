package demo.lq2007.mobilesafe.activity.items.setting;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.ui.SettingListShow;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;
import demo.lq2007.mobilesafe.utils.ToastUtil;

public class ToastActivity extends AppCompatActivity {

    @ViewInject(R.id.sl_theme)
    SettingListShow sl_theme;
    @ViewInject(R.id.sl_time)
    SettingListShow sl_time;
    @ViewInject(R.id.sl_location2)
    SettingListShow sl_position;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast);
        x.view().inject(this);

        //设置归属地Toast主题
        sl_theme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ToastActivity.this);
                builder.setTitle("选择主题");
                final String[] themes = new String[]{
                        KeyUtil.getThemeName(ToastUtil.THEME_BLUE),
                        KeyUtil.getThemeName(ToastUtil.THEME_GRAY),
                        KeyUtil.getThemeName(ToastUtil.THEME_GREEN),
                        KeyUtil.getThemeName(ToastUtil.THEME_ORANGE),
                        KeyUtil.getThemeName(ToastUtil.THEME_WHITE),
                };
                int index = 0;
                for (String theme : themes) {
                    if(theme.equals(KeyUtil.getThemeName(SpUtil.getInt(ToastActivity.this, KeyUtil.TOAST_THEME, KeyUtil.default_TOAST_THEME)))){
                        break;
                    }
                    index++;
                }
                builder.setSingleChoiceItems(themes, index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int themeCode = KeyUtil.getThemeCode(themes[which]);
                        SpUtil.putInt(ToastActivity.this, KeyUtil.TOAST_THEME, themeCode);
                        sl_theme.setChoose(themes[which]);
                    }
                });
                builder.setNegativeButton("确认", null);
                builder.show();
            }
        });

        //Toast存在时间
        sl_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(ToastActivity.this);
                builder.setTitle("选择时间");
                final String[] times = new String[]{
                        "2.0秒","2.5秒","3.0秒","3.5秒","4.0秒","4.5秒","5.0秒",
                };
                int index = (int) (SpUtil.getLong(ToastActivity.this, KeyUtil.TOAST_SHOW_TIME, KeyUtil.default_TOAST_SHOW_TIME) / 500 - 4);
                builder.setSingleChoiceItems(times, index, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        long timeCode = (which + 4) * 500;
                        SpUtil.putLong(ToastActivity.this, KeyUtil.TOAST_SHOW_TIME, timeCode);
                        sl_time.setChoose(times[which]);
                    }
                });
                builder.setNegativeButton("确认", null);
                builder.show();
            }
        });

        sl_position.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ToastActivity.this, ToastLocationActivity.class);
                startActivityForResult(i, 1);
            }
        });

        setValues();
    }

    private void setValues() {
        //显示时间
        sl_time.setChoose(String.valueOf((double)(SpUtil.getLong(this, KeyUtil.TOAST_SHOW_TIME, KeyUtil.default_TOAST_SHOW_TIME))/1000) + "秒");
        //显示主题
        sl_theme.setChoose(KeyUtil.getThemeName(SpUtil.getInt(ToastActivity.this, KeyUtil.TOAST_THEME, KeyUtil.default_TOAST_THEME)));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setValues();
    }
}
