package org.liangxw.travelfinder.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.model.UserWrapper;
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
        TitleTool.setLeftActionText(this, "我");
        super.onCreate(savedInstanceState);
        if (!isLogin()) {
            needLogin = true;
            Log.i(TAG, "need login");
            finish();
            return;
        }
        if (userWrapper.getType() == UserWrapper.TYPE_GUIDE) {
            TitleTool.addAction(this, R.id.btn_create_group, "建", this);
        }
    }

    private boolean isLogin() {
        userWrapper = UserWrapper.getCurrentUser();
        Log.i(TAG, "now user:" + userWrapper.getAvUser());
        if (userWrapper == null) {
            return false;
        } else {
            return true;
        }
    }


    @Override
    protected void onRightButtonClicked() {
        toast("还没有搞好哟");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_create_group:
                startActivity(CreateGroupActivity.class);
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
