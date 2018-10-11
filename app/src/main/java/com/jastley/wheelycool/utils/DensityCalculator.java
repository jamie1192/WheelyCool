package com.jastley.wheelycool.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

public class DensityCalculator {

    public static int calculateDp(Context context, int dp) {

        float dip = (float) dp;
        Resources r = context.getResources();

        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );

        return (int) px;
    }
}
