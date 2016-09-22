package demo.lq2007.mobilesafe.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.common.util.IOUtil;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;
import demo.lq2007.mobilesafe.utils.StreamUtil;

import static demo.lq2007.mobilesafe.utils.KeyUtil.TAG;

public class SplishActivity extends AppCompatActivity {

    Handler handler;

    private final int ERROR_CONNECT = -1;
    private final int GET_NEW_VERSION = 1;
    private final int NO_NEW_VERSION = 2;
    private final int ERROR_PROTOCOL = 3;
    private final int ERROR_URL = 4;
    private final int ERROR_IO = 5;
    private final int ERROR_JSON = 6;

    @ViewInject(value = R.id.tv_version)
    TextView tv_version;

    private String mCode;
    private String mApkUrl;
    private String mDes;
    private Callback.Cancelable mCancelable;
    // 更新下载位置
    File mSaveFilePath = Environment.getExternalStorageDirectory();
    File mSaveFile =  new File(mSaveFilePath, "1.json");

    boolean first = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splish);
        x.view().inject(this);
        initHandler();

        Log.i(TAG, "onCreate: 开始运行");

        tv_version.setText("版本号:" + getVersionName());

        //检查更新
        update();

        //拷贝数据库
        copyData();
    }

    /**
     * 用于加载号码归属地数据库
     */
    private void copyData() {
        AssetManager assetManager = getAssets();
        try {
            File db = new File(getFilesDir(), "address.db");
            //写入软件私有空间
            if(!db.exists()){
                InputStream open = assetManager.open("address.db");
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(db));

                IOUtil.copy(open, bos);
                IOUtil.closeQuietly(open);
                IOUtil.closeQuietly(bos);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 用于加载控件
     */
    private void initHandler() {
        // handler,用于线程通讯
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case GET_NEW_VERSION:
                        showUpdateDialog();
                        break;
                    case NO_NEW_VERSION:
                        enterHome();
                        break;
                    /*
                    由于这几个错误要调用的代码相同,case穿透
                    直接抛错误码,后期可以方便定位异常位置
                     */
                    case ERROR_CONNECT:
                    case ERROR_PROTOCOL:
                    case ERROR_URL:
                    case ERROR_IO:
                    case ERROR_JSON:
                        Toast.makeText(SplishActivity.this, "更新错误,错误码:" + msg.obj, Toast.LENGTH_SHORT).show();
                        enterHome();
                        break;
                }
            }
        };
    }

    /**
     * 提示用户更新
     */
    private void showUpdateDialog() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("更新提示");
        builder.setMessage("发现新版本!!\n" +
                "最新版本号:" + mCode + "\n" +
                "版本描述:\n" +
                "  " + mDes);
        builder.setPositiveButton("立即升级", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 设置权限
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    if(ContextCompat.checkSelfPermission(SplishActivity.this, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                        ActivityCompat.requestPermissions(SplishActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    }
                }
                startDownload();
                builder.create().dismiss();
            }
        });
        builder.setNegativeButton("以后再说", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                builder.create().dismiss();
                enterHome();
            }
        });
        builder.setCancelable(false);
        builder.show();
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
                builder.setMessage("我们需要申请内存卡读写权限来保存更新文件");
                builder.setPositiveButton("授权", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(SplishActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
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
                        Toast.makeText(SplishActivity.this, "若没有此权限，我们将不能下载新版本到您的手机中", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            } else {
                //失败--第二次,提示失败
                Toast.makeText(this, "若没有此权限，我们将不能下载新版本到您的手机中", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 用于下载新版本应用
     */
    private void startDownload() {
        // 设置下载对话框
        final ProgressDialog dialog = new ProgressDialog(SplishActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("正在下载...");
        dialog.setMessage("下载进度:\n 0/0");
        dialog.setMax(100);
        dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mCancelable.cancel();
            }
        });
        dialog.setCancelable(true);
        // 请求参数
        if(!mSaveFilePath.isDirectory()){
            mSaveFilePath.mkdirs();
        }
        RequestParams entity = new RequestParams(mApkUrl);
        entity.setSaveFilePath(mSaveFile.getAbsolutePath());
        entity.setAutoRename(true);
        // 控制参数
        Callback.ProgressCallback<File> callBack = new Callback.ProgressCallback<File>() {
            @Override
            public void onWaiting() {
            }

            @Override
            public void onStarted() {
                dialog.show();
            }

            @Override
            public void onLoading(long total, long current, boolean isDownloading) {
                dialog.setMessage("下载进度:\n " + current + "/" + total);
                dialog.setProgress((int) (current * 100 / total));
            }

            @Override
            public void onSuccess(File result) {
                dialog.dismiss();
                Toast.makeText(SplishActivity.this, "下载成功,开始安装", Toast.LENGTH_SHORT).show();
                installAPK();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                Toast.makeText(SplishActivity.this, "下载错误", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                enterHome();
            }

            @Override
            public void onCancelled(CancelledException cex) {
                Toast.makeText(SplishActivity.this, "下载中断", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
                enterHome();
            }

            @Override
            public void onFinished() {
            }
        };
        mCancelable = x.http().get(entity, callBack);
    }

    /**
     * 用安装下载好的APK文件
     */
    private void installAPK() {
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.fromFile(mSaveFile), "application/vnd.android.package-archive");
        //使用ForResult方便用户选择取消后可以直接进入主界面
        startActivityForResult(intent, 0);
    }

    /**
     * 跳转到主界面
     */
    private void enterHome() {
        // 启动主界面
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        // 结束该Activity
        finish();
    }

    /**
     * 获取更新信息,并保证启动界面至少2s
     */
    private void update() {
        new Thread(){
            private void delay(){
                // 检测延迟运行,使启动界面至少存在2s
                long delayTime = 2000 - SystemClock.currentThreadTimeMillis();
                Log.i(TAG, "delay: " + delayTime);
                if(delayTime > 0){
                    SystemClock.sleep(delayTime);
                }
            }

            @Override
            public void run() {
                super.run();
                // 删除以前的临时文件
                if(mSaveFile.isFile()){
                    mSaveFile.delete();
                }
                Message msg = Message.obtain();
                msg.what = NO_NEW_VERSION;
                if(SpUtil.getBoolean(SplishActivity.this, KeyUtil.CHECK_UPDATE, KeyUtil.default_CHECK_UPDATE)) {
                    try {
                        // 连接服务器
                        URL url = new URL(KeyUtil.UPDATA_MSG);
                        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                        // 设置超时
                        conn.setConnectTimeout(3000);   //连接超时
                        conn.setReadTimeout(5000);      //读取超时
                        // 设置请求方式
                        conn.setRequestMethod("GET");
                        // 连接
                        int responseCode = conn.getResponseCode();
                        Log.i(TAG, "run: responseCode : " + responseCode);
                        if (responseCode == 200) {
                        /*
                        获取版本信息
                        code    版本号
                        des     版本描述
                        apkUrl  下载路径
                        使用格式:常用JSON体积小,而XML可加密常用于需求安全性高的场合
                         */
                            // 获取数据
                            String json = StreamUtil.parserStream(conn.getInputStream());
                            conn.disconnect();
                            // 解析JSON,获取数据
                            JSONObject jsonObject = new JSONObject(json);
                            mCode = jsonObject.getString("code");
                            mApkUrl = jsonObject.getString("apkUrl");
                            mDes = jsonObject.getString("des");
                            // 检测延迟运行,使启动界面至少存在2s
                            Log.i(TAG, "run: delay");
                            delay();
                            // 校验版本
                            if (!mCode.equals(getVersionName())) {
                                // 返回新版本,提示更新
                                msg.what = GET_NEW_VERSION;
                            } else {
                                msg.what = NO_NEW_VERSION;
                            }
                        } else {
                            msg.obj = responseCode;
                            msg.what = ERROR_CONNECT;
                        }
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                        msg.what = ERROR_PROTOCOL;
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        msg.what = ERROR_URL;
                    } catch (IOException e) {
                        e.printStackTrace();
                        msg.what = ERROR_IO;
                    } catch (JSONException e) {
                        e.printStackTrace();
                        msg.what = ERROR_JSON;
                    } finally {
                        handler.sendMessage(msg);
                    }
                } else {
                    // 检测延迟运行,使启动界面至少存在2s
                    Log.i(TAG, "run: delay");
                    delay();
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 获取版本名称
     * @return 返回版本名称
     */
    private String getVersionName() {
        PackageManager manager = getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return info.versionName;
    }

}
