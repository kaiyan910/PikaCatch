package com.crookk.pikaplus.module.pokemon.ui.holder;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.bean.DatabaseManager;
import com.crookk.pikaplus.local.model.db.Pokemon;
import com.crookk.pikaplus.local.utils.ResourcesLoader;
import com.crookk.pikaplus.module.pokemon.listener.OnFilterCheckChangeListener;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.CheckedChange;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.ColorRes;

@EViewGroup(R.layout.holder_filter)
public class FilterHolder extends RelativeLayout implements View.OnClickListener {

    @ViewById(R.id.icon)
    ImageView mViewIcon;
    @ViewById(R.id.name)
    TextView mViewName;
    @ViewById(R.id.checkbox)
    AppCompatCheckBox mViewCheckbox;
    @ViewById(R.id.checkbox2)
    AppCompatCheckBox mViewCheckbox2;
    @ViewById(R.id.background)
    RelativeLayout mViewBackground;

    @ColorRes(R.color.row_odd)
    int mColorRowOdd;
    @ColorRes(R.color.row_even)
    int mColorRowEven;

    @Bean
    DatabaseManager mDatabaseManager;

    private Pokemon mPokemon;
    private OnFilterCheckChangeListener onFilterCheckChangeListener;

    public FilterHolder(Context context) {
        super(context);
    }

    public void bind(final Pokemon pokemon, OnFilterCheckChangeListener listener) {

        setOnClickListener(this);

        this.mPokemon = pokemon;
        this.onFilterCheckChangeListener = listener;

        mViewIcon.setImageResource(ResourcesLoader.getDrawableResources(getContext(), pokemon.getId()));
        mViewName.setText(pokemon.getName(getContext()));

        mViewCheckbox.setChecked(pokemon.isIgnore());
        mViewCheckbox2.setChecked(pokemon.isTracking());

        if (pokemon.getId() % 2 != 0) {
            mViewBackground.setBackgroundColor(mColorRowOdd);
        } else {
            mViewBackground.setBackgroundColor(mColorRowEven);
        }
    }

    @CheckedChange(R.id.checkbox)
    void checkbox(boolean b) {

        mPokemon.setIgnore(b);
        mDatabaseManager.updatePokemon(mPokemon);
    }

    @CheckedChange(R.id.checkbox2)
    void checkbox2(boolean b) {

        mPokemon.setTracking(b);
        mDatabaseManager.updatePokemon(mPokemon);
    }

    @Override
    public void onClick(View view) {
        mViewCheckbox.setChecked(!mViewCheckbox.isChecked());
    }
}
