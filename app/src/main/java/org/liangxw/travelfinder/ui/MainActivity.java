package org.liangxw.travelfinder.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.ui.guide.CreateGroupActivity;
import org.liangxw.travelfinder.ui.visitor.AddGroupActivity;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.TitleTool;
import org.liangxw.travelfinder.util.logger.Log;


public class MainActivity extends BaseActivity implements View.OnClickListener, ActivityStack.ActivityRule {

    private final static String TAG = MainActivity.class.getSimpleName();
    boolean needLogin = false;
    UserWrapper userWrapper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        setLeftActionText( "我");
        super.onCreate(savedInstanceState);
        Log.i(TAG, "start");
        if (!isLogin()) {
            needLogin = true;
            Log.i(TAG, "need login");
            finish();
            return;
        }
        if (userWrapper.getType() == UserWrapper.TYPE_GUIDE) {
            addAction( R.id.btn_create_group, "建", this);
        } else if (userWrapper.getType() == UserWrapper.TYPE_VISITOR) {
            addAction( R.id.btn_add_group, "加", this);
        }
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
    public boolean onPop(Activity prev, Activity now) {
        Log.i(TAG, "onPop");
        if (needLogin && !(prev instanceof LoginActivity)) {
            Log.i(TAG, "go login");
            startActivity(LoginActivity.class);
        }
        return true;
    }
}
