package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.squareup.okhttp.Request;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EquipInfoFragment extends Fragment {

    private TextView t_id;

    private TableLayout table_eq;

    private Map<Integer,EquipInfo> equipInfoMap;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
//            JSONArray rows = (JSONArray) msg.getData().getSerializable("data");
//            for (Object object : rows){
//                JSONObject jsonObject = (JSONObject) object;
//                EquipInfo equipInfo = equipInfoMap.get(jsonObject.getInteger("id"));
//                if (equipInfo.getTextView() != null){
//                    equipInfo.getTextView().setText(jsonObject.getString("userId"));
//                }
//            }

        }
    };

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.equip_list,container,false);
        t_id = view.findViewById(R.id.t_id);
        table_eq = view.findViewById(R.id.table_eq);
        boolean ishow = getArguments().getBoolean("show");
        if (!ishow){
            t_id.setVisibility(View.INVISIBLE);
            table_eq.setVisibility(View.VISIBLE);
//            createTableRow();

            TextView textView = new TextView(getActivity());
            TableRow.LayoutParams tx_linearParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics())),1);
            tx_linearParams.leftMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
            tx_linearParams.rightMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
//            textView.setLayoutParams(tx_linearParams);
            textView.setId(156351);
            textView.setText("545132");
            textView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()));
            textView.setGravity(Gravity.CENTER);
            textView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(getResources().getDrawable(R.drawable.shape_label_orange));
            }
            TableRow row = new TableRow(getActivity());
            TableRow.LayoutParams linearParams = new TableRow.LayoutParams();
            linearParams.height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
            linearParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                row.setBackground(getResources().getDrawable(R.drawable.shape_label_orange));
            }
            row.setVisibility(View.VISIBLE);
            if (row != null){
//                table_eq.removeAllViewsInLayout();
                row.addView(textView,tx_linearParams);
                table_eq.addView(row,linearParams);

            }
//            OkHttpUtils.get()
//                    .url("http://192.168.0.249:8080/mytest_war_exploded/user/getUserEquipInfo?token=cxskwnmluvoqtoun")
//                    .build()
//                    .execute(new StringCallback() {
//                        @Override
//                        public void onError(Request request, Exception e) {
//                            Log.i("RES",e.getMessage());
//                        }
//
//                        @Override
//                        public void onResponse(String response) {
//                            JSONObject jsonObject = JSONObject.parseObject(response);
//                            String state = (String) jsonObject.get("state");
//                            if (Integer.parseInt(state) == 0){
//                                Log.e("RES", (String) jsonObject.get("msg"));
//                            }
//                            JSONArray rows = (JSONArray) jsonObject.get("rows");
//                            Message message = Message.obtain(handler);
//                            Bundle data = new Bundle();
//                            data.putSerializable("data",rows);
//                            message.setData(data);
//                            message.sendToTarget();
//                        }
//                    });
        }
        return view;
    }

    private void createTableRow(){
        equipInfoMap = new HashMap<>();
        EquipInfo equipInfo = new EquipInfo();
        equipInfo.setId(559654);
        equipInfoMap.put(559654,equipInfo);
        List equiplist = new ArrayList();
        equiplist.addAll(equipInfoMap.values());
        EquipInfo equip1 = new EquipInfo();
        equip1.setId(1);
        equip1.setUserId("csad");
        TableRow row = null;
        for (int i = 0; i < equiplist.size(); i++){
            EquipInfo object = (EquipInfo) equiplist.get(i);
            if (i%2 == 0){
                row = new TableRow(this.getActivity());
                TableRow.LayoutParams linearParams = new TableRow.LayoutParams();
                linearParams.height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
                linearParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                linearParams.weight = 1;
                linearParams.leftMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                linearParams.rightMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
            }
            TextView textView = new TextView(getActivity());
            LinearLayout.LayoutParams tx_linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics())),1);
            tx_linearParams.leftMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
            tx_linearParams.rightMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
            textView.setLayoutParams(tx_linearParams);
            textView.setId(object.getId());
            textView.setText(object.getUserId());
            textView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()));
            textView.setGravity(Gravity.CENTER);
            textView.setVisibility(View.VISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                textView.setBackground(getResources().getDrawable(R.drawable.shape_label_orange));
            }
            if (row != null){
                row.addView(textView);
                object.setTextView(textView);
            }
            if (i == (equiplist.size() - 1) && equiplist.size()%2 == 1){
                TextView nulltextView = new TextView(getActivity());
                LinearLayout.LayoutParams tx_null_linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics())),1);                    tx_null_linearParams.height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
                tx_null_linearParams.leftMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                tx_null_linearParams.rightMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                nulltextView.setLayoutParams(tx_null_linearParams);
                nulltextView.setId(object.getId());
                nulltextView.setText(object.getUserId());
                nulltextView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()));
                nulltextView.setGravity(Gravity.CENTER);
                nulltextView.setVisibility(View.INVISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    nulltextView.setBackground(getResources().getDrawable(R.drawable.shape_label_orange));
                }
                if (row != null){
                    row.addView(nulltextView);
                }
            }
        }
        if (row != null){
            table_eq.addView(row);
        }
    }
}
