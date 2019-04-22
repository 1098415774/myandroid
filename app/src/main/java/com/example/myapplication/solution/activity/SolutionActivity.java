package com.example.myapplication.solution.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.example.myapplication.MyApplication;
import com.example.myapplication.R;
import com.example.myapplication.solution.SmartAction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SolutionActivity extends AppCompatActivity {

    private ListView lv_solution;

    private MyApplication myApplication;

    private Button btn_add_smart;

    private List<String> actionlist;

    private HashMap<String, SmartAction> actions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solution);
        myApplication = MyApplication.getInstance();
        init();
    }

    private void init(){
        lv_solution = findViewById(R.id.lv_solution);
        btn_add_smart = findViewById(R.id.btn_add_smart);
        actions = myApplication.getSmartActionHashMap();
        if (actions.size() == 0){
            actions.put("1111",new SmartAction());
        }
        actionlist = new ArrayList<>();
        actionlist.addAll(actions.keySet());
        lv_solution.setAdapter(new MySolutionListAdapter());
        btn_add_smart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SolutionActivity.this,ActionChoseEquipActivity.class);
                startActivity(intent);
            }
        });
    }

    class MySolutionListAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return actionlist.size();
        }

        @Override
        public Object getItem(int position) {
            return actions.get(actionlist.get(position));
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //加载item的布局，得到View对象
            if(convertView == null){    //如果直接创建，很有可能照成应用崩毁
                convertView = View.inflate(SolutionActivity.this, R.layout.smart_adapter, null) ;
            }
            //根据position设置对应的数据
            //得到当前行的数据对象
            SmartAction smartAction = actions.get(actionlist.get(position)) ;

            return convertView;
        }
    }

}
