package org.liangxw.travelfinder.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.util.logger.Log;


/**
 * Activity基础类
 * Created by houxg on 2014/12/14.
 */
public class BaseActivity extends ActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle(getTitle());
        View view = findViewById(R.id.btn_back);
        if (view != null) {
            view.setOnClickListener(this);
        }
        ActivityStack.getInstance(this).add(this);
    }

    @Override
    protected void onDestroy() {
        ActivityStack.getInstance(this).removeWithRules(this, getActivityRule());
        super.onDestroy();
    }

    protected ActivityStack.ActivityRule getActivityRule() {
        return null;
    }

    @Override
    public void setTitle(CharSequence title) {
        TextView textView = (TextView) findViewById(R.id.text_activity_title);
        if(textView!=null){
            textView.setText(title);
        }
    }

    public void startService(Class<?> cls) {
        Intent intent = new Intent(this, cls);
        startService(intent);
    }

    public void startActivity(Class<?> cls) {
        this.startActivity(cls, null, null);
    }

    public void startActivity(Class<?> cls, String action) {
        this.startActivity(cls, action, null);
    }

    public void startActivity(Class<?> cls, Bundle data) {
        this.startActivity(cls, null, data);
    }

    public void startActivity(Class<?> cls, String action, Bundle data) {
        Intent intent = new Intent(this, cls);
        if (action != null) {
            intent.setAction(action);
        }
        if (data != null) {
            intent.putExtras(data);
        }
        startActivity(intent);
    }

    public void toast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void toast(int resId) {
        Toast.makeText(this, resId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        onRightButtonClicked();
    }

    protected void onRightButtonClicked() {
        Log.i(this.getClass().getSimpleName(), "onClicked back");
        finish();
    }
}
