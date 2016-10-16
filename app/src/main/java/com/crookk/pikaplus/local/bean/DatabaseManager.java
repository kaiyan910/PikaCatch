package com.crookk.pikaplus.local.bean;

import com.crookk.pikaplus.DatabaseHelper;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.model.api.SpawnResult;
import com.crookk.pikaplus.local.model.db.Account;
import com.crookk.pikaplus.local.model.db.Pokemon;
import com.crookk.pikaplus.local.model.db.Spawn;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RawRowMapper;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.stmt.Where;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EBean;
import org.androidannotations.ormlite.annotations.OrmLiteDao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.http.Query;

@EBean(scope = EBean.Scope.Singleton)
public class DatabaseManager {

    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Spawn, String> spawnDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Account, Long> accountDao;
    @OrmLiteDao(helper = DatabaseHelper.class)
    Dao<Pokemon, Integer> pokemonDao;

    public List<Integer> getTrackingList() {

        try {

            return pokemonDao.queryRaw("SELECT id FROM pokemon WHERE tracking = 1", new RawRowMapper<Integer>() {
                @Override
                public Integer mapRow(String[] columnNames, String[] resultColumns) throws SQLException {
                    return Integer.parseInt(resultColumns[0]);
                }
            }).getResults();

        } catch (SQLException e) {

            LogUtils.e(this, e);
        }

        return new ArrayList<>();
    }

    public List<Pokemon> queryPokemonList() {

        try {

            return pokemonDao.queryForAll();

        } catch (SQLException e) {

            LogUtils.e(this, e);
        }

        return new ArrayList<>();
    }

    public List<Pokemon> queryBlacklistPokemonList() {

        try {

            return pokemonDao.queryBuilder().where().eq(Pokemon.COLUMN_IGNORE, true).query();

        } catch (SQLException e) {

            LogUtils.e(this, e);
        }

        return new ArrayList<>();
    }

    public List<Account> queryAccounts() {

        try {

            return accountDao.queryForAll();

        } catch (SQLException e) {

            LogUtils.e(this, e);
        }

        return new ArrayList<>();
    }

    public void updateAccount(Account account) {

        try {

            accountDao.update(account);

        } catch (SQLException e) {

            LogUtils.e(this, e);
        }
    }

    public void deleteAccount(Account account) {

        try {

            accountDao.delete(account);

        } catch (SQLException e) {

            LogUtils.e(this, e);
        }
    }

    public void updatePokemon(Pokemon pokemon) {

        try {

            pokemonDao.update(pokemon);

        } catch (SQLException e) {

            LogUtils.e(this, e);
        }
    }

    public void createAccount(Account account) {

        try {

            accountDao.create(account);

        } catch (SQLException e) {

            LogUtils.e(this, e);
        }
    }

    public List<Spawn> querySpawns() {

        try {

            Date date = new Date();

            QueryBuilder<Pokemon, Integer> pokemonBuilder = pokemonDao.queryBuilder();
            pokemonBuilder.where().eq(Pokemon.COLUMN_IGNORE, false);

            QueryBuilder<Spawn, String> spawnBuilder = spawnDao.queryBuilder();
            spawnBuilder.where().gt(Spawn.COLUMN_EXPIRE_TIME, date);

            return spawnBuilder.join(pokemonBuilder).query();

        } catch (SQLException e) {

            LogUtils.e(this, e);
        }

        return new ArrayList<>();
    }

    public void insert(List<Spawn> spawnList) {

        try {

            for (Spawn spawn : spawnList) spawnDao.createIfNotExists(spawn);

        } catch (SQLException e) {
            LogUtils.e(this, e);
        }
    }

    public void deleteExpired() {

        try {

            DeleteBuilder<Spawn, String> builder = spawnDao.deleteBuilder();

            builder.where()
                    .le(Spawn.COLUMN_EXPIRE_TIME, new Date());

            builder.delete();

        } catch (SQLException e) {
            LogUtils.e(this, e);
        }
    }

    public void update(Spawn spawn) {

        try {

            spawnDao.update(spawn);

        } catch (SQLException e) {
            LogUtils.e(this, e);
        }
    }

    public List<Account> getAccountList() {

        try {

            return accountDao.queryForAll();

        } catch (SQLException e) {
            LogUtils.e(this, e);
        }

        return new ArrayList<>();
    }

    public List<Account> getEnabledAccountList() {

        try {

            return accountDao.queryForEq(Account.COLUMN_ENABLED, true);

        } catch (SQLException e) {
            LogUtils.e(this, e);
        }

        return new ArrayList<>();
    }
}
