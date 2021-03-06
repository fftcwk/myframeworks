package com.android.myframeworks;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.android.myframeworks.simple.RExpandableActivity;
import com.android.myframeworks.simple.RListViewActivity;
import com.android.myframeworks.simple.SlideTestActivity;
import com.android.myframeworks.simple.SlideTestActivity2;
import com.android.myframeworks.widget.RExpandableListView;
import com.android.myframeworks.widget.dialog.MAlterBuilder;
import com.android.myframeworks.widget.dialog.MDialogController;
import com.android.myframeworks.widget.dialog.MInputDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_alter:
                showAlterDialog();
                break;
            case R.id.btn_input:
                showInputDialog();
                break;
            case R.id.btn_loading:
                showLoadingDialog();
                break;
            case R.id.btn_rlistview:
                startActivity(new Intent(this, RListViewActivity.class));
                break;
            case R.id.btn_rexpandable:
                startActivity(new Intent(this, RExpandableActivity.class));
                break;
            case R.id.btn_slide:
                startActivity(new Intent(this, SlideTestActivity.class));
                break;
        }
    }

    private void showAlterDialog() {
        MAlterBuilder builder = MDialogController.createAlterDialog(this, R.string.app_name, "测试");
        builder.show();
    }

    private void showInputDialog() {
        MInputDialog.MInputBuilder builder = MDialogController.createMInputDialog(this, R.string.app_name, "Hello");
        builder.setInputLimit(5, -1);
        builder.show();
    }

    private void showLoadingDialog() {
        final MAlterBuilder builder = MDialogController.createLoadingDialog(this);
        final AlertDialog dialog = builder.show();
        findViewById(R.id.btn_loading).postDelayed(new Runnable() {
            @Override
            public void run() {
                builder.dismiss(dialog);
            }
        }, 5000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
