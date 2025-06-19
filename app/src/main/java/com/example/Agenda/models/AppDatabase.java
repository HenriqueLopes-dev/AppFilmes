package com.example.Agenda.models;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {EntityCompromisso.class}, version = 4)
public abstract class AppDatabase extends RoomDatabase {
    public abstract DaoCompromisso daoCompromisso();
}

