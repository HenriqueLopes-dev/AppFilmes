package com.example.Filmes.controllers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Filmes.R;
import com.example.Filmes.models.AppDatabase;
import com.example.Filmes.models.Filmes;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    static AppDatabase db = null;
    public List<Filmes> listaFilmes = new ArrayList<>();
    public List<Filmes> listaFilmesBanco = new ArrayList<>();
    private boolean mostrandoCatalogo = true;
    private Button btnAdicionarRemover;
    private TextView tituloPagina;
    private int paginaAtual = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (db == null) {
            db = Room.databaseBuilder(getApplicationContext(),
                    AppDatabase.class, "bd-filmes").allowMainThreadQueries().build();
        }

        btnAdicionarRemover = findViewById(R.id.btnAdicionarRemover);
        tituloPagina = findViewById(R.id.titulo3);

        mostrarCatalogo();
    }

    public void mostrarFilme(List<Filmes> filmesParaMostrar) {
        LinearLayout listaLayout = findViewById(R.id.listaCompromissos);
        listaLayout.removeAllViews();

        if (filmesParaMostrar != null && !filmesParaMostrar.isEmpty()) {
            for (Filmes f : filmesParaMostrar) {
                TextView novoTextView = new TextView(this);
                String texto = f.getNome() + " - Nota: " + f.getNota();
                novoTextView.setText(texto);
                novoTextView.setPadding(0, 8, 0, 8);
                novoTextView.setTextSize(20);

                ColorStateList corOriginal = novoTextView.getTextColors();

                novoTextView.setOnClickListener(v -> {
                    boolean selecionado = f.isSelecionado();
                    f.setSelecionado(!selecionado);
                    novoTextView.setTextColor(!selecionado ? Color.BLUE : corOriginal.getDefaultColor());
                });

                listaLayout.addView(novoTextView);
            }
        } else {
            Toast.makeText(this, "Nenhum filme encontrado!", Toast.LENGTH_SHORT).show();
            TextView novoTextView = new TextView(this);
            novoTextView.setText("Nenhum filme encontrado!");
            novoTextView.setPadding(0, 8, 0, 8);
            novoTextView.setTextSize(20);
            listaLayout.addView(novoTextView);
        }
    }

    public void botaoCatalogo(View v) {
        mostrarCatalogo();
    }

    private void mostrarCatalogo() {
        mostrandoCatalogo = true;
        btnAdicionarRemover.setText("Adicionar");
        listaFilmes.clear();
        new ThreadBuscaFilmes(this, paginaAtual).execute();
        atualizarTituloPagina();
    }

    public void botaoLista(View v) {
        mostrarListaBanco();
    }

    private void mostrarListaBanco() {
        mostrandoCatalogo = false;
        btnAdicionarRemover.setText("Remover");

        try {
            listaFilmesBanco = db.daoFilmes().filtrarPorNota();
            if (listaFilmesBanco == null) {
                listaFilmesBanco = new ArrayList<>();
            }
        } catch (Exception e) {
            Toast.makeText(this, "Erro ao buscar lista do banco: " + e.getMessage(), Toast.LENGTH_LONG).show();
            listaFilmesBanco = new ArrayList<>();
        }

        mostrarFilme(listaFilmesBanco);
    }

    public void botaoAdicionarRemover(View v) {
        if (mostrandoCatalogo) {
            int filmesAdicionados = 0;
            for (Filmes filme : listaFilmes) {
                if (filme.isSelecionado()) {
                    List<Filmes> filmesNoBanco = db.daoFilmes().buscarPorNome(filme.getNome());
                    if (filmesNoBanco.isEmpty()) {
                        Filmes novoFilme = new Filmes();
                        novoFilme.setNome(filme.getNome());
                        novoFilme.setNota(filme.getNota());
                        db.daoFilmes().inserir(novoFilme);
                        filmesAdicionados++;
                    } else {
                        Toast.makeText(this, "Filme \"" + filme.getNome() + "\" já está na lista",
                                Toast.LENGTH_SHORT).show();
                    }
                    filme.setSelecionado(false);
                }
            }
            Toast.makeText(this, filmesAdicionados + " filme(s) adicionados", Toast.LENGTH_SHORT).show();

        } else {
            new Thread(() -> {
                final int[] filmesRemovidos = {0};
                List<Filmes> copiaLista = new ArrayList<>(listaFilmesBanco);

                for (Filmes filme : copiaLista) {
                    if (filme.isSelecionado()) {
                        db.daoFilmes().deletar(filme);
                        listaFilmesBanco.remove(filme);
                        filmesRemovidos[0]++;
                    }
                }

                runOnUiThread(() -> {
                    Toast.makeText(MainActivity.this,
                            filmesRemovidos[0] + " filme(s) removidos",
                            Toast.LENGTH_SHORT).show();
                    mostrarFilme(listaFilmesBanco);
                });
            }).start();
        }
    }

    public void paginaAnterior(View v) {
        if (paginaAtual > 1) {
            paginaAtual--;
            mostrarCatalogo();
        } else {
            Toast.makeText(this, "Você já está na primeira página!", Toast.LENGTH_SHORT).show();
        }
    }

    public void proximaPagina(View v) {
        paginaAtual++;
        mostrarCatalogo();
    }

    public void atualizarTituloPagina() {
        tituloPagina.setText("Página " + paginaAtual);
    }

    public void pesquisarFilme(View view) {
        EditText etPesquisa = findViewById(R.id.PesquisaFilme);
        String termo = etPesquisa.getText().toString().trim();

        if (!termo.isEmpty()) {
            new ThreadBuscaFilmes(this, termo).execute();
        } else {
            Toast.makeText(this, "Digite o nome de um filme", Toast.LENGTH_SHORT).show();
        }
        etPesquisa.setText("");
    }
}


