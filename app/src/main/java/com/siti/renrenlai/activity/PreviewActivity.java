package com.siti.renrenlai.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.PictureAdapter;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.util.Bimp;
import com.siti.renrenlai.view.NoScrollGridView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Dong on 2016/5/19.
 */
public class PreviewActivity extends BaseActivity {

    @Bind(R.id.tv_category)
    TextView tvCategory;
    @Bind(R.id.tv_subject)
    TextView tvSubject;
    @Bind(R.id.tv_start_time)
    TextView tvStartTime;
    @Bind(R.id.tv_end_time)
    TextView tvEndTime;
    @Bind(R.id.tv_deadline)
    TextView tvDeadline;
    @Bind(R.id.tv_place)
    TextView tvPlace;
    @Bind(R.id.tv_people)
    TextView tvPeople;
    @Bind(R.id.noScrollgridview)
    NoScrollGridView noScrollGridView;
    @Bind(R.id.tv_detail)
    TextView tvDetail;
    private PictureAdapter picAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        ButterKnife.bind(this);

        initTopBarForLeft("预览活动");

        initView();
    }

    private void initView() {
        Activity activity = (Activity) getIntent().getSerializableExtra("activity");
        ArrayList<String> images = getIntent().getStringArrayListExtra("images");

        tvCategory.setText(activity.getActivityType());
        tvSubject.setText(activity.getActivityName());
        tvStartTime.setText(activity.getActivityStartTime());
        tvEndTime.setText(activity.getActivityEndTime());
        tvDeadline.setText(activity.getDeadline());
        tvPlace.setText(activity.getActivityAddress());
        tvPeople.setText(activity.getParticipateNum());
        tvDetail.setText(activity.getActivityDescrip());

        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        picAdapter = new PictureAdapter(this, images);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(PreviewActivity.this, GalleryActivity.class);
                intent.putExtra("ID", i);
                startActivity(intent);
            }
        });
        noScrollGridView.setAdapter(picAdapter);
    }
}