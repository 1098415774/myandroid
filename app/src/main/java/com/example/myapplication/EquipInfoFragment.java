package com.example.myapplication;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
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

public class EquipInfoFragment extends Fragment {

    private TextView t_id;

    private TableLayout table_eq;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            JSONArray rows = (JSONArray) msg.getData().getSerializable("data");
            TableRow row = null;
            for (int i = 0; i < rows.size(); i++){
                JSONObject object = (JSONObject) rows.get(i);
                if (i%2 == 0){
                    row = new TableRow(MyApplication.getInstance());
                    LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) row.getLayoutParams();
                    linearParams.height = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics()));
                    linearParams.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    linearParams.weight = 1;
                    linearParams.leftMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));
                    linearParams.rightMargin = ((int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()));

                }
            }

        }
    };

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

            OkHttpUtils.get()
                    .url("http://192.168.0.249:8080/mytest_war_exploded/user/getUserEquipInfo?id=559654")
                    .build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Request request, Exception e) {
                            Log.i("RES",e.getMessage());
                        }

                        @Override
                        public void onResponse(String response) {
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
                        }
                    });
        }
        return view;
    }
}
