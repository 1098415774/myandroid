package com.example.myapplication;

import android.app.Application;
import android.content.Context;
import android.util.Xml;

import com.example.myapplication.base.util.StringUtils;
import com.example.myapplication.solution.SmartAction;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

public class MyApplication extends Application {
    private static MyApplication mInstance;

    private String token;

    private boolean islogin = false;

    private UserInfo userInfo;

    private String filepath = "";

    private HashMap<String,String> tabmap = new HashMap<>();

    private Hashtable<String, EquipInfo> equipInfoHashMap = new Hashtable<>();

    private HashMap<String, SmartAction> smartActionHashMap = new HashMap<>();

    /**
     * 获取context
     * @return
     */
    public synchronized static MyApplication getInstance() {
        if (mInstance == null) {
            mInstance = new MyApplication();
        }
        return mInstance;
    }

    public EquipInfo getEquipInfo(String id){
        return equipInfoHashMap.get(id);
    }

    public void addEquipInfo(String id, EquipInfo equipInfo){
        equipInfoHashMap.put(id,equipInfo);
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isIslogin() {
        return islogin;
    }

    public void setIslogin(boolean islogin) {
        this.islogin = islogin;
    }

    public UserInfo getUserInfo() {
        if (userInfo == null){
            userInfo = getLocalUserInfo();
        }
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getFilepath() {
        return filepath;
    }

    public void setFilepath(String filepath) {
        this.filepath = filepath;
    }

    public HashMap<String, SmartAction> getSmartActionHashMap() {
        return smartActionHashMap;
    }

    public void setSmartActionHashMap(HashMap<String, SmartAction> smartActionHashMap) {
        this.smartActionHashMap = smartActionHashMap;
    }

    public String getIdsInTabmap(String tabid) {
        String ids = "";
        if (tabid.equals("100")){
            for (String key : tabmap.keySet()){
                ids += tabmap.get(key);
            }
        }else {
            ids = tabmap.get(tabid);
        }
        return ids;
    }

    public void putIdsToTabmap(String tabid, String ids) {
        this.tabmap.put(tabid,ids);
    }

    public Set<String> getTabmapKeys(){
        return tabmap.keySet();
    }

    public boolean flushUserInfoXML(){
        tabmap.clear();
        equipInfoHashMap.clear();
        String ids = null;
        for (EquipInfo equipInfo : userInfo.getEquipInfoList()){
            ids = (ids = tabmap.get(equipInfo.getArea())) == null ? "" : ids;
            tabmap.put(equipInfo.getArea(), ids + equipInfo.getId() + ";");
            equipInfoHashMap.put(String.valueOf(equipInfo.getId()),equipInfo);
        }
        File file = new File(filepath + "/user.xml");
        return createEquipInfoXml(file);
    }

    private UserInfo getLocalUserInfo(){
        ArrayList<EquipInfo> equipInfos = null;
        FileInputStream inputStream = null;
        UserInfo user = null;
        try {
            File file = new File(filepath + "/user.xml");
            if (!file.exists()){
                return null;
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
                        if (name.equals("user")){
                            user = new UserInfo();
                        }
                        if (user != null){
                            if (name.equalsIgnoreCase("username")){
                                user.setUsername(parser.nextText());
                            }else if (name.equalsIgnoreCase("password")){
                                user.setPassword(parser.nextText());
                            }
                        }
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
                            else if (name.equalsIgnoreCase("typename")){
                                equipInfo.setTypename(parser.nextText());
                            }
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        String endname = parser.getName();
                        if (endname.equalsIgnoreCase("equip") && equipInfo != null){
                            this.addEquipInfo(String.valueOf(equipInfo.getId()),equipInfo);
                            equipInfos.add(equipInfo);
                            equipInfo = null;
                        }
                        if (endname.equalsIgnoreCase("user") && user != null){
                            user.setEquipInfoList(equipInfos);
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
        return user;
    }

    private boolean createEquipInfoXml(File file){
        if (file.exists()){
            file.delete();
        }
        FileOutputStream outputStream = null;
        try {
            file.createNewFile();
            outputStream = new FileOutputStream(file);
            XmlSerializer serializer = Xml.newSerializer();
            serializer.setOutput(outputStream, "UTF-8");
            serializer.startDocument("UTF-8", true);
            if (userInfo != null){
                serializer.startTag(null,"user");
                putdata(serializer);
                serializer.endTag(null,"user");
            }
            serializer.endDocument();
            serializer.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void putdata(XmlSerializer serializer) throws IOException {
        serializer.startTag(null,"username");
        if (StringUtils.isNotEmpty(userInfo.getUsername())){
            serializer.text(userInfo.getUsername());
        }
        serializer.endTag(null,"username");
        serializer.startTag(null,"password");
        if (StringUtils.isNotEmpty(userInfo.getPassword())){
            serializer.text(userInfo.getPassword());
        }
        serializer.endTag(null,"password");
        serializer.startTag(null,"equips");
        if (userInfo.getEquipInfoList() != null && userInfo.getEquipInfoList().size() > 0){
            for (EquipInfo equipInfo : userInfo.getEquipInfoList()){
                serializer.startTag(null,"equip");
                serializer.startTag(null,"area");
                serializer.text(equipInfo.getArea());
                serializer.endTag(null,"area");
                serializer.startTag(null, "id");
                serializer.text(String.valueOf(equipInfo.getId()));
                serializer.endTag(null, "id");
                serializer.startTag(null, "userId");
                serializer.text(equipInfo.getUserId());
                serializer.endTag(null, "userId");
                serializer.startTag(null, "type");
                serializer.text(String.valueOf(equipInfo.getType()));
                serializer.endTag(null, "type");
                serializer.startTag(null, "typename");
                serializer.text(String.valueOf(equipInfo.getTypename()));
                serializer.endTag(null, "typename");
                serializer.endTag(null,"equip");
            }
        }
        serializer.endTag(null,"equips");
    }

}
