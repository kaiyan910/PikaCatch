package com.crookk.pikaplus;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.crookk.pikaplus.local.model.db.Account;
import com.crookk.pikaplus.local.model.db.Pokemon;
import com.crookk.pikaplus.local.model.db.Spawn;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME = "pikacatchplus.db";
    private static final int DATABASE_VERSION = 2;

    private Dao<Account, Long> accountDao = null;
    private RuntimeExceptionDao<Account, Long> accountRuntimeDao = null;
    private Dao<Pokemon, Integer> pokemonDao = null;
    private RuntimeExceptionDao<Pokemon, Integer> pokemonRuntimeDao = null;
    private Dao<Spawn, String> spawnDao = null;
    private RuntimeExceptionDao<Spawn, String> spawnRuntimeDao = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, Account.class);
            TableUtils.createTable(connectionSource, Pokemon.class);
            TableUtils.createTable(connectionSource, Spawn.class);
            setupGenerationOnePokemon();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {

            switch (oldVersion) {
                case 1:
                    TableUtils.dropTable(connectionSource, Account.class, true);
                    TableUtils.createTable(connectionSource, Account.class);
                    break;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Dao<Account, Long> getEncounterDao() throws SQLException {
        if (accountDao == null) {
            accountDao = getDao(Account.class);
        }
        return accountDao;
    }

    public Dao<Pokemon, Integer> getPokemonDao() throws SQLException {
        if (pokemonDao == null) {
            pokemonDao = getDao(Pokemon.class);
        }
        return pokemonDao;
    }

    public Dao<Spawn, String> getSpawnDao() throws SQLException {
        if (spawnDao == null) {
            spawnDao = getDao(Spawn.class);
        }
        return spawnDao;
    }

    @Override
    public void close() {
        super.close();
        accountDao = null;
        pokemonDao = null;
        spawnDao = null;
        accountRuntimeDao = null;
        pokemonRuntimeDao = null;
        spawnRuntimeDao = null;
    }

    private void setupGenerationOnePokemon() throws SQLException {

        List<Pokemon> pokemonList = new ArrayList<>();
        for (int i = 1; i <= 151; i++) {
            pokemonList.add(new Pokemon(i));
        }
        getPokemonDao().create(pokemonList);
    }
}