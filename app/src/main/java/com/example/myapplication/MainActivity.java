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
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mytab;

    private ViewPager my_viewpager;

    private ArrayList<String> titles = new ArrayList();

    private HashMap<String,String> map = new HashMap<>();

    private List<Fragment> mFragment = new ArrayList<>();

    private MyEquipCache equipCache = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mytab = findViewById(R.id.mytab);
        my_viewpager = findViewById(R.id.my_viewpager);
        List<EquipInfo> equips = getLocalEquipInfo();
        HashMap<String,String> tabs = getTab();
        for (String tabcode : tabs.keySet()){
            map.put(tabcode,"");
            TabLayout.Tab tab = mytab.newTab().setText(tabs.get(tabcode));
            tab = setView(tab);
//            tab.getCustomView().setSoundEffectsEnabled(false);
            mytab.addTab(tab);
            titles.add(tabs.get(tabcode));
        }
        for (EquipInfo equipInfo : equips){
            String value = map.get(equipInfo.getArea());
            value += equipInfo.getId() + ";";
            map.put(equipInfo.getArea(),value);
        }
        EquipInfoFragment fragment1 = new EquipInfoFragment();
        EquipInfoFragment fragment2 = new EquipInfoFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("equipids",map.get("100"));
        bundle1.putBoolean("show",false);
        fragment1.setArguments(bundle1);
        Bundle bundle2 = new Bundle();
        bundle2.putString("equipids",map.get("101"));
        bundle2.putBoolean("show",true);
        fragment2.setArguments(bundle2);
        mFragment.add(fragment1);
        mFragment.add(fragment2);

        my_viewpager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int i) {
                return mFragment.get(i);
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

    private List<EquipInfo> getLocalEquipInfo(){
        equipCache = MyEquipCache.getInstance();
        ArrayList<EquipInfo> equipInfos = null;
        FileInputStream inputStream = null;
        String filepath = getApplicationContext().getFilesDir().getAbsolutePath();
        try {
            File file = new File(filepath + "/equipInfo.xml");
            if (!file.exists()){
                if (!file.createNewFile()){
                    throw new Exception("file create fail");
                }else {
                    createEquipInfoXml(file);
                }

            }
            XmlPullParser parser = Xml.newPullParser();
            inputStream = new FileInputStream(file);
            parser.setInput(inputStream,"UTF-8");
            EquipInfo equipInfo = null;
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType){
                    case XmlPullParser.START_DOCUMENT:
                        equipInfos = new ArrayList();
                        break;
                    case XmlPullParser.START_TAG:
                        String name = parser.getName();
                        if (name.equals("equip")){
                            equipInfo = new EquipInfo();
                        }else if (equipInfo != null){
                            if (name.equalsIgnoreCase("area")){
                                equipInfo.setArea(parser.nextText());
                            }else if (name.equalsIgnoreCase("id")){
                                equipInfo.setId(Integer.parseInt(parser.nextText()));
                            }else if (name.equalsIgnoreCase("userId")){
                                equipInfo.setUserId(parser.nextText());
                            }else if (name.equalsIgnoreCase("type")){
                                equipInfo.setType(Integer.parseInt(parser.nextText()));
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endname = parser.getName();
                        if (endname.equalsIgnoreCase("equip") && equipInfo != null){
                            equipCache.setEquipInfo(String.valueOf(equipInfo.getId()),equipInfo);
                            equipInfos.add(equipInfo);
                            equipInfo = null;
                        }
                        break;
                }
                eventType = parser.next();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (inputStream != null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return equipInfos;
    }

    private void createEquipInfoXml(File file){
        if (file == null || !file.exists()){
            return;
        }
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(file);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(outputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            serializer.startTag(null,"equips");
            testdata(serializer);
            serializer.endTag(null,"equips");
            serializer.endDocument();
            serializer.flush();
            outputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void testdata(XmlSerializer serializer) throws IOException {
        serializer.startTag(null,"equip");
        serializer.startTag(null,"area");
        serializer.text("100");
        serializer.endTag(null,"area");
        serializer.startTag(null, "id");
        serializer.text("1231");
        serializer.endTag(null, "id");
        serializer.startTag(null, "userId");
        serializer.text("1");
        serializer.endTag(null, "userId");
        serializer.startTag(null, "type");
        serializer.text("1");
        serializer.endTag(null, "type");
        serializer.endTag(null,"equip");

        serializer.startTag(null,"equip");
        serializer.startTag(null,"area");
        serializer.text("100");
        serializer.endTag(null,"area");
        serializer.startTag(null, "id");
        serializer.text("559656");
        serializer.endTag(null, "id");
        serializer.startTag(null, "userId");
        serializer.text("889954");
        serializer.endTag(null, "userId");
        serializer.startTag(null, "type");
        serializer.text("1");
        serializer.endTag(null, "type");
        serializer.endTag(null,"equip");
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
            bufw.write("100=我的设备");
            bufw.newLine();
            bufw.write("101=客厅");
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
