package com.android.myframeworks.simple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.myframeworks.R;
import com.android.myframeworks.widget.RListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Choi on 2018/2/6.
 */

public class RListVFragment extends Fragment implements RListView.OnRefreshLisenter{

    private RListView rListView;
    private List<String> sourceList;
    private SimpleAdapter adapter;
    private int curPage = 1;
    private final int pageSize = 15;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_rlistview, null);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
    }

    private void init(View view) {
        sourceList = new ArrayList<>();
        rListView = view.findViewById(R.id.rListView);
        rListView.setOnRefreshLisenter(this);
        rListView.setCanRefresh(true);
        rListView.setCanLoadMore(true);
        adapter = new SimpleAdapter(getActivity(), sourceList);
        rListView.setAdapter(adapter);
        rListView.showRefreshingView();
        onRefresh();
    }

    private void fillData(int page) {
        for (int i = 0; i < pageSize; i++) {
            sourceList.add("item"+((page-1)*pageSize+i));
        }
        curPage = page;
    }

    private boolean isHasMore() {
        return curPage < 3;
    }

    @Override
    public void onRefresh() {
//        refreshError(false);
        Log.e("onrefresh", "onRefresh");
        rListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                sourceList.clear();
                fillData(1);
                adapter.notifyDataSetChanged();
                rListView.stopRefresh();
//                refreshError(true);
                updateFooter();
                setListViewHeightBasedOnChildren(rListView);
            }
        }, 2000);
    }

    private void refreshError(boolean isShowError) {
        if(isShowError) {
            rListView.addRefreshError("");
        } else {
            rListView.removeRefreshError();
        }
    }

    @Override
    public void onLoad() {
        rListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                fillData(curPage+1);
                adapter.notifyDataSetChanged();
                rListView.stopLoad();
                updateFooter();
            }
        }, 2000);
    }

    private boolean isShowFooter = true;
    private void updateFooter() {
        boolean isHasMore = isHasMore();
        rListView.setCanLoadMore(isHasMore);
        if(isHasMore) {
            rListView.showReadyFooter();
        } else {
            if(isShowFooter) {
                rListView.showNoMoreFooter(null);
            } else {
                rListView.hideFooter();
            }
        }
    }

    private class SimpleAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> list;

        public SimpleAdapter(Context mContext, List<String> list) {
            this.mContext = mContext;
            this.list = list;
        }

        @Override
        public int getCount() {
            return list != null? list.size(): 0;
        }

        @Override
        public Object getItem(int position) {
            return list != null?list.get(position): null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;

            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_rlistview, null);
                viewHolder = new ViewHolder();
                viewHolder.tv = convertView.findViewById(R.id.tv_list);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.tv.setText(list.get(position));

            return convertView;
        }

        private class ViewHolder{
            TextView tv;
        }
    }

    private void setListViewHeightBasedOnChildren(RListView listView) {
        if (adapter == null) {  //判断是否为空
            return;
        }
        int totalHeight = 0;  //定义总高度
        //根据listAdapter.getCount()获取当前拥有多少个item项，然后进行遍历对每一个item获取高度再相加最终获得总的高度。
        for (int i = 0; i < adapter.getCount(); i++) {
            View listItem = adapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //获取到list的布局属性
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        //listview最终高度为item的高度+分隔线的高度，这是重新设置listview的属性
        params.height = totalHeight + (listView.getDividerHeight() * (adapter.getCount() - 1));
        //将重新设置的params再应用到listview中
        listView.setLayoutParams(params);
    }


}
