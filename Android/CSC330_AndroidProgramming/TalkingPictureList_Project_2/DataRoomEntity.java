package edu.miami.cs.enzo_carvalho.talking_picture_listproject2;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//=================================================================================================
@Entity(tableName = "DescribedImages")
public class DataRoomEntity {
//-------------------------------------------------------------------------------------------------
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @ColumnInfo(name = "imageID")
    private long imageID;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "recording")
    private byte[] recording;
//-------------------------------------------------------------------------------------------------
    public DataRoomEntity() {
    }
//-------------------------------------------------------------------------------------------------
    public int getId() {
        return(_id);
    }
//-------------------------------------------------------------------------------------------------
    public long getImageID() {
        return(imageID);
    }
//-------------------------------------------------------------------------------------------------
    public String getDescription() {
        return(description);
    }
//-------------------------------------------------------------------------------------------------
    public byte[] getRecording() {
        return(recording);
    }
//-------------------------------------------------------------------------------------------------
    public void setId(int newId) { _id = newId; }
//-------------------------------------------------------------------------------------------------
    public void setImageID(long newId) { imageID = newId; }
//-------------------------------------------------------------------------------------------------
    public void setDescription(String newDescription) { description = newDescription; }
//-------------------------------------------------------------------------------------------------
    public void setRecording(byte[] newRecording) { recording = newRecording; }
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================