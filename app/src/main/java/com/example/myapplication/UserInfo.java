package com.example.myapplication;

import java.util.List;

public class UserInfo {

    private String username;

    private String password;

    private List<EquipInfo> equipInfoList;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<EquipInfo> getEquipInfoList() {
        return equipInfoList;
    }

    public void setEquipInfoList(List<EquipInfo> equipInfoList) {
        this.equipInfoList = equipInfoList;
    }
}
