package com.android.myframeworks.widget.dialog;


import android.content.Context;

import com.android.myframeworks.R;

/**
 * Created by cuiwenkai on 2017/12/27.
 */

public class MDialogController {

    public static MAlterBuilder createAlterDialog(Context mContext, int mTitleId, String mMessage) {
        MAlterBuilder builder = new MAlterBuilder(mContext);
        builder.setAnInterface(new MAlterDialog(mContext));
        builder.setmTitle(mTitleId);
        builder.setmMessage(mMessage);
        builder.setPositiveButton(R.string.make_sure, null);
        return builder;
    }

    public static MInputDialog.MInputBuilder createMInputDialog(Context mContext, int mTitleId, String mEtMsg) {
        MInputDialog.MInputBuilder builder = new MInputDialog.MInputBuilder(mContext);
        builder.setAnInterface(new MInputDialog(mContext));
        builder.setmTitle(mTitleId);
        builder.setmEtMsg(mEtMsg);
        builder.setPositiveButton(R.string.make_sure, null);
        return builder;
    }

}
