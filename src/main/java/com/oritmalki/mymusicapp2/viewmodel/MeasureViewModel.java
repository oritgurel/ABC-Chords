package com.oritmalki.mymusicapp2.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.databinding.ObservableField;
import android.support.annotation.NonNull;

import com.oritmalki.mymusicapp2.BasicApp;
import com.oritmalki.mymusicapp2.database.DataRepository;
import com.oritmalki.mymusicapp2.model.Measure;


/**
 * Created by Orit on 27.1.2018.
 */

public class MeasureViewModel extends AndroidViewModel {

    private final LiveData<Measure> mObservableMeasure;
    public ObservableField<Measure> measure = new ObservableField<>();
    private final int mMeasureNumber;
//    private final LiveData<List<Beat>> mObservableBeats;

    public MeasureViewModel(@NonNull Application application, DataRepository repository, final int measureNumber) {
        super(application);
        mMeasureNumber = measureNumber;
        mObservableMeasure = repository.getMeasure(mMeasureNumber);
//        mObservableBeats = repository.getBeats(mMeasureNumber);
    }

    /**
     * Expose the LiveData Beats query so the UI can observe it.
     */

//    public LiveData<List<Beat>> getObservableBeats() {
//
//        return mObservableBeats;
//    }

    public LiveData<Measure> getObservableMeasure() {
        return mObservableMeasure;
    }

    public static class Factory extends ViewModelProvider.NewInstanceFactory {

        @NonNull
        private final Application mApplication;
        private final int mMeasureNumber;
        private final DataRepository mRepository;

        public Factory(@NonNull Application application, int measureNum) {
            mApplication = application;
            mMeasureNumber = measureNum;
            mRepository = ((BasicApp) application).getRepository();
        }
        @Override
        public <T extends ViewModel> T create(Class<T> modelClass) {
            //noinspection unchecked
            return (T) new MeasureViewModel(mApplication, mRepository, mMeasureNumber);
        }
    }

}
