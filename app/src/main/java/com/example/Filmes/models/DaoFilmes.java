package com.example.Filmes.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoFilmes {

    @Query("SELECT * FROM filmes ORDER BY nota DESC")
    List<Filmes> filtrarPorNota();

    @Query("SELECT * FROM filmes WHERE nome = :nome")
    List<Filmes> buscarPorNome(String nome);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Filmes filmes);

    @Update
    void atualizar(Filmes filmes);

    @Delete
    void deletar(Filmes filmes);
}
