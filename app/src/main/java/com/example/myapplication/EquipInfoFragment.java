package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import java.util.Map;

public class EquipInfoFragment extends Fragment {

    private TextView t_id;

    private TableLayout table_eq;

    private Map<Integer,EquipInfo> equipInfoMap = new HashMap<>();

    private boolean isNotFoundshow = true;

    private boolean ishow = false;

    private String tabid = null;

    private MyApplication myApplication = null;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            JSONArray rows = (JSONArray) msg.getData().getSerializable("data");
            for (Object object : rows){
                JSONObject jsonObject = (JSONObject) object;

                EquipInfo equipInfo = equipInfoMap.get(jsonObject.getInteger("id"));
                if (equipInfo.getTextView() != null){
                    equipInfo.getTextView().setText(jsonObject.getString("data") + "°");
                    equipInfo.getTv_online().setVisibility(View.INVISIBLE);
                }
            }

        }
    };

    @SuppressLint("ResourceType")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.equip_list,container,false);
        t_id = view.findViewById(R.id.t_id);
        table_eq = view.findViewById(R.id.table_eq);
        tabid = getArguments().getString("tabid");
        return view;
    }

    private void createTableRow(String[] ids) {
        table_eq.removeAllViewsInLayout();
        equipInfoMap.clear();

        myApplication = MyApplication.getInstance();
        TableRow row = null;
        for (int i = 0; i < ids.length; i++){
            EquipInfo object = myApplication.getEquipInfo(ids[i]);
            if (i%2 == 0){
                row = new TableRow(this.getActivity());
                TableLayout.LayoutParams tablerowParams = new TableLayout.LayoutParams();
                tablerowParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                tablerowParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                tablerowParams.leftMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
                tablerowParams.rightMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
                row.setLayoutParams(tablerowParams);
            }
            View view = createEquipView(object);
            row.addView(view);

            if (i%2 == 1){
                if (row != null){
                    table_eq.addView(row);
                    row = null;
                }
            }
        }
        if (ids.length%2 == 1) {
            TextView nulltextView = new TextView(getActivity());
            TableRow.LayoutParams tx_null_linearParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics())), 1);
            tx_null_linearParams.height = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
            tx_null_linearParams.leftMargin = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
            tx_null_linearParams.rightMargin = ((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
            nulltextView.setLayoutParams(tx_null_linearParams);
            nulltextView.setText("csaca");
            nulltextView.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()));
            nulltextView.setGravity(Gravity.CENTER);
            nulltextView.setVisibility(View.INVISIBLE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                nulltextView.setBackground(getResources().getDrawable(R.drawable.shape_label_orange));
            }
            if (row != null) {
                row.addView(nulltextView);
                table_eq.addView(row);
            }
        }
    }

    private View createEquipView(EquipInfo equipInfo){
        LinearLayout linearLayout = new LinearLayout(this.getActivity());
        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 150, getResources().getDisplayMetrics())));
        layoutParams.leftMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        layoutParams.rightMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
        layoutParams.weight = 1.0f;
        linearLayout.setLayoutParams(layoutParams);
        linearLayout.setId(equipInfo.getId());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            linearLayout.setBackground(getResources().getDrawable(R.drawable.shape_label_orange));
        }
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        RelativeLayout relativeLayout1 = new RelativeLayout(this.getActivity());
        LinearLayout.LayoutParams relativeparam1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 26, getResources().getDisplayMetrics())));
        relativeLayout1.setLayoutParams(relativeparam1);

        TextView tv_eq_type = new TextView(this.getActivity());
        RelativeLayout.LayoutParams tv_eq_type_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.MATCH_PARENT);
//        tv_eq_type_param.alignWithParent = true;
        tv_eq_type_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        tv_eq_type.setLayoutParams(tv_eq_type_param);
        tv_eq_type.setGravity(Gravity.CENTER_VERTICAL);
        tv_eq_type.setText( StringUtils.isNotEmpty(equipInfo.getTypename()) ? equipInfo.getTypename() : "test");
        tv_eq_type.setTextSize(((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 14, getResources().getDisplayMetrics())));
        relativeLayout1.addView(tv_eq_type);

        TextView tv_eq_area = new TextView(this.getActivity());
        RelativeLayout.LayoutParams tv_eq_area_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 22, getResources().getDisplayMetrics())));
