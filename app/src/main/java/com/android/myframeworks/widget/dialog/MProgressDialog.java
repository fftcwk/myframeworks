package com.android.myframeworks.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by Kevin Choi on 2018/1/5.
 */
public class MProgressDialog extends ProgressDialog{
    public MProgressDialog(Context context) {
        super(context);
    }

    public MProgressDialog(Context context, int theme) {
        super(context, theme);
    }

}
