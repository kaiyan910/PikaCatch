package com.crookk.pikaplus.local.model.api;


import com.crookk.pikaplus.core.utils.MathUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class SpawnResult {

    @JsonProperty("spawn_id")
    private Long spawnId;
    @JsonProperty("spawn_type")
    private int spawnType;
    @JsonProperty("expire_time")
    private Long expireTime;
    @JsonProperty("latitude")
    private Double latitude;
    @JsonProperty("longitude")
    private Double longitude;
    @JsonProperty("pokemon_id")
    private int pokemonId;

    public Long getSpawnId() {
        return spawnId;
    }

    public void setSpawnId(Long spawnId) {
        this.spawnId = spawnId;
    }

    public int getSpawnType() {
        return spawnType;
    }

    public void setSpawnType(int spawnType) {
        this.spawnType = spawnType;
    }

    public Long getExpireTime() {
        return expireTime * 1000L;
    }

    public void setExpireTime(Long expireTime) {
        this.expireTime = expireTime;
    }

    public Double getLatitude() {
        return MathUtils.toDecimal(latitude);
    }

    public int getNoDecimalLatitude() {

        return (int) (getLatitude() * 1000000);
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return MathUtils.toDecimal(longitude);
    }

    public int getNoDecimalLongitude() {

        return (int) (getLongitude() * 1000000);
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }
}
