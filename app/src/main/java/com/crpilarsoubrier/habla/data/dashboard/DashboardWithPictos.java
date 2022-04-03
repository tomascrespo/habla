package com.crpilarsoubrier.habla.data.dashboard;

import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

public class DashboardWithPictos {
    @Embedded
    Dashboard dashboard;

    @Relation(entityColumn = "dashboard_id", parentColumn = "dashboard_id")
    List<PictoInDashboard> pictosInDashboard;

    public List<PictoInDashboard> getPictosInDashboard(){
        return this.pictosInDashboard;
    }
}
