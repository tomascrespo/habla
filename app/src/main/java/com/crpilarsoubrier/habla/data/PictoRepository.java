package com.crpilarsoubrier.habla.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;


class PictoRepository {

    private PictoDao pictoDao;
    private LiveData<List<Picto>> allPictos;
    private LiveData<List<PictoWithChildren>> allPictosWithChildren;

    PictoRepository(Application application) {
        PictoRoomDatabase db = PictoRoomDatabase.getDatabase(application);
        pictoDao = db.pictoDao();
        allPictos = pictoDao.getAll();
        allPictosWithChildren = pictoDao.getPictosWithChildren();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<Picto>> getAllPictos() {
        return allPictos;
    }

    LiveData<List<PictoWithChildren>> getAllPictosWithChildren() {
        return allPictosWithChildren;
    }


    LiveData<List<Picto>> getPictosByCategory(Long category) {
        return pictoDao.getPictosByParentId(category);
    }

    LiveData<List<PictoWithChildren>> getPictosByCategoryWithChildren(Long category) {
        return pictoDao.getPictosByParentIdWithChildren(category);
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(Picto picto) {
        PictoRoomDatabase.databaseWriteExecutor.execute(() -> {
            pictoDao.insert(picto);
        });
    }

    void delete(Long pictoId) {
        PictoRoomDatabase.databaseWriteExecutor.execute(() -> {
            pictoDao.delete(pictoId);
        });
    }
}
