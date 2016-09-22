package demo.lq2007.mobilesafe.activity.items.findLeader;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.activity.GestureFlingActivity;
import demo.lq2007.mobilesafe.activity.HomeActivity;
import demo.lq2007.mobilesafe.activity.items.FindActivity;

/**
 * Created by lq200 on 2016/9/11.
 */
public abstract class SetupActivity extends GestureFlingActivity {

    /**
     * 常数：下一个/上一个
     */
    public static int TYPE_NEXT = 1;
    public static int TYPE_PREVIOUS = -1;

    /**
     * 设置界面列表
     */
    static Class[] list = new Class[]{
            HomeActivity.class, WelcomeActivity.class, SIMActivity.class, NumberActivity.class, FinalActivity.class, FindActivity.class
    };

    /**
     * 下一步界面按钮实现
     * @param v 按钮View
     */
    public void next(View v) {
        saveSetting();
        jumpToActivity(TYPE_NEXT);
    }

    public abstract void saveSetting();

    /**
     * 上一步界面按钮实现
     * @param v 按钮View
     */
    public void previous(View v) {
        jumpToActivity(TYPE_PREVIOUS);
    }

    /**
     * 跳转到上一步/下一步跳转的Activity
     * @param type 上一步/下一步
     */
    protected void jumpToActivity(int type) {
        int newActivity = getIntent().getIntExtra("thisActivity", 1) + type;
        Intent intent = new Intent(this, list[newActivity]);
        intent.putExtra("thisActivity", newActivity);
        startActivity(intent);
        if(type == TYPE_NEXT) {
            overridePendingTransition(R.anim.next_in_translate, R.anim.next_out_translate);
        }
        if(type == TYPE_PREVIOUS){
            overridePendingTransition(R.anim.previous_in_translate, R.anim.previous_out_translate);
        }
        finish();
    }

    /**
     * 重写覆盖返回键
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            previous(null);
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean act_RightToLeft() {
        next(null);
        return true;
    }

    @Override
    public boolean act_LeftToRight() {
        previous(null);
        return true;
    }
}
