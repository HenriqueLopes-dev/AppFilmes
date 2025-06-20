package com.example.Filmes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity
public class Filmes {

    @PrimaryKey
    private long  id;
    private float nota;
    private String nome;
    private boolean isSelecionado = false;

   //private List<String> genero;


    public boolean isSelecionado() {
        return isSelecionado;
    }

    public void setSelecionado(boolean selecionado) {
        isSelecionado = selecionado;
    }

    public float getNota() {
        return nota;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setNota(float nota) {
        this.nota = nota;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
