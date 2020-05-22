package com.auxiliary.myapplication.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.qqtheme.framework.picker.OptionPicker;
import cn.qqtheme.framework.widget.WheelView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.auxiliary.myapplication.adapter.CaseAdapter;
import com.auxiliary.myapplication.model.Case;
import com.auxiliary.myapplication.model.ReturnLoginInfo;
import com.auxiliary.myapplication.model.UpData;
import com.auxiliary.myapplication.util.BitmapUtil;
import com.auxiliary.myapplication.util.Contract;
import com.auxiliary.myapplication.util.DataFileUtil;
import com.auxiliary.myapplication.util.GetPhotoUtil;
import com.auxiliary.myapplication.util.GlobalHandler;
import com.auxiliary.myapplication.util.JsonUtils;
import com.auxiliary.myapplication.util.KeyboardUtil;
import com.auxiliary.myapplication.util.LoadingDailogUtil;
import com.auxiliary.myapplication.util.LogUtil;
import com.auxiliary.myapplication.util.MyApplication;
import com.auxiliary.myapplication.util.OkHttpUtils;
import com.auxiliary.myapplication.util.RSAUtils;
import com.auxiliary.myapplication.util.ToastUtil;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.IDCardParams;
import com.baidu.ocr.sdk.model.IDCardResult;
import com.baidu.ocr.sdk.model.OcrRequestParams;
import com.baidu.ocr.sdk.model.OcrResponseResult;
import com.baidu.ocr.ui.camera.CameraActivity;

