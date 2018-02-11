package com.oritmalki.mymusicapp2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;

import com.oritmalki.mymusicapp2.model.Measure;

import java.util.List;

/**
 * Created by Orit on 7.1.2018.
 */

@Dao
public interface MeasureDao {
    @Query("SELECT * FROM measure")
    LiveData<List<Measure>> getAll();

    @Query("SELECT * FROM measure where measure_number LIKE :measureNumber")
    LiveData<Measure> getMeasure(int measureNumber);

//    @Query("SELECT beats FROM measure WHERE measure_number LIKE :measureNumber")
//    LiveData<List<Beat>> getBeats(int measureNumber);


    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Measure> measures);

    @Transaction
    @Insert
    void newMeasure(Measure measure);

    @Delete
    void delete(Measure measure);

    @Delete
    void deleteAll(List<Measure> measures);

}
