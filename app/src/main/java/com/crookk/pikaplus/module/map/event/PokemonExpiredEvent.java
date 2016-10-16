package com.crookk.pikaplus.module.map.event;

import com.crookk.pikaplus.module.map.model.PokemonMarker;

public class PokemonExpiredEvent {

    private PokemonMarker marker;

    public PokemonExpiredEvent(PokemonMarker marker) {
        this.marker = marker;
    }

    public PokemonMarker getMarker() {
        return marker;
    }

    public void setMarker(PokemonMarker marker) {
        this.marker = marker;
    }
}
