package org.liangxw.travelfinder.util.dialog;

/**
 * Des:
 * Created by houxg on 2015/2/25.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import org.liangxw.travelfinder.R;


/**
 * Des:
 * Created by houxg on 2015/2/10.
 */
public class BaseDialog extends Dialog {

    public interface OnButtonClickListener {
        void onClick(BaseDialog dialog, int witch);
    }

    protected OnButtonClickListener positiveListener = null;
    protected OnButtonClickListener negativeListener = null;


    protected Bundle bundle;

    public Bundle getBundle() {
        return bundle;
    }

    public BaseDialog(Context context, int themeId) {
        super(context, themeId);
        bundle = new Bundle();
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    public void setButtonText(int which, String text) {
        switch (which) {
            case BUTTON_POSITIVE:
                ((TextView) findViewById(R.id.dialog_btn_positive)).setText(text);
                break;

            case BUTTON_NEGATIVE:
                ((TextView) findViewById(R.id.dialog_btn_negative)).setText(text);
                break;
        }
    }

    public void setButtonListener(int which, OnButtonClickListener listener) {
        switch (which) {
            case BUTTON_POSITIVE:
                this.positiveListener = listener;
                findViewById(R.id.dialog_btn_positive).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        positiveListener.onClick(BaseDialog.this, BUTTON_POSITIVE);
                    }
                });
                break;

