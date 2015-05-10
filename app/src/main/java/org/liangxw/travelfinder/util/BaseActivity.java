package org.liangxw.travelfinder.util;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.liangxw.travelfinder.R;
import org.liangxw.travelfinder.component.ActivityStack;
import org.liangxw.travelfinder.util.logger.Log;


/**
 * Activity基础类
 * Created by houxg on 2014/12/14.
 */
public abstract class BaseActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());

        setTitle(getTitle());
        View view = findViewById(R.id.btn_back);
        if (view != null) {
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onRightButtonClicked();
                }
            });
        }
        ActivityStack.getInstance(this).add(this);
    }

    protected abstract int getLayoutId();

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
        if (textView != null) {
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
        Intent intent = new Intent(this, cls);
        startActivity(intent);
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

    protected void onRightButtonClicked() {
        Log.i(this.getClass().getSimpleName(), "onClicked back");
        finish();
    }

    public void addAction(int id, String text, View.OnClickListener listener) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.panel_title_right);
        Button button = new Button(this, null);
        button.setId(id);
        button.setText(text);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.title_action_text_size));
        button.setOnClickListener(listener);
        button.setBackgroundColor(getResources().getColor(R.color.theme));
        button.setTextColor(getResources().getColor(R.color.white));
        linearLayout.addView(button, new LinearLayout.LayoutParams(UITool.dp2px(this, 50), ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void addAction(int id, int resId, View.OnClickListener listener) {
        LinearLayout linearLayout = (LinearLayout) findViewById(R.id.panel_title_right);
        ImageButton button = new ImageButton(this, null);
        button.setId(id);
        button.setImageResource(resId);
        button.setOnClickListener(listener);
        button.setBackgroundResource(R.color.selector_btn_title);
        linearLayout.addView(button, new LinearLayout.LayoutParams(UITool.dp2px(this, 50), ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public void setLeftActionVisibility(boolean isHide) {
        if (isHide) {
            findViewById(R.id.btn_back).setVisibility(View.INVISIBLE);
        } else {
            findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        }
    }


    public void setLeftActionResource(int resId) {
        ImageButton button = (ImageButton) findViewById(R.id.btn_back);
        button.setImageResource(resId);
    }
}
