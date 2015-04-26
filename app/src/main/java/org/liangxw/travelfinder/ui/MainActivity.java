package org.liangxw.travelfinder.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.DeleteCallback;
import com.avos.avoscloud.FindCallback;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.model.Globe;
import org.liangxw.travelfinder.model.Group;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.service.LocationUpdateService;
import org.liangxw.travelfinder.ui.guide.CreateGroupActivity;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.SharedPreferencesTool;
import org.liangxw.travelfinder.util.adapter.ListAdapter;
import org.liangxw.travelfinder.util.dialog.BaseDialog;
import org.liangxw.travelfinder.util.dialog.MessageDialog;
import org.liangxw.travelfinder.util.logger.Log;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnCheckedChanged;


public class MainActivity extends BaseActivity implements View.OnClickListener, ActivityStack.ActivityRule {

    private final static String TAG = MainActivity.class.getSimpleName();
    boolean needLogin = false;
    UserWrapper userWrapper;

    GroupAdapter adapter;

    @InjectView(R.id.list_group)
    ListView listGroup;

    @InjectView(R.id.text_no_group)
    TextView textNoGroup;
    @InjectView(R.id.switch_update)
    Switch switchUpdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        setLeftActionText("我");
        super.onCreate(savedInstanceState);
        Log.i(TAG, "start");
        if (!isLogin()) {
            needLogin = true;
            Log.i(TAG, "need login");
            finish();
            return;
        }
        if (userWrapper.getType() == UserWrapper.TYPE_GUIDE) {
            addAction(R.id.btn_create_group, "建", this);
            addAction(R.id.btn_add_group, "加", this);
        } else if (userWrapper.getType() == UserWrapper.TYPE_VISITOR) {
            addAction(R.id.btn_add_group, "加", this);
        }

        ButterKnife.inject(this);

        init();
    }

    void init() {
        boolean isUpdate = SharedPreferencesTool.read(this, Globe.CONFIG_NAME, Globe.ENABLE_UPDATE_LOCATION, false);
        switchUpdate.setChecked(isUpdate);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateGroup();
    }

    private void updateGroup() {
        AVQuery<Group> avQuery = new AVQuery<>(Group.CLASS_NAME);
        avQuery.whereEqualTo(Group.MEMBERS, userWrapper.getObjectId());
        avQuery.findInBackground(new FindCallback<Group>() {
            @Override
            public void done(List<Group> groups, AVException e) {
                if (e == null) {
                    if (groups.size() == 0) {
                        Log.i(TAG, "no group found???");
                        handler.obtainMessage(NO_GROUP).sendToTarget();
                    } else {
                        Log.i(TAG, "found some group, size:" + groups.size());
                        Log.printList(TAG, groups);
                        Message msg = handler.obtainMessage(UPDATE_GROUP);
                        msg.obj = groups;
                        msg.sendToTarget();
                    }
                } else {
                    e.printStackTrace();
                }
            }
        });
    }

    final static int NO_GROUP = 1;
    final static int UPDATE_GROUP = 2;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case NO_GROUP:
                    listGroup.setVisibility(View.INVISIBLE);
                    textNoGroup.setVisibility(View.VISIBLE);
                    break;
                case UPDATE_GROUP:
                    updateGroupListView((List<Group>) msg.obj);
                    break;
            }
        }
    };

    private void updateGroupListView(List<Group> groups) {
        listGroup.setVisibility(View.VISIBLE);
        textNoGroup.setVisibility(View.INVISIBLE);

        adapter = new GroupAdapter(this, groups);
        listGroup.setAdapter(adapter);
        listGroup.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Group group = (Group) parent.getItemAtPosition(position);
                Log.i(TAG, "go to GroupMap");
                GroupMapActivity.startActivity(MainActivity.this, group.getObjectId(), group.getName());
            }
        });
        setLongClickToDeleteGroupIfIsGuide();
    }

    private void setLongClickToDeleteGroupIfIsGuide() {
        if (userWrapper.getType() == UserWrapper.TYPE_GUIDE) {
            listGroup.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    return showDeleteGroupDialog((Group) parent.getItemAtPosition(position));
                }
            });
        }
    }

    private boolean showDeleteGroupDialog(final Group groupToDelete) {
        if (groupToDelete.getCreatorId() == null || userWrapper.getObjectId() == null) {
            Log.w(TAG, "delete wrong, group:" + groupToDelete + ", user:" + userWrapper);
            return false;
        }
        if (groupToDelete.getCreatorId().equals(userWrapper.getObjectId())) {
            new MessageDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("确认解散这个团 " + groupToDelete.getName() + " ？")
                    .setPositiveButton("是的", new BaseDialog.OnButtonClickListener() {
                        @Override
                        public void onClick(BaseDialog dialog, int witch) {
                            deleteGroup(groupToDelete);
                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("不是", new BaseDialog.OnButtonClickListener() {
                        @Override
                        public void onClick(BaseDialog dialog, int witch) {
                            dialog.dismiss();
                        }
                    }).create().show();
            return true;
        } else {
            return false;
        }
    }

    private void deleteGroup(Group groupToDelete) {
        groupToDelete.deleteInBackground(new DeleteCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    updateGroup();
                } else {
                    toast("解散失败");
                }
            }
        });
    }

    static class GroupAdapter extends ListAdapter<Group> {

        public GroupAdapter(Context context, List<Group> data) {
            super(context, data, R.layout.item_group, new int[]{R.id.text_group_name});
        }

        @Override
        protected void bindData(ViewHolder holder, Group item, int position) {
            holder.getTextView(R.id.text_group_name).setText(item.getName());
            holder.getTextView(R.id.text_group_name).setTag(item);
        }
    }


    @OnCheckedChanged(R.id.switch_update)
    void onCheckedChanged(Switch v, boolean state) {
        switch (v.getId()) {
            case R.id.switch_update:
                setUpdateState(state);
                break;
        }
    }

    private void setUpdateState(boolean state) {
        Intent intent = new Intent(this, LocationUpdateService.class);
        if (state) {
            SharedPreferencesTool.write(this, Globe.CONFIG_NAME, Globe.ENABLE_UPDATE_LOCATION, true);
            intent.setAction(LocationUpdateService.ACTION_RUN);
        } else {
            SharedPreferencesTool.write(this, Globe.CONFIG_NAME, Globe.ENABLE_UPDATE_LOCATION, false);
            intent.setAction(LocationUpdateService.ACTION_STOP);
        }
        startService(intent);
    }


    private boolean isLogin() {
        userWrapper = UserWrapper.getCurrentUser();
        Log.e(TAG, "now user:" + userWrapper);
        if (userWrapper == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_group:
                startActivity(CreateGroupActivity.class);
                break;
            case R.id.btn_add_group:
                startActivity(AddGroupActivity.class);
                break;
        }
    }


    @Override
    protected ActivityStack.ActivityRule getActivityRule() {
        Log.i(TAG, "go here");
        return this;
    }

    @Override
    protected void onRightButtonClicked() {
        startActivity(MineActivity.class);

    }

    @Override
    public boolean onPop(Activity prev, Activity now) {
        Log.i(TAG, "onPop");
        if (needLogin && !(prev instanceof LoginActivity)) {
            Log.i(TAG, "go login");
            startActivity(LoginActivity.class);
        }
        return true;
    }
}
