package com.android.myframeworks.simple;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.myframeworks.R;

/**
 * Created by Kevin Choi on 2018/2/6.
 */

public class SlideTestActivity2 extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_slide_test2);
        getSupportFragmentManager().beginTransaction().add(R.id.billContent, new RListVFragment(), "ya").commitAllowingStateLoss();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOne:

                break;
            case R.id.btnTwo:

                break;
        }
    }
}
