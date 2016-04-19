package com.siti.renrenlai.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.TimeLineAdapter;
import com.siti.renrenlai.bean.TimeLineModel;
import com.siti.renrenlai.view.FragmentBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 2016/4/1.
 */
public class FavoriteFragment extends FragmentBase {
    private View view;
    private Button btn_to_top;
    private XRecyclerView mRecyclerView;
    private LinearLayoutManager linearLayoutManager;
    private TimeLineAdapter mTimeLineAdapter;

    private List<TimeLineModel> mDataList = new ArrayList<>();
    private int refreshTime = 0;
    private int times = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_favorite, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initView();

    }

    private void initView() {
        btn_to_top = (Button) findViewById(R.id.btn_to_top);

        mRecyclerView = (XRecyclerView) findViewById(R.id.recyclerView);
        linearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mTimeLineAdapter = new TimeLineAdapter(mDataList);
        mRecyclerView.setAdapter(mTimeLineAdapter);

        mRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mRecyclerView.setLaodingMoreProgressStyle(ProgressStyle.SquareSpin);
        mRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0) {
                    btn_to_top.setVisibility(View.VISIBLE);
                } else {
                    btn_to_top.setVisibility(View.INVISIBLE);
                }
            }
        });

        btn_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linearLayoutManager.scrollToPositionWithOffset(0, 0);
                //ObjectAnimator.ofInt(mRecyclerView, "scrollY", 0).setDuration(1000).start();
            }
        });

        mRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                refreshTime++;
                times = 0;
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Log.d("refresh", "refreshTime:" + refreshTime);
                        refreshData();
                        mRecyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                Log.d("refresh", "refreshTime:" + refreshTime);
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        Log.d("refresh", "refreshTime:" + refreshTime);
                        loadData();
                        mRecyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });
    }

    private void initData() {
        for (int i = 0; i < 15; i++) {
            TimeLineModel model = new TimeLineModel();
            model.setName("Random" + i);
            model.setAge(i);
            mDataList.add(model);
        }
    }

    private void refreshData(){
        for (int i = 0; i < 2; i++) {
            TimeLineModel model = new TimeLineModel();
            model.setName("refresh" + i);
            model.setAge(i);
            mDataList.add(0, model);
        }
        mTimeLineAdapter.notifyDataSetChanged();
    }

    private void loadData() {
        for (int i = 0; i < 3; i++) {
            TimeLineModel model = new TimeLineModel();
            model.setName("load data" + i);
            model.setAge(i);
            mDataList.add(mDataList.size(), model);
        }
        mTimeLineAdapter.notifyDataSetChanged();
    }
}