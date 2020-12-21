package com.example.pokedex;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;



public interface PokeInterface {

    @GET("api/v1/pokemon/{id}")
    Call<Pokemonzinho> getPokemon(@Path("id") int id);

    @GET("{resource_uri}")
    Call<SpriteResponse> getSprite(@Path("resource_uri") String resourceUri);
}
