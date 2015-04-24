package org.liangxw.travelfinder.ui.guide;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.model.Globe;
import org.liangxw.travelfinder.model.Group;
import org.liangxw.travelfinder.ui.GroupQRCodeActivity;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.ToastTool;
import org.liangxw.travelfinder.util.logger.Log;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class CreateGroupActivity extends BaseActivity {

    private final static String TAG = CreateGroupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_group);
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);
    }

    @InjectView(R.id.edit_group_name)
    EditText editGroupName;

    @OnClick(R.id.btn_create_group)
    public void onCreateGroup(View v) {
        String name = editGroupName.getText().toString();
        createGroup(name);
    }


    void createGroup(String name) {
        final Group group = new Group();
        group.setName(name);
        group.saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.i(TAG, "save success, group:" + group);
                    openGroup(group);
                } else {
                    e.printStackTrace();
                    toast("创建失败，请重试");
                }
            }
        });
    }

    private void openGroup(Group group) {
        Intent intent = new Intent(this, GroupQRCodeActivity.class);
        intent.setAction(Globe.QR_PREFIX + group.getObjectId());
        ToastTool.show(this, "建好了");
        startActivity(intent);
        finish();
    }
}
