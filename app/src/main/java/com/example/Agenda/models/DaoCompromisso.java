package com.example.Agenda.models;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DaoCompromisso {

    @Query("SELECT * FROM compromisso")
    List<EntityCompromisso> todos();

    @Query("SELECT * FROM compromisso WHERE dia = :dia AND mes = :mes AND ano = :ano ORDER BY hora ASC, minuto ASC")
    List<EntityCompromisso> compromissosPorData(int dia, int mes, int ano);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void inserir(EntityCompromisso compromisso);

    @Update
    void atualizar(EntityCompromisso compromisso);

    @Delete
    void deletar(EntityCompromisso compromisso);

}
