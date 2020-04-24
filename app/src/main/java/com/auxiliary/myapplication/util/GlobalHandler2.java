package com.auxiliary.myapplication.util;

import android.os.Handler;

import android.os.Message;

public class GlobalHandler2 extends Handler {
    private HandleMsgListener listener;
    private String Tag = GlobalHandler.class.getSimpleName();

    //使用单例模式创建GlobalHandler
    private GlobalHandler2(){
        LogUtil.e(Tag,"GlobalHandler创建");
    }

    private static class Holder{
        private static final GlobalHandler2 HANDLER = new GlobalHandler2();
    }

    public static GlobalHandler2 getInstance(){
        return Holder.HANDLER;
    }

    @Override
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        if (getHandleMsgListener() != null){
            getHandleMsgListener().handleMsg(msg);
        }else {
            LogUtil.e(Tag,"请传入HandleMsgListener对象");
        }
    }

    public interface HandleMsgListener{
        void handleMsg(Message msg);
    }

    public void setHandleMsgListener(HandleMsgListener listener){
        this.listener = listener;
    }

    public HandleMsgListener getHandleMsgListener(){
        return listener;
    }


}
