package demo.lq2007.mobilesafe.activity.items.findLeader;

import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.View;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.ui.SettingChecked;
import demo.lq2007.mobilesafe.utils.KeyUtil;
import demo.lq2007.mobilesafe.utils.SpUtil;

public class SIMActivity extends SetupActivity {

    @ViewInject(value = R.id.sc_get)
    SettingChecked sc_sim;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sim);
        x.view().inject(this);

        sc_sim.setChecked(!TextUtils.isEmpty(SpUtil.getString(this, KeyUtil.SIM_ID, KeyUtil.default_SIM_ID)));

        sc_sim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sc_sim.setChecked(!sc_sim.isChecked());
            }
        });
    }

    private void disRegistSIM() {
        SpUtil.putString(this, KeyUtil.SIM_ID, KeyUtil.default_SIM_ID);
    }

    private void registSIM() {
        TelephonyManager tel = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);

        //获得SIM卡序列号
        String id = tel.getSimSerialNumber();
        SpUtil.putString(this, KeyUtil.SIM_ID, id);
    }

    @Override
    public void saveSetting() {
        if(sc_sim.isChecked()){
            registSIM();
        } else {
            disRegistSIM();
        }
    }
}
