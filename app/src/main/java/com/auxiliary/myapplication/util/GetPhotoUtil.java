package com.auxiliary.myapplication.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import java.util.ArrayList;
import java.util.List;


public class GetPhotoUtil {

    public static final int TAKEONCAMERA = 1002;

    private  static final String[] PROJECTION = {MediaStore.Images.Media._ID,
            MediaStore.Images.Media.DATE_ADDED,
            MediaStore.Images.Media.DATA};

    private static long startTemp = Long.MAX_VALUE;
    private static int state = 0;
    private static Uri mUri = Uri.parse("content://media/external/images/media");

    /**
     * 得到手机中所有的照片（async）
     */
    public static List<String> getAllPhoto(Context context){

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION,
                            null, null,
                            MediaStore.Images.Media.DATE_ADDED +  " DESC");
            if (cursor != null){
                List<String> photoList = new ArrayList<>(cursor.getCount());
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    photoList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    cursor.moveToNext();
                }
                return photoList;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 调手机相机拍多张照片
     */
    public static void takeOnCamera(Activity activity) {
        //打开相机之前，记录时间1
        startTemp = System.currentTimeMillis();
        Intent intent = new Intent();
        //此处之所以诸多try catch，是因为各大厂商手机不确定哪个方法
        try {
            intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA);
            activity.startActivityForResult(intent, TAKEONCAMERA);
        } catch (Exception e) {
            try {
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE_SECURE);
                activity.startActivityForResult(intent, TAKEONCAMERA);
            } catch (Exception e1) {
                try {
                    intent.setAction(MediaStore.INTENT_ACTION_STILL_IMAGE_CAMERA_SECURE);
                    activity.startActivityForResult(intent, TAKEONCAMERA);
                } catch (Exception ell) {
                    ToastUtil.showShortToast("打开摄像机失败，请从相册选择照片");
                }
            }
        }
    }


    /**
     * 获取拍摄的照片，在调用takeOnCamera之后返回界面是调用该方法。
     */
    public static List<Uri> getTakePhoto(Context context){

        Cursor cursor = null;
        try {
            cursor = context.getContentResolver()
                    .query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, PROJECTION,
                            null, null,
                            MediaStore.Images.Media.DATE_ADDED +  " DESC LIMIT 0,30");
            if (cursor != null){
               // List<String> photoList = new ArrayList<>(cursor.getCount());
                List<Uri> photoUri = new ArrayList<>();
                cursor.moveToFirst();
                while (!cursor.isAfterLast()){
                    long createTemp = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_ADDED));
                    if (isAfterStart(startTemp, createTemp)){
                        int ringtoneID = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
                         photoUri.add(Uri.withAppendedPath(mUri, "" + ringtoneID)) ;
                       // photoList.add(cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA)));
                    }
                    cursor.moveToNext();
                }
                return photoUri;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (cursor != null){
                cursor.close();
            }
        }
        return null;
    }

    /**
     * 判断照片创建时间是不是在开始之后，
     * 时间戳可能是10位或者是13位，要先统一位数
     *
     */
    private static boolean isAfterStart(long startTemp, long createTemp){
        if (state == 0){
            int startTempLength = String.valueOf(startTemp).length();
            int createTempLength = String.valueOf(createTemp).length();
            if (startTempLength == createTempLength){
                state = 1;
            }else if(startTempLength == 10 && createTempLength == 13){
                state = 2;
            }else if(startTempLength == 13 && createTempLength == 10){
                state = 3;
            }
        }
        if (state == 2){
            startTemp = startTemp * 1000;
        }else if(state == 3){
            startTemp = startTemp / 1000;
        }
        return createTemp - startTemp >= 0;
    }

}

