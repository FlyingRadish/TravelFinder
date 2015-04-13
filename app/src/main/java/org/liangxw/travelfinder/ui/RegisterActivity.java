package org.liangxw.travelfinder.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.avos.avoscloud.SignUpCallback;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.logger.Log;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Des:
 * Created by houxg on 2015/3/31.
 */
public class RegisterActivity extends BaseActivity implements ActivityStack.ActivityRule {

    private final static String TAG = RegisterActivity.class.getSimpleName();
    boolean isBack = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_register);
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
        Log.i(TAG, "onCreate");
    }

    @InjectView(R.id.edit_user_name)
    EditText editUserName;
    @InjectView(R.id.edit_password)
    EditText editPassword;
    @InjectView(R.id.edit_nick_name)
    EditText editNickName;
    @InjectView(R.id.edit_phone)
    EditText editPhone;
    @InjectView(R.id.radio_group)
    RadioGroup radioGroup;


    @OnClick(R.id.btn_confirm)
    void onRegister(View v) {

        String[] tokens = new String[]{
                editUserName.getText().toString(),
                editPassword.getText().toString(),
                editNickName.getText().toString(),
                editPhone.getText().toString()
        };

        UserWrapper user = new UserWrapper();
        user.setUsername(tokens[0]);
        user.setPassword(tokens[1]);
        user.setNickName(tokens[2]);
        user.setMobilePhoneNumber(tokens[3]);
        user.setType(getType());
        Log.i(TAG, "registing");
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(AVException e) {
                if (e == null) {
                    Log.i(TAG, "registing success");
                    toast("成功");
                    startActivity(MainActivity.class);
                    finish();
                } else {
                    e.printStackTrace();
                    toast("失败");
                }
            }
        });
    }

    int getType() {
        int type;
        switch (radioGroup.getCheckedRadioButtonId()) {
            case R.id.radio_guide:
                type = UserWrapper.TYPE_GUIDE;
                break;
            case R.id.radio_visitor:
                type = UserWrapper.TYPE_VISITOR;
                break;
            default:
                type = UserWrapper.TYPE_VISITOR;
                break;
        }
        return type;
    }

    LogInCallback quickLogin = new LogInCallback() {
        @Override
        public void done(AVUser avUser, AVException e) {
            if (e == null) {

            } else {
                toast("快速登入失败，请手动登录");
                isBack = true;
            }
            finish();
        }
    };

    @Override
    protected void onRightButtonClicked() {
        isBack = true;
        super.onRightButtonClicked();
    }

    @Override
    protected ActivityStack.ActivityRule getActivityRule() {
        return this;
    }

    @Override
    public boolean onPop(Activity prev, Activity now) {
        if (isBack && !(prev instanceof LoginActivity)) {
            startActivity(LoginActivity.class);
        }
        return true;
    }
}