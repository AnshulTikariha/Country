package com.project.countries.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.project.countries.Model.Data;

import java.util.List;

import static androidx.room.OnConflictStrategy.REPLACE;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void insert(Data data);

    @Delete
    void delete(Data data);

    @Delete
    void reset(List<Data> data);

    @Query("UPDATE country SET text = :sText WHERE ID = :sID")
    void update(int sID,String sText);

    @Query("SELECT * FROM country")
    List<Data> getAll();

}
