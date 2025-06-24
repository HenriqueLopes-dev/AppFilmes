package com.example.Filmes.controllers;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.Filmes.models.Filmes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThreadBuscaFilmes extends AsyncTask<Void, Void, List<Filmes>> {
    private MainActivity activity;
    private int pagina = 1;
    private String nomeFilme = null;
    private static final String APIKEY = "bbb428bd61mshe5f33a6e5614b01p107806jsn6ce8b54ce918";
    private static final String APIURL = "netflix-movies-and-tv-shows-api-by-apirobots.p.rapidapi.com";


    public ThreadBuscaFilmes(MainActivity activity, int pagina) {
        this.activity = activity;
        this.pagina = pagina;
    }

    public ThreadBuscaFilmes(MainActivity activity, String nomeFilme) {
        this.activity = activity;
        this.nomeFilme = nomeFilme;
    }

    @Override
    protected List<Filmes> doInBackground(Void... voids) {
        try {
            if (nomeFilme != null) return pesquisarPorNome();
            else return listarPorPagina();
        } catch (Exception e) {
            Log.e("ThreadBuscaFilmes", "Erro: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    private List<Filmes> listarPorPagina() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://netflix-movies-and-tv-shows-api-by-apirobots.p.rapidapi.com/v1/netflix?page=" + pagina)
                .get()
                .addHeader("x-rapidapi-key", APIKEY)
                .addHeader("x-rapidapi-host", APIURL)
                .build();

        Response response = client.newCall(request).execute();
        List<Filmes> filmes = new ArrayList<>();

        if (response.body() != null) {
            String json = response.body().string();
            JSONObject objeto = new JSONObject(json);
            JSONArray items = objeto.getJSONArray("items");

            for (int i = 0; i < items.length(); i++) {
                JSONObject item = items.getJSONObject(i);
                Filmes f = new Filmes();
                f.setNome(item.getString("title"));
                f.setNota((float) item.getDouble("imdb_average_rating"));
                filmes.add(f);
            }
        }

        return filmes;
    }

    private List<Filmes> pesquisarPorNome() throws IOException, JSONException {
        OkHttpClient client = new OkHttpClient();
        String esteFilme = URLEncoder.encode(nomeFilme, "UTF-8");
        Request request = new Request.Builder()
                .url("https://netflix-movies-and-tv-shows-api-by-apirobots.p.rapidapi.com/v1/netflix?name=" + esteFilme + "&page=1")
                .get()
                .addHeader("x-rapidapi-key", APIKEY)
                .addHeader("x-rapidapi-host", APIURL)
                .build();

        Response response = client.newCall(request).execute();
        List<Filmes> filmes = new ArrayList<>();

        if (response.body() != null) {
            String json = response.body().string();
            JSONObject obj = new JSONObject(json);

            if (obj.has("items")) {
                JSONArray items = obj.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject item = items.getJSONObject(i);
                    String titulo = item.getString("title");

                    // Verifica se o título é igual (ignorando maiúsculas/minúsculas)
                    if (titulo.equalsIgnoreCase(nomeFilme)) {
                        Filmes f = new Filmes();
                        f.setNome(titulo);
                        f.setNota((float) item.getDouble("imdb_average_rating"));
                        filmes.add(f);
                        break; // só pega o primeiro que bate exatamente
                    }
                }
            }
        }

        return filmes;
    }

    @Override
    protected void onPostExecute(List<Filmes> filmesEncontrados) {
        if (filmesEncontrados.isEmpty()) {
            Toast.makeText(activity, "Filme não encontrado!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (nomeFilme != null) {
            int adicionados = 0;
            for (Filmes filme : filmesEncontrados) {
                List<Filmes> existentes = activity.db.daoFilmes().buscarPorNome(filme.getNome());
                if (existentes.isEmpty()) {
                    activity.db.daoFilmes().inserir(filme);
                    adicionados++;
                }
            }
            Toast.makeText(activity, adicionados + " filme adicionados com sucesso!", Toast.LENGTH_SHORT).show();
        } else {
            activity.listaFilmes.clear();
            activity.listaFilmes.addAll(filmesEncontrados);
            activity.mostrarFilme(activity.listaFilmes);
        }
    }
}