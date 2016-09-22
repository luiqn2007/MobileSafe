package demo.lq2007.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.utils.KeyUtil;

/**
 * Created by lq200 on 2016/9/10.
 */
public class SettingChecked extends RelativeLayout {

    TextView tv_state,tv_des,tv_title;
    CheckBox cb;

    public SettingChecked(Context context) {
        super(context);
        init(null);
    }

    public SettingChecked(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public SettingChecked(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs){
        //创建布局
        View.inflate(getContext(), R.layout.ui_setting_checked, this);
        //设置属性
        cb = (CheckBox) findViewById(R.id.cb_check);
        tv_state = (TextView) findViewById(R.id.tv_state);
        tv_des = (TextView) findViewById(R.id.tv_des);
        tv_title = (TextView) findViewById(R.id.tv_title);
        //加载自定义属性
        if(attrs != null){
            setChecked(attrs.getAttributeBooleanValue(KeyUtil.UI, "checked", true));
            setTitle(attrs.getAttributeValue(KeyUtil.UI, "title"));
            setDes(attrs.getAttributeValue(KeyUtil.UI, "des"));
        }
        //添加点击事件响应
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                setChecked(!isChecked());
            }
        });
    }

    /**
     * 设置标题
     * @param title 标题
     */
    public void setTitle(String title){
        tv_title.setText(title);
    }

    /**
     * 设置描述信息
     * @param desc 表述信息
     */
    public void setDes(String desc){
        tv_des.setText(desc + " : ");
    }

    /**
     * 获取选中状态
     * @return 选择框是否被选中
     */
    public boolean isChecked(){
        return cb.isChecked();
    }

    /**
     * 设置选择框是否被选中
     * @param checked 选择框是否被选中
     */
    public void setChecked(boolean checked){
        cb.setChecked(checked);
        if(checked){
            tv_state.setText("开启");
        }else{
            tv_state.setText("关闭");
        }
    }
}
