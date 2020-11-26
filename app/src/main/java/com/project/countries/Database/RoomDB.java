package com.project.countries.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.project.countries.Model.Data;

@Database(entities = {Data.class},version = 1,exportSchema = false)
public abstract class RoomDB extends RoomDatabase {

    private static String Database_Name = "database";
    private static RoomDB database;

    public synchronized static RoomDB getInstance(Context context){
        //check condition
        if (database == null){
            database = Room.databaseBuilder(context.getApplicationContext(),
                    RoomDB.class,Database_Name)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return database;
    }

    public abstract MainDao mainDao();
}
