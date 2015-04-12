package org.liangxw.travelfinder.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

/**
 * Created by houxg on 2015/4/11.
 */
public class User extends AVUser{

    public final static int TYPE_VISITOR = 1;
    public final static int TYPE_GUDE = 2;


    public String getNickName() {
        return (String) get("nickName");
    }

    public void setNickName(String nickName) {
        put("nickName", nickName);
    }

    public AVGeoPoint getLocation() {
        return (AVGeoPoint) get("location");
    }

    public void setLocation(AVGeoPoint location) {
        put("location", location);
    }

    public long getLocationUpdateTime() {
        return (long) get("locationUpdateTime");
    }

    public void setLocationUpdateTime(long locationUpdateTime) {
        put("locationUpdateTime", locationUpdateTime);
    }

    public boolean isLocationUpdateState() {
        return (boolean) get("locationUpdateState");
    }

    public void setLocationUpdateState(boolean locationUpdateState) {
        put("locationUpdateState", locationUpdateState);
    }

    public int getType() {
        return (int) get("type");
    }

    public void setType(int type) {
        put("type", type);
    }

    public String getAvatarUrl() {
        return (String) get("avatarUrl");
    }

    public void setAvatarUrl(String avatarUrl) {
        put("avatarUrl", avatarUrl);
    }
}
