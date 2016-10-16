package com.crookk.pikaplus.core.utils;

import android.content.Context;
import android.graphics.Typeface;

public class FontUtils {

    public static Typeface obtainTypeface(Context context) {
        return Typeface.createFromAsset(context.getAssets(), "fonts/noto-light.otf");
    }
}
