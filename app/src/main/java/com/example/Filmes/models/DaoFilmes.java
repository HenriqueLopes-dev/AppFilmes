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

    @Query("SELECT * FROM filmes")
    List<Filmes> todos();

    //@Query("SELECT * FROM filmes WHERE dia = :dia AND mes = :mes AND ano = :ano ORDER BY hora ASC, minuto ASC")
    //List<Filmes> compromissosPorData(int dia, int mes, int ano);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(Filmes filmes);

    @Update
    void atualizar(Filmes filmes);

    @Delete
    void deletar(Filmes filmes);

}
