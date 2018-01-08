package com.android.myframeworks.simple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.android.myframeworks.R;
import com.android.myframeworks.widget.RExpandableListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by cuiwenkai on 2018/1/8.
 */

public class RExpandableActivity extends AppCompatActivity implements RExpandableListView.OnRefreshLisenter{

    private RExpandableListView rExpandableListView;

    private List<String> groups;
    private Map<String, List<String>> childs;
    private SimpleAdapter adapter;
    private int curPage = 1;
    private final int pageSize = 3;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rlist_expandable);
        init();
    }

    private void init() {
        groups = new ArrayList<>();
        childs = new HashMap<>();
        rExpandableListView = findViewById(R.id.rexpandablelistview);
        rExpandableListView.setOnRefreshLisenter(this);
        rExpandableListView.setCanRefresh(true);
        rExpandableListView.setCanLoadMore(true);
        adapter = new SimpleAdapter(this, groups, childs);
        rExpandableListView.setAdapter(adapter);
        rExpandableListView.showRefreshingView();
        onRefresh();
    }

    private void fillData(int page) {
        for (int i = 0; i < pageSize; i++) {
            String group = "group"+((page-1)*pageSize+i);
            groups.add(group);
            List<String> temp = new ArrayList<>();
            for (int j = 0; j < pageSize; j++) {
                temp.add("child"+((page-1)*pageSize+j));
            }
            childs.put(group, temp);
        }
        curPage = page;
    }


    @Override
    public void onRefresh() {
//        refreshError(false);
        rExpandableListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                groups.clear();
                childs.clear();
                fillData(1);
//                refreshError(true);
                adapter.notifyDataSetChanged();
                expandGroup();
                rExpandableListView.stopRefresh();
                updateFooter();
            }
        }, 2000);
    }

    private void refreshError(boolean isShowError) {
        if(isShowError) {
            rExpandableListView.addRefreshError(null);
        } else {
            rExpandableListView.removeRefreshError();
        }
    }

    private void expandGroup() {
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            rExpandableListView.expandGroup(i);
        }
    }


    @Override
    public void onLoad() {
        rExpandableListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                fillData(curPage+1);
                adapter.notifyDataSetChanged();
                expandGroup();
                rExpandableListView.stopLoad();
                updateFooter();
            }
        }, 2000);
    }

    private boolean isHasMore() {
        return curPage < 3;
    }

    private boolean isShowFooter = true;
    private void updateFooter() {
        boolean isHasMore = isHasMore();
        rExpandableListView.setCanLoadMore(isHasMore);
        if(isHasMore) {
            rExpandableListView.showReadyFooter();
        } else {
            if(isShowFooter) {
                rExpandableListView.showNoDateFooter(null);
            } else {
                rExpandableListView.hideFooter();
            }
        }
    }


    private class SimpleAdapter extends BaseExpandableListAdapter {
        private Context mContext;
        private List<String> groups;
        private Map<String, List<String>> childs;

        public SimpleAdapter(Context mContext, List<String> groups, Map<String, List<String>> childs) {
            this.mContext = mContext;
            this.groups = groups;
            this.childs = childs;
        }

        @Override
        public int getGroupCount() {
            return groups == null?0: groups.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return childs == null? 0: childs.get(groups.get(groupPosition)).size();
        }

        @Override
        public String getGroup(int groupPosition) {
            return groups.get(groupPosition);
        }

        @Override
        public String getChild(int groupPosition, int childPosition) {
            return childs.get(groups.get(groupPosition)).get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_rlist_group, null);
            }
            TextView tvGroup = convertView.findViewById(R.id.tv_group);
            tvGroup.setText(getGroup(groupPosition));
            return convertView;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            if(convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(R.layout.item_rlistview, null);
            }
            TextView tv = convertView.findViewById(R.id.tv_list);
            tv.setText(getChild(groupPosition, childPosition));
            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }
}
