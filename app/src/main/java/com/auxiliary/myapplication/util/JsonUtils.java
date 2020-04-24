package com.auxiliary.myapplication.util;
import com.google.gson.Gson;

public class JsonUtils {
    public static String conversionJsonString(Object object){
        Gson gson = new Gson();
        String json = "";
        try {
            json = gson.toJson(object);
        }catch (Exception e){
            e.printStackTrace();
        }
        return  json;

    }

    public static Object parsingJson(String json, Object object){
        Object returnObject = new Object();
        try {
            returnObject = new Gson().fromJson(json,object.getClass());
        }catch (Exception e){
            e.printStackTrace();
        }

        return returnObject;
    }
}
