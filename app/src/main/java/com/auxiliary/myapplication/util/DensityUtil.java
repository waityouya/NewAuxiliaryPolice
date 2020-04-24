package com.auxiliary.myapplication.util;

import android.content.Context;
import android.util.TypedValue;

public class DensityUtil {
    /**
     * dp to px
     *
     * @param context Context
     * @param dp      dp value
     * @return px value
     */
    public static int dp2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics());
    }

    /**
     * sp to px
     *
     * @param context Context
     * @param sp      sp value
     * @return px value
     */
    public static int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                context.getResources().getDisplayMetrics());
    }
}
