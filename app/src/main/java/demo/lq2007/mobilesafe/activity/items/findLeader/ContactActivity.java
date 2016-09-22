package demo.lq2007.mobilesafe.activity.items.findLeader;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.engine.ContactEngine;
import demo.lq2007.mobilesafe.utils.KeyUtil;

public class ContactActivity extends AppCompatActivity {
    @ViewInject(R.id.lv_contact)
    ListView lv_contact;
    @ViewInject(R.id.pb_loading)
    ProgressBar pb_loading;

    List<HashMap<String, String>> mAllContactInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        x.view().inject(this);

        new AsyncTask<Void, Void, Void>() {

            @Override
            protected void onPostExecute(Void aVoid) {
                pb_loading.setVisibility(View.INVISIBLE);
                lv_contact.setAdapter(new BaseAdapter() {
                    @Override
                    public int getCount() {
                        return mAllContactInfo.size();
                    }

                    @Override
                    public Object getItem(int position) {
                        return mAllContactInfo.get(position);
                    }

                    @Override
                    public long getItemId(int position) {
                        return position;
                    }

                    @Override
                    public View getView(int position, View convertView, ViewGroup parent) {
                        View v = View.inflate(ContactActivity.this, R.layout.list_contact, null);

                        TextView tv_name = (TextView) v.findViewById(R.id.tv_name);
                        TextView tv_phone = (TextView) v.findViewById(R.id.tv_phone);

                        tv_name.setText(((HashMap<String, String>)getItem(position)).get(KeyUtil.CONTACT_NAME));
                        tv_phone.setText(((HashMap<String, String>)getItem(position)).get(KeyUtil.CONTACT_PHONE));

                        return v;
                    }
                });
                lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String phone = ((TextView)view.findViewById(R.id.tv_phone)).getText().toString();
                        Intent data = new Intent(ContactActivity.this, NumberActivity.class);
                        data.putExtra(KeyUtil.CONTACT_PHONE, phone);
                        setResult(1, data);
                        finish();
                    }
                });
            }

            @Override
            protected Void doInBackground(Void... params) {
                mAllContactInfo = ContactEngine.getAllContactInfo(getApplicationContext());
                return null;
            }
        }.execute();
    }
}
