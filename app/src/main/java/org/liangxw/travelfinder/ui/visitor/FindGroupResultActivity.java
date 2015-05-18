package org.liangxw.travelfinder.ui.visitor;

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
import org.liangxw.travelfinder.model.Group;
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
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        userWrapper = UserWrapper.getCurrentUser();

        groupId = getIntent().getAction();

        Log.i(TAG, "groupId:" + groupId);

        btnConfirm.setClickable(false);
        findGroup();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_find_group_result;
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
                        findGroupMember(foundedGroup);
                    }
                } else {
                    Log.w(TAG, "Network error, detail:" + Log.getStackTraceString(e.getCause()));
                }
            }
        });
    }

    void findGroupMember(final Group group) {
        AVQuery<Group> groupAVQuery = new AVQuery<>(Group.CLASS_NAME);
        groupAVQuery.whereEqualTo(Group.OBJECT_ID, group.getObjectId());
        groupAVQuery.whereEqualTo("members", UserWrapper.getCurrentUser().getObjectId());
        groupAVQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> groups, AVException e) {
                if (e == null) {
                    if (groups == null || groups.size() == 0) {
                        Log.i(TAG, "not found member");
                        handler.obtainMessage(CODE_NOT_IN_GROUP).sendToTarget();
                    } else {
                        Log.i(TAG, "user already in group");

                        handler.obtainMessage(CODE_ALREADY_IN_GROUP).sendToTarget();
                    }
                } else {
                    Log.w(TAG, "Network error, detail:" + Log.getStackTraceString(e.getCause()));
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
        saveMember();
    }

    void saveMember() {
        UserWrapper userWrapper = UserWrapper.getCurrentUser();
        foundedGroup.addMember(userWrapper.getObjectId());
        foundedGroup.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    GroupMapActivity.getStartIntent(FindGroupResultActivity.this, foundedGroup.getObjectId(), foundedGroup.getName());
                    finish();
                } else {
                    e.printStackTrace();
                    toast("加入失败");
                }
            }
        });
    }
}
