package com.isro.geofauna.data;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class GeofaunaRepository {

    private GeofaunaDao mGeoDao;
    private LiveData<List<Geofauna>> mAllByDate;
    private LiveData<List<Geofauna>> mAll;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    GeofaunaRepository(Application application) {
        GeofaunaRoomDatabase db = GeofaunaRoomDatabase.getDatabase(application);
        mGeoDao = db.geofaunaDao();
        mAllByDate = mGeoDao.getAllByDate();
        mAll = mGeoDao.getAll();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Geofauna>> getAllByDate() {
        return mAllByDate;
    }

    LiveData<List<Geofauna>> getAll() {
        return mAll;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insertAll(Geofauna geofauna) {
        GeofaunaRoomDatabase.databaseWriteExecutor.execute(() -> {
            mGeoDao.insertAll(geofauna);
        });
    }

    public void deleteAll()  {
        new deleteAllAsyncTask(mGeoDao).execute();
    }

    public void deleteRecord(Geofauna word)  {
        new deleteRecordAsyncTask(mGeoDao).execute(word);
    }

    private static class deleteRecordAsyncTask extends AsyncTask<Geofauna, Void, Void> {
        private GeofaunaDao mAsyncTaskDao;

        deleteRecordAsyncTask(GeofaunaDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Geofauna... params) {
            mAsyncTaskDao.deleteRecord(params[0]);
            return null;
        }
    }


    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private GeofaunaDao mAsyncTaskDao;

        deleteAllAsyncTask(GeofaunaDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mAsyncTaskDao.deleteAll();
            return null;
        }
    }

}
