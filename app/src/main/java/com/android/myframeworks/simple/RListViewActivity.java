package com.android.myframeworks.simple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
 * Created by cuiwenkai on 2018/1/8.
 */

public class RListViewActivity extends AppCompatActivity implements RListView.OnRefreshLisenter{

    private RListView rListView;
    private List<String> sourceList;
    private SimpleAdapter adapter;
    private int curPage = 1;
    private final int pageSize = 15;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlistview);
        init();
    }

    private void init() {
        sourceList = new ArrayList<>();
        rListView = findViewById(R.id.rListView);
        rListView.setOnRefreshLisenter(this);
        rListView.setCanRefresh(true);
        rListView.setCanLoadMore(true);
        adapter = new SimpleAdapter(this, sourceList);
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
            }
        }, 2000);
    }

    private void refreshError(boolean isShowError) {
        if(isShowError) {
            rListView.addRefreshError(null);
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
                rListView.showNoDateFooter(null);
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
}
