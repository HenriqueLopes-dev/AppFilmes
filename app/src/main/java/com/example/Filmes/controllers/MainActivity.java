package com.example.Filmes.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Filmes.R;
import com.example.Filmes.models.AppDatabase;
import com.example.Filmes.models.Filmes;

import java.util.ArrayList;
import java.util.List;

/*
GitHub: https://github.com/udofritzke/FragmentosDataEHora
 */

public class MainActivity extends AppCompatActivity {
    static AppDatabase db = null;
    List<Filmes> listaFilmes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (db == null){
            db = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "bd-filmes").allowMainThreadQueries().build();
        }

    }

    public void mostrarFilme(){

        LinearLayout listaLayout = findViewById(R.id.listaCompromissos);

        if (listaFilmes != null) {
            listaLayout.removeAllViews();

            for (Filmes f : listaFilmes){
                TextView novoTextView = new TextView(this);
                novoTextView.setText(f.getNome());
                novoTextView.setPadding(0,8,0,8);
                novoTextView.setTextSize(20);
                novoTextView.isClickable();
                novoTextView.isFocusable();

                ColorStateList corOriginal = novoTextView.getTextColors();

                novoTextView.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        boolean selecionado = f.isSelecionado();
                        f.setSelecionado(!selecionado);

                        if (!selecionado) {
                            // Cor quando selecionado
                            novoTextView.setTextColor(Color.BLUE);
                        }else {
                            // Cor padrão
                            novoTextView.setTextColor(corOriginal);
                        }
                    }
                });

            listaLayout.addView(novoTextView);
            }

        } else {
            Toast.makeText(this, "Nenhum filme encontrado!", Toast.LENGTH_SHORT).show();
            TextView novoTextView = new TextView(this);
            novoTextView.setText("Nenhum filme encontrado!");
            novoTextView.setPadding(0,8,0,8);
            novoTextView.setTextSize(20);
            listaLayout.addView(novoTextView);
        }
    }

    public void botaoHoje(View v){
        new ThreadBuscaFilmes(this).execute();
    }

}
