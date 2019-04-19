package com.example.myapplication;

import java.util.HashMap;
import java.util.Hashtable;

public class MyEquipCache {

    private static MyEquipCache myEquipCache;

    private static Hashtable<String, EquipInfo> equipInfoHashMap = new Hashtable<>();

    private MyEquipCache(){}

    public synchronized static MyEquipCache getInstance(){
        if (myEquipCache == null){
            myEquipCache = new MyEquipCache();
        }
        return myEquipCache;
    }

    public EquipInfo getEquipInfo(String id){
        return equipInfoHashMap.get(id);
    }

    public void setEquipInfo(String id, EquipInfo equipInfo){
        equipInfoHashMap.put(id,equipInfo);
    }

}
