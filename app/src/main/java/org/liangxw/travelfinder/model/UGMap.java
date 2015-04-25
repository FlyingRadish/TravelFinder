package org.liangxw.travelfinder.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by houxg on 2015/4/24.
 */
@AVClassName("UGMap")
public class UGMap extends AVObject {

    public final static String CLASS_NAME = "UGMap";
    public final static String GROUP_ID = "groupId";
    public final static String USER_ID = "userId";

    public UGMap(){
    }

    public void setGroupId(String val) {
        put(GROUP_ID, val);
    }

    public void setUserId(String val) {
        put(USER_ID, val);
    }

    public String getGroupId() {
        return getString(GROUP_ID);
    }

    public String getUserId() {
        return getString(USER_ID);
    }
}
