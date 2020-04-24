package com.auxiliary.myapplication.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.myapplication.R;
import com.auxiliary.myapplication.model.ReturnLoginInfo;
import com.auxiliary.myapplication.model.User;
import com.auxiliary.myapplication.util.Contract;
import com.auxiliary.myapplication.util.GlobalHandler;
import com.auxiliary.myapplication.util.JsonUtils;
import com.auxiliary.myapplication.util.LoadingDailogUtil;
import com.auxiliary.myapplication.util.MD5Util;
import com.auxiliary.myapplication.util.OkHttpUtils;
import com.auxiliary.myapplication.util.RSAUtils;
import com.auxiliary.myapplication.util.ToastUtil;
import com.flyco.animation.BounceEnter.BounceTopEnter;
import com.flyco.animation.SlideExit.SlideBottomExit;
import com.flyco.dialog.listener.OnBtnClickL;
import com.flyco.dialog.widget.NormalDialog;
import com.google.gson.Gson;

public class RegisterActivity extends AppCompatActivity implements GlobalHandler.HandleMsgListener{
    private TextView textViewTitle;
    private EditText editTextUserName;
    private EditText editTextPaaword;
    private EditText getEditTextPaawordAgain;
    private Button buttonRgister;
    private GlobalHandler mHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initWidget();
        setListen();


    }
    private void initWidget(){
        mHandler = GlobalHandler.getInstance();
        textViewTitle = findViewById(R.id.tv_title);
        editTextUserName = findViewById(R.id.et_user_name);
        editTextPaaword = findViewById(R.id.et_password);
        getEditTextPaawordAgain = findViewById(R.id.et_password_again);
        buttonRgister = findViewById(R.id.user_register);
        textViewTitle.setText("注册");

    }

    private void setListen(){
        mHandler.setHandleMsgListener(this);
        buttonRgister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = editTextUserName.getText().toString();
                String passWord = editTextPaaword.getText().toString();
                String passWordAgain = getEditTextPaawordAgain.getText().toString();
                if(userName.isEmpty() || passWord.isEmpty() || passWordAgain.isEmpty()){
                    ToastUtil.showShortToast("请填写完整");
                }else if(!passWord.equals(passWordAgain)){
                    ToastUtil.showShortToast("两次密码不一致");
                }else {
                    LoadingDailogUtil.showLoadingDialog(RegisterActivity.this,"注册中...");
                    String passWordMd5 = MD5Util.MD5(passWord);
                    OkHttpUtils okHttpUtils = new OkHttpUtils();
                    User user = new User(userName,passWordMd5);
                    String registerJson = JsonUtils.conversionJsonString(user);

                    try {
                        String publicEncryptJson = RSAUtils.publicEncrypt(registerJson,RSAUtils.getPublicKey(RSAUtils.SERVER_PUBLIC_KEY));
                        okHttpUtils.postInfo(Contract.SERVER_ADDRESS+"PoliceRegisterServlet",publicEncryptJson);


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

    private void successDialog(){
        final NormalDialog dialog = new NormalDialog(RegisterActivity.this);
        LoadingDailogUtil.cancelLoadingDailog();
        dialog.content("注册成功")
                .btnNum(1)
                .btnText("确定")
                .showAnim(new BounceTopEnter())
                .dismissAnim(new SlideBottomExit())
                .show();
        dialog.setCancelable(false);
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();
                finish();
            }
        });

    }
    private void failDialog(){
        final NormalDialog dialog = new NormalDialog(RegisterActivity.this);
        LoadingDailogUtil.cancelLoadingDailog();
        dialog.content("注册失败，用户名存在")
                .btnNum(1)
                .btnText("确定")
                .showAnim(new BounceTopEnter())
                .dismissAnim(new SlideBottomExit())
                .show();
        dialog.setCancelable(false);
        dialog.setOnBtnClickL(new OnBtnClickL() {
            @Override
            public void onBtnClick() {
                dialog.dismiss();

            }
        });

    }

    @Override
    public void handleMsg(Message msg) {
        switch (msg.what){
            case 0:
                ToastUtil.showShortToast("网络出错，请检查网络");
                break;
            case 1:
                ReturnLoginInfo returnLoginInfo = new Gson().fromJson(msg.getData().getString("backInfo"),ReturnLoginInfo.class);
                switch (returnLoginInfo.getCode()){
                    case "ok":
                        successDialog();
                        break;
                    case "006":
                        failDialog();
                        break;
                    default:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ToastUtil.showShortToast("服务器出错");
                                LoadingDailogUtil.cancelLoadingDailog();

                            }

                        });


                }
                break;
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //销毁移除所有消息，避免内存泄露
        mHandler.removeCallbacks(null);
    }

}
