package com.oritmalki.mymusicapp2.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.oritmalki.mymusicapp2.model.Beat;
import com.oritmalki.mymusicapp2.model.Measure;

import java.util.List;

/**
 * Created by Orit on 7.1.2018.
 */

@Dao
public interface MeasureDao {
    @Query("SELECT * FROM measure")
    List<Measure> getAll();

    @Query("SELECT * FROM measure where measure_number LIKE :measureNumber")
    Measure findByNumber(int measureNumber);

    @Query("SELECT * FROM beat")
    List<Beat> getBeats();


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Measure> measures);

    @Delete
    void delete(Measure measure);
}
