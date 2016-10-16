package com.crookk.pikaplus.local.bean;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.core.bean.OttoBus;
import com.crookk.pikaplus.core.utils.LogUtils;
import com.crookk.pikaplus.local.event.ConsoleLogEvent;
import com.crookk.pikaplus.local.exception.NoAccountException;
import com.crookk.pikaplus.local.model.db.Account;
import com.crookk.pikaplus.local.model.db.Spawn;
import com.crookk.pikaplus.local.service.NianticService;
import com.crookk.pikaplus.module.map.event.UpdateSpawnEvent;
import com.pokegoapi.api.map.pokemon.encounter.EncounterResult;

import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@EBean(scope = EBean.Scope.Singleton)
public class PokemonManager {

    @Bean
    DatabaseManager mDatabaseManager;
    @Bean
    AccountManager mAccountManager;
    @Bean
    NianticService mNianticService;
    @Bean
    OttoBus mOttoBus;

    public void checkIndividualValue(List<Spawn> spawnList) {

        LogUtils.d(this, "Padding to check IV=[%d]", spawnList.size());

        for (Spawn spawn : spawnList) {

            check(spawn);
        }
    }

    @Background
    void check(Spawn spawn) {

        LogUtils.d(this, "Checking spawn Id=[%s]", spawn.getId());
        while (true) {

            //get PTC account
            try {

                Account account = mAccountManager.request(spawn.getLatitude(), spawn.getLongitude());

                if (account != null) {

                    account.setWorking(true);

                    mOttoBus.post(new ConsoleLogEvent(String.format(Locale.getDefault(), "[IV] account=[%s]", account.getUsername())));

                    //check IV
                    EncounterResult result = mNianticService.checkPokemonIV(account, spawn.getPokemon().getId(), spawn.getLatitude(), spawn.getLongitude());

                    if (result != null) {

                        spawn.setAttack(result.getPokemonData().getIndividualAttack());
                        spawn.setDefense(result.getPokemonData().getIndividualDefense());
                        spawn.setStamina(result.getPokemonData().getIndividualStamina());
                        spawn.setMove1(result.getPokemonData().getMove1().name());
                        spawn.setMove2(result.getPokemonData().getMove2().name());

                        mOttoBus.post(new ConsoleLogEvent(String.format(Locale.getDefault(),
                                "[IV] IV=[%02d/%02d/%02d], move=[%s|%s]",
                                spawn.getAttack(), spawn.getDefense(), spawn.getStamina(), spawn.getMove1(), spawn.getMove2())));

                        mOttoBus.post(new UpdateSpawnEvent(spawn));
                        //update database spawn object
                        mDatabaseManager.update(spawn);
                    } else {
                        mOttoBus.post(new ConsoleLogEvent("[IV] No Pokemon found"));
                    }

                    account.setLastLocation(spawn.getLatitude(), spawn.getLongitude());
                    account.setLastWorkingTime(new Date().getTime());
                    account.setWorking(false);
                    break;

                } else {

                    mOttoBus.post(new ConsoleLogEvent("[IV] No Worker"));
                    LogUtils.d(this, "No worker available sleep for=[%d]", Constant.SLEEP_WORKER);
                    Thread.sleep(Constant.SLEEP_WORKER);
                }

            } catch (NoAccountException e) {
                LogUtils.e(this, e);
                break;

            } catch (InterruptedException e) {
                LogUtils.e(this, e);
                break;
            }
        }
    }
}
