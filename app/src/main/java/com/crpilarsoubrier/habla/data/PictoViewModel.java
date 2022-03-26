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
    private LiveData<List<PictoWithChildren>> allPictosWithChildren;
    //public LiveData<List<Picto>> currentCategoryPictos;
    public LiveData<List<PictoWithChildren>> currentCategoryPictosWithChildren;
    private static Long MAIN_CATEGORY = null;
    private MutableLiveData<Long> currentCategory = new MutableLiveData<>(MAIN_CATEGORY);

    public PictoViewModel (Application application) {
        super(application);
        pictoRepository = new PictoRepository(application);
        allPictos = pictoRepository.getAllPictos();
        allPictosWithChildren = pictoRepository.getAllPictosWithChildren();
/*
        currentCategoryPictos = Transformations.switchMap(currentCategory, (filter) ->
                pictoRepository.getPictosByCategory(filter)); // When currentCategory changes, transformation is applied and currentCategoryPictos are updated
*/
        currentCategoryPictosWithChildren = Transformations.switchMap(currentCategory, filter ->
            pictoRepository.getPictosByCategoryWithChildren(filter)); // When currentCategory changes, transformation is applied and currentCategoryPictos are updated
    }

    public LiveData<List<Picto>> getAllPictos() { return allPictos; }

    public LiveData<List<PictoWithChildren>> getAllPictosWithChildren() { return allPictosWithChildren; }

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
