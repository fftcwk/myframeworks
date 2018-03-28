package com.android.myframeworks.widget;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.view.NestedScrollingChild;
import android.support.v4.view.NestedScrollingChildHelper;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.myframeworks.R;

/**
 * Created by Kevin Choi on 2018/1/5.
 */

public class RExpandableListView extends ExpandableListView implements AbsListView.OnScrollListener, NestedScrollingChild {


    public RExpandableListView(Context context) {
        super(context);
        init();
    }

    public RExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RExpandableListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public RExpandableListView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private static final int REFRESH_LOAD_READY = 10;//准备下拉刷新或上拉加载
    private static final int REFRESH_RELEASE = 11;//松开刷新
    private static final int REFRESHING = 12;//正在刷新
    private static final int LOAD_RELEASE = 13;//松开加载
    private final static int LOADING = 14;//正在加载
    private final static int LOAD_NO_MORE = 15;//没有更多数据
    private final static int LOAD_HIDE_FOOTER = 16;//隐藏

    private final static int PULL_LOAD_MORE_DELTA = 50;

    private NestedScrollingChildHelper mScrollingChildHelper;

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

    private View spareHeaderView;
    private ViewGroup spareViewGroup;

    private View refreshErrorView;
    private TextView errorText;
    private String errorHint;
    private boolean isShowErrorView = false;

    private OnRefreshLisenter onRefreshLisenter;
    private ROnRefreshScrollListener rOnRefreshScrollListener;

    private int downY = -1;
    private int currentState = REFRESH_LOAD_READY;
    private int mTotalItemCount = -1;
    private boolean canRefresh = true;
    private boolean canLoadMore = true;
    private boolean isShowFooter = true;
    private String noMoreHint;

    private void init() {
        setOnScrollListener(this);
        initHeaderView();
        initSpareHeaderView();
        initFooterView();
        mScrollingChildHelper = new NestedScrollingChildHelper(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setNestedScrollingEnabled(true);
        }
    }


