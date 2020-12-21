package com.example.novapokedex;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    private static final String feedUrl = "http://ax.itunes.apple.com/WebObjects/MZStoreServices.woa/ws/RSS/%s/limit=%d/xml";
    private int feedLimit = 10;
    private String feedType = "pokemon";
    private String feedCachedUrl = "INVALIDATED";
    private ListView lvPokemon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lvPokemon = findViewById(R.id.lvPokemon);

        downloadUrl(String.format(feedUrl, feedType, feedLimit)); // funciona como o sprintf do C
    }

    // Chamado quando é hora de "inflar" o menu
    // Vincular o arquivo de leiaute do menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feeds_menu, menu);
        return true;
    }

    /*@Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.mnuFree:
                feedType = "topfreeapplications";
                feedUrl = "";
                break;
            case R.id.mnuPaid:
                feedType = "toppaidapplications";
                feedUrl = "";
                break;
            case R.id.mnuSongs:
                feedType = "topsongs";
                feedUrl = "";

                int idi = item.getItemId();

            case R.id.mnu10:
            case R.id.mnu25:
                if (!item.isChecked()) {
                    item.setChecked(true);
                    feedLimit = 35 - feedLimit;
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " ajustando feedLimit para " + feedLimit);
                } else {
                    Log.d(TAG, "onOptionsItemSelected: " + item.getTitle() + " feedLimit não mudou.");
                }
                break;

            default:
                return super.onOptionsItemSelected(item); // caso haja um submenu esta opção é chamada
        }

        downloadUrl(String.format(feedUrl, feedType, feedLimit)); // funciona como o sprintf do C
        return true;
    }*/

    private void downloadUrl(String feedUrl) {
        if (!feedUrl.equalsIgnoreCase(feedCachedUrl))
            Log.d(TAG, "downloadUrl: iniciando a AsyncTask");
        DownloadData downloadData = new DownloadData();
        downloadData.execute(feedUrl);
        Log.d(TAG, "downloadUrl: terminou.");
    }

    // 1º parametro - dado passado para a classe - uma URL - String
    // 2º parâmetro - dados referente a progresso - ignoramos aqui - Void
    // 3º parâmetro - tipo de resultado - no nosso caso uma String
    private class DownloadData extends AsyncTask<String, Void, String> {
        private static final String TAG = "DownloadData";

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
//            Log.d(TAG, "onPostExecute: o parâmetro recebido é " + s);
            ParseApplications parser = new ParseApplications();
            parser.parse(s);

            //           FeedAdapter feedAdapter = new FeedAdapter(
            //                  MainActivity.this, R.layout.list_recorder, parser.getApplications()
            //         );
            FeedImageAdapter feedAdapter = new FeedImageAdapter(
                    MainActivity.this, R.layout.list_record_with_image, parser.getApplications()
            );

            List<FeedEntry> applications = parser.getApplications();

            lvPokemon.setAdapter(feedAdapter);
            lvPokemon.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Log.d(TAG, "onItemClick: Item clicado: "+applications);
                    Intent intent = new Intent(MainActivity.this, FeedDetails.class);
                    intent.putExtra("feedEntry", applications.get(i));
                    startActivity(intent);
                }
            });
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d(TAG, "doInBackground: recebeu " + strings[0]);
            String contents = downloadContents(strings[0]);
            if (contents == null) {
                Log.e(TAG, "doInBackground: Erro baixando dados.");
            }
            return contents;
        }
        protected Bitmap doInBackgroud(String ... urls){
            URL url = null;
            try {
                url = new URL(urls[0]);
                Bitmap bmp = null;
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                int resposta  = connection.getResponseCode();
                Log.d(TAG, "downloadJSON: Código de resposta:  " +resposta);

                if (resposta != HttpURLConnection.HTTP_OK){
                    if (resposta == HttpURLConnection.HTTP_MOVED_TEMP
                    || resposta == HttpURLConnection.HTTP_MOVED_PERM
                    || resposta == HttpURLConnection.HTTP_SEE_OTHER){

                        String novaUrl = connection.getHeaderField("location");
                        connection = (HttpURLConnection) new URL(novaUrl).openConnection();
                    }
                }

                InputStream inputStream = connection.getInputStream();

                return BitmapFactory.decodeStream(inputStream);
            }catch (Exception e){
                Log.e(TAG, "erro! " + e.getMessage());
                e.printStackTrace();
            }
            return null;



        }









        private String downloadContents(String urlPath) {
            // StringBuilder é mais eficiente para concatenar strings
            StringBuilder result = new StringBuilder();

            try {
                URL url = new URL(urlPath);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                int responseCode = connection.getResponseCode();
                Log.d(TAG, "downloadContents: O código de resposta foi: " + responseCode);

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                int charsRead; // quantidade de caracteres lida
                char[] inputBuffer = new char[500];

                while (true) {
                    charsRead = reader.read(inputBuffer);
                    if (charsRead < 0) {
                        break;
                    }
                    if (charsRead > 0) {
                        // copie o conteúdo do inputBuffer para dentro de result.
                        result.append(String.copyValueOf(inputBuffer, 0, charsRead));
                    }
                }
                reader.close();

                return result.toString();
            } catch (MalformedURLException ex) {
                Log.e(TAG, "downloadContents: URL inválida: " + ex.getMessage());
            } catch (IOException ex) {
                Log.e(TAG, "downloadContents: IOException ao ler os dados: " + ex.getMessage());
            } catch (SecurityException ex) {
                Log.e(TAG, "downloadContents: Exceção de segurança. Falta permissão? " + ex.getMessage());
            }

            return null;
        }
    }
}