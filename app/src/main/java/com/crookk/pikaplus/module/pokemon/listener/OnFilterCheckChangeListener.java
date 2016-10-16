package com.crookk.pikaplus.module.pokemon.listener;

import com.crookk.pikaplus.local.model.db.Pokemon;

public interface OnFilterCheckChangeListener {
    void onFilterChange(Pokemon pokemon, boolean checked);
}
