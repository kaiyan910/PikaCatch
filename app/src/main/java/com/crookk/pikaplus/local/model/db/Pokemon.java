package com.crookk.pikaplus.local.model.db;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import com.crookk.pikaplus.local.utils.ResourcesLoader;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "pokemon")
public class Pokemon implements Parcelable {

    public static final String COLUMN_IGNORE = "ignore";
    public static final String COLUMN_TRACKING = "tracking";

    @DatabaseField(id = true)
    private int id;
    private String name;
    @DatabaseField(defaultValue = "false", columnName = COLUMN_IGNORE)
    private boolean ignore;
    @DatabaseField(defaultValue = "false", columnName = COLUMN_TRACKING)
    private boolean tracking;
    @DatabaseField(defaultValue = "false")
    private boolean checkIV;

    public Pokemon() {
    }

    public Pokemon(int id) {
        this.id = id;
    }

    protected Pokemon(Parcel in) {
        id = in.readInt();
        name = in.readString();
        ignore = in.readByte() != 0;
        tracking = in.readByte() != 0;
        checkIV = in.readByte() != 0;
    }

    public static final Creator<Pokemon> CREATOR = new Creator<Pokemon>() {
        @Override
        public Pokemon createFromParcel(Parcel in) {
            return new Pokemon(in);
        }

        @Override
        public Pokemon[] newArray(int size) {
            return new Pokemon[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName(Context context) {
        return ResourcesLoader.getStringResources(context, id);
    }

    public boolean isIgnore() {
        return ignore;
    }

    public void setIgnore(boolean ignore) {
        this.ignore = ignore;
    }

    public boolean isTracking() {
        return tracking;
    }

    public void setTracking(boolean tracking) {
        this.tracking = tracking;
    }

    public boolean isCheckIV() {
        return checkIV;
    }

    public void setCheckIV(boolean checkIV) {
        this.checkIV = checkIV;
    }

    @Override
    public boolean equals(Object obj) {

        Pokemon that = (Pokemon) obj;

        return that.getId() == id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeByte((byte) (ignore ? 1 : 0));
        parcel.writeByte((byte) (tracking ? 1 : 0));
        parcel.writeByte((byte) (checkIV ? 1 : 0));
    }
}