import com.example.myapplication.R;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.listener.OnOperItemClickL;
import com.flyco.dialog.widget.ActionSheetDialog;
import com.flyco.dialog.widget.NormalDialog;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.gson.Gson;
import com.goyourfly.multi_picture.ImageLoader;
import com.goyourfly.multi_picture.MultiPictureView;
import com.goyourfly.vincent.Vincent;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.data.Type;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.ImageEngine;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class UpActivity extends AppCompatActivity implements OnDateSetListener, GlobalHandler.HandleMsgListener {
    private ImageView imageViewIdCard;
    private View parent;
    private ImageView imageViewCarNmuber;
    private TextView title;
    private EditText editTextName;
    private TextView textViewsex;
    private TextView textViewNationality;
    private TextView textViewNavitePlace;
    private TextView textViewDocumentType;
    private TextView textViewValidityTime;
    private TextView textViewIllegalTime;
    private RecyclerView recyclerView;
    private TextView textViewIllegalBehavior;
    private TextView textViewPunishmentType;
    private TextView textViewTagPunishmentMoney;
    private SearchView searchView;
    private EditText editTextPunishmentMoney;
    private EditText editTextIdCard;
    private EditText editTextIllegalPlace;
    private EditText editTextCarNumber;
    private SharedPreferences sp ;
    private static final int REQUEST_CODE_CAMERA = 102;
    private static final int REQUEST_CODE_LICENSE_PLATE = 122;
    private static final int REQUEST_ADD_IMAGE = 2;
    CityPickerView mPicker = new CityPickerView();
    private Button buttonUp;
    private GlobalHandler mHandler;
    private Case case1;
    private String token;
    private String userId;
    private boolean isQuery = false;
    private ArrayList<Case> mCases = new ArrayList<>();
    CaseAdapter adapter;
    private MultiPictureView multiPictureView;
    private TextView textViewHistory;
    private View v14;
    private PhotoView photoView;
    private int imageCount = 0;
    private  List<String> images = new LinkedList<>();
    private List<Uri> Added = new LinkedList<>();
    private ImageView nfc;
    private KeyboardUtil keyboardUtil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_up);
        sp = getSharedPreferences("login", 0);
        initView();

        setmListener();
    }
    private void initView(){
        photoView =  findViewById(R.id.photoview);

        recyclerView = findViewById(R.id.rl_up);
        imageViewIdCard = findViewById(R.id.iv_query);
        searchView = findViewById(R.id.serachview);
        imageViewCarNmuber = findViewById(R.id.iv_car_number);
        textViewsex = findViewById(R.id.tv_sex);
        textViewDocumentType = findViewById(R.id.tv_document_type);
        editTextIdCard = findViewById(R.id.et_id_number);
        editTextName = findViewById(R.id.et_name);
        textViewNavitePlace = findViewById(R.id.tv_native_place);
        title = findViewById(R.id.tv_title);
        textViewValidityTime = findViewById(R.id.tv_validity);
        textViewIllegalTime = findViewById(R.id.tv_illegal_time);
        textViewIllegalBehavior = findViewById(R.id.tv_illegal_behavior);
        textViewPunishmentType = findViewById(R.id.tv_punishment_type);
        textViewTagPunishmentMoney = findViewById(R.id.tag_punishment_money);
        editTextPunishmentMoney = findViewById(R.id.et_punishment_money);
        buttonUp = findViewById(R.id.add);
        textViewNationality = findViewById(R.id.tv_nationality);
        editTextIllegalPlace = findViewById(R.id.et_illegal_palce);
        editTextCarNumber = findViewById(R.id.et_car_number);
        nfc = findViewById(R.id.iv_nfc);
        textViewHistory = findViewById(R.id.tag_history);
        v14 = findViewById(R.id.v14);
        parent = findViewById(R.id.ll_parent);
        textViewTagPunishmentMoney.setVisibility(View.GONE);
        editTextPunishmentMoney.setVisibility(View.GONE);
        mHandler = GlobalHandler.getInstance();
        mHandler.setHandleMsgListener(this);
        title.setText("违法信息采集");
        mPicker.init(this);
        CityConfig cityConfig = new CityConfig.Builder().build();
        mPicker.setConfig(cityConfig);
        //当前时间
        Date currentTime = new Date(System.currentTimeMillis());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = format.format(currentTime);
        textViewIllegalTime.setText(time);
        searchView.setQueryHint("身份证");
        searchView.setIconifiedByDefault(false);
        textViewHistory.setVisibility(View.GONE);
        v14.setVisibility(View.GONE);
        recyclerView.setVisibility(View.GONE);
         token = sp.getString("token",null);
         userId = sp.getString("userId",null);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new CaseAdapter(mCases);
        recyclerView.setAdapter(adapter);
        multiPictureView = findViewById(R.id.multi_image_view);

        MultiPictureView.setImageLoader(new ImageLoader() {
            @Override
            public void loadImage(@NotNull ImageView imageView, @NotNull Uri uri) {
                Vincent.with(UpActivity.this)
                        .load(uri)
                        .placeholder(R.drawable.ic_placeholder_loading)
                        .error(R.drawable.ic_placeholder_loading)
                        .into(imageView);
            }
        });

        keyboardUtil = new KeyboardUtil(buttonUp,UpActivity.this, editTextCarNumber);
    }

    private void setmListener(){

        multiPictureView.setAddClickCallback(new MultiPictureView.AddClickCallback() {
            @Override
            public void onAddClick(View view) {
                SelectDialogNoTitle();


            }

        });

        multiPictureView.setDeleteClickCallback(new MultiPictureView.DeleteClickCallback() {
            @Override
            public void onDeleted(@NotNull View view, int i) {
                multiPictureView.removeItem(i);
                images.remove(i);
                Added.remove(i);
            }
        });

        nfc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpActivity.this,NfcActivity.class);
                startActivityForResult(intent,168);

            }
        });

        editTextCarNumber.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                keyboardUtil.hideSoftInputMethod();
                keyboardUtil.showKeyboard();
                buttonUp.setVisibility(View.GONE);
                return false;
            }
        });

        multiPictureView.setItemClickCallback(new MultiPictureView.ItemClickCallback() {
            @Override
            public void onItemClicked(@NotNull View view, int i, @NotNull ArrayList<Uri> arrayList) {




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
        imageViewIdCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        DataFileUtil.getSaveFile(MyApplication.getContext().getApplicationContext()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE, CameraActivity.CONTENT_TYPE_ID_CARD_FRONT);
                startActivityForResult(intent, REQUEST_CODE_CAMERA);
            }
        });

        imageViewCarNmuber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UpActivity.this, CameraActivity.class);
                intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                        DataFileUtil.getSaveFile(getApplication()).getAbsolutePath());
                intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                        CameraActivity.CONTENT_TYPE_GENERAL);
                startActivityForResult(intent, REQUEST_CODE_LICENSE_PLATE);
            }
        });
        textViewsex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionPicker(textViewsex,new String[]{"男","女"});
            }
        });
        textViewDocumentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionPicker(textViewDocumentType,new String[]{"中国居民身份证","外国人永久居留身份证","港澳台居民身份证"});
            }
        });
        textViewNavitePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPicker.showCityPicker();
            }
        });
        mPicker.setOnCityItemClickListener(new OnCityItemClickListener() {
            @Override
            public void onSelected(ProvinceBean province, CityBean city, DistrictBean district) {
                super.onSelected(province, city, district);
                textViewNavitePlace.setText(province.toString()+city.toString());

            }


        });

        textViewValidityTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TimePickerDialog dialog = new TimePickerDialog.Builder()
                        .setCancelStringId("取消")
                        .setSureStringId("确定")
                        .setTitleStringId("请选择")
                        .setCyclic(false)
                        .setThemeColor(getResources().getColor(R.color.blue))
                        .setType(Type.YEAR_MONTH_DAY)
                        .setCallBack(UpActivity.this)
                        .build();
                dialog.show(getSupportFragmentManager(),"年_月_日");
            }
        });

        textViewIllegalBehavior.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionPicker(textViewIllegalBehavior,new String[]{"逆行","超载","酒驾","超速","无牌无证" +
                        "违法停车", "在机动车道行驶","未按交通信号通行"});
            }
        });
        textViewPunishmentType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOptionPicker(textViewPunishmentType,new String[]{"教育","当场交款","银行交款"});
            }
        });
        textViewPunishmentType.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               if(!s.toString().equals("教育")){
                   textViewTagPunishmentMoney.setVisibility(View.VISIBLE);
                   editTextPunishmentMoney.setVisibility(View.VISIBLE);
               }else {
                   textViewTagPunishmentMoney.setVisibility(View.GONE);
                   editTextPunishmentMoney.setVisibility(View.GONE);
               }
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                getDataBySearch(query,userId,token);
                LoadingDailogUtil.showLoadingDialog(UpActivity.this,"搜索中...");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        buttonUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString();
                String sex = textViewsex.getText().toString();
                String nationality = textViewNationality.getText().toString();
                String documentType = textViewDocumentType.getText().toString();
                String idNumber = editTextIdCard.getText().toString();
                String nativePlace = textViewNavitePlace.getText().toString();
                String  validity = textViewValidityTime.getText().toString();
                String  offTime = textViewIllegalTime.getText().toString();
                String offPlace = editTextIllegalPlace.getText().toString();
                String offPunishmentType = textViewPunishmentType.getText().toString();
                String offIllegalBehavior = textViewIllegalBehavior.getText().toString();
                String punimentMoney = editTextPunishmentMoney.getText().toString();
                String carNumber = editTextCarNumber.getText().toString();
                imageCount = multiPictureView.getCount();
                if(name.isEmpty() || sex.isEmpty() || nationality.isEmpty() || documentType.isEmpty()||
                        idNumber.isEmpty()|| nativePlace.isEmpty()||offIllegalBehavior.isEmpty()||
                        offPunishmentType.isEmpty()||offPlace.isEmpty()
                ||carNumber.isEmpty() || validity.isEmpty() || offTime.isEmpty() || imageCount <=0){
                    ToastUtil.showShortToast("请填写完整");
                }else {
                    LoadingDailogUtil.showLoadingDialog(UpActivity.this,"提交中...");

                    int sexNo = 0;
                    if(sex.equals("男")){
                        sexNo = 1;
                    }
                    if(punimentMoney.isEmpty()){

                        case1 = new Case(carNumber,name,sexNo,nativePlace,documentType,
                                idNumber,validity,offTime,offPlace,offIllegalBehavior,offPunishmentType,Integer.valueOf(userId)
                                ,token,0,0);
                    }else {
                        case1 = new Case(carNumber,name,sexNo,nativePlace,documentType,
                                idNumber,validity,offTime,offPlace,offIllegalBehavior,offPunishmentType,Integer.valueOf(punimentMoney),Integer.valueOf(userId)
                                ,token,0,0);
                    }

                    OkHttpUtils okHttpUtils = new OkHttpUtils();

                    try {
                        String caseJson = JsonUtils.conversionJsonString(case1);
                        String caseEncryptJson = RSAUtils.publicEncrypt(caseJson,RSAUtils.getPublicKey(RSAUtils.SERVER_PUBLIC_KEY));
                        UpData upData = new UpData(caseEncryptJson,images);
                        String up = JsonUtils.conversionJsonString(upData);
                        okHttpUtils.postInfo(Contract.SERVER_ADDRESS+"AuxiliaryUpCaseServelt",up);
                    }catch (Exception e){
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
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_CAMERA && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                String contentType = data.getStringExtra(CameraActivity.KEY_CONTENT_TYPE);
                String filePath = DataFileUtil.getSaveFile(MyApplication.getContext().getApplicationContext()).getAbsolutePath();
                if (!TextUtils.isEmpty(contentType)) {
                    if (CameraActivity.CONTENT_TYPE_ID_CARD_FRONT.equals(contentType)) {
                        recIDCard(IDCardParams.ID_CARD_SIDE_FRONT, filePath);
                    }
                }
            }
        }
        if (requestCode == REQUEST_CODE_LICENSE_PLATE && resultCode == Activity.RESULT_OK) {
            recCarNumber(DataFileUtil.getSaveFile(MyApplication.getContext().getApplicationContext()).getAbsolutePath());
        }
        if (requestCode == REQUEST_ADD_IMAGE && resultCode == RESULT_OK) {
            LoadingDailogUtil.showLoadingDialog(UpActivity.this,"上传中...");
            multiPictureView.addItem(Matisse.obtainResult(data));

            for (Uri uri:multiPictureView.getList()
            ) {
                int flag = 0;
                for (Uri uri1:Added
                ) {
                    if(uri == uri1){
                        flag = 1;
                    }
                }
                if(flag == 0){
                    //获取路径
                    String path = BitmapUtil.getRealFilePath(MyApplication.getContext(),uri);
                    //获取压缩后的路径
                    String pathNew = BitmapUtil.compressImage(path);
                    //转Bitmap
                    Bitmap bitmap = BitmapUtil.getSmallBitmap(pathNew);
                    String base64 = BitmapUtil.bitmapTobase64NONseal(bitmap);
                    //转把base4并异或加密
                    String image = BitmapUtil.imageEncrypt(base64);
                    images.add(image);
                    Added.add(uri);
                }


            }
            LoadingDailogUtil.cancelLoadingDailog();
        }
        if(requestCode == 168 ){

            textViewsex.setText(data.getStringExtra("sex"));
            editTextName.setText(data.getStringExtra("name"));
            editTextIdCard.setText(data.getStringExtra("idNumber"));
            textViewValidityTime.setText(data.getStringExtra("validTime"));
        }
        if(requestCode == GetPhotoUtil.TAKEONCAMERA ){
            LogUtil.d("获取多张图片回调");
            if(GetPhotoUtil.getTakePhoto(UpActivity.this) != null){
                multiPictureView.addItem(GetPhotoUtil.getTakePhoto(UpActivity.this));
                for (Uri uri:multiPictureView.getList()
                ) {
                    int flag = 0;
                    for (Uri uri1:Added
                    ) {
                        if(uri == uri1){
                            flag = 1;
                        }
                    }
                    if(flag == 0){
                        //获取路径
                        String path = BitmapUtil.getRealFilePath(MyApplication.getContext(),uri);
                        //获取压缩后的路径
                        String pathNew = BitmapUtil.compressImage(path);
                        //转Bitmap
                        Bitmap bitmap = BitmapUtil.getSmallBitmap(pathNew);
                        String base64 = BitmapUtil.bitmapTobase64NONseal(bitmap);
                        //转把base4并异或加密
                        String image = BitmapUtil.imageEncrypt(base64);
                        images.add(image);
                        Added.add(uri);
                    }


                }

            }else {
                ToastUtil.showShortToast("获取拍摄图片失败");
            }

        }
    }

    private void recIDCard(final String idCardSide, String filePath) {
        IDCardParams param = new IDCardParams();
        param.setImageFile(new File(filePath));
        param.setIdCardSide(idCardSide);
        param.setDetectDirection(true);
        OCR.getInstance().recognizeIDCard(param, new OnResultListener<IDCardResult>() {
            @Override
            public void onResult(IDCardResult result) {
                if (result != null && result.toString().contains("front")) {

                    editTextName.setText(result.getName().toString());
                    textViewsex.setText(result.getGender().toString());
                    editTextIdCard.setText(result.getIdNumber().toString());
                    getDataBySearch(result.getIdNumber().toString(),userId,token);
                    LoadingDailogUtil.showLoadingDialog(UpActivity.this,"获取数据中...");
                    ToastUtil.showShortToast("识别成功");
                }
            }

            @Override
            public void onError(OCRError error) {
                LogUtil.d("onError", "error: " + error.getMessage());
                ToastUtil.showShortToast("识别失败，请重试");
            }
        });
    }

    private void recCarNumber(String filePath){
        OcrRequestParams params = new OcrRequestParams();
        params.setImageFile(new File(filePath));
        OCR.getInstance().recognizeLicensePlate(params, new OnResultListener<OcrResponseResult>() {
            @Override
            public void onResult(OcrResponseResult ocrResponseResult) {
                if(ocrResponseResult!=null){
                    LogUtil.d(ocrResponseResult.getJsonRes());

                }
            }

            @Override
            public void onError(OCRError ocrError) {
                ToastUtil.showShortToast("识别失败，请重试");
            }
        });

    }

    private void onOptionPicker(final TextView textView, String[] strings) {
        OptionPicker picker = new OptionPicker(this, strings);
        picker.setCanceledOnTouchOutside(false);
        picker.setDividerRatio(WheelView.DividerConfig.FILL);
        picker.setShadowColor(Color.RED, 40);
        picker.setSelectedIndex(1);
        picker.setCycleDisable(true);
        picker.setTextSize(20);
        picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
            @Override
            public void onOptionPicked(int index, String item) {
                textView.setText(item);

            }
        });
        picker.show();
    }

    @Override
    public void onDateSet(TimePickerDialog timePickerDialog, long millseconds){
        Date userSelectDate= new Date(millseconds);
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String timeSelect = format.format(userSelectDate);
        textViewValidityTime.setText(timeSelect);


    }

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what){
            case 0:
                ToastUtil.showShortToast("网络出错，请检查网络");
                LoadingDailogUtil.cancelLoadingDailog();
                break;
            case 1:
                //也可以用这个接收
                if(!isQuery){
                    ReturnLoginInfo returnLoginInfo = new Gson().fromJson(msg.getData().getString("backInfo"),ReturnLoginInfo.class);
                    switch (returnLoginInfo.getCode()){
                        case "ok":
                            LoadingDailogUtil.cancelLoadingDailog();
                            UpOkDialog();
                            break;
                        case "003":
                            LoadingDailogUtil.cancelLoadingDailog();
                            checkInvalidDialog();
                            break;
                        default:
                            ToastUtil.showShortToast("服务器错误");
                            LoadingDailogUtil.cancelLoadingDailog();


                    }
                }else {
                    isQuery = false;
                    try {
                        JSONObject jsonObject = new JSONObject(msg.getData().getString("backInfo"));
                        switch (jsonObject.getString("code")){
                            case "ok":
                                JSONArray jsonArray = jsonObject.getJSONArray("items");
                                ArrayList<Case> serachCases =new ArrayList<>();
                                for(int i=0;i<jsonArray.length();i++){
                                    JSONObject object = jsonArray.getJSONObject(i);
                                    Case case1 = new Case();
                                    case1.setOffName(object.getString("offName"));
                                    case1.setOffCertificateNumber(object.getString("offCertificateNumber"));
                                    case1.setCaseId(object.getInt("caseId"));
                                    case1.setOffType(object.getString("offType"));
                                    case1.setOffTime(object.getString("offTime"));
                                    serachCases.add(case1);

                                }
                                if(serachCases.size()>0){
                                    adapter.update(serachCases);
                                    recyclerView.setVisibility(View.VISIBLE);
                                    textViewHistory.setVisibility(View.VISIBLE);
                                    v14.setVisibility(View.VISIBLE);
                                }else {
                                    recyclerView.setVisibility(View.GONE);
                                    textViewHistory.setVisibility(View.GONE);
                                    v14.setVisibility(View.GONE);
                                }

                                LoadingDailogUtil.cancelLoadingDailog();

                                break;
                            case "003":
                                LoadingDailogUtil.cancelLoadingDailog();
                                checkInvalidDialog();
                                break;
                            default:
                                ToastUtil.showShortToast("服务器错误");
                                LoadingDailogUtil.cancelLoadingDailog();


                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }

                break;
        }
    }

   private void addImage() {

            Matisse.from(UpActivity.this)
                    .choose(MimeType.ofAll())
                    .maxSelectable(9 - multiPictureView.getCount())
                    .thumbnailScale(0.85f)
                    .imageEngine(new ImageEngine() {
                        @Override
                        public void loadThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
                            Vincent.with(context)
                                    .load(uri)
                                    .placeholder(R.drawable.ic_placeholder_loading)
                                    .error(R.drawable.ic_placeholder_loading)
                                    .into(imageView);
                        }

                        @Override
                        public void loadGifThumbnail(Context context, int resize, Drawable placeholder, ImageView imageView, Uri uri) {
                            Vincent.with(context)
                                    .load(uri)
                                    .placeholder(R.drawable.ic_placeholder_loading)
                                    .error(R.drawable.ic_placeholder_loading)
                                    .into(imageView);
                        }

                        @Override
                        public void loadImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
                            Vincent.with(context)
                                    .load(uri)
                                    .placeholder(R.drawable.ic_placeholder_loading)
                                    .error(R.drawable.ic_placeholder_loading)
                                    .into(imageView);
                        }

                        @Override
                        public void loadGifImage(Context context, int resizeX, int resizeY, ImageView imageView, Uri uri) {
                            Vincent.with(context)
                                    .load(uri)
                                    .placeholder(R.drawable.ic_placeholder_loading)
                                    .error(R.drawable.ic_placeholder_loading)
                                    .into(imageView);
                        }

                        @Override
                        public boolean supportAnimatedGif() {
                            return false;
                        }
                    }).forResult(REQUEST_ADD_IMAGE);
        }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁移除所有消息，避免内存泄露
        mHandler.removeCallbacks(null);
    }

    private void checkInvalidDialog(){
        final NormalDialog dialog = new NormalDialog(UpActivity.this);
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
                Intent intent = new Intent(UpActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    private void UpOkDialog(){
        // 进入动画
        BounceTopEnter mBasIn = new BounceTopEnter();
        // 退出动画
        SlideBottomExit mBasOut = new SlideBottomExit();
        final NormalDialog dialog = new NormalDialog(this);
        dialog.content("上传成功~")//
                .btnNum(1)
                .btnText("确定")//
                .showAnim(mBasIn)//
                .dismissAnim(mBasOut)
                .show();

        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                finish();
                dialog.dismiss();
            }
        });


