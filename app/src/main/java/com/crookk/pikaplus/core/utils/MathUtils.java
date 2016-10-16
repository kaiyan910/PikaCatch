package com.crookk.pikaplus.core.utils;

import android.location.Location;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class MathUtils {

    public static Double toDecimal(Double value) {

        DecimalFormat df = new DecimalFormat("#.000000");
        df.setRoundingMode(RoundingMode.DOWN);

        return Double.parseDouble(df.format(value));
    }


    public static Double to4Decimal(Double value) {

        DecimalFormat df = new DecimalFormat("#.0000");
        df.setRoundingMode(RoundingMode.DOWN);

        return Double.parseDouble(df.format(value));
    }

    public static float distanceTo(Double lat, Double lng, Double lat2, Double lng2) {

        float[] result = new float[3];
        Location.distanceBetween(lat, lng, lat2, lng2, result);

        return result[0];
    }
}
