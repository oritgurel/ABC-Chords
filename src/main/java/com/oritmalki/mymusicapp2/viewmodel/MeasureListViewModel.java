package com.oritmalki.mymusicapp2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.oritmalki.mymusicapp2.BasicApp;
import com.oritmalki.mymusicapp2.model.Beat;
import com.oritmalki.mymusicapp2.model.Measure;
import com.oritmalki.mymusicapp2.model.TimeSignature;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Orit on 27.1.2018.
 */

public class MeasureListViewModel extends AndroidViewModel {

    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Measure>> mObservableMeasures;

    public MeasureListViewModel(Application application) {
        super(application);

        mObservableMeasures = new MediatorLiveData<>();

        // set by default null, until we get data from the database.
        mObservableMeasures.setValue(null);

        LiveData<List<Measure>> measures = ((BasicApp) application).getRepository()
                .getAllMeasures();

        // observe the changes of the measures from the database and forward them
        mObservableMeasures.addSource(measures, mObservableMeasures::setValue);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<Measure>> getMeasures() {
        return mObservableMeasures;
    }

    public void addMeasure() {

        TimeSignature lastMesTimeSignature = mObservableMeasures.getValue().get(mObservableMeasures.getValue().size()-1).getTimeSignature();
        List <Beat> emptyBeats = new ArrayList<>();
        for (int i=0; i<lastMesTimeSignature.getNumerator(); i++) {
            emptyBeats.add(new Beat("  "));
        }
//        mObservableMeasures.setValue(mObservableMeasures.getValue().add(new Measure(mObservableMeasures.getValue().size() + 1, emptyBeats, lastMesTimeSignature, true)));
    }
}
