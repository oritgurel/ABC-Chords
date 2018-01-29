package com.oritmalki.mymusicapp2.database;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import com.oritmalki.mymusicapp2.model.Measure;

import java.util.List;

/**
 * Created by Orit on 14.12.2017.
 */

public class DataRepository {

    private static DataRepository sInstance;
    private final AppDataBase mDatabase;
    private MediatorLiveData<List<Measure>> mObservableMeasures;

//    private final MeasureDao measureDAO;
//    private final BeatDao beatDao;


    //private constructor
    private DataRepository(final AppDataBase database) {
        mDatabase = database;
        mObservableMeasures = new MediatorLiveData<>();

        mObservableMeasures.addSource(mDatabase.measureDao().getAll(), new Observer<List<Measure>>() {
            @Override
            public void onChanged(@Nullable List<Measure> measureEntities) {
                if (mDatabase.getDatabaseCreated().getValue() != null) {
                    mObservableMeasures.postValue(measureEntities);
                }
            }
        });
    }


    //new constructor from example
    public static DataRepository getInstance(final AppDataBase database) {
        if (sInstance == null) {
            synchronized (DataRepository.class) {
                if (sInstance == null) {
                    sInstance = new DataRepository(database);
                }
            }
        }
        return sInstance;
    }

//Room Measures DAO

    public void addNewMeasure(Measure measure) {
        mDatabase.measureDao().newMeasure(measure);
    }
    public void addMeasures(List<Measure> measures) {
        mDatabase.measureDao().insertAll(measures);
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
        mDatabase.measureDao().delete(measure);
    }

//    public LiveData<List<Beat>> getBeats(int measureNum) {
//        return mDatabase.measureDao().getBeats(measureNum);
//    }

    public void deleteAllMeasures(List<Measure> measures) {
        mDatabase.measureDao().deleteAll(measures);
    }

    //my old constructor

//    public DataRepository(Context context) {
//        AppDataBase appDataBase = AppDataBase.getINSTANCE(context.getApplicationContext());
//        measureDAO = appDataBase.getMeasureDao();
//        beatDao = appDataBase.getBeatDao();
//    }

////Room Beat DAO
//    public void addBeats(List<Beat> beats, Measure measure) {
//
//        try {
//
//            if (beats.size() == measure.getTimeSignature().getNumerator())
////
//                beatDao.insertAll(beats);
//        }
//        catch (Exception e) {
//            Log.v("BeatsException", "Beats number do not match time signature");
//        }
//
//    }
//
//    public void addBeat(Beat beat, Measure measure) {
//
//
//            if (measure.getTimeSignature().getNumerator() > measure.getBeats().size())
//            beatDao.insertBeat(beat);
//
//        else Log.v("BeatsException", "Beats number do not match time signature");
//    }
//
//    public List<Beat> getBeatsFromMeasure(int measureNum) {
//        return measureDAO.findByNumber(measureNum).getBeats();
//
//    }
//
//    public List<Beat> getBeats() {
//        return measureDAO.getBeats();
//    }


//
//    private HashMap<Integer, List<Beat>> measureHashMap;
//

//




//    public void saveMeasure(Measure measure) {
//        measureHashMap.put(measure.getNumber(), measure.getBeats());
//    }
//
//    public List<Beat> getMeasureContent(int number) {
//        return measureHashMap.get(number);
//    }
//
//    public ArrayList<List<Beat>> getAllMeasuresContents() {
//        return new ArrayList<>(measureHashMap.values());
//    }
//
//
//    AppDataBase db = Room.databaseBuilder(getApplicationContext(), AppDataBase.class, "app-database").build();

    }

