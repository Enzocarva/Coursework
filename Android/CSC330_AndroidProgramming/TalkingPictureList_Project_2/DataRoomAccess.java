package edu.miami.cs.enzo_carvalho.talking_picture_listproject2;

import android.database.Cursor;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
//=================================================================================================
@Dao
public interface DataRoomAccess {
//-------------------------------------------------------------------------------------------------
    @Query("SELECT * FROM DescribedImages ORDER BY _id ASC")
    Cursor fetchAllImages();
//----Could also return a List<DataRoomEntity>, which is useful for a SimpleAdapter use

    @Query("SELECT * FROM DescribedImages where _id LIKE :this_id")
    DataRoomEntity getImageById(long this_id);

    @Query("SELECT * FROM DescribedImages where imageID LIKE :this_id")
    DataRoomEntity getImageByImageId(long this_id);

    @Insert
    void addImage(DataRoomEntity newImage);

    @Delete
    void deleteImage(DataRoomEntity oldImage);

    @Update
    void updateImage(DataRoomEntity newImage);
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================

