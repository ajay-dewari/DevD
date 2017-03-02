package com.ajaysinghdewari.devd.utils;

import android.content.Context;
import android.util.DisplayMetrics;

/**
 * Created by Ajay on 25-02-2017.
 */

public class Utility {

    public static final int DATABASE_VERSION_CODE = 1;
    public static final String DATABASE_VERSION_NAME = "1.0";
    public static int calculateNoOfColumns(Context context, int columnWidth) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / columnWidth);
        return noOfColumns;
    }
}
