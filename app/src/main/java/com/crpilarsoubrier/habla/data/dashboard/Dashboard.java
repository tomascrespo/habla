package com.crpilarsoubrier.habla.data.dashboard;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "dashboard")
public class Dashboard {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "dashboard_id") public long dashboardId;

    public String dashboardName;

    boolean showPhraseBox = true;

    boolean showPictosText = true;

    boolean showFixedPictos = true;

    int fixedPictosSize = 4;

    int mainPictosSize = 4;

    @Ignore
    public Dashboard(String dashboardName, boolean showPhrase, boolean showPictoTexts, int fixedPictosSize, int mainPictosSize){
        this.dashboardName = dashboardName;
        this.showPhraseBox = showPhrase;
        this.fixedPictosSize = fixedPictosSize;
        this.mainPictosSize = mainPictosSize;
        this.showPictosText = showPictoTexts;
    }


    public Dashboard(long dashboardId, String dashboardName){
        this.dashboardId = dashboardId;
        this.dashboardName = dashboardName;
    }
}
