package com.android.myframeworks.widget.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Kevin Choi on 2018/1/5.
 */
public class MAlterDialog extends AlertDialog implements MDialogInterface<MAlterBuilder> {
    protected MAlterDialog(@NonNull Context context) {
        super(context);
    }

    protected MAlterDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected MAlterDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    public AlertDialog createDialog(MAlterBuilder configBuilder) {
        MAlterDialog.Builder builder = new MAlterDialog.Builder(configBuilder.mContext);
        if (!TextUtils.isEmpty(configBuilder.mTitle)) {
            builder.setTitle(configBuilder.mTitle);
        }
        if(!TextUtils.isEmpty(configBuilder.mMessage)) {
            builder.setMessage(configBuilder.mMessage);
        }

        initButtonBar(builder, configBuilder);

        AlertDialog dialog = builder.create();

        dialog.setCancelable(configBuilder.mCancelable);
        dialog.setCanceledOnTouchOutside(configBuilder.mCanceledOnTouchOutside);
        dialog.setOnCancelListener(configBuilder.mCancelListener);
        dialog.setOnKeyListener(configBuilder.mOnKeyListener);
        dialog.setOnDismissListener(configBuilder.mOnDismissListener);
        dialog.setOnShowListener(configBuilder.mOnShowListener);

        if (configBuilder.mGravity > 0) {
            Window window = dialog.getWindow();
            window.setGravity(configBuilder.mGravity);
        }

        if (configBuilder.mIsFullscreen) {
            Window win = dialog.getWindow();
            win.getDecorView().setPadding(0, 0, 0, 0);
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
        } else {
            Window win = dialog.getWindow();
            WindowManager.LayoutParams lp = win.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            win.setAttributes(lp);
        }

        return dialog;
    }

    private void initButtonBar(AlertDialog.Builder builder, final MAlterBuilder configBuilder) {
        if (configBuilder.positiveButton != null && !TextUtils.isEmpty(configBuilder.positiveButton.text)) {
            builder.setPositiveButton(configBuilder.positiveButton.text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(configBuilder.positiveButton.listener != null) {
                        configBuilder.positiveButton.listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }
        if (configBuilder.negativeButton != null && !TextUtils.isEmpty(configBuilder.negativeButton.text)) {
            builder.setPositiveButton(configBuilder.negativeButton.text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(configBuilder.negativeButton.listener != null) {
                        configBuilder.negativeButton.listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }
        if (configBuilder.neutralButton != null && !TextUtils.isEmpty(configBuilder.neutralButton.text)) {
            builder.setPositiveButton(configBuilder.neutralButton.text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if(configBuilder.neutralButton.listener != null) {
                        configBuilder.neutralButton.listener.onClick(dialog, which);
                    }
                    dialog.dismiss();
                }
            });
        }
    }

    @Override
    public AlertDialog showDialog(MAlterBuilder configBuilder) {
        AlertDialog dialog = createDialog(configBuilder);
        dialog.show();
        return dialog;
    }
}
