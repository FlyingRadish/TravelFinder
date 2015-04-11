package org.liangxw.travelfinder.model;

import com.avos.avoscloud.AVGeoPoint;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

/**
 * Created by houxg on 2015/4/11.
 */
public class UserWrapper {

    public final static int TYPE_VISITOR = 1;
    public final static int TYPE_GUDE = 2;
    AVUser avUser;

    public UserWrapper() {
        avUser = new AVUser();
    }

    public UserWrapper(AVUser avUser) {
        avUser = avUser;
    }

    public String getNickName() {
        return (String) avUser.get("nickName");
    }

    public void setNickName(String nickName) {
        avUser.put("nickName", nickName);
    }

    public String getUsername() {
        return avUser.getUsername();
    }

    public void setUsername(String userName) {
        avUser.setUsername(userName);
    }

    public void setPassword(String password) {
        avUser.setPassword(password);
    }

    public String getMobilePhoneNumber() {
        return avUser.getMobilePhoneNumber();
    }

    public void setMobilePhoneNumber(String mobilePhoneNumber) {
        avUser.setMobilePhoneNumber(mobilePhoneNumber);
    }

    public void signUpInBackground(SignUpCallback callback) {
        avUser.signUpInBackground(callback);
    }

    public static void logInInBackground(String userName, String password, LogInCallback callback) {
        AVUser.logInInBackground(userName, password, callback);
    }

    public static void is()
    {
        AVUser.
    }

    public String getObjectId() {
        return avUser.getObjectId();
    }


    public AVGeoPoint getLocation() {
        return (AVGeoPoint) avUser.get("location");
    }

    public void setLocation(AVGeoPoint location) {
        avUser.put("location", location);
    }

    public long getLocationUpdateTime() {
        return (long) avUser.get("locationUpdateTime");
    }

    public void setLocationUpdateTime(long locationUpdateTime) {
        avUser.put("locationUpdateTime", locationUpdateTime);
    }

    public boolean isLocationUpdateState() {
        return (boolean) avUser.get("locationUpdateState");
    }

    public void setLocationUpdateState(boolean locationUpdateState) {
        avUser.put("locationUpdateState", locationUpdateState);
    }

    public int getType() {
        return (int) avUser.get("type");
    }

    public void setType(int type) {
        avUser.put("type", type);
    }

    public String getAvatarUrl() {
        return (String) avUser.get("avatarUrl");
    }

    public void setAvatarUrl(String avatarUrl) {
        avUser.put("avatarUrl", avatarUrl);
    }
}
