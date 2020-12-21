package com.example.novapokedex;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class ParseJson {
    private static final String TAG ="ParseJson";
    private List<MainActivity> pokemons;

    public ParseJson(){
        pokemons = new ArrayList<>(); 
    }
    
    public List<MainActivity> getPokemons(){
        return pokemons;
    }

    public boolean parse(String jsonString){
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray pokemons = json.getJSONArray("pokemons");

            for (int i = 0; i < pokemons.length(); i++){
                JSONObject pokemon = pokemons.getJSONObject(i);
                Pokemon p = new Pokemon();
                p.setId(pokemon.getInt("id"));
                p.setNome(pokemon.getString("name"));
                p.setImageUrl(pokemon.getString("img"));
                p.setNumero(pokemon.getString("num"));
                p.setPeso(pokemon.getString("weight"));
                p.setAltura(pokemon.getString("height"));
                JSONArray tipos = pokemon.getJSONArray("type");

                for (int j = 0; j < tipos.length(); j++){
                    p.addTipo(tipos.getString(j));
                }
                this.pokemons.add(p);
                //
            }
            return true;
        }catch (JSONException e ){
            Log.e(TAG, "parse: Erro no parse: " +e.getMessage());
            return false;
        }
    }

}
