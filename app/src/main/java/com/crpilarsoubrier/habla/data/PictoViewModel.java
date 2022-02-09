package com.crpilarsoubrier.habla.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PictoViewModel extends AndroidViewModel {

    private PictoRepository pictoRepository;

    private final LiveData<List<Picto>> allPictos;

    public PictoViewModel (Application application) {
        super(application);
        pictoRepository = new PictoRepository(application);
        allPictos = pictoRepository.getAllPictos();
    }

    public LiveData<List<Picto>> getAllPictos() { return allPictos; }

    public void insert(Picto picto) { pictoRepository.insert(picto); }
}
