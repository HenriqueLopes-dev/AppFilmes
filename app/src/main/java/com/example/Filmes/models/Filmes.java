package com.example.Filmes.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Filmes {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private float nota;
    private String nome;
    private boolean isSelecionado = false;

    // Construtor vazio obrigatório
    public Filmes() {}

    // Construtor novo para dados mock
    public Filmes(String nome, float nota) {
        this.nome = nome;
        this.nota = nota;
    }

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
