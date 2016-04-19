package com.siti.renrenlai.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.siti.renrenlai.R;
import com.siti.renrenlai.view.HeaderLayout;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;

/**
 * Created by Dong on 2016/4/12.
 */
public class FundIntroActivity extends BaseActivity implements View.OnClickListener{

    private RelativeLayout layout_dream_go;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_intro);
        ShareSDK.initSDK(this);
        initView();
        initEvent();
    }

    private void initView(){
        initTopBarForBoth("家园创变大赛", android.R.drawable.ic_menu_share, new HeaderLayout.onRightImageButtonClickListener() {
            @Override
            public void onClick() {
                showShare();
            }
        });

        layout_dream_go = (RelativeLayout) findViewById(R.id.layout_dream_go);

    }

    private void initEvent(){
        layout_dream_go.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id){
            case R.id.layout_dream_go:
                startAnimActivity(ApplyWishActivity.class);
                break;
        }
    }

    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        // 分享时Notification的图标和文字  2.5.9以后的版本不调用此方法
        //oks.setNotification(R.drawable.ic_launcher, getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(getString(R.string.ssdk_oks_share));
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("dd");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        //oks.setImagePath("/sdcard/test.jpg");//确保SDcard下面存在此张图片
        //oks.setImageUrl("http://img05.tooopen.com/images/20160108/tooopen_sy_153700436869.jpg");
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite(getString(R.string.app_name));
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");
        // 启动分享GUI
        oks.show(this);
    }

}