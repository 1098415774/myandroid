package com.example.myapplication;

import android.widget.TextView;

public class EquipInfo {

    private int id;

    private String userId;

    private int status;

    private TextView textView;

    private TextView tv_online;

    private String area;

    private int type;

    private String typename;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public TextView getTextView() {
        return textView;
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public TextView getTv_online() {
        return tv_online;
    }

    public void setTv_online(TextView tv_online) {
        this.tv_online = tv_online;
    }

    public String getTypename() {
        return typename;
    }

    public void setTypename(String typename) {
        this.typename = typename;
    }
}
