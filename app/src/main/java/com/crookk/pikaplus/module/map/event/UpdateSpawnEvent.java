package com.crookk.pikaplus.module.map.event;

import com.crookk.pikaplus.local.model.db.Spawn;

public class UpdateSpawnEvent {

    private Spawn spawn;

    public UpdateSpawnEvent(Spawn spawn) {
        this.spawn = spawn;
    }

    public Spawn getSpawn() {
        return spawn;
    }

    public void setSpawn(Spawn spawn) {
        this.spawn = spawn;
    }
}
