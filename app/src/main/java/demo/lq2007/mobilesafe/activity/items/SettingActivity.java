package demo.lq2007.mobilesafe.activity.items;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.activity.items.setting.ToastActivity;
import demo.lq2007.mobilesafe.service.PhoneService;
import demo.lq2007.mobilesafe.ui.SettingChecked;
import demo.lq2007.mobilesafe.ui.SettingListShow;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.ServiceUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;

import static demo.lq2007.mobilesafe.utils.KeyUtil.TAG;


public class SettingActivity extends AppCompatActivity {

    @ViewInject(R.id.sl_update)
    SettingChecked sl_update;
    @ViewInject(R.id.sl_phone)
    SettingChecked sl_phone;
    @ViewInject(R.id.sl_toast)
    SettingListShow sl_toast;

    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        x.view().inject(this);

        requestPermission();
        //自动更新
        sl_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sl_update.setChecked(!sl_update.isChecked());
                SpUtil.putBoolean(SettingActivity.this, KeyUtil.CHECK_UPDATE, sl_update.isChecked());
            }
        });

        //号码归属地
        sl_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sl_phone.setChecked(!sl_phone.isChecked());
                if(sl_phone.isChecked()){
                    ServiceUtil.runService(SettingActivity.this, PhoneService.class);
                } else {
                    ServiceUtil.stopService(SettingActivity.this, PhoneService.class);
                }
            }
        });

        //Toast设置
        sl_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingActivity.this, ToastActivity.class);
                startActivity(i);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        sl_update.setChecked(SpUtil.getBoolean(SettingActivity.this, KeyUtil.CHECK_UPDATE, true));
        sl_phone.setChecked(ServiceUtil.isRunningService(this, PhoneService.class.getName()));
    }

    private void requestPermission() {
        Log.i(TAG, "requestPermission: 校验授权");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "requestPermission: 未授权");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.PROCESS_OUTGOING_CALLS}, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //检查是否完成授权
        if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        } else {
            //失败--第一次,提示重新授权
            if(first){
                first = false;
                //构建提示授权的Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("授权申请");
                builder.setMessage("为了更好地保护您的手机，我们需要这些权限:" +
                        "\n  电话--用于查询来电/去电归属地");
                builder.setPositiveButton("授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        requestPermission();
                        builder.create().dismiss();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        builder.create().cancel();
                    }
                });
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        //失败--第二次,提示失败
                        Toast.makeText(SettingActivity.this, "我们没有获得足够的权限，这会造成相关功能将不能使用，程序崩溃", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            } else {
                //失败--第二次,提示失败
                Toast.makeText(this, "我们没有获得足够的权限，这会造成相关功能将不能使用，程序崩溃", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
