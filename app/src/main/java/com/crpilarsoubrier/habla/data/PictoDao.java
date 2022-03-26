package com.crpilarsoubrier.habla.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;
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

    @Query("SELECT * FROM picto ORDER BY text ASC")
    LiveData<List<Picto>> getAll();

    @Query("SELECT * FROM picto WHERE parent_picto_id = :parentId")
    LiveData<List<Picto>> getPictosByParentId(Long parentId);

    @Transaction
    @Query("SELECT * FROM picto WHERE parent_picto_id = :parentId or (parent_picto_id is null and :parentId is null)")
    LiveData<List<PictoWithChildren>> getPictosByParentIdWithChildren(Long parentId);

    @Query("SELECT * FROM picto WHERE picto_id = :pictoId")
    Picto getPictoById(long pictoId);

    @Query("DELETE FROM picto WHERE picto_id = :pictoId")
    void delete(long pictoId);

    @Transaction
    @Query("SELECT * FROM picto")
    public LiveData<List<PictoWithChildren>> getPictosWithChildren();

}
