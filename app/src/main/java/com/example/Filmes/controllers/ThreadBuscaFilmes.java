package com.example.Filmes.controllers;

import android.os.AsyncTask;
import android.util.Log;

import com.example.Filmes.models.Filmes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class ThreadBuscaFilmes extends AsyncTask<Void, Void, Void> {
    private MainActivity activity;

    public ThreadBuscaFilmes(MainActivity activity) {
        this.activity = activity;
    }

    @Override
    protected Void doInBackground(Void... voids){
        getFilmes();
        return null;
    }

    public void getFilmes(){
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url("https://netflix-movies-and-tv-shows-api-by-apirobots.p.rapidapi.com/v1/netflix?page=1")
                .get()
                .addHeader("x-rapidapi-key", "d801ec94dcmsh3aa210322a27b34p100aefjsnfe026056eaa5")
                .addHeader("x-rapidapi-host", "netflix-movies-and-tv-shows-api-by-apirobots.p.rapidapi.com")
                .build();


        try {
            Response response = client.newCall(request).execute();

            assert response.body() != null;
            String responseBody = response.body().string();
            JSONObject corpoJson = new JSONObject(responseBody);
            JSONArray dataJsonArray = corpoJson.getJSONArray("items");

            for (int i = 0; i < dataJsonArray.length(); i++){
                JSONObject dataJsonObject = dataJsonArray.getJSONObject(i);
                Filmes filme = new Filmes();
                filme.setNome(dataJsonObject.getString("title"));
                filme.setNota((float) dataJsonObject.getDouble("imdb_average_rating"));
                activity.listaFilmes.add(filme);
            }


        } catch (IOException | JSONException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    protected void onPostExecute(Void aVoid) {
        activity.mostrarFilme();
    }
}
