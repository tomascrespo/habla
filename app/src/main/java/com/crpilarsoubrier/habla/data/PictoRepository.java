package com.crpilarsoubrier.habla.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

class PictoRepository {

    private PictoDao pictoDao;
    private LiveData<List<Picto>> allPictos;

    PictoRepository(Application application) {
        PictoRoomDatabase db = PictoRoomDatabase.getDatabase(application);
        pictoDao = db.pictoDao();
        allPictos = pictoDao.getAll();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Picto>> getAllPictos() {
        return allPictos;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Picto picto) {
        PictoRoomDatabase.databaseWriteExecutor.execute(() -> {
            pictoDao.insert(picto);
        });
    }
}
