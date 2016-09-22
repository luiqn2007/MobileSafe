package demo.lq2007.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.utils.KeyUtil;

/**
 * Created by lq200 on 2016/9/10.
 */
public class SettingListShow extends RelativeLayout {

    TextView tv_des,tv_title;
    ImageView ib_show;
    OnClickListener l;

    public SettingListShow(Context context) {
        super(context);
        init(null);
    }

    public SettingListShow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SettingListShow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        //创建布局
        View.inflate(getContext(), R.layout.ui_setting_list, this);
        //设置属性
        tv_des = (TextView) findViewById(R.id.tv_des);
        tv_title = (TextView) findViewById(R.id.tv_title);
        ib_show = (ImageView) findViewById(R.id.ib_show);
        //加载自定义属性
        if (attrs != null) {
            setTitle(attrs.getAttributeValue(KeyUtil.UI, "title"));
            setChoose(attrs.getAttributeValue(KeyUtil.UI, "choose"));
            setDesHide(attrs.getAttributeBooleanValue(KeyUtil.UI, "hideDesHead", false));
        }
    }

    /**
     * 设置标题
     * @param title 标题
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }

    /**
     * 设置选择信息
     * @param desc 选择信息
     */
    public void setChoose(String desc){
        tv_des.setText("当前选择 : " + desc);
    }

    /**
     * 获取选择信息
     * @return 选择信息
     */
    public String getChoose() {
        return tv_des.getText().toString();
    }

    /**
     * 设置ImageButton点击事件
     * @param l
     */
    public void setOnButonClickListener(OnClickListener l){
        ib_show.setOnClickListener(l);
    }

    /**
     * 设置隐藏选择信息
     * @param isHide 是否隐藏
     */
    public void setDesHide(boolean isHide){
        tv_des.setVisibility(isHide?INVISIBLE:VISIBLE);
    }

    @Override
    public void setOnClickListener(OnClickListener l) {
        setOnButonClickListener(l);
        super.setOnClickListener(l);
    }
}
