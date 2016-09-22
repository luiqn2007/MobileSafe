package demo.lq2007.mobilesafe.activity.items.findLeader;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.widget.Toast;

import demo.lq2007.mobilesafe.R;

import static demo.lq2007.mobilesafe.utils.KeyUtil.TAG;


public class WelcomeActivity extends SetupActivity {

    private boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        requestPermission();
    }

    @Override
    public void saveSetting() {

    }

    private void requestPermission() {
        Log.i(TAG, "requestPermission: 校验授权");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(ContextCompat.checkSelfPermission(this, android.Manifest.permission.PROCESS_OUTGOING_CALLS) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED){
                Log.i(TAG, "requestPermission: 未授权");
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.PROCESS_OUTGOING_CALLS,android.Manifest.permission.ACCESS_FINE_LOCATION,
                        android.Manifest.permission.SEND_SMS,android.Manifest.permission.READ_CONTACTS}, 1);
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //检查是否完成授权
        if(grantResults.length > 3
                && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED
                && grantResults[2] == PackageManager.PERMISSION_GRANTED
                && grantResults[3] == PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        } else {
            //失败--第一次,提示重新授权
            if(first){
                first = false;
                //构建提示授权的Dialog
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("授权申请");
                builder.setMessage("为了更好地保护您的手机，我们需要这些权限:" +
                        "\n  电话本--用于选择安全号码" +
                        "\n  电话--用于查询来电/去电归属地" +
                        "\n  短信--用于短信控制手机，实现手机防盗" +
                        "\n  位置信息--用于手机防盗获取手机位置");
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
                        Toast.makeText(WelcomeActivity.this, "我们没有获得足够的权限，这会造成相关功能将不能使用，程序崩溃", Toast.LENGTH_SHORT).show();
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
