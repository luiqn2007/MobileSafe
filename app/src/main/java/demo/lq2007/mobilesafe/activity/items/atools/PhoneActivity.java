package demo.lq2007.mobilesafe.activity.items.atools;

import android.os.Bundle;
import android.os.Vibrator;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.db.dao.AddressDao;
import demo.lq2007.mobilesafe.utils.KeyUtil;

public class PhoneActivity extends AppCompatActivity {

    @ViewInject(R.id.et_phone)
    EditText et_number;
    @ViewInject(R.id.tv_result)
    TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone);
        x.view().inject(this);

        et_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                check(null);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void check(View view) {
        String number = et_number.getText().toString();
        if(view != null && TextUtils.isEmpty(number)){
            Animation shake = AnimationUtils.loadAnimation(this, R.anim.shake);
            et_number.startAnimation(shake);
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
            vibrator.vibrate(200);
            return;
        }
        String regx = "^1[34578]\\d{9}$";
        if(number.matches(regx)){
            tv_result.setText(KeyUtil.LOCATION_HEAD + AddressDao.queryAddressByNumber(PhoneActivity.this, number));
            return;
        } else {
            switch (number.length()) {
                case 3:
                    tv_result.setText(KeyUtil.LOCATION_HEAD + "特殊电话");
                    break;
                case 4:
                    tv_result.setText(KeyUtil.LOCATION_HEAD + "虚拟电话");
                    break;
                case 5:
                    tv_result.setText(KeyUtil.LOCATION_HEAD + "客服电话");
                    break;
                case 7:
                case 8:
                    tv_result.setText(KeyUtil.LOCATION_HEAD + "本地电话");
                    break;
                default:
                    if(number.length() >= 10 && number.startsWith("0")){
                        tv_result.setText(KeyUtil.LOCATION_HEAD + "长途电话 -- " + AddressDao.queryAddressByCode(PhoneActivity.this, number));
                        break;
                    }
                    tv_result.setText(KeyUtil.LOCATION_HEAD);
            }
        }
    }
}
