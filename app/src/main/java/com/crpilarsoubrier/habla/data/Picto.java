package com.crpilarsoubrier.habla.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.annotation.NonNull;

@Entity
public class Picto {

    @PrimaryKey(autoGenerate = true)
    public long id;
    public String picFilePath;
    public String text;
    public String audioFilePath = null;

    public Picto(String picFilePath, String text) {
        this.picFilePath = picFilePath;
        this.text = text;
    }

    public long getId() { return id; }

    public void setId(long id) { this.id = id; }

    public String getPicFilePath() {
        return picFilePath;
    }

    public String getText() {
        return text;
    }

    public String getAudioFile() {
        return audioFilePath;
    }

    public void setPicFilePath(String picFilePath) {
        this.picFilePath = picFilePath;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setAudioFile(String audioFilePath) {
        this.audioFilePath = audioFilePath;
    }


    public boolean hasAudio(){
        return !(this.audioFilePath == null);
    }

    // Load pictograms and categories from /assets and populates the database
    // Each folder is a category
    // Each pictogram in a folder is a pictogram of that category
    // A pic with the same name of the folder is the picture for the category
    // Folder name and filename are the text of category or pictogram.
    // Pictures in root folder are uncategorized pictograms
    public static void LoadFromAssets() {

    }
}


