package com.example.myapplication;

import android.app.TabActivity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TabHost;

import com.alibaba.fastjson.JSONObject;
import com.example.myapplication.solution.activity.SolutionActivity;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.HashMap;

public class MyMainActivity extends TabActivity {

    private Intent intent1,intent2,intent3;
    private TabHost tabHost;
    private RadioButton guide_home,guide_my,guide_smart,checked_btn;
    private HashMap<Integer,Integer> checkedmap = new HashMap<>();
    private HashMap<Integer,Integer> uncheckedmap = new HashMap<>();
    private String tab="tab0";
    private MyApplication myApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_main);

        intent1 = new Intent(this,MainActivity.class);
        intent2 = new Intent(this, SolutionActivity.class);
        intent3 = new Intent(this,Main3Activity.class);
        myApplication = MyApplication.getInstance();
        Log.i("PAT",getApplicationContext().getFilesDir().getAbsolutePath());
        myApplication.setFilepath(getApplicationContext().getFilesDir().getAbsolutePath());
        init();
        inittAB();
        initLogin();
    }

    private void init(){
        guide_home = findViewById(R.id.guide_home);
        guide_my = findViewById(R.id.guide_my);
        guide_smart = findViewById(R.id.guide_smart);
        guide_home.setOnClickListener(new MyOnPageChangeListener());
        guide_my.setOnClickListener(new MyOnPageChangeListener());
        guide_smart.setOnClickListener(new MyOnPageChangeListener());
        initDrawable();
        uncheckedmap.put(R.id.guide_home,R.drawable.tab0);
        uncheckedmap.put(R.id.guide_my,R.drawable.tab1);
        uncheckedmap.put(R.id.guide_smart,R.drawable.smart);
        checkedmap.put(R.id.guide_home,R.drawable.tab0_checked);
        checkedmap.put(R.id.guide_my,R.drawable.tab1_checked);
        checkedmap.put(R.id.guide_smart,R.drawable.smart_checked);
    }

    private void initDrawable() {

        setDrawable(guide_home,R.drawable.tab0_checked);
        checked_btn = guide_home;
        setDrawable(guide_my,R.drawable.tab1);
        setDrawable(guide_smart,R.drawable.smart);

    }

    //填充TabHost
    private void inittAB() {
        tabHost = getTabHost();
        //这里tab0是第一个，tab1是第二个窗口，以此类推
        tabHost.addTab(tabHost.newTabSpec("tab0")
                .setIndicator("tab0")
                .setContent(intent1));
        tabHost.addTab(tabHost.newTabSpec("tab1")
                .setIndicator("tab1")
                .setContent(intent2));
        tabHost.addTab(tabHost.newTabSpec("tab2")
                .setIndicator("tab2")
                .setContent(intent3));
        if(tab.equalsIgnoreCase("tab0")){
            tabHost.setCurrentTabByTag("tab0");
        }
    }

    private void initLogin(){
        UserInfo userInfo = myApplication.getUserInfo();
        if (userInfo == null){
            return;
        }
        final String username = userInfo.getUsername().trim();
        final String password = userInfo.getPassword().trim();
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
                            }
                        }catch (Exception e){
                            Log.e("RES", e.getMessage());
                        }
                    }
                });
    }

    /**
     * 点击事件类
     */
    private class MyOnPageChangeListener implements View.OnClickListener {


        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.guide_home:
                    if (checked_btn != null){
                        setDrawable(checked_btn,uncheckedmap.get(checked_btn.getId()));
                    }
                    setDrawable(guide_home,R.drawable.tab0_checked);
                    checked_btn = guide_home;
                    tabHost.setCurrentTabByTag("tab0");
                    break;
                case R.id.guide_my:
                    if (checked_btn != null){
                        setDrawable(checked_btn,uncheckedmap.get(checked_btn.getId()));
                    }
                    setDrawable(guide_my,R.drawable.tab1_checked);
                    checked_btn = guide_my;
                    tabHost.setCurrentTabByTag("tab2");
                    break;
                case R.id.guide_smart:
                    if (checked_btn != null){
                        setDrawable(checked_btn,uncheckedmap.get(checked_btn.getId()));
                    }
                    setDrawable(guide_smart,R.drawable.smart_checked);
                    checked_btn = guide_smart;
                    tabHost.setCurrentTabByTag("tab1");
                    break;
            }
        }
    }

    private void setDrawable(RadioButton radioButton, int id){
        Drawable drawable = getResources().getDrawable(id);
        Rect r = new Rect(0, 0, 50, 50);
        drawable.setBounds(r);
        radioButton.setCompoundDrawables(null,drawable,null,null);
    }

}
