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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.activity.items.findLeader.WelcomeActivity;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;
import static demo.lq2007.mobilesafe.utils.KeyUtil.TAG;

public class FindActivity extends AppCompatActivity {
    boolean first = true;

    @ViewInject(value = R.id.tv_safeNum)
    TextView tv_safeNum;
    @ViewInject(value = R.id.ib_lock)
    ImageView iv_lock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find);
        x.view().inject(this);

        requestPermission();

        if(SpUtil.getBoolean(this, KeyUtil.NEED_SET_FIND, KeyUtil.default_NEED_SET_FIND)){
            toSet(null);
        }

        initValues();
    }

    private void initValues() {
        //获取数据
        tv_safeNum.setText(SpUtil.getString(this, KeyUtil.SAFE_NUMBER, KeyUtil.default_SAFE_NUMBER));
        if(SpUtil.getBoolean(this, KeyUtil.OPEN_FIND, KeyUtil.default_OPEN_FIND)){
            iv_lock.setImageResource(R.drawable.lock);
        } else {
            iv_lock.setImageResource(R.drawable.unlock);
        }
    }

    public void toSet(View v){
        Intent intent = new Intent(this, WelcomeActivity.class);
        startActivity(intent);
        finish();
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
                        Toast.makeText(FindActivity.this, "我们没有获得足够的权限，这会造成相关功能将不能使用，程序崩溃", Toast.LENGTH_SHORT).show();
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
