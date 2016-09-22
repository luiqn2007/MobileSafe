package demo.lq2007.mobilesafe.activity.items.findLeader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;

public class NumberActivity extends SetupActivity {

    @ViewInject(R.id.et_num)
    EditText et_safe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_number);
        x.view().inject(this);

        et_safe.setText(SpUtil.getString(this, KeyUtil.SAFE_NUMBER, KeyUtil.default_SAFE_NUMBER));
    }

    public void choose(View v){
        Intent intent = new Intent(this, ContactActivity.class);
        startActivityForResult(intent, 0);
    }

    @Override
    public void saveSetting() {
        SpUtil.putString(this, KeyUtil.SAFE_NUMBER, et_safe.getText().toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data == null){
            return;
        }

        String phone = data.getStringExtra(KeyUtil.CONTACT_PHONE);

        et_safe.setText(phone);
    }
}
