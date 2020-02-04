package com.isro.geofauna.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GeofaunaDao {


    @Query("SELECT * FROM geofauna")
    LiveData<List<Geofauna>> getAll();

    @Query("SELECT * FROM geofauna ORDER BY Date ASC")
    LiveData<List<Geofauna>> getAllByDate();

    @Insert
    void insertAll(Geofauna... geofaunas);

    @Query("DELETE FROM geofauna")
    void deleteAll();

}
