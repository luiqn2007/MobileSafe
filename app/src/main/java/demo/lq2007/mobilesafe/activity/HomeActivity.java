package demo.lq2007.mobilesafe.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.activity.items.AToolsActivity;
import demo.lq2007.mobilesafe.activity.items.FindActivity;
import demo.lq2007.mobilesafe.activity.items.SettingActivity;
import demo.lq2007.mobilesafe.ui.PasswordText;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.MD5Util;
import demo.lq2007.mobilesafe.utils.SpUtil;

public class HomeActivity extends AppCompatActivity {

    @ViewInject(value = R.id.gv_tools)
    GridView gv_tools;

    private String[] mTitles;
    private int[] mImages;
    private Class[] mActivityClass;

    Intent mIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        x.view().inject(this);

        mTitles = new String[]{
                "手机防盗", "通信卫士", "软件管理",
                "进程管理", "流量统计", "手机杀毒",
                "缓存清理", "高级工具", "设置中心"
        };
        mImages = new int[]{
                R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
                R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
                R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings
        };
        mActivityClass = new Class[]{
                FindActivity.class , null, null,
                null, null, null,
                null, AToolsActivity.class, SettingActivity.class
        };

        BaseAdapter adapter = new MyAdapter();
        gv_tools.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        gv_tools.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mIntent = new Intent(HomeActivity.this, mActivityClass[position]);
                if(position == 0){
                    showCheckedDialog();
                    return;
                }
                startActivity(mIntent);
            }
        });
    }

    /**
     * 显示登录或设置密码界面
     */
    private void showCheckedDialog() {
        boolean hasPsd = SpUtil.getBoolean(this, KeyUtil.HAS_PWD, KeyUtil.default_HAS_PWD);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog alert = builder.create();
        View v;
        Button btn_cancel,btn_set;
        if(hasPsd){
            v = View.inflate(HomeActivity.this, R.layout.dialog_pwd_check, null);
            btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
            btn_set = (Button) v.findViewById(R.id.btn_set);
            final PasswordText pt_pwd = (PasswordText) v.findViewById(R.id.pt_pwd);
            btn_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String psd = pt_pwd.getPassword();

                    if(TextUtils.isEmpty(psd)){
                        Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!MD5Util.getMD5(psd).equals(SpUtil.getString(HomeActivity.this, KeyUtil.PASSWORD, KeyUtil.default_PASSWORD))){
                        Toast.makeText(HomeActivity.this, "密码错误", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startActivity(mIntent);
                    alert.dismiss();
                }
            });
        } else {
            v = View.inflate(HomeActivity.this, R.layout.dialog_pwd_add, null);
            btn_cancel = (Button) v.findViewById(R.id.btn_cancel);
            btn_set = (Button) v.findViewById(R.id.btn_set);
            final PasswordText pt_pwd1 = (PasswordText) v.findViewById(R.id.pt_pwd1);
            final PasswordText pt_pwd2 = (PasswordText) v.findViewById(R.id.pt_pwd2);
            btn_set.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String psd = pt_pwd1.getPassword();
                    String psd2 = pt_pwd2.getPassword();

                    if(TextUtils.isEmpty(psd) || TextUtils.isEmpty(psd2)){
                        Toast.makeText(HomeActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(psd.length() < 6){
                        Toast.makeText(HomeActivity.this, "密码不得小于六位数", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!psd.equals(psd2)){
                        Toast.makeText(HomeActivity.this, "两次密码不匹配", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SpUtil.putString(HomeActivity.this, KeyUtil.PASSWORD, MD5Util.getMD5(psd));
                    SpUtil.putBoolean(HomeActivity.this, KeyUtil.HAS_PWD, true);
                    startActivity(mIntent);
                    alert.dismiss();
                }
            });
        }
        alert.setView(v, 0, 0, 0, 0);
        alert.setCancelable(false);
        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
            }
        });
        alert.show();
    }

    private class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = View.inflate(getApplicationContext(), R.layout.list_home_item, null);

            ImageView iv_item = (ImageView) v.findViewById(R.id.iv_item);
            TextView tv_item = (TextView) v.findViewById(R.id.tv_item);

            iv_item.setImageResource(mImages[position]);
            tv_item.setText(mTitles[position]);

            return v;
        }
    }
}
