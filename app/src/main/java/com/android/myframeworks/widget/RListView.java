package com.android.myframeworks.widget;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.myframeworks.R;

/**
 * Created by cuiwenkai on 2018/1/5.
 */

public class RListView extends ListView implements AbsListView.OnScrollListener{
    public RListView(Context context) {
        super(context);
    }

    public RListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public RListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public RListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private final static int MODE_RERESH = 0;
    private final static int MODE_ON_LOAD = 1;

    private static final int REFRESH_LOAD_READY = 10;//准备下拉刷新或上拉加载
    private static final int REFRESH_RELEASE = 11;//松开刷新
    private static final int REFRESHING = 12;//正在刷新
    private static final int LOAD_RELEASE = 13;//松开加载
    private final static int LOADING = 14;//正在加载
    private final static int LOAD_NO_MORE = 15;//没有更多数据
    private final static int LOAD_HIDE_FOOTER = 16;//隐藏

    private final static int PULL_LOAD_MORE_DELTA = 50;

    private View headerView;
    private int headerHeight;
    private ImageView arrowImage;
    private TextView hintText;
    private ProgressBar progressBar;
    private RotateAnimation upAnimation;
    private RotateAnimation downAnimation;

    private View footerView;
    private TextView footerText;
    private ProgressBar footerProgressBar;
    private int footerHeight;

    private View errorView;
    private OnRefreshLisenter onRefreshLisenter;
    private ROnRefreshScrollListener rOnRefreshScrollListener;

    private int downY = -1;
    private int currentState = REFRESH_LOAD_READY;
    private int mTotalItemCount = -1;
    private boolean canRefresh = true;
    private boolean canLoadMore = true;
    private boolean isShowFooter = true;
    private String noMoreHint = "加载完成，无更多记录";

