package com.isro.geofauna.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class GeofaunaViewModel extends AndroidViewModel {

    private GeofaunaRepository mRepository;

    private LiveData<List<Geofauna>> mAllByDate;

    public GeofaunaViewModel (Application application) {
        super(application);
        mRepository = new GeofaunaRepository(application);
        mAllByDate = mRepository.getAllByDate();
    }

    public LiveData<List<Geofauna>> getAllByDate() { return mAllByDate; }

    public void insertAll(Geofauna geofauna) { mRepository.insertAll(geofauna); }

    public void updateRecord(Geofauna geofauna) { mRepository.updateRecord(geofauna); }

    public void deleteAll() {mRepository.deleteAll();}

    public void deleteRecord(Geofauna geofauna) {mRepository.deleteRecord(geofauna);}

}
