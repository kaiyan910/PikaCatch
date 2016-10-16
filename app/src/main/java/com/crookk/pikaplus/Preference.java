package com.crookk.pikaplus;

import org.androidannotations.annotations.sharedpreferences.DefaultBoolean;
import org.androidannotations.annotations.sharedpreferences.DefaultLong;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(SharedPref.Scope.APPLICATION_DEFAULT)
public interface Preference {

    @DefaultLong(0L)
    long lastRequestTime();
    @DefaultBoolean(false)
    boolean background();
}
