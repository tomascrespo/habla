package com.crpilarsoubrier.habla.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;


public class PictoRepository {

    private PictoDao pictoDao;
    private LiveData<List<Picto>> allPictos;
    private LiveData<List<PictoWithChildren>> allPictosWithChildren;

    public PictoRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        pictoDao = db.pictoDao();
        allPictos = pictoDao.getAll();
        allPictosWithChildren = pictoDao.getPictosWithChildren();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    public LiveData<List<Picto>> getAllPictos() {
        return allPictos;
    }

    public LiveData<List<PictoWithChildren>> getAllPictosWithChildren() {
        return allPictosWithChildren;
    }


    public LiveData<List<Picto>> getPictosByCategory(Long category) {
        return pictoDao.getPictosByParentId(category);
    }

    public LiveData<List<PictoWithChildren>> getPictosByCategoryWithChildren(Long category) {
        return pictoDao.getPictosByParentIdWithChildren(category);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    public void insert(Picto picto) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pictoDao.insert(picto);
        });
    }

    public void delete(Long pictoId) {
        AppDatabase.databaseWriteExecutor.execute(() -> {
            pictoDao.delete(pictoId);
        });
    }
}
