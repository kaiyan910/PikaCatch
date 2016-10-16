package com.crookk.pikaplus.module.pokemon.view;

import com.crookk.pikaplus.core.ui.view.BaseView;
import com.crookk.pikaplus.local.model.db.Pokemon;

import java.util.List;

public interface PokemonView extends BaseView {
    void onPokemonQueried(List<Pokemon> pokemonList);
}
