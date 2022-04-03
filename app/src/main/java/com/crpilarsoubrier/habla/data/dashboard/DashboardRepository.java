package com.crpilarsoubrier.habla.data.dashboard;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.lifecycle.LiveData;

import com.crpilarsoubrier.habla.data.AppDatabase;
import com.crpilarsoubrier.habla.data.PictoWithChildren;

import java.util.List;

public class DashboardRepository {

    private PictoInDashboardDao pictoInDashboardDao;
    private DashboardDao dashboardDao;
    public LiveData<List<Dashboard>> allDashboards;
    public LiveData<List<PictoInDashboardEntity>> allPictosInDashboard;
    private LiveData<List<PictoWithChildren>> allPictosWithChildren;

    public DashboardRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        pictoInDashboardDao = db.pictoInDashboardDao();

        dashboardDao = db.dashboardDao();
        allDashboards = dashboardDao.getAll();
        //allPictosInDashboard = pictoInDashboardDao.getAll();
        //allPictosWithChildren = pictoDao.getPictosWithChildren();
    }

    public Dashboard getDashboard(long dashboardId){
        Log.println(Log.INFO, "MyDebug", "Repository.getDashboard(" + dashboardId + "): " + dashboardDao.getDashboard(dashboardId));
        return dashboardDao.getDashboard(dashboardId);
    }

    public void createDashboard(String dashboardName, boolean showPhrase, boolean showPictoTexts, int fixedPictosSize, int mainPictosSize){
        Dashboard dashboard = new Dashboard(dashboardName, showPhrase, showPictoTexts, fixedPictosSize, mainPictosSize);
        createDashboard(dashboard);
    }

    public void createDashboard(Dashboard dashboard) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                dashboardDao.insert(dashboard);
            }
        });
    }

    public void createPictoInDashboardEntity(PictoInDashboardEntity pictoInDashboardEntity) {
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                pictoInDashboardDao.insert(pictoInDashboardEntity);
            }
        });
    }

    public void createPictoInDashboardEntity(long dashboardId, long pictoId, boolean showInRoot, int type) {
        PictoInDashboardEntity pictoInDashboardEntity = new PictoInDashboardEntity(dashboardId, pictoId, showInRoot, type);
        createPictoInDashboardEntity(pictoInDashboardEntity);
    }



        public LiveData<List<PictoInDashboardEntity>> getPictosInDashboard(Long dashboard) {
        return pictoInDashboardDao.getPictosInDashboard(dashboard);
    }


}
