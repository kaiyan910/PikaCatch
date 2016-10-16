package com.crookk.pikaplus;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Constant {

    public static final String POKEMON_DRAWABLE_PREFIX = "pkm_";
    public static final String POKEMON_STRING_PREFIX = "pokemon_";
    public static final String HASH_SALT = "P1KaCat2h+";

    public static final int REFRESH_DISTANCE = 800;

    public static final String ACCOUNT_TYPE_PTC = "PTC";
    public static final int SLEEP_WORKER = 5000;

    public static final double DEFAULT_LATITUDE = 22.313558;
    public static final double DEFAULT_LONGITUDE = 114.261283;

    public static final String ALERT_SPAWN = "spawn_alert";

    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss dd/MM/yyy", Locale.getDefault());
    public static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
    public static final SimpleDateFormat DATE_FORMAT_WINDOW = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
    public static final SimpleDateFormat TIMER_FORMAT = new SimpleDateFormat("mm:ss", Locale.getDefault());

    public static final List<Integer> POKEMON_ALERT = new ArrayList<>();

    static {

        POKEMON_ALERT.add(3);
        POKEMON_ALERT.add(6);
        POKEMON_ALERT.add(9);
        POKEMON_ALERT.add(59);
        POKEMON_ALERT.add(65);
        POKEMON_ALERT.add(68);
        POKEMON_ALERT.add(87);
        POKEMON_ALERT.add(91);
        POKEMON_ALERT.add(94);
        POKEMON_ALERT.add(102);
        POKEMON_ALERT.add(103);
        POKEMON_ALERT.add(113);
        POKEMON_ALERT.add(130);
        POKEMON_ALERT.add(131);
        POKEMON_ALERT.add(143);
        POKEMON_ALERT.add(149);
    }
}
