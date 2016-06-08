package com.siti.renrenlai.activity;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.siti.renrenlai.R;
import com.siti.renrenlai.adapter.PictureAdapter;
import com.siti.renrenlai.bean.Activity;
import com.siti.renrenlai.util.Bimp;
import com.siti.renrenlai.util.ConstantValue;
import com.siti.renrenlai.util.CustomApplication;
import com.siti.renrenlai.util.DateTimePicker;
import com.siti.renrenlai.util.ImageHelper;
import com.siti.renrenlai.util.PhotoUtil;
import com.siti.renrenlai.util.SharedPreferencesUtil;
import com.siti.renrenlai.view.NoScrollGridView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rebus.bottomdialog.BottomDialog;

/**
 * Created by Dong on 2016/3/29.
 */

public class LaunchActivity extends BaseActivity implements View.OnClickListener {

    @Bind(R.id.layout_type)
    RelativeLayout layoutType;
    @Bind(R.id.tv_activity_type)
    TextView tv_activity_type;
    @Bind(R.id.ll_interest)
    LinearLayout ll_interest;
    @Bind(R.id.ll_help)
    LinearLayout ll_help;
    @Bind(R.id.ll_advice)
    LinearLayout ll_advice;
    @Bind(R.id.et_subject)
    EditText et_subject;
    @Bind(R.id.tv_start_time)
    TextView tv_start_time;
    @Bind(R.id.tv_end_time)
    TextView tv_end_time;
    @Bind(R.id.tv_deadline)
    TextView tv_deadline;
    @Bind(R.id.et_place)
    EditText et_place;
    @Bind(R.id.et_epople)
    EditText et_people;
    @Bind(R.id.layout_cover)
    RelativeLayout layout_cover;
    @Bind(R.id.noScrollgridview)
    NoScrollGridView noScrollGridView;
    @Bind(R.id.et_detail)
    EditText et_detail;
    @Bind(R.id.spinner_project)
    Spinner spinner_project;
    @Bind(R.id.btn_preview)
    Button btn_preview;
    @Bind(R.id.btn_publish)
    Button btn_publish;

