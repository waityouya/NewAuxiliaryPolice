package com.auxiliary.myapplication.util;


import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;

import com.example.myapplication.R;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;


public class KeyboardUtil {

    private Activity mActivity;
    private KeyboardView mKeyboardView;
    private EditText mEdit;
    /**
     * 省份简称键盘
     */
    private Keyboard provinceKeyboard;
    /**
     * 数字与大写字母键盘
     */
    private Keyboard numberKeyboard;
    private Button mButtonUp;
   // View viewContainer;
    public KeyboardUtil(Button buttonUp,Activity activity, EditText edit) {
        mActivity = activity;
        mEdit = edit;
//        if (viewContainer == null) {
//            viewContainer = mActivity.getLayoutInflater().inflate(R.layout.keyboardview_layout, null);
//        } else {
//            if (viewContainer.getParent() != null)
//                return;
//        }
        this.mButtonUp = buttonUp;
        provinceKeyboard = new Keyboard(activity, R.xml.province_abbreviation);
        numberKeyboard = new Keyboard(activity, R.xml.number_or_letters);
        mKeyboardView = (KeyboardView) activity.findViewById(R.id.keyboard_view1);
        mKeyboardView.setKeyboard(provinceKeyboard);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setOnKeyboardActionListener(listener);
    }

    private KeyboardView.OnKeyboardActionListener listener = new KeyboardView.OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {

        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            Editable editable = mEdit.getText();
            int start = mEdit.getSelectionStart();
            //判定是否是中文的正则表达式 [\\u4e00-\\u9fa5]判断一个中文 [\\u4e00-\\u9fa5]+多个中文
            String reg = "[\\u4e00-\\u9fa5]";
            if (primaryCode == -1) {// 省份简称与数字键盘切换
                if (mEdit.getText().toString().matches(reg)) {
                    changeKeyboard(true);
                }
            } else if (primaryCode == -3) {
                if (editable != null && editable.length() > 0) {
                    //没有输入内容时软键盘重置为省份简称软键盘
                    if (editable.length() == 1) {
                        changeKeyboard(false);
                    }
                    if (start > 0) {
                        editable.delete(start - 1, start);
                    }
                }
            }else if(primaryCode == -2){
                hideKeyboard();
                openInputMethod();
            }else  if (primaryCode == -4){
                hideKeyboard();
            }
            else {
                editable.insert(start, Character.toString((char) primaryCode));
                // 判断第一个字符是否是中文,是，则自动切换到数字软键盘
//                if (mEdit.getText().toString().matches(reg)) {
//                    changeKeyboard(true);
//                }
            }
        }
    };

    /**
     * 指定切换软键盘 isNumber false表示要切换为省份简称软键盘 true表示要切换为数字软键盘
     */
    private void changeKeyboard(boolean isNumber) {
        if (isNumber) {
            mKeyboardView.setKeyboard(numberKeyboard);
        } else {
            mKeyboardView.setKeyboard(provinceKeyboard);
        }
    }

    /**
     * 软键盘展示状态
     */
//    public boolean isShow() {
//        return mKeyboardView.getVisibility() == View.VISIBLE;
//    }

    /**
     * 软键盘展示
     */
    public void showKeyboard() {
        LogUtil.d("软键盘开启");
//
//        FrameLayout frameLayout = (FrameLayout) mActivity.getWindow().getDecorView();
//
//
//        FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
//        lp.gravity = Gravity.BOTTOM;
//        viewContainer.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.down_to_up));
//        frameLayout.addView(viewContainer, lp);
        //viewContainer.setVisibility(View.GONE);
        mKeyboardView.setAnimation(AnimationUtils.loadAnimation(mActivity, R.anim.down_to_up));

        if(mKeyboardView.getVisibility() == View.GONE){
            mKeyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 软键盘隐藏
     */
    public void hideKeyboard() {
        LogUtil.d("软键盘隐藏");
//        if (viewContainer != null && viewContainer.getParent() != null) {
//            ((ViewGroup) viewContainer.getParent()).removeView(viewContainer);
//        }
        if(mButtonUp.getVisibility() == View.GONE){
            mButtonUp.setVisibility(View.VISIBLE);
        }
        if(mKeyboardView.getVisibility() == View.VISIBLE){
            mKeyboardView.setVisibility(View.GONE);
        }
    }

    /**
     * 禁掉系统软键盘
     */
    public void hideSoftInputMethod() {
        LogUtil.d("系统键盘隐藏");
        mActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        String methodName = null;
        if (currentVersion >= 16) {
            // 4.2
            methodName = "setShowSoftInputOnFocus";
        } else if (currentVersion >= 14) {
            // 4.0
            methodName = "setSoftInputShownOnFocus";
        }
        if (methodName == null) {
            mEdit.setInputType(InputType.TYPE_NULL);
        } else {
            Class<EditText> cls = EditText.class;
            Method setShowSoftInputOnFocus;
            try {
                setShowSoftInputOnFocus = cls.getMethod(methodName, boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(mEdit, false);
            } catch (NoSuchMethodException e) {
                mEdit.setInputType(InputType.TYPE_NULL);
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    private void openInputMethod(){
        LogUtil.d("系统键盘开启");
        Timer timer = new Timer();

        timer.schedule(new TimerTask() {

            public void run() {

                InputMethodManager inputManager = (InputMethodManager) mEdit

                        .getContext().getSystemService(

                                Context.INPUT_METHOD_SERVICE);

                inputManager.showSoftInput(mEdit, 0);

            }

        }, 200);

    }

}

