package com.example.pokedex;

//criado por: Ruan

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    List<Pokemonzinho> pokemonzinhoList = new ArrayList<>();
    RecyclerView recyclerView;
    PokeAdapter pokeAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.rv_pokemons);

        pokeAdapter = new PokeAdapter(pokemonzinhoList);//cria um novo poke



        RecyclerView.LayoutManager layoutManager;
        layoutManager = new LinearLayoutManager(this);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(pokeAdapter);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Intent i = new Intent(MainActivity.this, DetailActivity.class);
                i.putExtra("ID", pokemonzinhoList.get(position).getPokedexId());
                startActivity(i);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));

        addData();
    }

    private void addData() {
        PokeInterface apiService = ApiClient.getClient().create(PokeInterface.class);

        for(int i = 1; i <= 30; i++) {
            Call<Pokemonzinho> call = apiService.getPokemon(i);
            call.enqueue(new Callback<Pokemonzinho>() {
                @Override
                public void onResponse(Call<Pokemonzinho> call, Response<Pokemonzinho> response) {
                    if(response.isSuccessful()) {
                        Pokemonzinho pokemonzinho = response.body();

                        pokemonzinhoList.add(pokemonzinho);
                        pokeAdapter.notifyDataSetChanged();

                        Log.i("POKEMON", "Nome: " + pokemonzinho.getName());
                        Log.i("POKEMON", "Ataque: " + pokemonzinho.getAttack());
                        Log.i("POKEMON", "Defesa: " + pokemonzinho.getDefense());
                        Log.i("POKEMON", "Saúde: " + pokemonzinho.getHealth());
                        Log.i("POKEMON", "Altura: " + pokemonzinho.getHeight());
                        Log.i("POKEMON", "Largura: " + pokemonzinho.getWeight());

                    }
                }

                @Override
                public void onFailure(Call<Pokemonzinho> call, Throwable t) {
                }
            });
        }
    }
}
