package com.auxiliary.myapplication.util;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class OkHttpUtils {
    private String backInfo;

    private GlobalHandler mHandler = GlobalHandler.getInstance();
    private GlobalHandler1 globalHandler1 = GlobalHandler1.getInstance();
    private GlobalHandler2 globalHandler2 = GlobalHandler2.getInstance();

    public String getInfo(String url) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d("info:", "filed");
                e.printStackTrace();

            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    LogUtil.d("info:", "success");
                    LogUtil.d("code:", "response.code()==" + response.code());

                    backInfo= response.body().string();
//                    请勿多次调用 response.body().string() 会出现： java.lang.IllegalStateException: closed错
                    LogUtil.d("ResponseBody:", "response.body().string()==" + backInfo);
                }
            }
        });

        return backInfo;
    }

    public String postInfo(String url,String json){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d("info:", "filed");
                Message msg = new Message();
                msg.what = 0;
                mHandler.sendMessage(msg);
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    LogUtil.d("info:", "success");
                    LogUtil.d("code:", "response.code()==" + response.code());
                    backInfo= response.body().string();
//                    请勿多次调用 response.body().string() 会出现： java.lang.IllegalStateException: closed错
                    LogUtil.d("ResponseBody:", "response.body().string()==" + backInfo);
                    Message msg = new Message();
                     msg.what = 1;
                    Bundle build = new Bundle();
                    build.putString("backInfo",backInfo);
                     msg.setData(build);
                    mHandler.sendMessage(msg);
                }
            }
        });
        return backInfo;
    }

    public String postInfo1(String url,String json){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d("info:", "filed");
                Message msg = new Message();
                msg.what = 0;
                globalHandler1.sendMessage(msg);
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    LogUtil.d("info:", "success");
                    LogUtil.d("code:", "response.code()==" + response.code());
                    backInfo= response.body().string();
//                    请勿多次调用 response.body().string() 会出现： java.lang.IllegalStateException: closed错
                    LogUtil.d("ResponseBody:", "response.body().string()==" + backInfo);
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle build = new Bundle();
                    build.putString("backInfo",backInfo);
                    msg.setData(build);
                    globalHandler1.sendMessage(msg);
                }
            }
        });
        return backInfo;
    }

    public String postInfo2(String url,String json){
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient对象。

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");//数据类型为json格式，

        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LogUtil.d("info:", "filed");
                Message msg = new Message();
                msg.what = 0;
                globalHandler2.sendMessage(msg);
                e.printStackTrace();
            }
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful()) {//回调的方法执行在子线程。
                    LogUtil.d("info:", "success");
                    LogUtil.d("code:", "response.code()==" + response.code());
                    backInfo= response.body().string();
//                    请勿多次调用 response.body().string() 会出现： java.lang.IllegalStateException: closed错
                    LogUtil.d("ResponseBody:", "response.body().string()==" + backInfo);
                    Message msg = new Message();
                    msg.what = 1;
                    Bundle build = new Bundle();
                    build.putString("backInfo",backInfo);
                    msg.setData(build);
                    globalHandler2.sendMessage(msg);
                }
            }
        });
        return backInfo;
    }

}
