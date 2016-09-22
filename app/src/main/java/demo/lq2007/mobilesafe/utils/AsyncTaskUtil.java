package demo.lq2007.mobilesafe.utils;

import android.os.Handler;
import android.os.Message;

/**
 * Created by lq200 on 2016/9/14.
 */
public abstract class AsyncTaskUtil {
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    onSucceed();
                    break;
                case -1:
                    onFailed();
                    break;
            }

            onFinishTask();
        }
    };

    /**
     * 在子线程之前执行:
     */
    public void onBeforeTask(){}

    /**
     * 在子线程中执行
     */
    public boolean onDuringTask(){return true;}

    /**
     * 在子线程自行成功/失败之后执行(1 2)
     * 无论子线程执行成功与否都执行(3)
     */
    public void onSucceed(){}
    public void onFailed(){}
    public abstract void onFinishTask();

    public void execute(){
        onBeforeTask();
        new Thread(){
            @Override
            public void run() {
                boolean result = onDuringTask();
                if(result){
                    handler.sendEmptyMessage(1);
                } else {
                    handler.sendEmptyMessage(-1);
                }
                super.run();
            }
        }.start();
    }
}
