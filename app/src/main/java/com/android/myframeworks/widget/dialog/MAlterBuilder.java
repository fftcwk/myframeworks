package com.android.myframeworks.widget.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

/**
 * Created by cuiwenkai on 2017/12/28.
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

    public void setmTitle(int mTitle) {
        this.mTitle = mContext.getString(mTitle);
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void setmMessage(int mMessage) {
        this.mMessage = mContext.getString(mMessage);
    }

    public void setmMessage(String mMessage) {
        this.mMessage = mMessage;
    }

    public void setmContentView(int mContentViewId) {
        this.mContentView = LayoutInflater.from(mContext).inflate(mContentViewId, null);
    }

    public void setmContentView(View mContentView) {
        this.mContentView = mContentView;
    }

    public void setPositiveButton(int txtId, DialogInterface.OnClickListener listener) {
        setPositiveButton(mContext.getString(txtId), listener);
    }

    public void setPositiveButton(String txt, DialogInterface.OnClickListener listener) {
        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.text = txt;
        buttonInfo.listener = listener;
        this.positiveButton = buttonInfo;
    }

    public void setNegativeButton(int txtId, DialogInterface.OnClickListener listener) {
        setNegativeButton(mContext.getString(txtId), listener);
    }

    public void setNegativeButton(String txt,DialogInterface.OnClickListener listener) {
        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.text = txt;
        buttonInfo.listener = listener;
        this.negativeButton = buttonInfo;
    }

    public void setNeutralButton(int txtId, DialogInterface.OnClickListener listener) {
        setNeutralButton(mContext.getString(txtId), listener);
    }

    public void setNeutralButton(String txt, android.content.DialogInterface.OnClickListener listener) {
        ButtonInfo buttonInfo = new ButtonInfo();
        buttonInfo.text = txt;
        buttonInfo.listener = listener;
        this.neutralButton = buttonInfo;
    }

    public void setmCancelable(boolean mCancelable) {
        this.mCancelable = mCancelable;
    }

    public void setmIsFullscreen(boolean mIsFullscreen) {
        this.mIsFullscreen = mIsFullscreen;
    }

    public void setmCanceledOnTouchOutside(boolean mCanceledOnTouchOutside) {
        this.mCanceledOnTouchOutside = mCanceledOnTouchOutside;
    }

    public void setmGravity(int mGravity) {
        this.mGravity = mGravity;
    }

    public void setmCancelListener(DialogInterface.OnCancelListener mCancelListener) {
        this.mCancelListener = mCancelListener;
    }

    public void setmOnKeyListener(DialogInterface.OnKeyListener mOnKeyListener) {
        this.mOnKeyListener = mOnKeyListener;
    }

    public void setmOnDismissListener(DialogInterface.OnDismissListener mOnDismissListener) {
        this.mOnDismissListener = mOnDismissListener;
    }

    public void setmOnShowListener(DialogInterface.OnShowListener mOnShowListener) {
        this.mOnShowListener = mOnShowListener;
    }

    public void setAnInterface(MDialogInterface anInterface) {
        this.anInterface = anInterface;
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

    class ButtonInfo {
        String text;
        DialogInterface.OnClickListener listener;
    }
}
