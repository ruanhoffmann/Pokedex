package com.example.novapokedex;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.StringJoiner;

public class PokemonListAdapter extends ArrayAdapter {
    private static final String TAG = "PokemonListAdapter";
    private final int layoutResource;
    private final LayoutInflater layoutInflater;
    private List<Pokemon> pokemons;

    public PokemonListAdapter(Context context, int resource, List<Pokemon> pokemons){
        super(context, resource);
        this.layoutInflater = LayoutInflater.from(context);
        this.layoutResource = resource;
        this.pokemons = pokemons;
    }

    @Override
    public int getCount(){
        return  pokemons.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
         ViewHolder viewHolder;

        if (convertView == null){
            Log.d(TAG, "getView: Chama convertView como null");
            convertView = layoutInflater.inflate(layoutResource, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
            }else {
            Log.d(TAG, "getView: recebe o convertview");
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Pokemon pokemonAtual = pokemons.get(position);

        viewHolder.tvNome.setText(pokemonAtual.getNome());
        viewHolder.tvNumero.setText(pokemonAtual.getNumero());
        viewHolder.tvPeso.setText(pokemonAtual.getPeso());
        viewHolder.tvAltura.setText(pokemonAtual.getAltura());

        String tipos="";
        for (String t : pokemonAtual.getTipo()){
            tipos += t  +'\n';
        }

        viewHolder.tvTipo.setText(tipos);


        Picasso.Builder builder = new Picasso.Builder(getContext());
        builder.downloader(new OkHttpDownloader(getContext()));
        builder.build().load(pokemonAtual.getImageUrl())
        .placeholder(R.drawable.baseline_image_black_48dp).into(viewHolder.ivPokemonImg);

        return convertView;
    }

    public class ViewHolder {
        final TextView tvNome;
        final TextView tvNumero;
        final TextView tvPeso;
        final TextView tvAltura;
        final TextView tvTipo;
        final ImageView ivPokemonImg;

        ViewHolder(View v){

            this.tvNome = v.findViewById(R.id.tvNome);
            this.tvNumero = v.findViewById(R.id.tvNumero);
            this.tvPeso = v.findViewById(R.id.tvPeso);
            this.tvAltura = v.findViewById(R.id.tvAltura);
            this.tvTipo = v.findViewById(R.id.tvTipo);
            this.ivPokemonImg = v.findViewById(R.id.ivPokemonImg);
        }


    }
}