    private void initHeaderView() {
        headerView = View.inflate(getContext(), R.layout.rlist_head, null);
        arrowImage = headerView.findViewById(R.id.img_rlist_head_arrow);
        progressBar = headerView.findViewById(R.id.pb_rlist_head);
        hintText = headerView.findViewById(R.id.tv_rlist_head_tips);

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
                hintText.setText(R.string.rlist_head_ready);
                arrowImage.startAnimation(downAnimation);
                break;
            case REFRESH_RELEASE:
                arrowImage.clearAnimation();
                arrowImage.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.GONE);
                hintText.setText(R.string.rlist_head_release);
                arrowImage.startAnimation(upAnimation);
                break;
            case REFRESHING:
                arrowImage.clearAnimation();
                arrowImage.setVisibility(View.GONE);
                progressBar.setVisibility(View.VISIBLE);
                hintText.setText(R.string.rlist_head_refreshing);
                break;
        }
    }

    private void resetHeaderView(){
        currentState = REFRESH_LOAD_READY;
        arrowImage.clearAnimation();
        arrowImage.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        hintText.setText(R.string.rlist_head_ready);
        hideHeaderView();
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
        noMoreHint = getResources().getString(R.string.rlist_footer_nodata_default);
        footerView = View.inflate(getContext(), R.layout.rlist_bottom, null);
        footerText = footerView.findViewById(R.id.tv_rlist_bottom);
        footerProgressBar = footerView.findViewById(R.id.pb_rlist_bottom);
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
                footerText.setText(R.string.rlist_footer_ready);
                break;
            case LOAD_RELEASE :
                currentState = state;
                showFooterView();
                footerProgressBar.setVisibility(View.GONE);
                footerText.setText(R.string.rlist_footer_release);
                break;
            case LOADING:
                currentState = state;
                showFooterView();
                footerProgressBar.setVisibility(View.VISIBLE);
                footerText.setText(R.string.rlist_footer_loading);
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

    private int getFooterPadding() {
        return footerView.getPaddingTop();
    }

    private void updateFooterView(int deltaY) {
        //获取新的padding值
        int paddingTop = getFooterPadding()+deltaY;
        footerView.setPadding(0, paddingTop, 0, 0);
        if (paddingTop >= PULL_LOAD_MORE_DELTA) {
            setFooterState(LOAD_RELEASE);
        } else {
            setFooterState(REFRESH_LOAD_READY);
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
                    updateFooterView(-deltaY);
                }
                break;
            case MotionEvent.ACTION_UP:
                downY = -1;
                if(getFirstVisiblePosition() == 0) {
                    if(canRefresh && currentState == REFRESH_RELEASE) {
                        showHeaderView();
                        setHeaderState(REFRESHING);
                        if (onRefreshLisenter != null) {
                            onRefreshLisenter.onRefresh();
                        }
                    } else {
                        resetHeaderView();
                    }
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

    public boolean isRefresh() {
        return currentState == REFRESHING || currentState == LOADING;
    }

    //show header refresh loading
    public void showRefreshingView() {
        showHeaderView();
        setHeaderState(REFRESHING);
    }

    //Can listView use to refresh
    public void setCanRefresh(boolean canRefresh) {
        this.canRefresh = canRefresh;
    }

    //Can listView use to load more
    public void setCanLoadMore(boolean canLoadMore) {
        this.canLoadMore = canLoadMore;
    }

    //show footer nomal
    public void showReadyFooter() {
        isShowFooter = true;
        setFooterState(REFRESH_LOAD_READY);
    }

    //show footer no more data
    public void showNoMoreFooter(String noMoreHint) {
        if(!TextUtils.isEmpty(noMoreHint)) {
            this.noMoreHint = noMoreHint;
        }
        isShowFooter = true;
        setFooterState(LOAD_NO_MORE);
    }

    //hide footer
    public void hideFooter() {
        isShowFooter = false;
        setFooterState(LOAD_HIDE_FOOTER);
    }

    public void setNoMoreHint(String noMoreHint) {
        this.noMoreHint = noMoreHint;
    }

    //stop to refresh and reset refresh headerView
    public void stopRefresh() {
        if (currentState == REFRESHING) {
            currentState = REFRESH_LOAD_READY;
            resetHeaderView();
        }
    }

    //stop to load and reset load footerView
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

    private void initSpareHeaderView() {
        spareHeaderView = View.inflate(getContext(), R.layout.rlist_head_spare, null);
        spareViewGroup = spareHeaderView.findViewById(R.id.rlist_heade_spare_content);
        addHeaderView(spareHeaderView);
    }

    public void addSpareView(View childView) {
        this.addSpareView(childView, -1);
    }

    public void addSpareView(View childView, int index) {
        int childCount = getSpareChildCount();
        if(index > childCount) {
            index = -1;
        }
        spareViewGroup.addView(childView, index);
    }

    public void removeSpareView(View childView) {
        spareViewGroup.removeView(childView);
    }

    public void removeSpareView(int index) {
        if(index < getSpareChildCount()) {
            spareViewGroup.removeViewAt(index);
        }
    }

    public int getSpareChildCount() {
        return spareViewGroup.getChildCount();
    }

    private void initRefreshErrorView() {
        refreshErrorView = LayoutInflater.from(getContext()).inflate(R.layout.rlist_header_error, null);
        errorText = refreshErrorView.findViewById(R.id.tv_rlist_error);
        errorHint = getResources().getString(R.string.rlist_refresh_error_default);
    }

    //if your refreshing happen error, you can use it to show errorMsg
    public void addRefreshError(String errorMsg) {
        if(isShowErrorView) {
            return;
        }
        if(refreshErrorView == null) {
            initRefreshErrorView();
        }
        if(!TextUtils.isEmpty(errorMsg)) {
            this.errorHint = errorMsg;
        }
        errorText.setText(this.errorHint);
        this.addSpareView(refreshErrorView);
        isShowErrorView = true;
    }

    public void addRefreshError(View errorView) {
        if(isShowErrorView) {
            return;
        }
        if(errorView != null) {
            refreshErrorView = errorView;
            this.addSpareView(refreshErrorView);
            isShowErrorView = true;
        }
    }

    //if you show refreshing's error, you can use it to hide or remove errorMsg
    public void removeRefreshError() {
        if(refreshErrorView != null && isShowErrorView) {
            this.removeSpareView(refreshErrorView);
            isShowErrorView = false;
        }
    }

    @Override
    public void setNestedScrollingEnabled(boolean enabled) {
        mScrollingChildHelper.setNestedScrollingEnabled(enabled);
    }

    @Override
    public boolean isNestedScrollingEnabled() {
        return mScrollingChildHelper.isNestedScrollingEnabled();
    }

    @Override
    public boolean startNestedScroll(int axes) {
        return mScrollingChildHelper.startNestedScroll(axes);
    }

    @Override
    public void stopNestedScroll() {
        mScrollingChildHelper.stopNestedScroll();
    }

    @Override
    public boolean hasNestedScrollingParent() {
        return mScrollingChildHelper.hasNestedScrollingParent();
    }

    @Override
    public boolean dispatchNestedScroll(int dxConsumed, int dyConsumed, int dxUnconsumed,
                                        int dyUnconsumed, int[] offsetInWindow) {
        return mScrollingChildHelper.dispatchNestedScroll(dxConsumed, dyConsumed,
                dxUnconsumed, dyUnconsumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedPreScroll(int dx, int dy, int[] consumed, int[] offsetInWindow) {
        return mScrollingChildHelper.dispatchNestedPreScroll(dx, dy, consumed, offsetInWindow);
    }

    @Override
    public boolean dispatchNestedFling(float velocityX, float velocityY, boolean consumed) {
        return mScrollingChildHelper.dispatchNestedFling(velocityX, velocityY, consumed);
    }

    @Override
    public boolean dispatchNestedPreFling(float velocityX, float velocityY) {
        return mScrollingChildHelper.dispatchNestedPreFling(velocityX, velocityY);
    }
}
