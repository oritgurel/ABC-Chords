package com.oritmalki.mymusicapp2;

import android.content.Context;
import android.util.Log;

import java.util.List;

/**
 * Created by Orit on 14.12.2017.
 */

public class DataRepository {

    private final MeasureDao measureDAO;
    private final BeatDao beatDao;


    public void addMeasures(List<Measure> measures) {
        measureDAO.insertAll(measures);
    }

    public List<Measure> getAllMeasures() {
       return measureDAO.getAll();
    }


    public void addBeats(List<Beat> beats, Measure measure) {

        try {

            if (beats.size() == measure.getTimeSignature().getNumerator())
//
                beatDao.insertAll(beats);
        }
        catch (Exception e) {
            Log.v("BeatsException", "Beats number do not match time signature");
        }

    }

    public void addBeat(Beat beat, Measure measure) {


            if (measure.getTimeSignature().getNumerator() > measure.getBeats().size())
            beatDao.insertBeat(beat);

        else Log.v("BeatsException", "Beats number do not match time signature");
    }

    public List<Beat> getBeatsFromMeasure(int measureNum) {
        return measureDAO.findByNumber(measureNum).getBeats();

    }

    public List<Beat> getBeats() {
        return measureDAO.getBeats();
    }


//
//    private HashMap<Integer, List<Beat>> measureHashMap;
//

//
    public DataRepository(Context context) {
        AppDataBase appDataBase = AppDataBase.getINSTANCE(context.getApplicationContext());
        measureDAO = appDataBase.getMeasureDao();
        beatDao = appDataBase.getBeatDao();
    }



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

