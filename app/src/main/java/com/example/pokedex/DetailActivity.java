package com.example.pokedex;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DetailActivity extends AppCompatActivity {

    private TextView tvName, tvTypes, tvAttack, tvDefense, tvSpeed;
    private ImageView ivPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes);

        tvName = (TextView) findViewById(R.id.tv_detail_name);
        tvTypes = (TextView) findViewById(R.id.tv_detail_types);
        tvAttack = (TextView) findViewById(R.id.tv_detail_attack);
        tvDefense = (TextView) findViewById(R.id.tv_detail_defense);
        tvSpeed = (TextView) findViewById(R.id.tv_detail_speed);
        ivPokemon = (ImageView) findViewById(R.id.iv_detail_pokemon);

        int id = getIntent().getIntExtra("ID", 0);
        requestData(id);
    }

    private void requestData(int id) {
        final PokeInterface apiService = ApiClient.getClient().create(PokeInterface.class);

        Call<Pokemonzinho> call = apiService.getPokemon(id);
        call.enqueue(new Callback<Pokemonzinho>() {
            @Override
            public void onResponse(Call<Pokemonzinho> call, Response<Pokemonzinho> response) {
                Pokemonzinho pokemonzinho;

                if(response.isSuccessful()) {
                    pokemonzinho = response.body();

                    tvName.setText(pokemonzinho.getName());
                    tvTypes.setText(pokemonzinho.pokeTypesToString());
                    tvAttack.setText("Attack: " + pokemonzinho.getAttack().toString());
                    tvDefense.setText("Defense: " + pokemonzinho.getDefense().toString());
                    tvSpeed.setText("Speed: " + pokemonzinho.getSpeed().toString());

                    Call<SpriteResponse> callSprite;
                    Sprite sprite = pokemonzinho.getSprites().get(0);
                    callSprite = apiService.getSprite(sprite.getResourceUri());
                    callSprite.enqueue(new Callback<SpriteResponse>() {
                        @Override
                        public void onResponse(Call<SpriteResponse> call, Response<SpriteResponse> response) {
                            SpriteResponse spriteResponse;
                            if(response.isSuccessful()) {
                                spriteResponse = response.body();
                                Picasso.with(DetailActivity.this)
                                        .load("http://pokeapi.co" + spriteResponse.getImage())
                                        .resize(128,128)
                                        .into(ivPokemon);
                            }
                        }

                        @Override
                        public void onFailure(Call<SpriteResponse> call, Throwable t) {

                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<Pokemonzinho> call, Throwable t) {

            }
        });
    }

}
