package org.liangxw.travelfinder.ui.visitor;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.model.Globe;
import org.liangxw.travelfinder.model.Group;
import org.liangxw.travelfinder.model.UGMap;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.ui.GroupMapActivity;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.logger.Log;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class FindGroupResultActivity extends BaseActivity {

    private final static String TAG = FindGroupResultActivity.class.getSimpleName();
    String groupId;
    Group foundedGroup;
    UserWrapper userWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_find_group_result);
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        userWrapper = UserWrapper.getCurrentUser();

        groupId = getIntent().getAction();

        Log.i(TAG, "groupId:" + groupId);

        btnConfirm.setClickable(false);
        findGroup();

    }


    void findGroup() {
        AVQuery<Group> groupAVQuery = new AVQuery<>(Group.CLASS_NAME);
        groupAVQuery.getInBackground(groupId, new GetCallback<Group>() {
            @Override
            public void done(Group group, AVException e) {
                if (e == null) {
                    if (group == null) {
                        Log.i(TAG, "not found group");
                    } else {
                        foundedGroup = group;
                        findRelation(foundedGroup);
                    }
                } else {
                    Log.w(TAG, "Network error, detail:" + Log.getStackTraceString(e.getCause()));
                }
            }
        });
    }

    void findRelation(Group group) {
        AVQuery<UGMap> avQuery = new AVQuery<>(UGMap.CLASS_NAME);
        avQuery.whereEqualTo(UGMap.USER_ID, userWrapper.getObjectId());
        avQuery.whereEqualTo(UGMap.GROUP_ID, foundedGroup.getObjectId());
        avQuery.findInBackground(new FindCallback<UGMap>() {
            @Override
            public void done(List<UGMap> ugMaps, AVException e) {
                if (e == null && ugMaps.size() == 0) {
                    Log.i(TAG, "not found user-group map");
                    handler.obtainMessage(CODE_NOT_IN_GROUP).sendToTarget();
                } else if (e == null && ugMaps.size() != 0) {
                    Log.i(TAG, "found user-group map, size:" + ugMaps.size());
                    handler.obtainMessage(CODE_ALREADY_IN_GROUP).sendToTarget();
                } else {
                    if (e != null) {
                        e.printStackTrace();
                    } else {
                        Log.wtf(TAG, "find ugMap, e==null, user:" + userWrapper + ", group:" + foundedGroup);
                    }
                }
            }
        });
    }


    @InjectView(R.id.text_group_name)
    TextView textGroupName;
    @InjectView(R.id.btn_confirm)
    Button btnConfirm;


    final static int CODE_ALREADY_IN_GROUP = 1;
    final static int CODE_NOT_IN_GROUP = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case CODE_NOT_IN_GROUP:
                    textGroupName.setText("群名称：" + foundedGroup.getName());
                    btnConfirm.setText("加入");
                    btnConfirm.setClickable(true);
                    break;
                case CODE_ALREADY_IN_GROUP:
                    textGroupName.setText("群名称：" + foundedGroup.getName());
                    btnConfirm.setText("已经加入");
                    btnConfirm.setClickable(false);
                    break;
            }
        }
    };

    @OnClick(R.id.btn_confirm)
    void onClicked(View v) {
        UGMap ugMap = new UGMap();
        ugMap.setGroupId(foundedGroup.getObjectId());
        ugMap.setUserId(userWrapper.getObjectId());
        ugMap.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    GroupMapActivity.startActivity(FindGroupResultActivity.this, foundedGroup.getObjectId(), foundedGroup.getName());
                    finish();
                } else {
                    e.printStackTrace();
                    toast("加入失败");
                }
            }
        });
    }
}
