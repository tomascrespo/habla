package com.crpilarsoubrier.habla.data;

import androidx.lifecycle.LiveData;
import androidx.room.Embedded;
import androidx.room.Relation;

import java.util.List;

/**
 * Define a Picto with its children in case it has some.
 * This is an intermediate class, necessary to build a
 * various to one relationship.
 * Show documentation at https://developer.android.com/training/data-storage/room/relationships
 * Another option would be returning a multi-map
 */
public class PictoWithChildren {
    @Embedded public Picto picto;
    @Relation(parentColumn = "picto_id", entityColumn = "parent_picto_id")
    public List<Picto> children;

    public boolean isCategory(){
        return children.size() > 0;
    }
}
