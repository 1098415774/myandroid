package com.example.myapplication;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TabHost;

public class MyMainActivity extends TabActivity {

    private Intent intent1,intent2;
    private TabHost tabHost;
    private RadioButton guide_home,guide_store;
    private String tab="tab0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_activity_main);

        intent1 = new Intent(this,MainActivity.class);
        intent2 = new Intent(this,Main3Activity.class);
        init();
        inittAB();

    }

    private void init(){
        guide_home = findViewById(R.id.guide_home);
        guide_store = findViewById(R.id.guide_store);

        guide_home.setOnClickListener(new MyOnPageChangeListener());
        guide_store.setOnClickListener(new MyOnPageChangeListener());
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
        if(tab.equalsIgnoreCase("tab0")){
            tabHost.setCurrentTabByTag("tab0");
        }
    }

    /**
     * 点击事件类
     */
    private class MyOnPageChangeListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.guide_home:
                    //切换第一个窗口
                    tabHost.setCurrentTabByTag("tab0");
                    break;
                case R.id.guide_store:
                    //切换第三个窗口
                    tabHost.setCurrentTabByTag("tab1");
                    break;
            }
        }
    }

}
