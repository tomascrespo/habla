package com.crpilarsoubrier.habla.view_models;

import android.app.Application;

import androidx.lifecycle.ViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.crpilarsoubrier.habla.data.Picto;
import com.crpilarsoubrier.habla.data.dashboard.Dashboard;
import com.crpilarsoubrier.habla.data.dashboard.DashboardRepository;
import com.crpilarsoubrier.habla.data.dashboard.PictoInDashboardEntity;

import java.util.List;

public class DashboardDetailViewModel extends ViewModel {

    private DashboardRepository dashboardRepository;
    public LiveData<List<PictoInDashboardEntity>> pictosInDashboard;
    long dashboardId = 1;
    Dashboard dashboard;

    /**
     * @todo We should pass the dashboardId in the constructor, but we need to create a ViewModelFactory for that
     */
    public DashboardDetailViewModel(DashboardRepository dashboardRepository, long dashboardId) {
        super(); // necessary?
        this.dashboardRepository = dashboardRepository;
        this.dashboardId = dashboardId;
        dashboard = dashboardRepository.getDashboard(dashboardId);
        pictosInDashboard = dashboardRepository.getPictosInDashboard(dashboardId);
    }

    public void setDashboardId(long dashboardId){
        this.dashboardId = dashboardId;
        dashboard = dashboardRepository.getDashboard(dashboardId);
        pictosInDashboard = dashboardRepository.getPictosInDashboard(dashboardId);
    }

    public void addPictoToDashboard(long pictoId, boolean showInRoot, int type) {
        dashboardRepository.createPictoInDashboardEntity(this.dashboardId, pictoId, showInRoot, type);
    }


    public void addPictoToDashboard(Picto picto, boolean showInRoot, int type) {
        addPictoToDashboard(picto.pictoId, showInRoot, type);
    }

}