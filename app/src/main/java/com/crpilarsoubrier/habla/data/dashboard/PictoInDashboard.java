package com.crpilarsoubrier.habla.data.dashboard;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.crpilarsoubrier.habla.data.Picto;

public class PictoInDashboard {
    @Embedded
    PictoInDashboardEntity pictoInDashboardEntity;

    @Relation(parentColumn = "dashboard_id", entityColumn = "dashboard_id")
    Dashboard dashboard;

    @Relation(parentColumn = "picto_id", entityColumn = "picto_id")
    Picto picto;

}
