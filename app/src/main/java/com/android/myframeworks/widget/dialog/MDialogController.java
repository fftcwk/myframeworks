package com.android.myframeworks.widget.dialog;


import android.content.Context;

import com.android.myframeworks.R;

/**
 * Created by Kevin Choi on 2018/1/5.
 */
public class MDialogController {

    public static MAlterBuilder createAlterDialog(Context mContext, int mTitleId, String mMessage) {
        MAlterBuilder builder = new MAlterBuilder(mContext);
        builder.setAnInterface(new MAlterDialog());
        builder.setmTitle(mTitleId);
        builder.setmMessage(mMessage);
        builder.setPositiveButton(R.string.make_sure, null);
        return builder;
    }

    public static MInputDialog.MInputBuilder createMInputDialog(Context mContext, int mTitleId, String mEtMsg) {
        MInputDialog.MInputBuilder builder = new MInputDialog.MInputBuilder(mContext);
        builder.setAnInterface(new MInputDialog());
        builder.setmTitle(mTitleId);
        builder.setmEtMsg(mEtMsg);
        builder.setPositiveButton(R.string.make_sure, null);
        return builder;
    }

    public static MAlterBuilder createLoadingDialog(Context mContext) {
        MAlterBuilder builder = new MAlterBuilder(mContext);
        builder.setAnInterface(new MLoadingProgressDialog());
        builder.setmMessage(R.string.loading);
        builder.setmContentView(R.layout.dialog_prigress);
        return builder;
    }

    public static MAlterBuilder createLoadingDialog(Context mContext, String msg) {
        MAlterBuilder builder = new MAlterBuilder(mContext);
        builder.setAnInterface(new MLoadingProgressDialog());
        builder.setmMessage(msg);
        builder.setmContentView(R.layout.dialog_prigress);
        return builder;
    }

}
