package com.crpilarsoubrier.habla.data.dashboard;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PictoInDashboardDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(PictoInDashboardEntity pictoInDashboardEntity);

    @Update
    void update(PictoInDashboardEntity pictoInDashboardEntity);

    @Query("DELETE FROM picto_in_dashboard")
    void deleteAll();

    @Query("SELECT * FROM picto_in_dashboard ORDER BY picto_in_dashboard_id ASC")
    List<PictoInDashboardEntity> getAll();

    @Query("SELECT * FROM picto_in_dashboard WHERE dashboard_id = :dashboardId")
    LiveData<List<PictoInDashboardEntity>> getPictosInDashboard(Long dashboardId);

}
