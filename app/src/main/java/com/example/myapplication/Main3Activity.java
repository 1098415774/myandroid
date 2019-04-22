package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.myapplication.base.util.StringUtils;

public class Main3Activity extends AppCompatActivity {

    private RelativeLayout rl_tologin;

    private TextView tv_username,tv_equip_num;

    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        tv_username = findViewById(R.id.tv_username);
        tv_equip_num = findViewById(R.id.tv_equip_num);
        myApplication = MyApplication.getInstance();
        init();

    }

    private void init(){
        rl_tologin = findViewById(R.id.rl_tologin);
        rl_tologin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(Main3Activity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserInfo user = myApplication.getUserInfo();
        if (user != null){
            if (StringUtils.isNotEmpty(user.getUsername())){
                tv_username.setText(user.getUsername());
            }
            if (user.getEquipInfoList() != null){
                tv_equip_num.setText(user.getEquipInfoList().size() + "个智能设备");
            }
            tv_equip_num.setVisibility(View.VISIBLE);
        }else {
            tv_username.setText("点击登陆账号");
            tv_equip_num.setVisibility(View.INVISIBLE);
        }
    }
}
