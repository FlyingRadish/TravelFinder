package org.liangxw.travelfinder.model;

import com.avos.avoscloud.AVClassName;
import com.avos.avoscloud.AVObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by houxg on 2015/4/12.
 */
@AVClassName("Group")
public class Group extends AVObject {

    public final static String CLASS_NAME = "Group";
    public final static String OBJECT_ID = "objectId";
    public final static String NAME = "name";
    public final static String CREATOR_ID = "creatorId";
    public final static String MEMBERS = "members";

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

    public void addMember(String userId) {
        List<String> members = getList(MEMBERS);
        if (members == null) {
            members = new ArrayList<>();
        }
        members.add(userId);
        put(MEMBERS, members);
    }

    public List<String> getMembers(){
        List<String> members = getList(MEMBERS);
        if (members == null) {
            members = new ArrayList<>();
        }
        return members;
    }
}
