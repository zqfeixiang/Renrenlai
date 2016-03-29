package com.siti.renrenlai.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

/**
 * Created by Dong on 2016/3/22.
 */
public class IntroductionActivity extends BaseActivity implements OnClickListener{


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduction);
        initViews();
    }

    private void initViews() {
        initTopBarForLeft("简介");

    }


    @Override
    public void onClick(View v) {
        int id = v.getId();

    }

}
