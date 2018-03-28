package com.android.myframeworks.simple;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.myframeworks.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin Choi on 2018/2/6.
 */

public class SlideTestActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager vPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_test);
        vPager = findViewById(R.id.vPager);
        vPager.setAdapter(new MPagerAdapter(getSupportFragmentManager()));
        vPager.setCurrentItem(0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOne:
                vPager.setCurrentItem(0);
                break;
            case R.id.btnTwo:
                vPager.setCurrentItem(1);
                break;
        }
    }

    private class MPagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> list;

        public MPagerAdapter(FragmentManager fm) {
            super(fm);
            list = new ArrayList<>();
            list.add(new RListVFragment());
            list.add(new RExpandableFragment());
        }

        @Override
        public Fragment getItem(int position) {
            return list.get(position);
        }

        @Override
        public int getCount() {
            return list.size();
        }
    }
}
