package edu.miami.cs.enzo_carvalho.phloggingproject3;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

//=================================================================================================
@Dao
public interface DataRoomAccess {
    //-------------------------------------------------------------------------------------------------
    @Query("SELECT * FROM Phloggs ORDER BY _id ASC")
    List<DataRoomEntity> fetchAllPhloggs();
//----Could also return a List<DataRoomEntity>, which is useful for a SimpleAdapter use

    @Query("SELECT * FROM Phloggs where _id LIKE :this_id")
    DataRoomEntity getPhloggById(long this_id);

    @Query("SELECT * FROM Phloggs where phloggID LIKE :this_id")
    DataRoomEntity getPhloggByPhloggId(long this_id);

    @Query("SELECT * FROM Phloggs where time LIKE :thisTime")
    DataRoomEntity getPhloggByTime(String thisTime);

    @Insert
    void addPhlogg(DataRoomEntity newPhlogg);

    @Delete
    void deletePhlogg(DataRoomEntity oldPhlogg);

    @Update
    void updatePhlogg(DataRoomEntity newPhlogg);
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================