package demo.lq2007.mobilesafe.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.GestureDetector;
import android.view.MotionEvent;

/**
 * 实现左右滑动的Activity
 * Created by lq200 on 2016/9/11.
 */
public class GestureFlingActivity extends AppCompatActivity {

    GestureDetector mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mDetector = new GestureDetector(this, new MyGestureList());
        super.onCreate(savedInstanceState);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //注册手势识别
        mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    /**
     * 实现向左滑动方法
     * @return 是否执行
     */
    public boolean act_RightToLeft(){
        return false;
    }

    /**
     * 实现向右滑动方法
     * @return 是否执行
     */
    public boolean act_LeftToRight(){
        return false;
    }

    /**
     * 实现手势识别
     */
    private class MyGestureList extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float startX = e1.getRawX();
            float startY = e1.getRawY();
            float endX = e2.getRawX();
            float endY = e2.getRawY();

            boolean doAct = false;

            if(Math.abs(startY - endY) > 200){
                return false;
            }
            if(startX - endX > 100){
                doAct = act_RightToLeft();
            } else if(startX - endX < -100){
                doAct = act_LeftToRight();
            }
            return doAct;
        }
    }
}
