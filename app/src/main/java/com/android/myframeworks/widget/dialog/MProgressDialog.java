package com.android.myframeworks.widget.dialog;

import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by cuiwenkai on 2017/12/28.
 */

public class MProgressDialog extends ProgressDialog{
    public MProgressDialog(Context context) {
        super(context);
    }

    public MProgressDialog(Context context, int theme) {
        super(context, theme);
    }
}