//        dialog.setOnBtnClickL(
//                new OnBtnClickL() {
//                    //取消
//                    @Override
//                    public void onBtnClick() {
//                        finish();
//                        dialog.dismiss();
//                    }
//                },
//                new OnBtnClickL() {
//                    //确定
//                    @Override
//                    public void onBtnClick() {
//
//                        dialog.dismiss();
//                        Intent intent = new Intent(UpActivity.this,PrinterSettingActivity.class);
//                        intent.putExtra("caseInfo",case1);
//
//
//                        startActivity(intent);
//                        finish();
//                    }
//                });

    }

    private void SelectDialogNoTitle() {
        final String[] stringItems = {"拍摄", "从相册中选择"};
        final ActionSheetDialog dialog = new ActionSheetDialog(UpActivity.this, stringItems, null);
        dialog.isTitleShow(false).show();

        dialog.setOnOperItemClickL(new OnOperItemClickL() {
            @Override
            public void onOperItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(position == 1){
                    addImage();
                }else if(position == 0){
                    GetPhotoUtil.takeOnCamera(UpActivity.this);
                }
                dialog.dismiss();
            }
        });
    }

    private void getDataBySearch(String query,String userId,String token){
        isQuery = true;
        OkHttpUtils okHttpUtils = new OkHttpUtils();
        Case case1 = new Case();
        case1.setOffName(query);
        case1.setPunishmentId(Integer.valueOf(userId));
        case1.setAppToken(token);

        String getDataJson = JsonUtils.conversionJsonString(case1);
        try {
            String publicEncryptJson = RSAUtils.publicEncrypt(getDataJson,RSAUtils.getPublicKey(RSAUtils.SERVER_PUBLIC_KEY));
            okHttpUtils.postInfo(Contract.SERVER_ADDRESS+"GetSearchCaseServlet",publicEncryptJson);
        }catch (Exception e){
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
        LogUtil.d("返回");
        if (photoView.getVisibility() == View.VISIBLE) {

            photoView.setVisibility(View.GONE);
            parent.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
        }
    }


}
