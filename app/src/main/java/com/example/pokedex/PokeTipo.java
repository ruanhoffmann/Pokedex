package com.example.pokedex;

import com.google.gson.annotations.SerializedName;



public class PokeTipo {

    @SerializedName("name")
    private String name;

    public PokeTipo(String name) {
        this.name = name;
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
