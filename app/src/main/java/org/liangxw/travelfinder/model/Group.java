package org.liangxw.travelfinder.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by houxg on 2015/4/12.
 */
@AVClassName("Group")
public class Group extends AVObject {

    public void setName(String name) {
        put("name", name);
    }

    public void getName(String name) {
        getString("name");
    }
}
