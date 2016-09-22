package demo.lq2007.mobilesafe.ui;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by lq200 on 2016/9/10.
 */
public class MarqueeText extends TextView {
    //单参:在代码中调用
    //双参:在布局文件中调用
    //布局文件中的控件最终会通过反射转换为代码,attrs保存了空间中的所有属性
    //在布局文件中调用,保存了样式文件
    //设置自动获取焦点
    public MarqueeText(Context context) {
        super(context);
        //设置跑马灯效果
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }

    public MarqueeText(Context context, AttributeSet attrs) {
        super(context, attrs);
        //设置跑马灯效果
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }

    public MarqueeText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //设置跑马灯效果
        setSingleLine(true);
        setEllipsize(TextUtils.TruncateAt.MARQUEE);
        setMarqueeRepeatLimit(-1);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
