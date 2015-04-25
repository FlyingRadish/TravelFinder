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
    public final static String NAME = "name";
    public final static String CREATOR_ID = "creatorId";

    public Group() {

    }

    public void setName(String name) {
        put(NAME, name);
    }

    public String getName() {
        return getString(NAME);
    }

    public void setCreatorId(String val) {
        put(CREATOR_ID, val);
    }

    public String getCreatorId() {
        return getString(CREATOR_ID);
    }
}
