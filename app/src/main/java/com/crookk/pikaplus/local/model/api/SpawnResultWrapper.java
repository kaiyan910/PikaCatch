package com.crookk.pikaplus.local.model.api;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class SpawnResultWrapper {

    @JsonProperty("time")
    private Long time;
    @JsonProperty("data")
    private List<SpawnResult> spawnResultList = new ArrayList<>();

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public List<SpawnResult> getSpawnResultList() {
        return spawnResultList;
    }

    public void setSpawnResultList(List<SpawnResult> spawnResultList) {
        this.spawnResultList = spawnResultList;
    }
}
