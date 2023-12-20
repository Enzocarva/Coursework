package edu.miami.cs.enzo_carvalho.talking_picture_listproject2;

import androidx.room.Database;
import androidx.room.RoomDatabase;

//=================================================================================================
@Database(entities = {DataRoomEntity.class},version = 1, exportSchema = false)
public abstract class DataRoomDB extends RoomDatabase {
//-------------------------------------------------------------------------------------------------
    public abstract DataRoomAccess daoAccess();
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================
