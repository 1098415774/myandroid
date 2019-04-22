package com.example.myapplication;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.myapplication.base.util.StringUtils;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout mytab;

    private ViewPager my_viewpager;

    private ArrayList<String> titles = new ArrayList();


    private HashMap<String, Fragment> mFragment = new HashMap<>();

    private ImageButton ibtn_add;

    private MyApplication myApplication = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mytab = findViewById(R.id.mytab);
        ibtn_add = findViewById(R.id.ibtn_add);
        my_viewpager = findViewById(R.id.my_viewpager);
        my_viewpager = findViewById(R.id.my_viewpager);
        myApplication = MyApplication.getInstance();
        HashMap<String,String> titlemap = getTab();

        List<Map.Entry<String,String>> list = new ArrayList<Map.Entry<String,String>>(titlemap.entrySet());
        Collections.sort(list,new Comparator<Map.Entry<String,String>>() {
            //升序排序
            public int compare(Map.Entry<String, String> o1,
                               Map.Entry<String, String> o2) {
                return o1.getKey().compareTo(o2.getKey());
            }

        });

        for (Map.Entry<String,String> mapping:list){
            TabLayout.Tab tab = mytab.newTab().setText(mapping.getValue());
            tab = setView(tab);
            mytab.addTab(tab);
            titles.add(titlemap.get(mapping.getKey()));
            myApplication.putIdsToTabmap(mapping.getKey(),"");
        }
//        UserInfo userInfo = new UserInfo();
//        EquipInfo equip = new EquipInfo();
//        equip.setId(1231);
//        equip.setType(1);
//        equip.setArea("100");
//        ArrayList<EquipInfo> equipInfos = new ArrayList<>();
//        equipInfos.add(equip);
//        userInfo.setEquipInfoList(equipInfos);
//        myApplication.setUserInfo(userInfo);
        if (myApplication.getUserInfo() != null && myApplication.getUserInfo().getEquipInfoList() != null && myApplication.getUserInfo().getEquipInfoList().size() > 0){
            List<EquipInfo> equips = myApplication.getUserInfo().getEquipInfoList();
            for (EquipInfo equipInfo : equips){
                String value = myApplication.getIdsInTabmap(equipInfo.getArea());
                value += equipInfo.getId() + ";";
                myApplication.addEquipInfo(String.valueOf(equipInfo.getId()),equipInfo);
                myApplication.putIdsToTabmap(equipInfo.getArea(),value);
            }
        }

        for (String areakey : myApplication.getTabmapKeys()){
            EquipInfoFragment fragment = new EquipInfoFragment();
            Bundle bundle = new Bundle();
            bundle.putString("tabid",areakey);
            fragment.setArguments(bundle);
            mFragment.put(titlemap.get(areakey),fragment);
        }


        my_viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragment.get(titles.get(i));
            }

            @Override
            public int getCount() {
                return mFragment.size();
            }

            @Nullable
            @Override
            public CharSequence getPageTitle(int position) {
                return titles.get(position);
            }
        });
        my_viewpager.setOffscreenPageLimit(2);
        mytab.setupWithViewPager(my_viewpager);
    }



    private HashMap<String,String> getTab(){
        String filepath = getApplicationContext().getFilesDir().getAbsolutePath();
        BufferedReader reader = null;
        HashMap<String,String> tabmap = null;
        try {
            File file = new File(filepath + "/usertab.txt");
            if (!file.exists()){
                if (!file.createNewFile()){
                    throw new Exception("file create fail");
                }else {
                    createTabFile(file);
                }
            }
            reader = new BufferedReader(new FileReader(file));
            tabmap = new HashMap<>();
            String str = null;
            while ((str = reader.readLine()).length() > 0){
                tabmap.put(str.substring(0,str.lastIndexOf('=')),str.substring(str.lastIndexOf('=') + 1, str.length()));
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return tabmap;
    }

    private void createTabFile(File file){
        if (file == null || !file.exists()){
            return;
        }
        BufferedWriter bufw = null;
        try {
            bufw = new BufferedWriter(new FileWriter(file));
            bufw.write("101=客厅");
            bufw.newLine();
            bufw.write("100=我的设备");
            bufw.newLine();
            bufw.flush();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bufw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private TabLayout.Tab setView(TabLayout.Tab tab){
        Class clazz = TabLayout.Tab.class;
        try {
            Field field = clazz.getDeclaredField("view");
            View tabView = (View) field.get(tab);
            tabView.setSoundEffectsEnabled(false);
            field.set(tab,tabView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tab;
    }
}
