package com.crookk.pikaplus.module.pokemon.presenter;

import com.crookk.pikaplus.core.presenter.BasePresenter;
import com.crookk.pikaplus.local.bean.DatabaseManager;
import com.crookk.pikaplus.local.model.db.Pokemon;
import com.crookk.pikaplus.module.pokemon.view.PokemonView;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean
public class FilterPresenter implements BasePresenter<PokemonView> {

    @Bean
    DatabaseManager mDatabaseManager;

    PokemonView mView;

    @Override
    public void attach(PokemonView view) {
        this.mView = view;
    }

    @Background
    public void query() {

        if (mView == null) throw new RuntimeException("no attached view");

        mView.onPokemonQueried(mDatabaseManager.queryPokemonList());
    }

    @Background
    public void updatePokemon(Pokemon pokemon) {

        mDatabaseManager.updatePokemon(pokemon);
    }

}
