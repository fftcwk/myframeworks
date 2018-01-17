package com.android.myframeworks.widget.dialog;

import android.support.v7.app.AlertDialog;

/**
 * Created by Kevin Choi on 2018/1/5.
 */
public interface MDialogInterface<T extends MAlterBuilder> {
    AlertDialog createDialog(T configBuilder);
    AlertDialog showDialog(T configBuilder);
}
