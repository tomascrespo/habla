package com.crpilarsoubrier.habla.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface PictoDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Picto picto);

    @Update
    void update(Picto picto);

    @Query("DELETE FROM picto")
    void deleteAll();

    @Query("SELECT * FROM picto ORDER BY id ASC")
    LiveData<List<Picto>> getAll();

}