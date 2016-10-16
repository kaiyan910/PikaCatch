package com.crookk.pikaplus.module.pokemon.ui.activity;

import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.ui.activity.BaseActivity;
import com.crookk.pikaplus.module.pokemon.ui.custom.FilterView;

import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.res.StringRes;

@EActivity(R.layout.activity_pokemon)
public class PokemonActivity extends BaseActivity {

    @ViewById(R.id.filter_view)
    FilterView mViewFilter;

    @StringRes(R.string.pokemon_title)
    String mStringTitle;

    @Override
    protected void afterViews() {
        super.afterViews();
        setupBackNavigation();
    }

    @Override
    protected String getActivityTitle() {
        return mStringTitle;
    }
}
