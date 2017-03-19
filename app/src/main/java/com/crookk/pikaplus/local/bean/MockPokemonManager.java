package com.crookk.pikaplus.local.bean;

import com.crookk.pikaplus.core.bean.OttoBus;
import com.crookk.pikaplus.local.event.ConsoleLogEvent;
import com.crookk.pikaplus.local.model.db.Spawn;
import com.crookk.pikaplus.module.map.event.UpdateSpawnEvent;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

import java.util.List;
import java.util.Locale;
import java.util.Random;

@EBean
public class MockPokemonManager implements PokemonManager {

    @Bean
    OttoBus mOttoBus;

    @Override
    public void checkIndividualValue(List<Spawn> spawnList) {

        if (spawnList.size() == 0) return;

        Spawn spawn = spawnList.get(0);

        Random random = new Random();

        spawn.setAttack(random.nextInt(16));
        spawn.setDefense(random.nextInt(16));
        spawn.setStamina(random.nextInt(16));
        spawn.setMove1("UNKNOWN");
        spawn.setMove2("UNKNOWN");

        mOttoBus.post(new UpdateSpawnEvent(spawn));
    }
}
