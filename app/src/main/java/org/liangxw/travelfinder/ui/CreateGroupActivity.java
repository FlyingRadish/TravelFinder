package org.liangxw.travelfinder.ui;

import android.os.Bundle;
import android.view.View;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.ToastTool;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class CreateGroupActivity extends BaseActivity {

    private final static String TAG = CreateGroupActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_create_group);
        super.onCreate(savedInstanceState);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_create_group)
    public void onCreateGroup(View v) {
        ToastTool.show(this, "建好了");
        startActivity(GroupQRCodeActivity.class);
        finish();
    }
}
