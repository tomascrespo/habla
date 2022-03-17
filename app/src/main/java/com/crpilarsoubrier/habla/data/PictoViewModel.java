package com.crpilarsoubrier.habla.data;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.crpilarsoubrier.habla.ui.dashboard.PictoRecyclerViewAdapter;

import java.util.List;

public class PictoViewModel extends AndroidViewModel {

    private PictoRepository pictoRepository;
    private LiveData<List<Picto>> allPictos;
    public LiveData<List<Picto>> currentCategoryPictos;
    private static Long MAIN_CATEGORY = null;
    private MutableLiveData<Long> currentCategory = new MutableLiveData<>(MAIN_CATEGORY);

    public PictoViewModel (Application application) {
        super(application);
        pictoRepository = new PictoRepository(application);
        allPictos = pictoRepository.getAllPictos();
        currentCategoryPictos = Transformations.switchMap(currentCategory, filter ->
                pictoRepository.getPictosByCategory(filter)); // When currentCategory changes, transformation is applied and currentCategoryPictos are updated
    }

    public LiveData<List<Picto>> getAllPictos() { return allPictos; }

    public void setCategory(Long category) {
        this.currentCategory.postValue(category);
    }

    public void insert(Picto picto) {
        pictoRepository.insert(picto);
    }

    public void delete(long pictoId){
        pictoRepository.delete(pictoId);
    }
}
