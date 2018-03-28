package com.android.myframeworks.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.text.InputFilter;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.android.myframeworks.R;

/**
 * Created by Kevin Choi on 2018/1/5.
 */
public class MInputDialog implements MDialogInterface<MInputDialog.MInputBuilder>{

    @Override
    public AlertDialog createDialog(MInputBuilder configBuilder) {
        Context mContext = configBuilder.mContext;
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        configBuilder.initTitle(builder);
        View mContentView = LayoutInflater.from(mContext).inflate(R.layout.dialog_alert_input, null);
        configBuilder.setmContentView(mContentView);
        configBuilder.initEtView(mContentView);
        builder.setView(mContentView);
        configBuilder.initButtonBar(builder);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(configBuilder.mCancelable);
        dialog.setCanceledOnTouchOutside(configBuilder.mCanceledOnTouchOutside);
        return dialog;
    }

    @Override
    public AlertDialog showDialog(MInputBuilder configBuilder) {
        AlertDialog dialog = createDialog(configBuilder);
        dialog.show();
        return dialog;
    }

    @Override
    public void dismiss(AlertDialog dialog) {
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }


    public static class MInputBuilder extends MAlterBuilder {

        public MInputBuilder(Context mContext) {
            super(mContext);
        }

        protected String mEtMsg;
        protected int mEtStyle = -1;
        protected String mInputHint;
        protected int maxInputLength = -1;
        protected int inputType = -1;

        public void setmEtMsg(String mEtMsg) {
            this.mEtMsg = mEtMsg;
        }

        public void setmEtStyle(int mEtStyle) {
            this.mEtStyle = mEtStyle;
        }

        public void setmInputHint(String mInputHint) {
            this.mInputHint = mInputHint;
        }

        public void setMaxInputLength(int maxInputLength) {
            this.maxInputLength = maxInputLength;
        }

        public void setInputType(int inputType) {
            this.inputType = inputType;
        }

        public void setInputLimit(int maxInputLength, int inputType) {
            this.maxInputLength = maxInputLength;
            this.inputType = inputType;
        }

        protected void initTitle(AlertDialog.Builder builder) {
            if(!TextUtils.isEmpty(mTitle)) {
                builder.setTitle(mTitle);
            }
        }

        protected void initEtView(View mContentView) {
            EditText et = (EditText) mContentView.findViewById(R.id.etInput);
            if(mEtStyle != 0) {
                et.setTextAppearance(mContext, mEtStyle);
            }

            if(!TextUtils.isEmpty(mInputHint)) {
                et.setHint(mInputHint);
            }

            if(maxInputLength != -1 && maxInputLength != 0) {
                et.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxInputLength)});
            }

            if(inputType != -1) {
                et.setInputType(inputType);
            }

            if(!TextUtils.isEmpty(mEtMsg)) {
                et.setText(mEtMsg);
                int selection = mEtMsg.length();
                if(maxInputLength != -1 && selection > maxInputLength) {
                    selection = maxInputLength;
                }
                et.setSelection(selection);
            }
        }

        protected void initButtonBar(AlertDialog.Builder builder) {
            if (positiveButton != null && !TextUtils.isEmpty(positiveButton.text)) {
                builder.setPositiveButton(positiveButton.text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(positiveButton.listener != null) {
                            positiveButton.listener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });
            }
            if (negativeButton != null && !TextUtils.isEmpty(negativeButton.text)) {
                builder.setPositiveButton(negativeButton.text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(negativeButton.listener != null) {
                            negativeButton.listener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });
            }
            if (neutralButton != null && !TextUtils.isEmpty(neutralButton.text)) {
                builder.setPositiveButton(neutralButton.text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(neutralButton.listener != null) {
                            neutralButton.listener.onClick(dialog, which);
                        }
                        dialog.dismiss();
                    }
                });
            }
        }

        public String getString() {
            EditText et = (EditText) mContentView.findViewById(R.id.etInput);
            return et.getText() == null? "": et.getText().toString().trim();
        }

        public EditText getEditText() {
            EditText et = (EditText) mContentView.findViewById(R.id.etInput);
            return et;
        }
    }


}
