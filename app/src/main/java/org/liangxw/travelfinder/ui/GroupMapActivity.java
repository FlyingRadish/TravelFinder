package org.liangxw.travelfinder.ui;

import android.os.Bundle;
import android.view.View;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.TitleTool;

import butterknife.ButterKnife;
import butterknife.OnClick;


public class GroupMapActivity extends BaseActivity {

    private final static String TAG = GroupMapActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_group_map);
        super.onCreate(savedInstanceState);
        TitleTool.addAction(this, R.id.btn_group_qr_code, "码", null);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_group_qr_code)
    void onClicked(View v){
        switch (v.getId()){
            case R.id.btn_group_qr_code:
                startActivity(GroupQRCodeActivity.class);
                break;
        }
    }
}
