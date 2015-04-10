package org.liangxw.travelfinder.util.dialog;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import org.liangxw.travelfinder.R;


/**
 * Des:
 * Created by houxg on 2015/2/10.
 */
public class ListDialog extends BaseDialog {
    public ListDialog(Context context, int themeId) {
        super(context, themeId);
    }

    public ListView getListView() {
        try {
            return (ListView) findViewById(R.id.dialog_list);
        } catch (Exception ex) {
            return null;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public static class Builder extends BaseDialog.Builder {
        ListDialog dialog;
        AdapterView.OnItemClickListener itemClickListener = null;
        AdapterView.OnItemLongClickListener itemLongClickListener = null;
        int dividerResId = -1;
        BaseAdapter adapter;

        public Builder(Context context) {
            super(context);
            dialogLayoutId = R.layout.dialog_list;
        }

        public Builder setNoTitle() {
            return (Builder) super.setNoTitle();
        }

        public Builder setTitle(String title) {
            return (Builder) super.setTitle(title);
        }

        public Builder setTitle(int titleId) {
            return (Builder) super.setTitle(titleId);
        }

        @Deprecated
        public Builder setContentView(View v) {
            return this;
        }

        public Builder setNegativeButton(int textResId, OnButtonClickListener listener) {
            return (Builder) super.setNegativeButton(textResId, listener);
        }

        public Builder setNegativeButton(String negativeButtonText, OnButtonClickListener listener) {
            return (Builder) super.setNegativeButton(negativeButtonText, listener);
        }

        public Builder setPositiveButton(int positiveButtonText, OnButtonClickListener listener) {
            return (Builder) super.setPositiveButton(positiveButtonText, listener);
        }

        public Builder setPositiveButton(String positiveButtonText, OnButtonClickListener listener) {
            return (Builder) super.setPositiveButton(positiveButtonText, listener);
        }

        public Builder setButtonInverse() {
            return (Builder) super.setButtonInverse();
        }

        public Builder setButtonBg(int buttonType, int resId) {
            return (Builder) super.setButtonBg(buttonType, resId);
        }

        public Builder setButtonTextColor(int buttonType, int colorId) {
            return (Builder) super.setButtonTextColor(buttonType, colorId);
        }

        public Builder setCancelable(boolean cancelable) {
            return (Builder) super.setCancelable(cancelable);
        }

        public Builder setOnItemClickListener(AdapterView.OnItemClickListener listener) {
            this.itemClickListener = listener;
            return this;
        }

        public Builder setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
            this.itemLongClickListener = listener;
            return this;
        }

        public Builder setDivider(int resId) {
            this.dividerResId = resId;
            return this;
        }

        public Builder setAdapter(BaseAdapter adapter) {
            this.adapter = adapter;
            return this;
        }

        private int resizeListViewHeight(ListView listView, int showItemNum) {
            int totalHeight = 0;

            int cnt_data = listView.getAdapter().getCount();
            if (cnt_data == 0) {
                return 0;
            }
            int max = showItemNum > cnt_data ? cnt_data : showItemNum;
            View listItem = null;
            for (int i = 0; i < max; i++) {
                listItem = listView.getAdapter().getView(i, null, listView);
                listItem.measure(0, 0);
                totalHeight += listItem.getMeasuredHeight();
            }
            // show half to let user see it.
            if (cnt_data > showItemNum) {
                totalHeight += listItem.getMeasuredHeight() / 2;
            }
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalHeight + (listView.getDividerHeight() * (max - 1));
            listView.setLayoutParams(params);
            return max;
        }

        public ListDialog create() {
            ListDialog dialog = (ListDialog) super.create(new ListDialog(mContext, R.style.dialog));
            ListView listView = dialog.getListView();
            if (adapter != null) {
                listView.setAdapter(adapter);
            }
            if (dividerResId != -1) {
                listView.setDivider(mContext.getResources().getDrawable(dividerResId));
            }
            if (itemClickListener != null) {
                listView.setOnItemClickListener(itemClickListener);
            }
            if (itemLongClickListener != null) {
                listView.setOnItemLongClickListener(itemLongClickListener);
            }
            resizeListViewHeight(listView, 5);
            return dialog;
        }
    }

}