package demo.lq2007.mobilesafe.utils;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.AsyncTask;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.activity.items.setting.ToastLocationActivity;

import static demo.lq2007.mobilesafe.utils.KeyUtil.TAG;


/**
 * Created by lq200 on 2016/9/16.
 */
public class ToastUtil {

    public static final int THEME_NULL = 10;
    public static final int THEME_GRAY = 11;
    public static final int THEME_ORANGE = 12;
    public static final int THEME_WHITE = 13;
    public static final int THEME_GREEN = 14;
    public static final int THEME_BLUE = 15;

    private static View v;
    private static WindowManager manager;
    private static WindowManager.LayoutParams mParams;

    public static void showToast(Context context, String text, long showTime, int theme, int position, int x, int y) {

        final Context ctx = context;
        final String t = text;
        final long time = showTime;
        final int color = theme;
        final int posi = position;
        final int dx = x;
        final int dy = y;

        new AsyncTask<Void, Void, Void>(){

            @Override
            protected void onPreExecute() {
                showToastWithoutDismiss(ctx, t, color, posi, dx, dy);
            }

            @Override
            protected Void doInBackground(Void... params) {
                SystemClock.sleep(time);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                closeToast(ctx);
            }
        }.execute();
    }

    public static void showToastWithoutDismiss(Context context, String text, int theme, int position, int x, int y){
        if (v != null || manager != null) {
            closeToast(context);
        }

        manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        v = View.inflate(context, R.layout.toast_phone_lcation, null);
        RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.layout_toast);
        TextView tv = (TextView) v.findViewById(R.id.tv_result);

        tv.setText(text);
        switch (theme) {
            case THEME_BLUE:
                rl.setBackgroundResource(R.drawable.call_locate_blue);
                break;
            case THEME_GRAY:
                rl.setBackgroundResource(R.drawable.call_locate_gray);
                break;
            case THEME_ORANGE:
                rl.setBackgroundResource(R.drawable.call_locate_orange);
                break;
            case THEME_WHITE:
                rl.setBackgroundResource(R.drawable.call_locate_white);
                break;
            case THEME_GREEN:
                rl.setBackgroundResource(R.drawable.call_locate_green);
                break;
        }

        mParams = new WindowManager.LayoutParams();
        mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mParams.format = PixelFormat.TRANSLUCENT;
        mParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        mParams.setTitle("MyToast");
        mParams.alpha = 0.8f;
        mParams.gravity = position;
        mParams.x = x;
        mParams.y = y;
        mParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE;
        manager.addView(v, mParams);

        v.setOnTouchListener(new View.OnTouchListener() {

            private int endY;
            private int endX;
            private int startY;
            private int startX;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //View--控件
                //motionEvent--事件
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        //初始:
                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();
                    case MotionEvent.ACTION_MOVE:
                        //移动:
                        endX = (int) motionEvent.getRawX();
                        endY = (int) motionEvent.getRawY();
                        Log.i(TAG, "onTouch: " + endX + " : " + endY);
                        //对象
                        int dX = endX - startX;
                        int dY = endY - startY;
                        //Toast
                        mParams.x += dX;
                        mParams.y += dY;
                        if(mParams.x < 0){
                            mParams.x = 0;
                        }
                        if(mParams.y < 0){
                            mParams.y = 0;
                        }
                        if(mParams.x > manager.getDefaultDisplay().getWidth() - mParams.width){
                            mParams.x = manager.getDefaultDisplay().getWidth() - mParams.width;
                        }
                        if(mParams.y > manager.getDefaultDisplay().getHeight() - mParams.height){
                            mParams.y = manager.getDefaultDisplay().getHeight() - mParams.height;
                        }
                        manager.updateViewLayout(v, mParams);
                        //刷新Start位置
                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();
                        break;
                }

                //true--执行完了
                //false--未执行完，拦截了
                return false ;
            }
        });

        rl.startAnimation(AnimationUtils.loadAnimation(context, R.anim.toast_enter));
    }

    public static void closeToast(Context context){
        if (v != null && manager != null) {
            RelativeLayout rl = (RelativeLayout) v.findViewById(R.id.layout_toast);
            rl.startAnimation(AnimationUtils.loadAnimation(context, R.anim.toast_exit));
            manager.removeView(v);
            manager = null;
            v = null;
        }
    }
}
