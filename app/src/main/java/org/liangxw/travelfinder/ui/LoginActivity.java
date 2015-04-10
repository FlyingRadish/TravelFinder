package org.liangxw.travelfinder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.CheckTool;

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
        setContentView(R.layout.activity_login);
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_login)
    void onLogin(View view) {
        String[] tokens = new String[]{editUserName.getText().toString(), editPassword.getText().toString()};
        int mask = CheckTool.MASK_EMPTY;
//        if (CheckTool.check(tokens, mask) != CheckTool.RESULT_OK) {
//            toast("用户名或密码不能为空");
//            return;
//        }
        startActivity(MainActivity.class);
        finish();
    }
}