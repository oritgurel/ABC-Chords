package com.oritmalki.mymusicapp2.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.oritmalki.mymusicapp2.model.Sheet;

import java.util.List;

@Dao
public interface SheetDao {
    @Query("SELECT * FROM sheet")
    LiveData<List<Sheet>> getAll();

    @Query("SELECT * FROM sheet where title LIKE :sheetTitle")
    LiveData<Sheet> getSheetByTitle(String sheetTitle);

    @Query("SELECT * FROM sheet where author LIKE :author")
    LiveData<Sheet> getSheetByAuthor(String author);

    @Query("SELECT * FROM sheet where name LIKE :name")
    LiveData<Sheet> getSheetByName(String name);

    @Query("SELECT * FROM sheet where id LIKE :id")
    LiveData<Sheet> getSheetById(int id);

    @Insert
    void newSheet(Sheet sheet);

    @Insert
    void insertAll(List<Sheet> sheets);

    @Delete
    void delete(Sheet sheet);

    @Update
    int updateSheet(Sheet sheet);





}
