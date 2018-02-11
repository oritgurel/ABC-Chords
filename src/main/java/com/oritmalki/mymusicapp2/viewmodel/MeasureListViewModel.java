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

    //something is wrong here.
    public void addEmptyMeasure(Application application) {

        //prepare the time signature and beatList
        TimeSignature lastMesTimeSignature;
        if (mObservableMeasures.getValue().size() != 0) {
            lastMesTimeSignature = mObservableMeasures.getValue().get(mObservableMeasures.getValue().size() - 1).getTimeSignature();
        } else {
            lastMesTimeSignature = new TimeSignature(4,4);
        }

        List<Beat> emptyBeats = new ArrayList<>();
        for (int i = 0; i < lastMesTimeSignature.getNumerator(); i++) {
            emptyBeats.add(new Beat("  "));
        }

        //insert empty measure
        if (mObservableMeasures.getValue().size() != 0) {
            ((BasicApp) application).getRepository().addNewMeasure(new Measure(mObservableMeasures.getValue().size() + 1, emptyBeats, lastMesTimeSignature, true));
        } else
            ((BasicApp) application).getRepository().addNewMeasure(new Measure(0, emptyBeats, lastMesTimeSignature, true));

    }




    public void deleteMeasure(Application application) {
        if (mObservableMeasures.getValue().size() != 0) {
            ((BasicApp) application).getRepository().deleteMeasure(mObservableMeasures.getValue().get(mObservableMeasures.getValue().size() - 1));
        }

    }
}
