package com.crookk.pikaplus.local.model.db;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.auth.PtcCredentialProvider;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import java.util.Date;

import okhttp3.OkHttpClient;

@DatabaseTable(tableName = "account")
public class Account {

    public static final String COLUMN_ENABLED ="enabled";

    @DatabaseField(generatedId = true)
    private Long id;
    @DatabaseField
    private String type;
    @DatabaseField
    private String username;
    @DatabaseField
    private String password;
    @DatabaseField(columnName = COLUMN_ENABLED)
    private boolean enabled;

    private Double lastLocationLatitude = 0.0;
    private Double lastLocationLongitude = 0.0;
    private Long lastWorkingTime = 0L;

    private PokemonGo pokemonGo;
    private PtcCredentialProvider loginProvider;
    private boolean login = false;
    private boolean working = false;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Double getLastLocationLatitude() {
        return lastLocationLatitude;
    }

    public Double getLastLocationLongitude() {
        return lastLocationLongitude;
    }

    public void setLastLocation(Double lastLocationLatitude, Double lastLocationLongitude) {
        this.lastLocationLatitude = lastLocationLatitude;
        this.lastLocationLongitude = lastLocationLongitude;
    }

    public PokemonGo getPokemonGo() {
        return pokemonGo;
    }

    public void setPokemonGo(PokemonGo pokemonGo) {
        this.pokemonGo = pokemonGo;
    }

    public PtcCredentialProvider getLoginProvider() {
        return loginProvider;
    }

    public void setLoginProvider(PtcCredentialProvider loginProvider) {
        this.loginProvider = loginProvider;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public Long getLastWorkingTime() {
        return lastWorkingTime;
    }

    public void setLastWorkingTime(Long lastWorkingTime) {
        this.lastWorkingTime = lastWorkingTime;
    }

    public boolean isWorking() {
        return working;
    }

    public void setWorking(boolean working) {
        this.working = working;
    }

    public boolean canWork() {
        return !working && new Date().getTime() - lastWorkingTime > Constant.SLEEP_WORKER;
    }

    public boolean login(OkHttpClient client) {

        if(!login || isLoginTokenExpired()) {

            pokemonGo = new PokemonGo(client);

            try {
                LogUtils.d(this, "username=[%s], password=[%s]", username, password);

                loginProvider = new PtcCredentialProvider(client, username, password);
                pokemonGo.login(loginProvider);
                login = true;

                LogUtils.d(this, "status=[%s]", "YES");

                return true;

            } catch (LoginFailedException e) {

                login = false;
                LogUtils.e(this, e);

            } catch (RemoteServerException e) {

                login = false;
                LogUtils.e(this, e);
            }

            LogUtils.d(this, "status=[%s]", "NO");
            return false;

        } else {

            return true;
        }
    }

    public boolean isLoginTokenExpired() {
        return loginProvider.isTokenIdExpired();
    }
}