//        tv_eq_area_param.alignWithParent = true;
        tv_eq_area_param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        tv_eq_area_param.topMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, getResources().getDisplayMetrics()));
        tv_eq_area.setLayoutParams(tv_eq_area_param);
        tv_eq_area.setGravity(Gravity.CENTER);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            tv_eq_area.setBackground(getResources().getDrawable(R.drawable.shape_label_orange_m));
        }
        tv_eq_area.setText(equipInfo.getArea().equals("100")? "客厅" : "test");
        tv_eq_area.setTextSize(((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 9, getResources().getDisplayMetrics())));
        relativeLayout1.addView(tv_eq_area);
        linearLayout.addView(relativeLayout1);

        TextView tv_eq_online = new TextView(this.getActivity());
        LinearLayout.LayoutParams tv_eq_online_param = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_eq_online.setLayoutParams(tv_eq_online_param);
        tv_eq_online.setGravity(Gravity.CENTER_VERTICAL);
        tv_eq_online.setText("设备离线");
        tv_eq_online.setTextSize(((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics())));
        linearLayout.addView(tv_eq_online);
        equipInfo.setTv_online(tv_eq_online);

        RelativeLayout relativeLayout2 = new RelativeLayout(this.getActivity());
        LinearLayout.LayoutParams relativeparam2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout2.setLayoutParams(relativeparam2);

        ImageView iv_type = new ImageView(this.getActivity());
        RelativeLayout.LayoutParams iv_type_param = new RelativeLayout.LayoutParams(((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 61, getResources().getDisplayMetrics())),((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 91, getResources().getDisplayMetrics())));
//        iv_type_param.alignWithParent = true;
        iv_type_param.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        if (equipInfo.getType() == 1){
            iv_type.setImageDrawable(getResources().getDrawable(R.mipmap.tem));
        }else if (equipInfo.getType() == 2){
            iv_type.setImageDrawable(getResources().getDrawable(R.mipmap.ctrl));
        }
        iv_type.setLayoutParams(iv_type_param);
        relativeLayout2.addView(iv_type);

        TextView tv_data = new TextView(this.getActivity());
        RelativeLayout.LayoutParams tv_data_param = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_eq_area_param.alignWithParent = true;
        tv_data_param.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
//        tv_data_param.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        tv_data_param.rightMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()));
        tv_data.setLayoutParams(tv_data_param);
        tv_data.setTextSize(((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 56, getResources().getDisplayMetrics())));
        tv_data.setText("--°");
        equipInfo.setTextView(tv_data);
        equipInfoMap.put(equipInfo.getId(),equipInfo);
        if (equipInfo.getType() != 1){
            linearLayout.setClickable(true);
            linearLayout.setOnClickListener(new MyEquipViewOnClickListener());
        }else {
            linearLayout.setClickable(false);
            relativeLayout2.addView(tv_data);
        }
        linearLayout.addView(relativeLayout2);
        return linearLayout;
    }

    class MyEquipViewOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            EquipInfo equipInfo = myApplication.getEquipInfo(String.valueOf(v.getId()));
            if (equipInfo == null){
                return;
            }
            switch (equipInfo.getType()){
                case 2:
                    Log.i("ETP","this is a controller");
                    break;
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        myApplication = MyApplication.getInstance();
        String equipids = myApplication.getIdsInTabmap(tabid);
        isNotFoundshow = true;
        if (StringUtils.isNotEmpty(equipids)){
            isNotFoundshow = false;
            ishow = true;
        }
        if (!isNotFoundshow && ishow){
            t_id.setVisibility(View.INVISIBLE);
            table_eq.setVisibility(View.VISIBLE);

            String[] ids = null;
            if (StringUtils.isNotEmpty(equipids)){
                equipids = equipids.substring(0,equipids.lastIndexOf(';'));
                ids = equipids.split(";");
            }
            createTableRow(ids);
        }else {
            t_id.setVisibility(View.VISIBLE);
            table_eq.setVisibility(View.INVISIBLE);
        }
        if (myApplication.getUserInfo() != null && myApplication.getUserInfo().getEquipInfoList() != null){
            for (EquipInfo equipInfo : myApplication.getUserInfo().getEquipInfoList()){
                myApplication.getEquipInfo(String.valueOf(equipInfo.getId())).getTv_online().setVisibility(View.VISIBLE);
            }
        }
        if (ishow && StringUtils.isNotEmpty(myApplication.getToken())){
            OkHttpUtils.get()
                    .url("http://10.50.35.173:8081/mymqtt/user/getUserEquipInfo?token=" + myApplication.getToken())
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
                                }
                                JSONArray rows = (JSONArray) jsonObject.get("rows");
                                Message message = Message.obtain(handler);
                                Bundle data = new Bundle();
                                data.putSerializable("data",rows);
                                message.setData(data);
                                message.sendToTarget();
                            }catch (Exception e){
                                Log.e("RES", e.getMessage());
                            }
                        }
                    });
        }
    }
}
