package com.crpilarsoubrier.habla.data;

import androidx.annotation.Nullable;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "picto",
        foreignKeys = {@ForeignKey(entity = Picto.class, parentColumns = {"picto_id"}, childColumns = {"parent_picto_id"})},
        indices = {@Index("parent_picto_id")})
public class Picto {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "picto_id") public long pictoId;

    @ColumnInfo(name = "parent_picto_id") public Long parentId = null; // Long allows null values (vs long which does not allow it)

    public String picFilePath;

    public String text;

    public String audioFilePath = null;

    public boolean shouldBeRead = true;

    //@Relation(parentColumn = "picto_id", entityColumn = "parent_picto_id")
    //private List<Picto> children = new ArrayList<>();

    public Picto(String picFilePath, String text) {
        this.picFilePath = picFilePath;
        this.text = text;
    }

    public long getId() { return pictoId; }
    public void setId(long pictoId) { this.pictoId = pictoId; }

    public long getParentId() { return parentId; }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

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

    public boolean hasParent(){
        return !(this.parentId == null);
    }

    /**
     * Some pictos acts as categories, and in some cases when you select one of these
     * it has not to be read
     * @return
     */
    public boolean shouldBeRead() { return this.shouldBeRead; }

    @Override
    public boolean equals(@Nullable Object obj) {
        return obj instanceof Picto
                && this.pictoId == (((Picto) obj).pictoId) && this.text.equals((((Picto) obj).text));
    }

}


