package com.android.myframeworks.widget.dialog;

import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.widget.TextView;

import com.android.myframeworks.R;

/**
 * Created by Kevin Choi on 2018/1/5.
 */
public class MLoadingProgressDialog implements MDialogInterface<MAlterBuilder>{

    @Override
    public AlertDialog createDialog(MAlterBuilder config) {
        AlertDialog.Builder builder = new AlertDialog.Builder(config.mContext);
        if(TextUtils.isEmpty(config.mMessage)) {
            config.setmMessage(R.string.loading);
        }
        if(config.mContentView == null) {
            config.setmContentView(R.layout.dialog_prigress);
        }

        TextView tv = config.mContentView.findViewById(R.id.message);
        if(tv != null) {
            tv.setText(config.mMessage);
        }
        builder.setView(config.mContentView);
        AlertDialog dialog = builder.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        return dialog;
    }

    @Override
    public AlertDialog showDialog(MAlterBuilder config) {
        AlertDialog dialog = createDialog(config);
        dialog.show();
        return dialog;
    }

    @Override
    public void dismiss(AlertDialog dialog) {
        if(dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }
}
