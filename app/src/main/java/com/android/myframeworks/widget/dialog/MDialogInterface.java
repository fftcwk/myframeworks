package com.android.myframeworks.widget.dialog;

import android.support.v7.app.AlertDialog;

/**
 * Created by cuiwenkai on 2017/12/27.
 */

public interface MDialogInterface<T extends MAlterBuilder> {
    AlertDialog createDialog(T configBuilder);
    AlertDialog showDialog(T configBuilder);
}
