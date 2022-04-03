package com.crpilarsoubrier.habla.data.dashboard;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import com.crpilarsoubrier.habla.data.Picto;

/**
 * Represent a picto in a dashboard. It adds some info to a picto, like if is showed at root or not
 * @todo We could divide this class into two, one for fixedPictoInDashboard and other for mainPictoInDashboard
 */
@Entity(tableName = "picto_in_dashboard",
        foreignKeys = {@ForeignKey(entity = Dashboard.class, parentColumns = {"dashboard_id"}, childColumns = {"dashboard_id"}),
                       @ForeignKey(entity = Picto.class, parentColumns = {"picto_id"}, childColumns = {"picto_id"})},
        indices = {@Index("dashboard_id"), @Index("picto_id")}
)

public class PictoInDashboardEntity {

    public static final int FIXED_PICTO = 0;
    public static final int MAIN_PICTO = 1;

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "picto_in_dashboard_id")
    long pictoInDashboardId;

    @ColumnInfo(name = "dashboard_id")
    long dashboardId;

    @ColumnInfo(name = "picto_id")
    long pictoId;

    boolean showInRoot = false;

    int type = 0; // 0 for fixed, 1 for main

    @Ignore
    public PictoInDashboardEntity(long pictoInDashboardId, long dashboardId, long pictoId, int type){
        this.pictoInDashboardId = pictoInDashboardId;
        this.dashboardId = dashboardId;
        this.pictoId = pictoId;
    }

    public PictoInDashboardEntity(long dashboardId, long pictoId, boolean showInRoot, int type){
        this.dashboardId = dashboardId;
        this.pictoId = pictoId;
        this.showInRoot = showInRoot;
        this.type = type;
    }

}
