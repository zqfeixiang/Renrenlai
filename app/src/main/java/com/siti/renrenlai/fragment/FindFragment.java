package com.siti.renrenlai.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jcodecraeer.xrecyclerview.ProgressStyle;
import com.jcodecraeer.xrecyclerview.XRecyclerView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnBackPressListener;
import com.orhanobut.dialogplus.OnCancelListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.siti.renrenlai.R;
import com.siti.renrenlai.activity.FundIntroActivity;
import com.siti.renrenlai.adapter.ActivityAdapter;
import com.siti.renrenlai.bean.ItemBean;
import com.siti.renrenlai.activity.ActivityInfo;
import com.siti.renrenlai.activity.ApplyActivity;
import com.siti.renrenlai.activity.IntroductionActivity;
import com.siti.renrenlai.activity.SearchActivity;
import com.siti.renrenlai.activity.ViewProjectActivity;
import com.siti.renrenlai.bean.TimeLineModel;
import com.siti.renrenlai.view.FragmentBase;
import com.siti.renrenlai.view.HeaderLayout;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;
import com.siti.renrenlai.view.HeaderLayout.onLeftTextClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dong on 3/22/2016.
 */
public class FindFragment extends FragmentBase implements View.OnClickListener{

    private View view;
    private Context mContext;
    private XRecyclerView mXRecyclerView;
    private List<ItemBean> itemList;
    private RelativeLayout layout_introduction, layout_apply, layout_view_project;
    private ActivityAdapter adapter;
    private TextView tv_fund_intro;
    private String[] images = new String[]{
            "http://api.androidhive.info/music/images/adele.png",
            "http://api.androidhive.info/music/images/eminem.png",
            "http://www.ld12.com/upimg358/allimg/c140921/14112A4V34010-219218.jpg",
            "http://www.ld12.com/upimg358/allimg/c150619/1434F6225920Z-105122.jpg",
            "http://api.androidhive.info/music/images/mj.png"
    };
    private String[] strs = new String[]{"缤纷广场舞","南新七色馆\n儿童绘画营","春季夜跑族","缤纷广场舞","春季夜跑族"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_find,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();
        initView();
        initEvent();
    }

    private void initView() {

        initTopBarForLeftTextBoth("发现", "阳光小区", new onLeftTextClickListener() {
            @Override
            public void onClick() {
                DialogPlus dialogPlus = DialogPlus.newDialog(getActivity())
                        .setAdapter(new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, new String[]{"上海","北京","广州","深圳"}))
                        .setCancelable(true)
                        .setGravity(Gravity.TOP)
                        .setOnDismissListener(new OnDismissListener() {
                            @Override
                            public void onDismiss(DialogPlus dialog) {

                            }
                        })
                        .setOnItemClickListener(new OnItemClickListener() {
                            @Override
                            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                                showToast("position:" + position);
                                dialog.dismiss();
                            }
                        })
                        .setOnCancelListener(new OnCancelListener() {
                            @Override
                            public void onCancel(DialogPlus dialog) {

                            }
                        })
                        .setOnBackPressListener(new OnBackPressListener() {
                            @Override
                            public void onBackPressed(DialogPlus dialogPlus) {

                            }
                        })
                        .create();

                dialogPlus.show();
            }
        }, R.drawable.ic_action_action_search, new onRightImageButtonClickListener(){

            @Override
            public void onClick() {
                startAnimActivity(SearchActivity.class);
            }
        });


        //layout_introduction = (RelativeLayout) findViewById(R.id.layout_introduction);
        //layout_apply = (RelativeLayout) findViewById(R.id.layout_apply);
        //layout_view_project = (RelativeLayout) findViewById(R.id.layout_view_project);
        tv_fund_intro= (TextView) findViewById(R.id.tv_fund_intro);

        mXRecyclerView = (XRecyclerView) findViewById(R.id.list);
        // 设置LinearLayoutManager
        mXRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter = new ActivityAdapter(getActivity(), itemList);
        mXRecyclerView.setAdapter(adapter);
        mXRecyclerView.setRefreshProgressStyle(ProgressStyle.BallSpinFadeLoader);
        mXRecyclerView.setArrowImageView(R.drawable.iconfont_downgrey);

        mXRecyclerView.setLoadingListener(new XRecyclerView.LoadingListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        mXRecyclerView.refreshComplete();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        loadData();
                        mXRecyclerView.loadMoreComplete();
                    }
                }, 1000);
            }
        });

    }

    private void initData() {
        itemList = new ArrayList<>();
        for (int i = 0; i < strs.length; i++) {
            ItemBean item = new ItemBean();
            item.setImg(images[i]);
            item.setTv(strs[i]);
            itemList.add(item);
        }
    }

    private void loadData() {
        for (int i = 0; i < 3; i++) {
            ItemBean item = new ItemBean();
            item.setImg(images[i]);
            item.setTv(strs[i]);
            itemList.add(item);
        }
        adapter.notifyDataSetChanged();
    }

    private void initEvent(){
        //layout_introduction.setOnClickListener(this);
        //layout_apply.setOnClickListener(this);
        //layout_view_project.setOnClickListener(this);
        tv_fund_intro.setOnClickListener(this);

        adapter.setOnItemClickListener(new ActivityAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, Object data) {
                //showToast(data.toString());
                int pos = Integer.parseInt(data.toString());
                Intent intent = new Intent(getActivity(),ActivityInfo.class);
                Bundle bundle = new Bundle();
                bundle.putString("name", itemList.get(pos).getTv());
                bundle.putString("img", itemList.get(pos).getImg());
                intent.putExtras(bundle);
                startAnimActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            /*case R.id.layout_introduction:
                startAnimActivity(IntroductionActivity.class);
                break;
            case R.id.layout_apply:
                startAnimActivity(ApplyActivity.class);
                break;
            case R.id.layout_view_project:
                startAnimActivity(ViewProjectActivity.class);
                break;*/
            case R.id.tv_fund_intro:
                startAnimActivity(FundIntroActivity.class);
                break;
        }
    }
}
