package com.oritmalki.mymusicapp2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Transaction;
import android.arch.persistence.room.Update;

import com.oritmalki.mymusicapp2.model.Measure;

import java.util.List;

/**
 * Created by Orit on 7.1.2018.
 */

@Dao
public abstract class MeasureDao {
    @Transaction @Query("SELECT * FROM measure")
    abstract LiveData<List<Measure>> getAll();

    @Query("SELECT * FROM measure where sheetId LIKE :sheetId")
    abstract List<Measure> getMeasuresOfSheet(long sheetId);

    @Transaction @Query("SELECT * FROM measure where measure_number LIKE :measureNumber")
    abstract LiveData<Measure> getMeasure(int measureNumber);

//    @Query("SELECT beats FROM measure WHERE measure_number LIKE :measureNumber")
//    LiveData<List<Beat>> getBeats(int measureNumber);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract void insertAll(List<Measure> measures);

    @Insert
    abstract void newMeasure(Measure measure);

    @Query("DELETE FROM measure")
    abstract void deleteAll();

    @Query("DELETE FROM measure where sheetId like :sheetId")
    abstract void deleteMeasuresOfSheet(long sheetId);

    @Delete
    abstract void delete(Measure measure);

    @Delete
    abstract void delete(List<Measure> measures);

    @Update
    abstract int updateMeasure(Measure measure);


    /*
    @Query("UPDATE measure SET beats = :beats where measure_number = :measureNumber")
    abstract void updateBeats(List<Beat> beats, int measureNumber);
    */

    @Transaction
    public void insertAndDeleteInTransaction(Measure newMeasure, Measure oldMeasure, List<Measure> measures) {
        newMeasure(newMeasure);
        delete(oldMeasure);
        insertAll(measures);
    }

}
