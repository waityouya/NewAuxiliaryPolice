package com.auxiliary.myapplication.ui.activitys;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;



import com.example.myapplication.R;
import com.auxiliary.myapplication.model.ReturnLoginInfo;
import com.auxiliary.myapplication.model.User;
import com.auxiliary.myapplication.util.Contract;
import com.auxiliary.myapplication.util.GlobalHandler;
import com.auxiliary.myapplication.util.JsonUtils;
import com.auxiliary.myapplication.util.LoadingDailogUtil;
import com.auxiliary.myapplication.util.LogUtil;
import com.auxiliary.myapplication.util.MD5Util;

import com.auxiliary.myapplication.util.OkHttpUtils;
import com.auxiliary.myapplication.util.RSAUtils;
import com.auxiliary.myapplication.util.ToastUtil;
import com.google.gson.Gson;



public class MainActivity extends AppCompatActivity implements GlobalHandler.HandleMsgListener{
    private EditText mUserNameEditText;
    private EditText mPassWordEditText;
    private Button mLoginButton;
    private Button mRegisterButton;
    private GlobalHandler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //判断token是否存在
        SharedPreferences sp = getSharedPreferences("login", 0);
        if(sp.getString("token",null) != null && sp.getBoolean("isTokenValid",false)){
            Intent intent=new Intent(MainActivity.this,UserActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        }
        setContentView(R.layout.activity_main);
        initWidget();
        setListen();
    }

    private void initWidget(){
        mHandler = GlobalHandler.getInstance();
        mHandler.setHandleMsgListener(this);
        mUserNameEditText = findViewById(R.id.et_username);
        mPassWordEditText = findViewById(R.id.et_password);
        mLoginButton = findViewById(R.id.login);
        mRegisterButton = findViewById(R.id.register);

    }

    private void setListen(){
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userName = mUserNameEditText.getText().toString();
                String passWord = mPassWordEditText.getText().toString();
                if(userName.isEmpty() || passWord.isEmpty()){
                    ToastUtil.showShortToast("请填写完整");
                }else {
                    LoadingDailogUtil.showLoadingDialog(MainActivity.this,"验证中...");
                    String passWordMd5 = MD5Util.MD5(passWord);
                    OkHttpUtils okHttpUtils = new OkHttpUtils();
                    User user = new User(userName,passWordMd5);
                    String loginJson = JsonUtils.conversionJsonString(user);

                    try {
                        String publicEncryptJson = RSAUtils.publicEncrypt(loginJson,RSAUtils.getPublicKey(RSAUtils.SERVER_PUBLIC_KEY));
                        okHttpUtils.postInfo(Contract.SERVER_ADDRESS+"AuxiliaryLogin",publicEncryptJson);

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



//
                }
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,RegisterActivity.class);
                startActivity(intent);

            }
        });

    }
    @Override
    public void handleMsg(Message msg) {
        switch (msg.what){
            case 0:
                ToastUtil.showShortToast("网络出错，请检查网络");
                LoadingDailogUtil.cancelLoadingDailog();
                break;
            case 1:
                ReturnLoginInfo returnLoginInfo = new Gson().fromJson(msg.getData().getString("backInfo"),ReturnLoginInfo.class);
                switch (returnLoginInfo.getCode()){
                    case "ok":
                        Intent intent = new Intent(MainActivity.this,UserActivity.class);
                        //Log.i("loginsucess",String.valueOf(returnLoginInfo.getData().getUid()));
                        intent.putExtra("userId",returnLoginInfo.getData().getUserId());
                        //intent.putExtra("token")
                        //token保存到本地
                        SharedPreferences sp = getSharedPreferences("login", 0);
                        SharedPreferences.Editor editor = sp.edit();
                        editor.putString("token",returnLoginInfo.getData().getAppToken());
                        editor.putString("userId",returnLoginInfo.getData().getUserId());
                        editor.putBoolean("isTokenValid",true);
                        editor.commit();
                        LoadingDailogUtil.cancelLoadingDailog();
                        startActivity(intent);
                        finish();
                        break;
                    default:
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                LogUtil.i("111");
                                ToastUtil.showShortToast("账号或密码错误");
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
