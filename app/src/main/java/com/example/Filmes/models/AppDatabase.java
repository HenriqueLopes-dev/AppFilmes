package com.example.Filmes.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Filmes.class}, version = 7)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoFilmes daoFilmes();
}
