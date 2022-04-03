package com.crpilarsoubrier.habla.view_models;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;
import androidx.lifecycle.ViewModel;

import com.crpilarsoubrier.habla.data.PictoWithChildren;
import com.crpilarsoubrier.habla.data.dashboard.Dashboard;
import com.crpilarsoubrier.habla.data.dashboard.DashboardRepository;
import com.crpilarsoubrier.habla.data.dashboard.PictoInDashboard;
import com.crpilarsoubrier.habla.data.dashboard.PictoInDashboardEntity;

import java.util.List;

public class DashboardListViewModel extends AndroidViewModel {

    private DashboardRepository dashboardRepository;
    public LiveData<List<Dashboard>> allDashboards;
    public LiveData<List<PictoInDashboardEntity>> currentDashboardPictosInDashboard;
    private static Long MAIN_CATEGORY = null;
    private MutableLiveData<Long> currentDashboard = new MutableLiveData<>(MAIN_CATEGORY);

    public DashboardListViewModel(Application application) {
        super(application);
        dashboardRepository = new DashboardRepository(application);
        allDashboards = dashboardRepository.allDashboards;
        currentDashboardPictosInDashboard = Transformations.switchMap(currentDashboard, filter ->
                dashboardRepository.getPictosInDashboard(filter));
    }
    public void addDashboard(String dashboardName, boolean showPhrase, boolean showPictoTexts, int fixedPictosSize, int mainPictosSize) {
        dashboardRepository.createDashboard(dashboardName, showPhrase, showPictoTexts, fixedPictosSize, mainPictosSize);
    }

    public void addDashboard(Dashboard dashboard) {
        dashboardRepository.createDashboard(dashboard);
    }

}