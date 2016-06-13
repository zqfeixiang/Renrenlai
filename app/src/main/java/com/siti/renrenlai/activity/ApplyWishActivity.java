package com.siti.renrenlai.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.bean.ProjectIntention;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.DateTimePicker;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.HeaderLayout.onRightImageButtonClickListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Dong on 2016/4/12.
 */
public class ApplyWishActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.tv_fund_intro)
    TextView tvFundIntro;
    @Bind(R.id.et_how)
    EditText etHow;
    @Bind(R.id.et_which)
    EditText etWhich;
    @Bind(R.id.et_what)
    EditText etWhat;
    @Bind(R.id.et_who)
    EditText etWho;
    @Bind(R.id.tv_when)
    TextView tvWhen;
    @Bind(R.id.btn_preview)
    Button btnPreview;
    @Bind(R.id.btn_publish)
    Button btnPublish;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        initTopBarForBoth("申请意愿", "暂存", new onRightImageButtonClickListener() {
            @Override
            public void onClick() {

            }
        });
    }

    @OnClick({R.id.tv_when, R.id.btn_preview, R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_when:
                showTimeDialog(view);
                break;
            case R.id.btn_preview:
                preview();
                break;
            case R.id.btn_publish:
                publish();
                break;
        }
    }

    public void showTimeDialog(View v) {
        final int id = v.getId();

        DateTimePicker picker = new DateTimePicker(this, DateTimePicker.HOUR_OF_DAY);
        picker.setRange(2000, 2030);
        picker.setSelectedItem(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.DAY_OF_MONTH),
                calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
        picker.setOnDateTimePickListener(new DateTimePicker.OnYearMonthDayTimePickListener() {
            @Override
            public void onDateTimePicked(String year, String month, String day, String hour, String minute) {
                Date date = null;
                try {
                    date = sdf.parse(year + "-" + month + "-" + day + " " + hour + ":" + minute);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                tvWhen.setText(sdf.format(date));
            }
        });
        picker.show();
    }

    //预览项目意愿
    public void preview(){
        Intent intent = new Intent(ApplyWishActivity.this, PreviewWish.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("projectIntention", getProjectIntention());
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public ProjectIntention getProjectIntention(){
        ProjectIntention intention = new ProjectIntention();
        intention.setProjectIntentionName(etHow.getText().toString());
        intention.setProjectIntentionDescrip(etWhich.getText().toString());
        intention.setProjectIntentionPurpose(etWhat.getText().toString());
        intention.setProjectIntentionBenefitForWho(etWho.getText().toString());
        intention.setProjectIntentionExecuteTime(tvWhen.getText().toString());

        return intention;
    }

    //发布项目意愿
    public void publish(){
        String url = ConstantValue.LAUNCH_PROJECT_INTENTION;
        String userName = SharedPreferencesUtil.readString( SharedPreferencesUtil.getSharedPreference(
                        getApplicationContext(), "login"), "userName");

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("userName", userName);
            jsonObject.put("projectIntentionName", etHow.getText().toString());
            jsonObject.put("projectIntentionDescrip", etWhich.getText().toString());
            jsonObject.put("projectIntentionPurpose", etWhat.getText().toString());
            jsonObject.put("projectIntentionBenefitForWho", etWho.getText().toString());
            jsonObject.put("projectIntentionExecuteTime", tvWhen.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, url, jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("response", "response:" + response.toString());
                        showToast("发布成功!");
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error: ", "error.getMessage():" + error.getMessage());
                showToast("出错了!");
            }
        });
        req.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        CustomApplication.getInstance().addToRequestQueue(req);
    }
}
