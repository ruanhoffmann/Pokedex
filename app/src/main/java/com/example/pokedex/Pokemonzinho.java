package com.example.pokedex;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;



public class Pokemonzinho {

    @SerializedName("name")
    private String name;

    @SerializedName("attack")
    private Integer attack;

    @SerializedName("defense")
    private Integer defense;

    @SerializedName("height")
    private String height;

    @SerializedName("hp")
    private Integer health;

    @SerializedName("pkdx_id")
    private Integer pokedexId;

    @SerializedName("speed")
    private Integer speed;

    @SerializedName("weight")
    private String weight;

    @SerializedName("sprites")
    private List<Sprite> sprites = new ArrayList<>();

    @SerializedName("types")
    private List<PokeTipo> pokeTipos = new ArrayList<>();

    public Pokemonzinho(String name, Integer attack, Integer defense, String height, Integer health, Integer pokedexId, Integer speed, String weight, List<Sprite> sprites, List<PokeTipo> pokeTipos) {
        this.name = name;
        this.attack = attack;
        this.defense = defense;
        this.height = height;
        this.health = health;
        this.pokedexId = pokedexId;
        this.speed = speed;
        this.weight = weight;
        this.sprites = sprites;
        this.pokeTipos = pokeTipos;
    }

    public String pokeTypesToString() {
        String types = "";
        for (int i = 0; i < pokeTipos.size(); i++) {
            if(i > 0)
                types += ", ";
            types += pokeTipos.get(i).getName();
        }

        return types;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAttack() {
        return attack;
    }

    public void setAttack(Integer attack) {
        this.attack = attack;
    }

    public Integer getDefense() {
        return defense;
    }

    public void setDefense(Integer defense) {
        this.defense = defense;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public Integer getHealth() {
        return health;
    }

    public void setHealth(Integer health) {
        this.health = health;
    }

    public Integer getPokedexId() {
        return pokedexId;
    }

    public void setPokedexId(Integer pokedexId) {
        this.pokedexId = pokedexId;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public List<Sprite> getSprites() {
        return sprites;
    }

    public void setSprites(List<Sprite> sprites) {
        this.sprites = sprites;
    }

    public List<PokeTipo> getPokeTipos() {
        return pokeTipos;
    }

    public void setPokeTipos(List<PokeTipo> pokeTipos) {
        this.pokeTipos = pokeTipos;
    }
}
