package demo.lq2007.mobilesafe.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.utils.KeyUtil;

/**
 * Created by lq200 on 2016/9/10.
 */
public class PasswordText extends RelativeLayout {
    public PasswordText(Context context) {
        super(context);
        init(null);
    }

    public PasswordText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public PasswordText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        View.inflate(getContext(), R.layout.ui_password, this);

        final EditText et_psd = (EditText) findViewById(R.id.et_password);
        ImageButton ib_show = (ImageButton) findViewById(R.id.ib_show);

        ib_show.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    et_psd.setInputType(0);
                } else {
                    et_psd.setInputType(129);
                }
                return false;
            }
        });

        if(attrs != null){
            setHint(attrs.getAttributeValue(KeyUtil.UI, "hint"));
            setPassword(attrs.getAttributeValue(KeyUtil.UI, "password"));
            setCanView(attrs.getAttributeBooleanValue(KeyUtil.UI, "canView", true));
            setShowValue(attrs.getAttributeBooleanValue(KeyUtil.UI, "showValue", false));
        }
    }

    public void setHint(String hint){
        EditText et_psd = (EditText) findViewById(R.id.et_password);
        et_psd.setHint(hint);
    }

    public void setPassword(String pwd){
        EditText et_psd = (EditText) findViewById(R.id.et_password);
        et_psd.setText(pwd);
    }

    public void setCanView(boolean view){
        ImageButton ib_show = (ImageButton) findViewById(R.id.ib_show);
        ib_show.setVisibility(view?VISIBLE:INVISIBLE);
    }

    public void setShowValue(boolean value){
        EditText et_psd = (EditText) findViewById(R.id.et_password);
        et_psd.setInputType(value?0:129);
    }

    public String getPassword(){
        EditText et_psd = (EditText) findViewById(R.id.et_password);
        return et_psd.getText().toString();
    }
}
