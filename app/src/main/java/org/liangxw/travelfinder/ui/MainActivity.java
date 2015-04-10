package org.liangxw.travelfinder.ui;

import android.os.Bundle;
import android.view.View;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.util.BaseActivity;
import org.liangxw.travelfinder.util.TitleTool;
import org.liangxw.travelfinder.util.ToastTool;


public class MainActivity extends BaseActivity implements View.OnClickListener{

    private final static String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);
        TitleTool.setLeftActionText(this, "我");
        TitleTool.addAction(this, R.id.btn_create_group, "建", this);
    }

    @Override
    protected void onRightButtonClicked() {
        toast("还没有搞好哟");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_create_group:
                startActivity(CreateGroupActivity.class);
                break;
        }
    }
}
