package com.isro.geofauna.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface GeofaunaDao {


    @Query("SELECT * FROM geofauna")
    LiveData<List<Geofauna>> getAll();

    @Query("SELECT * FROM geofauna ORDER BY Date DESC")
    LiveData<List<Geofauna>> getAllByDate();

    @Query("SELECT * FROM geofauna ORDER BY Date DESC")
    List<Geofauna> getAllByDateList();

    @Insert
    void insertAll(Geofauna... geofaunas);

    @Update
    public void updateGeofauna(Geofauna... geofaunas);

    @Query("DELETE FROM geofauna")
    void deleteAll();

    @Delete
    void deleteRecord(Geofauna geofauna);

}
