package com.oritmalki.mymusicapp2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;

import com.oritmalki.mymusicapp2.BasicApp;
import com.oritmalki.mymusicapp2.model.Beat;
import com.oritmalki.mymusicapp2.model.Measure;
import com.oritmalki.mymusicapp2.model.Sheet;
import com.oritmalki.mymusicapp2.model.TimeSignature;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class SheetListViewModel extends AndroidViewModel {


    // MediatorLiveData can observe other LiveData objects and react on their emissions.
    private final MediatorLiveData<List<Sheet>> mObservableSheets;

    public SheetListViewModel(Application application) {
        super(application);

        mObservableSheets = new MediatorLiveData<>();

        // set by default null, until we get data from the database.
        mObservableSheets.setValue(null);


        LiveData<List<Sheet>> sheets = ((BasicApp) application).getSheetRepository()
                .getAllSheets();

        // observe the changes of the measures from the database and forward them
        mObservableSheets.addSource(sheets, mObservableSheets::setValue);
    }

    /**
     * Expose the LiveData Products query so the UI can observe it.
     */
    public LiveData<List<Sheet>> getSheets() {
        return mObservableSheets;
    }

    public void updateSheet(Application application, Sheet sheet) {
        ((BasicApp) application).getSheetRepository().updateSheet(sheet);
    }

    public void createNewSheet(Application application, int numOfMeasures, TimeSignature timeSig, AtomicBoolean lock) {

        List<Beat> emptyBeats = new ArrayList<>();
        for (int i = 0; i < timeSig.getNumerator(); i++) {
            emptyBeats.add(new Beat("  "));
        }

        List<Measure> defaultMeasureList = new ArrayList<>();
        int i=1;
        while (i<numOfMeasures) {
            defaultMeasureList.add(new Measure(i, emptyBeats, timeSig, true));
        }

        //insert empty sheet
            ((BasicApp) application).getSheetRepository().addNewSheet(new Sheet(defaultMeasureList), lock);

    }




    public void deleteSheet(Application application, Sheet sheet) {
        if (mObservableSheets.getValue().size() != 0) {
            ((BasicApp) application).getSheetRepository().deleteSheet(sheet);
        }

    }
}
