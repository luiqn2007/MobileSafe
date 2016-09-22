package demo.lq2007.mobilesafe.activity.items;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import demo.lq2007.mobilesafe.R;
import demo.lq2007.mobilesafe.activity.items.atools.PhoneActivity;

public class AToolsActivity extends AppCompatActivity {

    @ViewInject(R.id.lv_atools)
    ListView lv_atools;

    String[] mTitles;
    Class[] mClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atools);
        x.view().inject(this);

        mTitles = new String[]{
                "号码归属地查询"
        };

        mClass = new Class[]{
                PhoneActivity.class
        };

        BaseAdapter adapter = new MyAdapter();
        lv_atools.setAdapter(adapter);
    }

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public Object getItem(int position) {
            return mTitles[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = View.inflate(getApplicationContext(), R.layout.list_atools, null);

            Button btn_list = (Button) v.findViewById(R.id.btn_atools);

            btn_list.setText(mTitles[position]);
            btn_list.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AToolsActivity.this, mClass[position]);
                    startActivity(intent);
                }
            });

            return v;
        }
    }
}
