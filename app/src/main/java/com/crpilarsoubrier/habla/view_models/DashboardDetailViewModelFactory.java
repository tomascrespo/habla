package com.crpilarsoubrier.habla.view_models;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.crpilarsoubrier.habla.data.dashboard.DashboardRepository;

public class DashboardDetailViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private DashboardRepository dashboardRepository;
    private long dashboardId;

    public DashboardDetailViewModelFactory(DashboardRepository dashboardRepository, long dashboardId) {
        this.dashboardRepository = dashboardRepository;
        this.dashboardId = dashboardId;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new DashboardDetailViewModel(dashboardRepository, dashboardId);
    }
}
