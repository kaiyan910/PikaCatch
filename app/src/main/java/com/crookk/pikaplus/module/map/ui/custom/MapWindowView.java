package com.crookk.pikaplus.module.map.ui.custom;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.bean.OttoBus;
import com.crookk.pikaplus.module.map.event.PokemonExpiredEvent;
import com.crookk.pikaplus.module.map.model.PokemonMarker;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EViewGroup(R.layout.view_winodws)
public class MapWindowView extends LinearLayout {

    @ViewById(R.id.name)
    TextView mViewName;
    @ViewById(R.id.expire_time)
    TextView mViewExpireTime;
    @ViewById(R.id.iv)
    TextView mViewIV;
    @ViewById(R.id.attack)
    TextView mViewAttack;
    @ViewById(R.id.defense)
    TextView mViewDefense;
    @ViewById(R.id.stamina)
    TextView mViewStamina;
    @ViewById(R.id.move_1)
    TextView mViewMove1;
    @ViewById(R.id.move_2)
    TextView mViewMove2;
    @ViewById(R.id.extra)
    LinearLayout mViewExtra;

    @Bean
    OttoBus mOttoBus;

    @StringRes(R.string.map_window_iv)
    String mStringIV;

    private PokemonMarker mMarker;

    public MapWindowView(Context context) {
        super(context);
    }

    public void bind(PokemonMarker marker) {

        mMarker = marker;

        String name = mMarker.getSpawn().getPokemon().getName(getContext());
        String timeLeft = mMarker.getSpawn().getTimeLeft(getContext());

        mViewName.setText(String.format("%s (%s)", name, timeLeft));
        mViewExpireTime.setText(mMarker.getSpawn().getExpireTimeText());

        if(mMarker.getSpawn().getMove1() != null && mMarker.getSpawn().getMove2() != null) {

            mViewAttack.setText(String.valueOf(mMarker.getSpawn().getAttack()));
            mViewDefense.setText(String.valueOf(mMarker.getSpawn().getDefense()));
            mViewStamina.setText(String.valueOf(mMarker.getSpawn().getStamina()));

            mViewIV.setText(String.format(mStringIV, mMarker.getSpawn().calculateIV()));
            mViewMove1.setText(mMarker.getSpawn().getMove1());
            mViewMove2.setText(mMarker.getSpawn().getMove2());

        } else {

            mViewExtra.setVisibility(GONE);
        }

        updateTimer();
    }

    @UiThread(delay = 1000)
    void updateTimer() {

        if (mMarker.getMarker().isInfoWindowShown()) {
            mMarker.getMarker().hideInfoWindow();
            mMarker.getMarker().showInfoWindow();
        }
    }
}
