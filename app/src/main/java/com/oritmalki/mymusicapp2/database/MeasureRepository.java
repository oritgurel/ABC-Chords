package com.oritmalki.mymusicapp2.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.util.Log;

import com.oritmalki.mymusicapp2.AppExecutors;
import com.oritmalki.mymusicapp2.model.Beat;
import com.oritmalki.mymusicapp2.model.Measure;

import java.util.List;
import java.util.TreeMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Orit on 14.12.2017.
 */

public class MeasureRepository {

    private AtomicInteger atomicMeasureNum = new AtomicInteger();
    CountDownLatch startSignal = new CountDownLatch(1);

    private static MeasureRepository sInstance;
    private final AppDataBase mDatabase;
    private MediatorLiveData<List<Measure>> mObservableMeasures;

    //my addition
    public AppExecutors appExecutors = new AppExecutors();


    //private constructor
    private MeasureRepository(final AppDataBase database) {

        mDatabase = database;

        mObservableMeasures = new MediatorLiveData<>();

        mObservableMeasures.addSource(mDatabase.measureDao().getAll(), new Observer<List<Measure>>() {
            @Override
            public void onChanged(@Nullable List<Measure> measureEntities) {
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    appExecutors.diskIO().execute(() ->
                            mObservableMeasures.postValue(measureEntities));
                    startSignal.countDown();
                }
            }
        });
    }

    //new constructor from example
    public static MeasureRepository getInstance(final AppDataBase database) {
        if (sInstance == null) {
            synchronized (MeasureRepository.class) {
                if (sInstance == null) {
                    sInstance = new MeasureRepository(database);
                }
            }
        }
        return sInstance;
    }

    /*****Room Measures DAO*****/

    public void addNewMeasure(Measure measure, AtomicBoolean lock) {

        appExecutors.diskIO().execute(new Runnable() {
            @Override
            public void run() {

                if (!lock.get()) {
                    lock.set(true);
                    mDatabase.measureDao().newMeasure(measure);
                }
                lock.set(false);
            }


        });

        Log.d("ADD_MEASURE", "Added empty measure to database");

    }

    public void addAllMeasures(List<Measure> measures) {
        appExecutors.diskIO().execute(() ->
                mDatabase.measureDao().insertAll(measures));
    }

    public LiveData<List<Measure>> getAllMeasures() {

        return mObservableMeasures;
    }

    public LiveData<Measure> getMeasure(int measureNum) {

        return mDatabase.measureDao().getMeasure(measureNum);
    }


    public void InsertMeasure(Measure measure) {

        mDatabase.measureDao().newMeasure(measure);
    }

    public void deleteMeasure(Measure measure) {
        appExecutors.diskIO().execute(() ->
                mDatabase.measureDao().delete(measure));
    }

    public void updateMeasure(Measure measure) {
        appExecutors.diskIO().execute(() ->
                mDatabase.measureDao().updateMeasure(measure));
    }

//    public LiveData<List<Beat>> getBeats(int measureNum) {
//        return mDatabase.measureDao().getBeats(measureNum);
//    }

    public void deleteAllMeasures(List<Measure> measures) {
        appExecutors.diskIO().execute(() ->
                mDatabase.measureDao().deleteAll(measures));
    }

    public void destroyDatabase() {

    }

    /*****Local App Memory DAO*****/

    private TreeMap<Integer, Measure> measureTreeMap = new TreeMap<>();

    public TreeMap<Integer, Measure> getMeasureTreeMap() {
        return measureTreeMap;
    }

    public void setMeasureTreeMap(TreeMap<Integer, Measure> measureTreeMap) {
        this.measureTreeMap = measureTreeMap;
    }

    public void addMeasureLocal(Measure measure) {
        measureTreeMap.put(measure.getNumber(), measure);
    }

    public void deleteMeasureLocal(int measureNumber) {
        measureTreeMap.remove(measureNumber);
    }

    public void updateMeasureLocal(int measureNumber, List<Beat> beatsList) {
        Measure editMeasure = measureTreeMap.get(measureNumber);
        editMeasure.setBeats(beatsList);
    }

    public List<Measure> getAllMeasuresLocal() {
        return (List<Measure>) measureTreeMap.values();
    }

    public void setListOfMeasuresLocal(List<Measure> measuresList) {
        for (Measure measure : measuresList) {
            measureTreeMap.put(measure.getNumber(), measure);
        }
    }
}