    private void init() {
        setOnScrollListener(this);
        initHeaderView();
        initFooterView();
    }


    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.list_head, null);
        arrowImage = headerView.findViewById(R.id.head_arrowImageView);
        progressBar = headerView.findViewById(R.id.head_progressBar);
        hintText = headerView.findViewById(R.id.head_tipsTextView);

        headerView.measure(0, 0); // 系统会帮我们测量出headerView的高度
        headerHeight = headerView.getMeasuredHeight();
        hideHeaderView();
        addHeaderView(headerView); // 向ListView的顶部添加一个view对象

        initAnimation();
    }

    private void initAnimation() {
        upAnimation = new RotateAnimation(0, -180,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        upAnimation.setInterpolator(new LinearInterpolator());
        upAnimation.setDuration(250);
        upAnimation.setFillAfter(true);

        downAnimation = new RotateAnimation(-180, 0,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f,
                RotateAnimation.RELATIVE_TO_SELF, 0.5f);
        downAnimation.setInterpolator(new LinearInterpolator());
        downAnimation.setDuration(200);
        downAnimation.setFillAfter(true);
    }

    private void setHeaderState(int state) {
        currentState = state;
        switch (state) {
            case REFRESH_LOAD_READY:
                arrowImage.clearAnimation();
                arrowImage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                hintText.setText("向下拖动进行刷新");
                arrowImage.startAnimation(downAnimation);
                break;
            case REFRESH_RELEASE:
                arrowImage.clearAnimation();
                arrowImage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                hintText.setText("松开进行刷新");
                arrowImage.startAnimation(upAnimation);
                break;
            case REFRESHING:
                arrowImage.clearAnimation();
                arrowImage.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                hintText.setText("正在刷新...");
                break;
        }
    }

    private void resetHeaderView(){
        currentState = REFRESH_LOAD_READY;
        arrowImage.clearAnimation();
        arrowImage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        hintText.setText("向下拖动进行刷新");
    }

    private void showHeaderView() {
        headerView.setPadding(0, 0, 0, 0);
    }

    private void hideHeaderView() {
        headerView.setPadding(0, -headerHeight, 0, 0);
    }

    private void updateHeaderHeight(int deltaY) {
        //获取新的padding值
        int paddingTop = -headerHeight + deltaY;
        if (paddingTop > -headerHeight) {
            //向下滑，且处于顶部，设置padding值，该方法实现了顶布局慢慢滑动显现
            headerView.setPadding(0, paddingTop, 0, 0);
            if (paddingTop >= 0 && currentState == REFRESH_LOAD_READY) {
                //从下拉刷新进入松开刷新状态
                //刷新头布局
                setHeaderState(REFRESH_RELEASE);
            } else if (paddingTop < 0 && currentState == REFRESH_RELEASE) {
                //进入下拉刷新状态
                setHeaderState(REFRESH_LOAD_READY);
            }
        }
    }

    private void initFooterView() {
        footerView = View.inflate(getContext(), R.layout.list_bottom, null);
        footerText = (TextView) footerView.findViewById(R.id.textView1);
        footerProgressBar = (ProgressBar) footerView.findViewById(R.id.progressBar1);
        footerView.measure(0, 0);
        footerHeight = footerView.getMeasuredHeight();
        hideFooterView();
        addFooterView(footerView);
    }

    private void setFooterState(int state) {
        switch (state) {
            case REFRESH_LOAD_READY:
                currentState = state;
                showFooterView();
                footerProgressBar.setVisibility(View.GONE);
                footerText.setText("上拉加载更多");
                break;
            case LOAD_RELEASE :
                currentState = state;
                showFooterView();
                footerProgressBar.setVisibility(View.VISIBLE);
                footerText.setText("松开加载更多");
                break;
            case LOADING:
                currentState = state;
                showFooterView();
                footerProgressBar.setVisibility(View.VISIBLE);
                footerText.setText("正在加载中...");
                break;
            case LOAD_NO_MORE:
                showFooterView();
                footerProgressBar.setVisibility(View.GONE);
                footerText.setText(noMoreHint);
                break;
            case LOAD_HIDE_FOOTER:
                hideFooterView();
                break;

        }
    }

    private void showFooterView() {
        footerView.setPadding(0, 0, 0, 0);//显示出footerView
    }

    private void hideFooterView() {
        footerView.setPadding(0, -footerHeight, 0, 0);
    }

    private void updateFooterView(int deltaY) {
        //获取新的padding值
        int paddingTop = -footerHeight + deltaY;
        if (paddingTop > -footerHeight) {
            //向下滑，且处于顶部，设置padding值，该方法实现了顶布局慢慢滑动显现
            footerView.setPadding(0, paddingTop, 0, 0);
            if (paddingTop >= PULL_LOAD_MORE_DELTA && currentState == REFRESH_LOAD_READY) {
                //从下拉刷新进入松开刷新状态
                //刷新头布局
                setHeaderState(LOAD_RELEASE);
            } else if (paddingTop < 0 && currentState == LOAD_RELEASE) {
                //进入下拉刷新状态
                setHeaderState(REFRESH_LOAD_READY);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取按下时y坐标
                downY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if(currentState == REFRESHING || currentState == LOADING) {
                    break;
                }
                //手指滑动偏移量
                int deltaY = (int) (ev.getY() - downY)/3;
                if(canRefresh && deltaY > 0 && getFirstVisiblePosition() == 0) {
                    updateHeaderHeight(deltaY);
                } else if(canLoadMore && deltaY < 0
                        && getLastVisiblePosition() == mTotalItemCount - 1) {
                    updateFooterView(deltaY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if(getFirstVisiblePosition() == 0) {
                    if(canRefresh && currentState == REFRESH_RELEASE) {
                        showHeaderView();
                        setHeaderState(REFRESHING);
                        if (onRefreshLisenter != null) {
                            onRefreshLisenter.onRefresh();
                        }
                    }
                    resetHeaderView();
                }
                if(getLastVisiblePosition() == mTotalItemCount - 1) {
                    if(canLoadMore && currentState == LOAD_RELEASE) {
                        showFooterView();
                        setFooterState(LOADING);
                        if(onRefreshLisenter != null) {
                            onRefreshLisenter.onLoad();
                        }
                    }
                }

                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mTotalItemCount = totalItemCount;
        if(rOnRefreshScrollListener != null) {
            rOnRefreshScrollListener.onRefreshScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
        }
    }


    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    public void showReadyFooter() {
        isShowFooter = true;
        setFooterState(REFRESH_LOAD_READY);
    }

    public void showNoDateFooter(String noMoreHint) {
        if(!TextUtils.isEmpty(noMoreHint)) {
            this.noMoreHint = noMoreHint;
        }
        isShowFooter = true;
        setFooterState(LOAD_NO_MORE);
    }

    public void hideFooter() {
        isShowFooter = false;
        setFooterState(LOAD_HIDE_FOOTER);
    }

    public void setNoMoreHint(String noMoreHint) {
        this.noMoreHint = noMoreHint;
    }

    public void stopRefresh() {
        if (currentState == REFRESHING) {
            currentState = REFRESH_LOAD_READY;
            resetHeaderView();
        }
    }

    public void stopLoad() {
        if(currentState == LOADING) {
            currentState = REFRESH_LOAD_READY;
            setFooterState(REFRESH_LOAD_READY);
        }
    }


    public void setOnRefreshLisenter(OnRefreshLisenter onRefreshLisenter) {
        this.onRefreshLisenter = onRefreshLisenter;
    }

    public void setROnRefreshScrollListener(ROnRefreshScrollListener rOnRefreshScrollListener) {
        this.rOnRefreshScrollListener = rOnRefreshScrollListener;
    }

    public interface ROnRefreshScrollListener {
        void onRefreshScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);
    }

    public interface OnRefreshLisenter {
        void onRefresh();
        void onLoad();
    }

}
