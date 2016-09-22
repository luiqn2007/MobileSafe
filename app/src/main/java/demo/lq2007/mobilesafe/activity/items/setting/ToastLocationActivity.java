package demo.lq2007.mobilesafe.activity.items.setting;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;

import static demo.lq2007.mobilesafe.utils.KeyUtil.TAG;


public class ToastLocationActivity extends Activity {

    @ViewInject(R.id.layout_toast)
    RelativeLayout rl_toast;
    @ViewInject(R.id.tv_bottommsg)
    TextView tv_bottom_msg;
    @ViewInject(R.id.tv_topmsg)
    TextView tv_top_msg;

    private Point mScreenSize;
    long[] mHits = new long[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_toast_location);
        x.view().inject(this);

        //设置初始位置
        int x = SpUtil.getInt(this, KeyUtil.TOAST_X, KeyUtil.default_TOAST_X);
        int y = SpUtil.getInt(this, KeyUtil.TOAST_Y, KeyUtil.default_TOAST_Y);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) rl_toast.getLayoutParams();
        layoutParams.leftMargin = x;
        layoutParams.topMargin = y;
        rl_toast.setLayoutParams(layoutParams);

        //屏幕大小
        mScreenSize = new Point();
        getWindowManager().getDefaultDisplay().getSize(mScreenSize);

        //提示框
        if(y > mScreenSize.y / 2){
            tv_bottom_msg.setVisibility(View.INVISIBLE);
            tv_top_msg.setVisibility(View.VISIBLE);
        } else {
            tv_top_msg.setVisibility(View.INVISIBLE);
            tv_bottom_msg.setVisibility(View.VISIBLE);
        }

        //设置触摸事件
        rl_toast.setOnTouchListener(new View.OnTouchListener() {

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
                        //重新绘制控件
                        /*
                        left,top,left+width,top+height
                         */
                        //Toast
                        int l = dX + view.getLeft();
                        int t = dY + view.getTop();
                        //防止控件移出屏幕
                        if(l > mScreenSize.x - view.getWidth() || l < 0 || t > mScreenSize.y - view.getHeight() || t < 0){
                           break;
                        }
                        view.layout(l, t, l + view.getWidth(), t + view.getHeight());
                        //提示框
                        if(t > mScreenSize.y / 2){
                            tv_bottom_msg.setVisibility(View.INVISIBLE);
                            tv_top_msg.setVisibility(View.VISIBLE);
                        } else {
                            tv_top_msg.setVisibility(View.INVISIBLE);
                            tv_bottom_msg.setVisibility(View.VISIBLE);
                        }
                        //刷新Start位置
                        startX = (int) motionEvent.getRawX();
                        startY = (int) motionEvent.getRawY();
                        break;
                    case MotionEvent.ACTION_UP:
                        //提交位置
                        SpUtil.putInt(ToastLocationActivity.this, KeyUtil.TOAST_POSITION, KeyUtil.default_TOAST_SET_POSITION);
                        SpUtil.putInt(ToastLocationActivity.this, KeyUtil.TOAST_X, rl_toast.getLeft());
                        SpUtil.putInt(ToastLocationActivity.this, KeyUtil.TOAST_Y, rl_toast.getTop());
                        break;
                }

                //true--执行完了
                //false--未执行完，拦截了
                return false;
            }
        });
        rl_toast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.arraycopy(mHits, 1, mHits, 0, mHits.length - 1);
                //SystemClock.uptimeMillis() 开机时间(ms)，休眠时间不算
                mHits[mHits.length - 1] = SystemClock.uptimeMillis();
                //判断在0.5s内是否多击  多击次数由mHits数组长度决定
                if(mHits[0] >= (SystemClock.uptimeMillis() - 500)){
                    int l = (mScreenSize.x - view.getWidth()) / 2;
                    int t = (mScreenSize.y - view.getHeight()) / 2;
                    view.layout(l, t, l + view.getWidth(), t + view.getHeight());

                    SpUtil.putInt(ToastLocationActivity.this, KeyUtil.TOAST_POSITION, KeyUtil.default_TOAST_SET_POSITION);
                    SpUtil.putInt(ToastLocationActivity.this, KeyUtil.TOAST_X, rl_toast.getLeft());
                    SpUtil.putInt(ToastLocationActivity.this, KeyUtil.TOAST_Y, rl_toast.getTop());
                }
            }
        });
    }
}