            case BUTTON_NEGATIVE:
                this.negativeListener = listener;
                findViewById(R.id.dialog_btn_negative).setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        negativeListener.onClick(BaseDialog.this, BUTTON_NEGATIVE);
                    }
                });
                break;
        }
    }

    @Override
    protected void onStop() {
        positiveListener = null;
        negativeListener = null;
        bundle.clear();
        bundle = null;
        super.onStop();
    }

    public static class Builder {
        protected Context mContext;
        protected int dialogLayoutId = R.layout.dialog_message;
        protected String title = "";
        protected View contentView = null;
        protected boolean mCancelable = true;

        // Button
        public final static int BUTTON_HAS_NO_ONE = 0;
        public final static int BUTTON_HAS_POSITIVE = 1;
        public final static int BUTTON_HAS_NEGATIVE = 2;

        protected String positiveBtnText = null;
        protected String negativeBtnText = null;
        protected OnButtonClickListener positiveBtnClickListener = null;
        protected OnButtonClickListener negativeBtnClickListener = null;
        protected int positiveBtnBgId = -1;
        protected int negativeBtnBgId = -1;
        protected int positiveBtnTextColorId = R.color.selector_btn_dialog_positive;
        protected int negativeBtnTextColorId = R.color.selector_btn_dialog_negative;
        protected int buttonState = BUTTON_HAS_NO_ONE;

        public Builder(Context context) {
            this.mContext = context;
        }

        protected void setDialogLayoutId(int dialogLayoutId) {
            this.dialogLayoutId = dialogLayoutId;
        }

        public Builder setNoTitle() {
            this.title = null;
            return this;
        }

        public Builder setTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder setTitle(int titleId) {
            this.title = (String) mContext.getText(titleId);
            return this;
        }

        public Builder setContentView(View v) {
            this.contentView = v;
            return this;
        }

        public Builder setNegativeButton(int negativeButtonText, OnButtonClickListener listener) {
            return setNegativeButton((String) mContext.getText(negativeButtonText), listener);
        }

        public Builder setNegativeButton(String negativeButtonText, OnButtonClickListener listener) {
            this.negativeBtnText = negativeButtonText;
            this.negativeBtnClickListener = listener;
            this.buttonState |= BUTTON_HAS_NEGATIVE;
            return this;
        }

        public Builder setPositiveButton(int positiveButtonText, OnButtonClickListener listener) {
            return setPositiveButton((String) mContext.getText(positiveButtonText), listener);
        }

        public Builder setPositiveButton(String positiveButtonText, OnButtonClickListener listener) {
            this.positiveBtnText = positiveButtonText;
            this.positiveBtnClickListener = listener;
            this.buttonState |= BUTTON_HAS_NEGATIVE;
            return this;
        }

        public Builder setButtonInverse() {
            positiveBtnBgId = R.drawable.dialog_btn_negative;
            negativeBtnBgId = R.drawable.dialog_btn_positive;
            positiveBtnTextColorId = R.color.dialog_btn_text_negative;
            negativeBtnTextColorId = R.color.dialog_btn_text_positive;
            return this;
        }

        public Builder setButtonBg(int buttonType, int resId) {
            switch (buttonType) {
                case BUTTON_POSITIVE:
                    positiveBtnBgId = resId;
                    break;
                case BUTTON_NEGATIVE:
                    negativeBtnBgId = resId;
                    break;
            }
            return this;
        }

        public Builder setButtonTextColor(int buttonType, int colorId) {
            switch (buttonType) {
                case BUTTON_POSITIVE:
                    positiveBtnTextColorId = colorId;
                    break;
                case BUTTON_NEGATIVE:
                    negativeBtnTextColorId = colorId;
                    break;
            }
            return this;
        }

        public Builder setCancelable(boolean cancelable) {
            this.mCancelable = cancelable;
            return this;
        }

        public BaseDialog create() {
            // instantiate the dialog_message with the custom Theme
            BaseDialog dialog = new BaseDialog(mContext, R.style.dialog);
            return create(dialog);
        }

        protected BaseDialog create(BaseDialog dialog) {
            // get View
            View dialogView = LayoutInflater.from(mContext).inflate(dialogLayoutId, null);
            dialog.setContentView(dialogView);

            // set dialog_message's property
            WindowManager windowManager = ((Activity) mContext).getWindowManager();
            Display display = windowManager.getDefaultDisplay();
            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = (int) (display.getWidth() * 888 / 1000);
            dialog.getWindow().setAttributes(lp);

            dialog.setCancelable(mCancelable);
            dialog.setCanceledOnTouchOutside(mCancelable);

            // set the dialog_message title
            if (this.title != null) {
                ((TextView) dialogView.findViewById(R.id.dialog_title)).setText(title);
            } else {
                dialogView.findViewById(R.id.dialog_title).setVisibility(View.GONE);
            }
            // if contentView set
            // put the contentView to the dialog_message body
            if (contentView != null) {
                ViewGroup container = (ViewGroup) dialogView.findViewById(R.id.dialog_container);
                container.removeAllViews();
                container.addView(contentView);
            }

            TextView btnPositive = (TextView) dialogView.findViewById(R.id.dialog_btn_positive);
            TextView btnNegative = (TextView) dialogView.findViewById(R.id.dialog_btn_negative);

            // set the positive button
            setButton(dialog, btnPositive, Dialog.BUTTON_POSITIVE, positiveBtnTextColorId, positiveBtnText,
                    positiveBtnClickListener);

            // set the cancel button
            setButton(dialog, btnNegative, Dialog.BUTTON_NEGATIVE, negativeBtnTextColorId, negativeBtnText,
                    negativeBtnClickListener);

            // set button background if need
            try {
                if (positiveBtnBgId != -1) {
                    btnPositive.setBackgroundResource(positiveBtnBgId);
                }
                if (negativeBtnBgId != -1) {
                    btnNegative.setBackgroundResource(negativeBtnBgId);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            //if only have one button
            if ((buttonState & (BUTTON_HAS_NEGATIVE | BUTTON_HAS_POSITIVE)) == 0) {
                contentView.findViewById(R.id.divider_negative).setVisibility(View.GONE);
            }
            return dialog;
        }

        private void setButton(BaseDialog dialog, TextView button, int which, int colorId, String text,
                               OnButtonClickListener listener) {
            if (text != null) {
                button.setText(text);
                button.setTextColor(mContext.getResources().getColorStateList(colorId));
                if (listener != null) {
                    dialog.setButtonListener(which, listener);
                }
            } else {
                // if no button just set the visibility to GONE
                button.setVisibility(View.GONE);
            }
        }
    }

}