package org.liangxw.travelfinder.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.model.UserWrapper;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.logger.Log;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class MineActivity extends BaseActivity {

    private final static String TAG = MineActivity.class.getSimpleName();

    UserWrapper userWrapper;
    @InjectView(R.id.text_nick_name)
    TextView textNickName;
    @InjectView(R.id.text_user_name)
    TextView textUserName;
    @InjectView(R.id.text_phone)
    TextView textPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        userWrapper = UserWrapper.getCurrentUser();
        if (userWrapper == null) {
            //to-do
            Log.i(TAG, "no current user");
            ActivityStack.getInstance(this).restart();
            return;
        }

        ButterKnife.inject(this);

        textNickName.setText(userWrapper.getNickName());
        textUserName.setText("登录名:" + userWrapper.getUsername());
        textPhone.setText(userWrapper.getMobilePhoneNumber());
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_mine;
    }


    @OnClick({R.id.btn_logout})
    void onClicked(View v) {
        switch (v.getId()) {
            case R.id.btn_logout:
                userWrapper.logOut();
                ActivityStack.getInstance(this).restart();
                break;
        }
    }

}
