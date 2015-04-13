package org.liangxw.travelfinder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.GetCallback;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.model.Globe;
import org.liangxw.travelfinder.model.Group;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.logger.Log;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class AddGroupActivity extends BaseActivity {

    private final static String TAG = AddGroupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_add_group);
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @InjectView(R.id.edit_group_id)
    EditText editGroupId;

    @OnClick(R.id.btn_confirm)
    void onClicked(View v) {
        String groupUrl = editGroupId.getText().toString();
        if (groupUrl.contains(Globe.QR_PREFIX) && groupUrl.length() > Globe.QR_PREFIX.length()) {
            String groupId = groupUrl.replace(Globe.QR_PREFIX, "");
            Log.i(TAG, "groupId:" + groupId);
            AVQuery<Group> groupAVQuery = new AVQuery<>(Group.CLASS_NAME);
            groupAVQuery.getInBackground(groupId, new GetCallback<Group>() {
                @Override
                public void done(Group group, AVException e) {
                    if (e == null) {
                        if (group == null) {
                            Log.i(TAG, "not found group");
                        } else {
                            askToAddGroup(group);
                        }
                    } else {
                        Log.w(TAG, "Network error, detail:" + Log.getStackTraceString(e.getCause()));
                    }
                }
            });
        }
    }

    private void askToAddGroup(Group group) {
        Log.i(TAG, "found group:" + group);
    }
}
