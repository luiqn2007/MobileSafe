package demo.lq2007.mobilesafe.activity.items.findLeader;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.receiver.SafeDeviceAdminReceiver;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;

public class FinalActivity extends SetupActivity {

    private ComponentName mAdminReceiver;
    private DevicePolicyManager mDevicePolicyManager;

    @ViewInject(value = R.id.cb_open)
    CheckBox cb_open;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final);
        x.view().inject(this);

        cb_open.setChecked(SpUtil.getBoolean(this, KeyUtil.OPEN_FIND, true));

        mAdminReceiver = new ComponentName(this, SafeDeviceAdminReceiver.class);
        mDevicePolicyManager = (DevicePolicyManager) this.getSystemService(Context.DEVICE_POLICY_SERVICE);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        if(mDevicePolicyManager.isAdminActive(mAdminReceiver)) {
            builder.setTitle("管理员权限");
            builder.setMessage("您已设定了该权限，如果您想卸载本应用，可以在此取消该权限");
            builder.setNegativeButton("保持权限", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    builder.create().dismiss();
                }
            });
            builder.setPositiveButton("取消权限", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mDevicePolicyManager.removeActiveAdmin(mAdminReceiver);
                    Toast.makeText(FinalActivity.this, "您已取消本程序的超级管理员权限，可以正常卸载", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            builder.setTitle("管理员权限");
            builder.setMessage("手机防盗短信锁屏、清空手机信息需要超级管理员权限，请赋予本程序相应权限。请注意，一旦激活超级管理员权限，您将不能卸载本程序，但可以再次进入该设置界面取消权限，即可正常卸载");
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Toast.makeText(FinalActivity.this, "您未激活该权限，将无法通过短信控制手机锁屏和清除用户信息", Toast.LENGTH_SHORT).show();
                    builder.create().dismiss();
                }
            });
            builder.setPositiveButton("设置", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mAdminReceiver);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "手机安全所需权限");
                    startActivityForResult(intent, 1);
                    builder.create().dismiss();
                }
            });
        }
        builder.show();
    }

    @Override
    public void saveSetting() {
        SpUtil.putBoolean(this, KeyUtil.NEED_SET_FIND, false);
        SpUtil.putBoolean(this, KeyUtil.OPEN_FIND, cb_open.isChecked());
    }

    @Override
    public void next(View v) {
        super.next(v);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (mDevicePolicyManager.isAdminActive(mAdminReceiver)){
            Toast.makeText(this, "激活成功", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "您未激活该权限，将无法通过短信控制手机锁屏和清除用户信息", Toast.LENGTH_SHORT).show();
        }
    }
}
