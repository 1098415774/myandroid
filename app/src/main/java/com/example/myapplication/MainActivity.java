package com.example.myapplication;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout mytab;

    private ViewPager my_viewpager;

    private ArrayList<String> list = new ArrayList<>();

    private List<Fragment> mFragment = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mytab = findViewById(R.id.mytab);
        my_viewpager = findViewById(R.id.my_viewpager);

        mytab.addTab(mytab.newTab().setText("选项卡一"));
        mytab.addTab(mytab.newTab().setText("选项"));
        mytab.addTab(mytab.newTab().setText("选项卡三"));
        mytab.addTab(mytab.newTab().setText("选项卡四"));
        list.add("选项卡一");
        list.add("选项");
        list.add("选项卡三");
        list.add("选项卡四");
        EquipInfoFragment fragment1 = new EquipInfoFragment();
        EquipInfoFragment fragment2 = new EquipInfoFragment();
        EquipInfoFragment fragment3 = new EquipInfoFragment();
        EquipInfoFragment fragment4 = new EquipInfoFragment();
        Bundle bundle1 = new Bundle();
        bundle1.putString("flag",list.get(0));
        bundle1.putBoolean("show",false);
        fragment1.setArguments(bundle1);
        Bundle bundle2 = new Bundle();
        bundle2.putString("flag",list.get(1));
        bundle2.putBoolean("show",true);
        fragment2.setArguments(bundle2);
        Bundle bundle3 = new Bundle();
        bundle3.putString("flag",list.get(2));
        bundle3.putBoolean("show",true);
        fragment3.setArguments(bundle3);
        Bundle bundle4 = new Bundle();
        bundle4.putString("flag",list.get(3));
        bundle4.putBoolean("show",true);
        fragment4.setArguments(bundle4);
        mFragment.add(fragment1);
        mFragment.add(fragment2);
        mFragment.add(fragment3);
        mFragment.add(fragment4);

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
                return list.get(position);
            }
        });
        my_viewpager.setOffscreenPageLimit(2);
        mytab.setupWithViewPager(my_viewpager);
    }
}
