package com.auxiliary.myapplication.util;

import android.content.Context;

import com.android.tu.loadingdialog.LoadingDailog;

public class LoadingDailogUtil {

    private static LoadingDailog dialog;

    public static void showLoadingDialog(Context context,String msg){
        LoadingDailog.Builder loadBuilder = new LoadingDailog.Builder(context)
                .setCancelable(false)
                .setMessage(msg)
                .setCancelOutside(false);
        dialog = loadBuilder.create();
        dialog.show();


    }

    public static void cancelLoadingDailog(){
        if(dialog !=null){
            dialog.dismiss();
        }

    }
}
