package com.crookk.pikaplus.local.utils;

import android.content.Context;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.core.utils.LogUtils;

public class ResourcesLoader {

    public static int getDrawableResources(Context context, int id) {

        String resourceName = Constant.POKEMON_DRAWABLE_PREFIX + id;
        return context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
    }

    public static String getStringResources(Context context, int id) {

        String resourceName = Constant.POKEMON_STRING_PREFIX + id;
        return context.getResources().getString(context.getResources().getIdentifier(resourceName, "string", context.getPackageName()));
    }

}
