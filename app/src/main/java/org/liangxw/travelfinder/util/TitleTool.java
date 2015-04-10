package org.liangxw.travelfinder.util;

import android.app.Activity;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.liangxw.travelfinder.R;

/**
 * Created by houxg on 2015/4/10.
 */
public class TitleTool {

    public static void addAction(Activity activity, int id, String text, View.OnClickListener listener) {
        LinearLayout linearLayout = (LinearLayout) activity.findViewById(R.id.panel_title_right);
        Button button = new Button(activity, null);
        button.setId(id);
        button.setText(text);
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, activity.getResources().getDimension(R.dimen.title_action_text_size));
        button.setOnClickListener(listener);
        button.setBackgroundColor(activity.getResources().getColor(R.color.theme));
        button.setTextColor(activity.getResources().getColor(R.color.white));
        linearLayout.addView(button, new LinearLayout.LayoutParams(UITool.dp2px(activity, 50), ViewGroup.LayoutParams.MATCH_PARENT));
    }

    public static void setLeftActionVisibility(Activity activity, boolean isHide) {
        if (isHide) {
            activity.findViewById(R.id.btn_back).setVisibility(View.INVISIBLE);
        } else {
            activity.findViewById(R.id.btn_back).setVisibility(View.VISIBLE);
        }
    }


    public static void setLeftActionText(Activity activity, String text) {
        Button button = (Button) activity.findViewById(R.id.btn_back);
        button.setText(text);
    }
}
