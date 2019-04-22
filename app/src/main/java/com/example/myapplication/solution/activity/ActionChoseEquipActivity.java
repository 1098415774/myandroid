package com.example.myapplication.solution.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myapplication.EquipInfo;
import com.example.myapplication.MyApplication;
import com.example.myapplication.R;

import java.util.List;

public class ActionChoseEquipActivity extends AppCompatActivity {

    private ListView lv_equiplist;

    private MyApplication myApplication;

    private List<EquipInfo> equipInfos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_chose_equip);
        myApplication = MyApplication.getInstance();
        init();
    }

    private void init(){
        lv_equiplist = findViewById(R.id.lv_equiplist);
        equipInfos = myApplication.getUserInfo().getEquipInfoList();
        if (equipInfos == null){
            return;
        }
        lv_equiplist.setAdapter(new MyEquipListViewAdapter());
    }

    class MyEquipListViewAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return equipInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return equipInfos.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){    //如果直接创建，很有可能照成应用崩毁
                convertView = View.inflate(ActionChoseEquipActivity.this, R.layout.smart_adapter, null) ;
            }
            ImageView iv_icon = convertView.findViewById(R.id.iv_icon);
            switch (equipInfos.get(position).getType()){
                case 1:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        iv_icon.setBackground(getResources().getDrawable(R.drawable.tem_icon));
                    }
                    break;
                case 2:
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        iv_icon.setBackground(getResources().getDrawable(R.drawable.ctrl_icon));
                    }
                    break;
            }

            TextView tv_main = convertView.findViewById(R.id.tv_main);
            tv_main.setText(equipInfos.get(position).getTypename());
            TextView tv_tip = convertView.findViewById(R.id.tv_tip);
            String areaid = equipInfos.get(position).getArea();
            if (!areaid.equalsIgnoreCase("100")){
                tv_tip.setText("客厅");
                tv_tip.setVisibility(View.VISIBLE);
            }
            return convertView;
        }
    }

}
