package com.crookk.pikaplus.local.model.db;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.crookk.pikaplus.Constant;
import com.crookk.pikaplus.R;
import com.crookk.pikaplus.core.utils.Hash;
import com.crookk.pikaplus.local.model.api.SpawnResult;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@DatabaseTable(tableName = "spawn")
public class Spawn implements Parcelable {

    public static final String COLUMN_EXPIRE_TIME = "expireTime";
    public static final String COLUMN_POKEMON = "pokemon_id";

    @DatabaseField(id = true)
    private String id;
    @DatabaseField(foreign = true, foreignAutoRefresh = true, index = true)
    private Pokemon pokemon;
    @DatabaseField
    private double latitude;
    @DatabaseField
    private double longitude;
    @DatabaseField
    private int spawnPointType;
    @DatabaseField(columnName = COLUMN_EXPIRE_TIME)
    private Date expireTime;
    @DatabaseField
    private Date discoverTime;
    @DatabaseField(defaultValue = "0")
    private int attack;
    @DatabaseField(defaultValue = "0")
    private int defense;
    @DatabaseField(defaultValue = "0")
    private int stamina;
    @DatabaseField
    private String move1;
    @DatabaseField
    private String move2;

    public Spawn() {
    }

    protected Spawn(Parcel in) {
        id = in.readString();
        pokemon = in.readParcelable(Pokemon.class.getClassLoader());
        latitude = in.readDouble();
        longitude = in.readDouble();
        spawnPointType = in.readInt();
        expireTime = new Date(in.readLong());
        discoverTime = new Date(in.readLong());
        attack = in.readInt();
        defense = in.readInt();
        stamina = in.readInt();
        move1 = in.readString();
        move2 = in.readString();
    }

    public static final Creator<Spawn> CREATOR = new Creator<Spawn>() {
        @Override
        public Spawn createFromParcel(Parcel in) {
            return new Spawn(in);
        }

        @Override
        public Spawn[] newArray(int size) {
            return new Spawn[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Pokemon getPokemon() {
        return pokemon;
    }

    public void setPokemon(Pokemon pokemon) {
        this.pokemon = pokemon;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public int getSpawnPointType() {
        return spawnPointType;
    }

    public void setSpawnPointType(int spawnPointType) {
        this.spawnPointType = spawnPointType;
    }

    public Date getDiscoverTime() {
        return discoverTime;
    }

    public void setDiscoverTime(Date discoverTime) {
        this.discoverTime = discoverTime;
    }

    public Date getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(Date expireTime) {
        this.expireTime = expireTime;
    }

    public int getAttack() {
        return attack;
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public int getDefense() {
        return defense;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public String getMove1() {
        return move1;
    }

    public void setMove1(String move1) {
        this.move1 = move1;
    }

    public String getMove2() {
        return move2;
    }

    public void setMove2(String move2) {
        this.move2 = move2;
    }

    public Double calculateIV() {
        return (attack + defense + stamina) / 45.0 * 100;
    }

    public String getExpireTimeText() {
        return Constant.DATE_FORMAT_WINDOW.format(expireTime);
    }

    public String getTimeLeft(Context context) {

        Long value = getExpireTime().getTime() - System.currentTimeMillis();

        if (value > 0) {

            Date date = new Date(value);
            return Constant.TIMER_FORMAT.format(date);

        } else {

            return context.getResources().getString(R.string.bye);
        }
    }

    public boolean isExpired() {
        return getExpireTime().getTime() - System.currentTimeMillis() <= 0;
    }

    public static List<Spawn> convert(List<SpawnResult> spawnResultList) {

        List<Spawn> converted = new ArrayList<>();

        for(SpawnResult result : spawnResultList) {
            converted.add(convert(result));
        }

        return converted;
    }

    public static Spawn convert(SpawnResult result) {

        Spawn converted = new Spawn();

        converted.setId(Hash.encode(Constant.HASH_SALT, result.getPokemonId(), result.getNoDecimalLatitude(), result.getNoDecimalLongitude()));
        converted.setDiscoverTime(new Date());
        converted.setExpireTime(new Date(result.getExpireTime()));
        converted.setPokemon(new Pokemon(result.getPokemonId()));
        converted.setLatitude(result.getLatitude());
        converted.setLongitude(result.getLongitude());
        converted.setSpawnPointType(result.getSpawnType());

        return converted;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeParcelable(pokemon, i);
        parcel.writeDouble(latitude);
        parcel.writeDouble(longitude);
        parcel.writeInt(spawnPointType);
        parcel.writeLong(expireTime.getTime());
        parcel.writeLong(discoverTime.getTime());
        parcel.writeInt(attack);
        parcel.writeInt(defense);
        parcel.writeInt(stamina);
        parcel.writeString(move1);
        parcel.writeString(move2);
    }
}
