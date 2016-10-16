package com.crookk.pikaplus.module.pokemon.ui.custom;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.model.db.Pokemon;
import com.crookk.pikaplus.module.pokemon.listener.OnFilterCheckChangeListener;
import com.crookk.pikaplus.module.pokemon.presenter.FilterPresenter;
import com.crookk.pikaplus.module.pokemon.ui.adapter.FilterAdapter;
import com.crookk.pikaplus.module.pokemon.view.PokemonView;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EViewGroup;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.util.ArrayList;
import java.util.List;

@EViewGroup(R.layout.view_filter)
public class FilterView extends LinearLayout implements PokemonView,
        OnFilterCheckChangeListener {

    @ViewById(R.id.list)
    RecyclerView mViewList;
    @ViewById(R.id.title)
    TextView mViewText;

    @Bean
    FilterPresenter mPresenter;
    @Bean
    FilterAdapter mAdapter;

    protected List<Pokemon> pokemonList = new ArrayList<>();
    protected List<String> list = new ArrayList<>();

    public FilterView(Context context) {
        super(context);
    }

    public FilterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @AfterViews
    void afterViews() {

        mPresenter.attach(this);
        mPresenter.query();

        mViewList.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    @UiThread
    @Override
    public void onPokemonQueried(List<Pokemon> pokemonList) {

        this.pokemonList = pokemonList;

        createList(pokemonList);

        mAdapter.setData(this.pokemonList);
        mAdapter.setOnFilterCheckChangeListener(this);

        mViewList.setAdapter(mAdapter);
    }

    @Override
    public void onFilterChange(Pokemon pokemon, boolean checked) {

        /*Pokemon db = pokemonList.get(pokemonList.indexOf(pokemon));

        if (db.isIgnore() != checked) {

            db.setIgnore(checked);
            mPresenter.updatePokemon(db);

            if (checked) {
                list.add(String.valueOf(db.getId()));
            } else {
                list.remove(String.valueOf(db.getId()));
            }

            mAdapter.notifyDataSetChanged();
        }*/
    }

    protected void createList(List<Pokemon> pokemonList) {
        for (Pokemon pokemon : pokemonList) {
            if (pokemon.isIgnore()) {
                list.add(String.valueOf(pokemon.getId()));
            }
        }
    }

    public List<String> getList() {
        return list;
    }
}
