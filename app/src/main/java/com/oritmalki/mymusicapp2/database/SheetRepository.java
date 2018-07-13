package com.oritmalki.mymusicapp2.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.oritmalki.mymusicapp2.AppExecutors;
import com.oritmalki.mymusicapp2.model.Sheet;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SheetRepository {

    private static SheetRepository sInstance;
    private final AppDataBase mDatabase;
    private MediatorLiveData<List<Sheet>> mObservableSheets;

    //my addition
    public AppExecutors appExecutors = new AppExecutors();


    //private constructor
    private SheetRepository(final AppDataBase database) {

        mDatabase = database;



        mObservableSheets = new MediatorLiveData<>();

        mObservableSheets.addSource(mDatabase.sheetDao().getAll(), new Observer<List<Sheet>>() {
            @Override
            public void onChanged(@Nullable List<Sheet> sheets) {
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    appExecutors.diskIO().execute(() ->
                            mObservableSheets.postValue(sheets));
                }
            }
        });

    }

    //new constructor from example
    public static SheetRepository getInstance(final AppDataBase database) {
        if (sInstance == null) {
            synchronized (SheetRepository.class) {
                if (sInstance == null) {
                    sInstance = new SheetRepository(database);
                }
            }
        }
        return sInstance;
    }

    /*****Room Measures DAO*****/

    public void addNewSheet(Sheet sheet, AtomicBoolean lock) {

        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                if (!lock.get()) {
                    lock.set(true);
                    mDatabase.sheetDao().newSheet(sheet);
                }
                lock.set(false);
            }


        });

        Log.d("ADD_MEASURE", "Added empty measure to database");

    }


    public void addAllSheets(List<Sheet> sheets) {
        appExecutors.diskIO().execute(() ->
                mDatabase.sheetDao().insertAll(sheets));
    }

    public LiveData<List<Sheet>> getAllSheets() {

        return mObservableSheets;
    }

    public LiveData<Sheet> getSheet(int sheetId) {

        return mDatabase.sheetDao().getSheetById(sheetId);
    }

    public LiveData<Sheet> getSheet(String sheetName) {

        return mDatabase.sheetDao().getSheetByName(sheetName);
    }


    public void InsertSheet(Sheet sheet) {

        mDatabase.sheetDao().newSheet(sheet);
    }

    public void deleteSheet(Sheet sheet) {
        appExecutors.diskIO().execute(() ->
                mDatabase.sheetDao().delete(sheet));
    }

    public void updateSheet(Sheet sheet) {
        appExecutors.diskIO().execute(() ->
                mDatabase.sheetDao().updateSheet(sheet));
    }

//    public LiveData<List<Beat>> getBeats(int measureNum) {
//        return mDatabase.measureDao().getBeats(measureNum);
//    }

    public void destroyDatabase() {

    }
}
