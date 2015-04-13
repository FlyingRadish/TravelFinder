package org.liangxw.travelfinder.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

/**
 * Created by houxg on 2015/4/12.
 */
@AVClassName("Group")
public class Group extends AVObject {

    public final static String CLASS_NAME = "Group";
    public final static String OBJECT_ID = "objectId";
    public final static String CREATOR_ID = "creatorId";

    public void setName(String name) {
        put("name", name);
    }

    public void getName(String name) {
        getString("name");
    }
}
