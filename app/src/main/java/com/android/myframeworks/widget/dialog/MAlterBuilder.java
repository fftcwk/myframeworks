package com.android.myframeworks.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by Kevin Choi on 2018/1/5.
 */
public class MAlterBuilder {
    protected MDialogInterface anInterface;
    protected Context mContext;
    protected String mTitle;
    protected String mMessage;
    protected View mContentView;
    protected ButtonInfo positiveButton, negativeButton, neutralButton;
    protected int mGravity = -1;
    protected boolean mCancelable = true;
    protected boolean mIsFullscreen = false;
    protected boolean mCanceledOnTouchOutside = false;
    protected DialogInterface.OnCancelListener mCancelListener;
    protected DialogInterface.OnKeyListener mOnKeyListener;
    protected DialogInterface.OnDismissListener mOnDismissListener;
    protected DialogInterface.OnShowListener mOnShowListener;

    public MAlterBuilder(Context mContext) {
        this.mContext = mContext;
    }

    public MAlterBuilder setmTitle(int mTitle) {
        this.mTitle = mContext.getString(mTitle);
        return this;
    }

    public MAlterBuilder setmTitle(String mTitle) {
        this.mTitle = mTitle;
        return this;
    }

    public MAlterBuilder setmMessage(int mMessage) {
        this.mMessage = mContext.getString(mMessage);
        return this;
    }

    public MAlterBuilder setmMessage(String mMessage) {
        this.mMessage = mMessage;
        return this;
    }

    public MAlterBuilder setmContentView(int mContentViewId) {
        this.mContentView = LayoutInflater.from(mContext).inflate(mContentViewId, null);
        return this;
    }

    public MAlterBuilder setmContentView(View mContentView) {
        this.mContentView = mContentView;
        return this;
    }

    public MAlterBuilder setPositiveButton(int txtId, DialogInterface.OnClickListener listener) {
        setPositiveButton(mContext.getString(txtId), listener);
        return this;
    }

    public MAlterBuilder setPositiveButton(String txt, DialogInterface.OnClickListener listener) {
        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.text = txt;
        buttonInfo.listener = listener;
        this.positiveButton = buttonInfo;
        return this;
    }

    public MAlterBuilder setNegativeButton(int txtId, DialogInterface.OnClickListener listener) {
        setNegativeButton(mContext.getString(txtId), listener);
        return this;
    }

    public MAlterBuilder setNegativeButton(String txt,DialogInterface.OnClickListener listener) {
        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.text = txt;
        buttonInfo.listener = listener;
        this.negativeButton = buttonInfo;
        return this;
    }

    public MAlterBuilder setNeutralButton(int txtId, DialogInterface.OnClickListener listener) {
        setNeutralButton(mContext.getString(txtId), listener);
        return this;
    }

    public MAlterBuilder setNeutralButton(String txt, android.content.DialogInterface.OnClickListener listener) {
        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.text = txt;
        buttonInfo.listener = listener;
        this.neutralButton = buttonInfo;
        return this;
    }

    public MAlterBuilder setmCancelable(boolean mCancelable) {
        this.mCancelable = mCancelable;
        return this;
    }

    public MAlterBuilder setmIsFullscreen(boolean mIsFullscreen) {
        this.mIsFullscreen = mIsFullscreen;
        return this;
    }

    public MAlterBuilder setmCanceledOnTouchOutside(boolean mCanceledOnTouchOutside) {
        this.mCanceledOnTouchOutside = mCanceledOnTouchOutside;
        return this;
    }

    public MAlterBuilder setmGravity(int mGravity) {
        this.mGravity = mGravity;
        return this;
    }

    public MAlterBuilder setmCancelListener(DialogInterface.OnCancelListener mCancelListener) {
        this.mCancelListener = mCancelListener;
        return this;
    }

    public MAlterBuilder setmOnKeyListener(DialogInterface.OnKeyListener mOnKeyListener) {
        this.mOnKeyListener = mOnKeyListener;
        return this;
    }

    public MAlterBuilder setmOnDismissListener(DialogInterface.OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
        return this;
    }

    public MAlterBuilder setmOnShowListener(DialogInterface.OnShowListener mOnShowListener) {
        this.mOnShowListener = mOnShowListener;
        return this;
    }

    public MAlterBuilder setAnInterface(MDialogInterface anInterface) {
        this.anInterface = anInterface;
        return this;
    }

    public AlertDialog create() {
        if(anInterface != null) {
            return this.anInterface.createDialog(this);
        } else {
            Log.e("error", "There is not dialog interface");
            return null;
        }

    }

    public AlertDialog show() {
        if(this.anInterface != null && mContext instanceof Activity
                && !((Activity) mContext).isFinishing()) {
            return this.anInterface.showDialog(this);
        } else {
            Log.e("error", "There is not dialog interface");
            return null;
        }
    }

    public void dismiss(AlertDialog dialog) {
        if(this.anInterface != null && mContext instanceof Activity
                && !((Activity) mContext).isFinishing()) {
            this.anInterface.dismiss(dialog);
        } else {
            Log.e("error", "There is not dialog interface");
        }
    }

    class ButtonInfo {
        String text;
        DialogInterface.OnClickListener listener;
    }
}