    public static Bitmap bitmap;
    private String imgName;
    private int activity_type;
    private static final int SELECT_PICTURE = 0;
    private static final int TAKE_PICTURE = 1;
    private PictureAdapter picAdapter;
    // 照相机拍照得到的图片
    private File mCurrentPhotoFile;
    /* 拍照的照片存储位置 */
    private File PHOTO_DIR = null;
    private Calendar calendar = Calendar.getInstance();
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    private static final String TAG = "LaunchActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        ButterKnife.bind(this);
        initViews();
    }

    private void initViews() {
        initTopBarForLeft("发起活动");

        noScrollGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
        picAdapter = new PictureAdapter(this);
        noScrollGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == Bimp.getTempSelectBitmap().size()) {
                    showPicDialog();
                } else {
                    Intent intent = new Intent(LaunchActivity.this,
                            GalleryActivity.class);
                    intent.putExtra("ID", i);
                    startActivity(intent);
                }
            }
        });
        noScrollGridView.setAdapter(picAdapter);

    }

    /**
     * 弹出时间选择对话框
     * 开始时间、结束时间、报名截止
     * @param v
     */
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
                if (id == R.id.tv_start_time) {
                    tv_start_time.setText(sdf.format(date));
                } else if (id == R.id.tv_end_time) {
                    tv_end_time.setText(sdf.format(date));
                } else {
                    tv_deadline.setText(sdf.format(date));
                }
            }
        });
        picker.show();

    }

    /**
     * 从相册选择或拍照弹出框
     */
    public void showPicDialog() {
        BottomDialog dialog = new BottomDialog(LaunchActivity.this);
        dialog.title(R.string.str_select);
        dialog.canceledOnTouchOutside(true);
        dialog.cancelable(true);
        dialog.inflateMenu(R.menu.menu_dialog);
        dialog.setOnItemSelectedListener(new BottomDialog.OnItemSelectedListener() {
            @Override
            public boolean onItemSelected(int id) {
                switch (id) {
                    case R.id.item_pick_photo:
                        startAnimActivity(AlbumActivity.class);
                        return true;
                    case R.id.item_take_pic:
                        goCamera();
                        return true;
                    default:
                        return false;
                }

            }

        });
        dialog.show();
    }

    /**
     * 调用拍照
     */
    private void goCamera() {
        File imgFolder = new File(Environment.getExternalStorageDirectory(), "RenrenLai");
        imgFolder.mkdirs();
        File img = new File(imgFolder, new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg");
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        Uri imgUri = Uri.fromFile(img);
        camera.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(camera, TAKE_PICTURE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == SELECT_PICTURE) {
            ContentResolver resolver = getContentResolver();
            // 照片的原始资源地址
            Uri originalUri = data.getData();
            // 使用ContentProvider通过URI获取原始图片
            Bitmap photo = null;
            try {
                photo = BitmapFactory.decodeStream(resolver.openInputStream(originalUri));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            if (photo != null) {
                // 为防止原始图片过大导致内存溢出，这里先缩小原图显示，然后释放原始Bitmap占用的内存
                photo = ImageHelper.resizeImage(photo, 180, 180);
                //bitmap = ImageHelper.getRoundedCornerBitmap(photo, 130);
                // 释放原始图片占用的内存，防止out of memory异常发生
                //photo.recycle();
                ////iv_cover.setImageBitmap(photo);
            }
        } else if (requestCode == TAKE_PICTURE) {

            File imgFolder = new File(Environment.getExternalStorageDirectory(), "RenrenLai");
            imgName = new SimpleDateFormat("yyyy-MM-dd").format(new Date()) + ".jpg";
            File img = new File(imgFolder, imgName);
            bitmap = PhotoUtil.getImageThumbnail(img.getAbsolutePath(), 180, 180);
            bitmap = PhotoUtil.rotaingImageView(90, bitmap);
        }
    }


    @OnClick({R.id.layout_type, R.id.ll_interest, R.id.ll_help, R.id.ll_advice, R.id.tv_start_time, R.id.tv_end_time,
            R.id.layout_cover, R.id.tv_deadline, R.id.btn_preview, R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_interest:
                unselectedAll();
                ll_interest.setSelected(true);
                activity_type = 1;
                break;
            case R.id.ll_help:
                unselectedAll();
                ll_help.setSelected(true);
                activity_type = 2;
                break;
            case R.id.ll_advice:
                unselectedAll();
                ll_advice.setSelected(true);
                activity_type = 3;
                break;
            case R.id.tv_start_time:
                showTimeDialog(view);
                break;
            case R.id.tv_end_time:
                showTimeDialog(view);
                break;
            case R.id.tv_deadline:
                showTimeDialog(view);
                break;
            case R.id.layout_cover:
                showPicDialog();
                break;
            case R.id.btn_preview:
                Intent previewIntent = new Intent(LaunchActivity.this, PreviewActivity.class);
                int picCount = picAdapter.getCount();
                System.out.println("count:" + picCount);
                ArrayList<String> imgs = new ArrayList<>();
                for (int i = 0; i < picCount - 1; i++) {
                    System.out.println("path:" + Bimp.getTempSelectBitmap().get(i).getPath());
                    imgs.add(Bimp.getTempSelectBitmap().get(i).getPath());
                }
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("images", imgs);
                bundle.putSerializable("activity", getActivityInfo());
                previewIntent.putExtras(bundle);
                startActivity(previewIntent);
                break;
            case R.id.btn_publish:
                publishActivity();
                break;
        }
    }

    /**
     * 发布活动
     */
    public void publishActivity() {
        showProcessDialog("发布中");
        Log.d(TAG, "publishActivity() returned: ");
        String userName = SharedPreferencesUtil.readString(SharedPreferencesUtil.getSharedPreference(this, "login"), "userName");
        String api = null;

        JSONObject activityContent = new JSONObject();
        try {
            activityContent.put("userName", userName);
            activityContent.put("activityName", et_subject.getText().toString());
            activityContent.put("beginTimeOfActivity", tv_start_time.getText().toString());
            activityContent.put("endTimeOfActivity", tv_end_time.getText().toString());
            activityContent.put("signUpDeadLine", tv_deadline.getText().toString());
            activityContent.put("activityAddress", et_place.getText().toString());
            activityContent.put("activityDescrip", et_detail.getText().toString());
            activityContent.put("activityTypeId", activity_type);
            activityContent.put("groupId", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String url = ConstantValue.LAUNCH_ACTIVITY;
        System.out.println("url:" + url);
        JsonObjectRequest req = new JsonObjectRequest(url, activityContent,
                new Response.Listener<JSONObject>() {
                    public String activity_id;

                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "response:" + response.toString());
                        try {
                            activity_id = response.getString("result");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        String path, imageName;
                        for (int i = 0; i < picAdapter.getCount() - 1; i++) {
                            path = Bimp.getTempSelectBitmap().get(i).getPath();
                            imageName = path.substring(path.lastIndexOf("/") + 1);
                            File imgFile = new File(path);
                            if (imgFile.exists()) {
                                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                                upload(activity_id, myBitmap, imageName);
                            }
                        }
                        dismissProcessDialog();
                        finish();
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

    /**
     * 上传图片
     * @param activity_id   活动id(可关联多张照片)
     * @param bitmap        上传的bitmap图片
     * @param imageName     图片名字
     */
    private void upload(String activity_id, Bitmap bitmap, String imageName) {
        String api = "/myupload";
        String url = ConstantValue.urlRoot + api;
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("activityId", activity_id);
            jsonObject.put("imageName", imageName);
            jsonObject.put("imageData", Bitmap2StrByBase64(bitmap));
            Log.d(TAG, "upload: jsonObject：" + jsonObject.toString());
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

    /**
     * 通过Base32将Bitmap转换成Base64字符串
     *
     * @param bit
     * @return
     */
    public String Bitmap2StrByBase64(Bitmap bit) {

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        bit.compress(Bitmap.CompressFormat.JPEG, 40, bos);// 参数100表示不压缩
        byte[] bytes = bos.toByteArray();
        return Base64.encodeToString(bytes, Base64.NO_WRAP);

    }

    public Activity getActivityInfo() {
        Activity activity = new Activity();
        activity.setActivityType(activity_type + "");
        activity.setActivityName(et_subject.getText().toString());
        activity.setActivityStartTime(tv_start_time.getText().toString());
        activity.setActivityEndTime(tv_end_time.getText().toString());
        activity.setDeadline(tv_deadline.getText().toString());
        activity.setActivityAddress(et_place.getText().toString());
        activity.setParticipateNum(et_people.getText().toString());
        activity.setactivityDetailDescrip(et_detail.getText().toString());

        return activity;
    }

    /**
     * 选择活动活动类型处
     */
    public void unselectedAll() {
        ll_interest.setSelected(false);
        ll_help.setSelected(false);
        ll_advice.setSelected(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        picAdapter.notifyDataSetChanged();
    }
}