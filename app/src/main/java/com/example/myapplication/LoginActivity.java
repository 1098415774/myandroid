package com.example.myapplication;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.base.util.StringUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private EditText et_account,et_password;
    private Button btn_login;
    private TextView tv_link_account,tv_link_password;

    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        myApplication = MyApplication.getInstance();
        init();
    }

    private void init(){
        et_account = findViewById(R.id.et_account);
        et_password = findViewById(R.id.et_password);
        tv_link_account = findViewById(R.id.tv_link_account);
        tv_link_password = findViewById(R.id.tv_link_password);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_link_password.setBackgroundColor(Color.rgb(170,170,170));
                tv_link_account.setBackgroundColor(Color.rgb(170,170,170));
                if (StringUtils.isEmpty(et_account.getText().toString().trim())){
                    tv_link_account.setBackgroundColor(Color.RED);
                    return;
                }
                if (StringUtils.isEmpty(et_password.getText().toString().trim())){
                    tv_link_password.setBackgroundColor(Color.RED);
                    return;
                }
                final String username = et_account.getText().toString().trim();
                final String password = et_password.getText().toString().trim();
                String url = "http://10.50.35.173:8081/mymqtt/user/login?username=" + username
                        + "&password=" + password;
                OkHttpUtils.post()
                        .url(url)
                        .addParams("cc","")
                        .build()
                        .execute(new StringCallback() {
                            @Override
                            public void onError(Request request, Exception e) {
                                Log.i("RES",e.getMessage());
                            }

                            @Override
                            public void onResponse(String response) {
                                try {
                                    JSONObject jsonObject = JSONObject.parseObject(response);
                                    String state = (String) jsonObject.get("state");
                                    if (Integer.parseInt(state) == 0){
                                        Log.e("RES", (String) jsonObject.get("msg"));
                                    }else {
                                        String token = (String) jsonObject.get("msg");
                                        myApplication.setToken(token);
                                        setUserInfoInLocal(username,password,token);
                                    }
                                }catch (Exception e){
                                    Log.e("RES", e.getMessage());
                                }
                            }
                        });
            }
        });
    }

    private void setUserInfoInLocal(String username, String password, String token){
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)){
            return;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setUsername(username);
        userInfo.setPassword(password);
        myApplication.setUserInfo(userInfo);
        if (StringUtils.isNotEmpty(token)){
            String url = "http://10.50.35.173:8081/mymqtt/user/getUserEquipInfo?token=" + token;
            OkHttpUtils.get()
                    .url(url)
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.i("RES",e.getMessage());
                            myApplication.flushUserInfoXML();
                            LoginActivity.this.finish();
                        }

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = JSONObject.parseObject(response);
                                String state = (String) jsonObject.get("state");
                                if (Integer.parseInt(state) == 0){
                                    Log.e("RES", (String) jsonObject.get("msg"));
                                }else {
                                    JSONArray array = (JSONArray) jsonObject.get("rows");
                                    List<EquipInfo> equipInfoList = new ArrayList<>();
                                    for (int i = 0; i < array.size(); i++){
                                        JSONObject object = (JSONObject) array.get(i);
                                        EquipInfo equipInfo = new EquipInfo();
                                        equipInfo.setId(object.getInteger("id"));
                                        equipInfo.setArea(object.getString("area"));
                                        equipInfo.setType(object.getInteger("type"));
                                        equipInfo.setStatus(object.getInteger("status"));
                                        equipInfo.setUserId(object.getString("userId"));
                                        equipInfo.setTypename(object.getString("typename"));
                                        equipInfoList.add(equipInfo);
                                    }
                                    myApplication.getUserInfo().setEquipInfoList(equipInfoList);
                                    myApplication.flushUserInfoXML();
                                    LoginActivity.this.finish();
                                }
                            }catch (Exception e){
                                Log.e("RES", e.getMessage());
                            }
                        }
                    });

        }

    }

}
