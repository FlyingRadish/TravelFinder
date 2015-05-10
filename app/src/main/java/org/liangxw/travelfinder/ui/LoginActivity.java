package org.liangxw.travelfinder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.CheckTool;
import org.liangxw.travelfinder.util.logger.Log;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Des:
 * Created by houxg on 2015/3/31.
 */
public class LoginActivity extends BaseActivity {

    private final static String TAG = LoginActivity.class.getSimpleName();

    @InjectView(R.id.edit_user_name)
    EditText editUserName;
    @InjectView(R.id.edit_password)
    EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @OnClick(R.id.btn_login)
    void onLogin(View view) {
        String[] tokens = new String[]{editUserName.getText().toString(), editPassword.getText().toString()};
        int mask = CheckTool.MASK_EMPTY;
        if (CheckTool.check(tokens, mask) != CheckTool.RESULT_OK) {
            toast("用户名或密码不能为空");
            return;
        }

        UserWrapper.logInInBackground(tokens[0], tokens[1], new LogInCallback() {
            public void done(AVUser user, AVException e) {
                if (user != null) {
                    // 登录成功
                    Log.i(TAG, "login success");
                    startActivity(MainActivity.class);
                    finish();
                } else {
                    // 登录失败
                    toast("登录失败");
                    e.printStackTrace();
                }
            }
        });
    }

    @OnClick(R.id.btn_register)
    void onRegister(View view) {
        startActivity(RegisterActivity.class);
        finish();
    }
}