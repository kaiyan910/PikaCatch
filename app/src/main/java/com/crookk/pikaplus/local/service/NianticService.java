package com.crookk.pikaplus.local.service;

import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.core.utils.MathUtils;
import com.crookk.pikaplus.local.model.db.Account;
import com.pokegoapi.api.PokemonGo;
import com.pokegoapi.api.map.pokemon.CatchablePokemon;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;
import com.pokegoapi.exceptions.LoginFailedException;
import com.pokegoapi.exceptions.RemoteServerException;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.Random;
import java.util.concurrent.Exchanger;

import okhttp3.OkHttpClient;

@EBean(scope = EBean.Scope.Singleton)
public class NianticService {

    public static final int SLEEP_RETRY = 2000;
    public static final int SLEEP_ACTION = 1000;
    public static final int RETRY_MAX = 2;

    private OkHttpClient mHttpClient;

    @AfterInject
    void afterInject() {

        mHttpClient = new OkHttpClient();
    }

    public EncounterResult checkPokemonIV(Account account, int pokemonId, Double latitude, Double longitude) {

        int retry = 0;

        while (RETRY_MAX > retry) {

            try {

                if (account.login(mHttpClient)) {

                    LogUtils.d(this, "setLocation: location=[%f, %f]", latitude, longitude);
                    account.getPokemonGo().setLocation(latitude, longitude, new Random().nextInt(100));
                    Thread.sleep(SLEEP_ACTION);
                    List<CatchablePokemon> catchablePokemonList = account.getPokemonGo().getMap().getCatchablePokemon();
                    LogUtils.d(this, "CatchablePokemon: size=[%d]", catchablePokemonList.size());
                    for(CatchablePokemon pokemon : catchablePokemonList) {

                        LogUtils.d(this, "Encounter Pokemon: number=[%d]", pokemon.getPokemonId().getNumber());
                        if(pokemon.getPokemonId().getNumber() == pokemonId) {

                            LogUtils.d(this, "Encounter Pokemon FOUND");
                            EncounterResult result = pokemon.encounterPokemon();
                            if (result != null) return result;
                        }
                    }

                    break;

                } else {

                    retry++;
                    Thread.sleep(SLEEP_RETRY);
                }

            } catch (InterruptedException e) {
                retry++;
                LogUtils.e(this, e);
            } catch (RemoteServerException e) {
                retry++;
                LogUtils.e(this, e);
            } catch (LoginFailedException e) {
                retry++;
                LogUtils.e(this, e);
            } catch (Exception e) {
                retry++;
                LogUtils.e(this, e);
            }
        }

        return null;
    }
}
