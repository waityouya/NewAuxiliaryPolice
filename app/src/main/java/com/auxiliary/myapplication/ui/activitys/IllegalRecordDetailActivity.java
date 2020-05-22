package com.auxiliary.myapplication.ui.activitys;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.auxiliary.myapplication.adapter.AuxiliaryDetailAdapter;
import com.auxiliary.myapplication.model.AuxiliaryParams;
import com.auxiliary.myapplication.model.AuxiliryCaseDetail;
import com.auxiliary.myapplication.model.Case;
import com.auxiliary.myapplication.model.ReturnAuxiliryCase;
import com.auxiliary.myapplication.util.Contract;
import com.auxiliary.myapplication.util.GlobalHandler;
import com.auxiliary.myapplication.util.JsonUtils;
import com.auxiliary.myapplication.util.LoadingDailogUtil;
import com.auxiliary.myapplication.util.OkHttpUtils;
import com.auxiliary.myapplication.util.RSAUtils;
import com.auxiliary.myapplication.util.ToastUtil;
import com.example.myapplication.R;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.goyourfly.multi_picture.ImageLoader;
import com.goyourfly.multi_picture.MultiPictureView;
import com.goyourfly.vincent.Vincent;

import java.util.ArrayList;
import java.util.List;

public class IllegalRecordDetailActivity extends AppCompatActivity implements GlobalHandler.HandleMsgListener {
    private RecyclerView recyclerView;
    private MultiPictureView multiPictureView;
    private AuxiliaryDetailAdapter adapter;
    private ArrayList<AuxiliryCaseDetail> mCases = new ArrayList<>();
    private ImageView back;
    String token;
    String userId;
    private PhotoView photoView;
    private Case mCase;
    private View parent;
    private View auditDetail;
    private TextView textViewAuditType;
    private TextView textViewAuditName;
    private TextView textViewAuditReason;
    private int auxiliaryCaseId = 0;
    private SharedPreferences sp;
    //private View emptyView;
    private GlobalHandler mHandler;
    private Boolean isAudit = false;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_illegal_record_detail);
        init();
        getData(auxiliaryCaseId, userId, token);
        setmLinster();
    }

    private void init() {
        back = findViewById(R.id.iv_title);
        recyclerView = findViewById(R.id.audit_detail_recyclerView);
        multiPictureView = findViewById(R.id.multi_image_view_detail);
        photoView = findViewById(R.id.photoview);
        textView = findViewById(R.id.tv_title);
        auditDetail = findViewById(R.id.audit_detail);
        textViewAuditType = findViewById(R.id.tv_audit_type);
        textViewAuditName = findViewById(R.id.tv_audit_name);
        textViewAuditReason = findViewById(R.id.tv_reason);
        auxiliaryCaseId = getIntent().getIntExtra("auxiliaryCaseId", 0);

        MultiPictureView.setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(@NonNull ImageView imageView, @NonNull Uri uri) {
                Vincent.with(IllegalRecordDetailActivity.this)
                        .load(uri)
                        .placeholder(R.drawable.ic_placeholder_loading)
                        .error(R.drawable.ic_placeholder_loading)
                        .into(imageView);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new AuxiliaryDetailAdapter(mCases);
        recyclerView.setAdapter(adapter);
        parent = findViewById(R.id.sl);
        sp = getSharedPreferences("login", 0);
        token = sp.getString("token", null);
        userId = sp.getString("userId", null);
        mHandler = GlobalHandler.getInstance();
        mHandler.setHandleMsgListener(this);
        textView.setText("记录详情");
    }

    private void setmLinster() {
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        multiPictureView.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(@NonNull View view, int i, @NonNull ArrayList<Uri> arrayList) {
                photoView.setImageURI(arrayList.get(i));
                photoView.setVisibility(View.VISIBLE);
                parent.setVisibility(View.GONE);

            }
        });

        photoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoView.setVisibility(View.GONE);
                parent.setVisibility(View.VISIBLE);

            }
        });
    }

    private void getData(int auxiliaryId, String userId, String token) {
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        AuxiliaryParams auxiliaryParams = new AuxiliaryParams(auxiliaryId, userId, token);
        String getDataJson = JsonUtils.conversionJsonString(auxiliaryParams);
        try {
            String publicEncryptJson = RSAUtils.publicEncrypt(getDataJson, RSAUtils.getPublicKey(RSAUtils.SERVER_PUBLIC_KEY));
            okHttpUtils.postInfo(Contract.SERVER_ADDRESS + "AuxiliaryGetOneAuxiliaryCase", publicEncryptJson);
        } catch (Exception e) {
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showShortToast("未知错误");
                    LoadingDailogUtil.cancelLoadingDailog();

                }

            });
        }


    }

    @Override
    public void onBackPressed() {
        if (photoView.getVisibility() == View.VISIBLE) {

            photoView.setVisibility(View.GONE);
            parent.setVisibility(View.VISIBLE);

        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what) {
            case 0:
                ToastUtil.showShortToast("网络出错，请检查网络");
                LoadingDailogUtil.cancelLoadingDailog();
                break;
            case 1:


                try {
                    ReturnAuxiliryCase returnAuxiliryCase = new Gson().fromJson(msg.getData().getString("backInfo"), ReturnAuxiliryCase.class);
                    switch (returnAuxiliryCase.getCode()) {
                        case "ok":

                            mCase = returnAuxiliryCase.getData();
                            for (int i = 0; i < 12; i++) {
                                switch (i) {
                                    case 0:
                                        AuxiliryCaseDetail caseDetail = new AuxiliryCaseDetail(R.drawable.xingming, "姓名:", mCase.getOffName());
                                        mCases.add(caseDetail);
                                        break;
                                    case 1:
                                        AuxiliryCaseDetail caseDetail1 = new AuxiliryCaseDetail(R.drawable.jiguan, "籍贯:", mCase.getOffBirthPlace());
                                        mCases.add(caseDetail1);
                                        break;
                                    case 2:
                                        AuxiliryCaseDetail caseDetail2 = new AuxiliryCaseDetail(R.drawable.zhengjianleix, "证件类型:", mCase.getOffCertificateType());
                                        mCases.add(caseDetail2);
                                        break;
                                    case 3:
                                        AuxiliryCaseDetail caseDetail3 = new AuxiliryCaseDetail(R.drawable.zhengjianhaoma, "证件号码:", mCase.getOffCertificateNumber());
                                        mCases.add(caseDetail3);
                                        AuxiliryCaseDetail caseDetail12 = new AuxiliryCaseDetail(R.drawable.chepai, "车辆号码:", mCase.getOffPlateNumber());
                                        mCases.add(caseDetail12);
                                        break;
                                    case 4:
                                        caseDetail3 = new AuxiliryCaseDetail(R.drawable.zhengjianhaoma, "证件号码:", mCase.getOffCertificateNumber());
                                        mCases.add(caseDetail3);
                                        break;

                                    case 5:
                                        AuxiliryCaseDetail caseDetail4 = new AuxiliryCaseDetail(R.drawable.weifashij, "违法时间:", mCase.getOffTime());
                                        mCases.add(caseDetail4);
                                        break;

                                    case 6:
                                        AuxiliryCaseDetail caseDetail5 = new AuxiliryCaseDetail(R.drawable.weifadidian, "违法地点:", mCase.getOffPlace());
                                        mCases.add(caseDetail5);
                                        break;
                                    case 7:
                                        AuxiliryCaseDetail caseDetail6 = new AuxiliryCaseDetail(R.drawable.zhonglei, "违法类型:", mCase.getOffType());
                                        mCases.add(caseDetail6);
                                        break;

                                    case 8:
                                        AuxiliryCaseDetail caseDetail7 = new AuxiliryCaseDetail(R.drawable.chufa, "处罚方式:", mCase.getOffPunishmentType());
                                        mCases.add(caseDetail7);
                                        break;

                                    case 9:
                                        AuxiliryCaseDetail caseDetail8 = new AuxiliryCaseDetail(R.drawable.chufajine, "处罚金额:", String.valueOf(mCase.getOffMoney()));
                                        mCases.add(caseDetail8);
                                        break;

                                    case 10:
                                        AuxiliryCaseDetail caseDetail9 = new AuxiliryCaseDetail(R.drawable.jingyuan, "处罚人:", String.valueOf(mCase.getPunishmentId()));
                                        mCases.add(caseDetail9);
                                        break;

                                    case 11:
                                        AuxiliryCaseDetail caseDetail10 = new AuxiliryCaseDetail(R.drawable.jingyuan, "处罚人:", String.valueOf(mCase.getPunishmentName()));
                                        mCases.add(caseDetail10);
                                        break;


                                }
                            }
                            adapter.update(mCases);
                            String[] images = mCase.getImages().split("[;]");
                            List<Uri> uriList = new ArrayList<>();
                            for (String s : images
                            ) {
                                byte[] bitmapByte = Base64.decode(s, Base64.DEFAULT);
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapByte, 0, bitmapByte.length);
                                Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, null, null));
                                uriList.add(uri);
                            }

                            multiPictureView.setList(uriList);
                            switch (mCase.getAuditType()){
                                case 0:
                                   textViewAuditType.setText("未审核");
                                   auditDetail.setVisibility(View.GONE);
                                   break;
                                case 1:
                                    textViewAuditType.setText("审核通过");
                                    textViewAuditName.setText(mCase.getAuditorName());
                                    textViewAuditReason.setText(mCase.getAudit());
                                    break;
                                case 2:
                                    textViewAuditType.setText("审核未通过");
                                    textViewAuditName.setText(mCase.getAuditorName());
                                    textViewAuditReason.setText(mCase.getAudit());
                                    break;
                            }

                            break;
                        case "003":

                            checkInvalidDialog();
                            LoadingDailogUtil.cancelLoadingDailog();
                            break;
                        default:
                            LoadingDailogUtil.cancelLoadingDailog();
                            ToastUtil.showShortToast("服务器错误");

                    }
                    break;
                } catch (Exception e) {
                    e.printStackTrace();
                    LoadingDailogUtil.cancelLoadingDailog();
                    ToastUtil.showShortToast("未知错误");

                }


        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁移除所有消息，避免内存泄露
        mHandler.removeCallbacks(null);
    }

    private void checkInvalidDialog() {
        final NormalDialog dialog = new NormalDialog(IllegalRecordDetailActivity.this);
        dialog.content("身份失效，请重新登录")
                .btnNum(1)
                .btnText("确定")
                .showAnim(new BounceTopEnter())
                .dismissAnim(new SlideBottomExit())
                .show();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("isTokenValid", false);
                editor.apply();
                Intent intent = new Intent(IllegalRecordDetailActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

}
