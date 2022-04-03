package com.crpilarsoubrier.habla.data.dashboard;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DashboardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Dashboard dashboard);

    @Update
    void update(Dashboard dashboard);

    @Query("DELETE FROM dashboard")
    void deleteAll();

    @Query("SELECT * FROM dashboard ORDER BY dashboard_id ASC")
    LiveData<List<Dashboard>> getAll();

    @Query("SELECT * FROM dashboard WHERE dashboard_id = :dashboardId")
    Dashboard getDashboard(Long dashboardId);

    @Transaction
    @Query("SELECT * FROM picto_in_dashboard")
    public List<PictoInDashboard> getAllPictosInDashboards();

    @Transaction
    @Query("SELECT * FROM picto_in_dashboard WHERE dashboard_id = :dashboardId")
    public List<PictoInDashboard> getPictosInDashboard(long dashboardId);

}
