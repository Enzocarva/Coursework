package edu.miami.cs.enzo_carvalho.phloggingproject3;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

//=================================================================================================
@Entity(tableName = "Phloggs")
public class DataRoomEntity {
//-------------------------------------------------------------------------------------------------
    @PrimaryKey(autoGenerate = true)
    private int _id;
    @ColumnInfo(name = "phloggID")
    private long phloggID;
    @ColumnInfo(name = "title")
    private String title;
    @ColumnInfo(name = "description")
    private String description;
    @ColumnInfo(name = "photoUri")
    private String photoUri;
    @ColumnInfo(name = "time")
    private String time;
    @ColumnInfo(name = "latitude")
    private double latitude;
    @ColumnInfo(name = "longitude")
    private double longitude;
//-------------------------------------------------------------------------------------------------
    public DataRoomEntity() {
    }
//-------------------------------------------------------------------------------------------------
    public int getId() {
        return(_id);
    }
//-------------------------------------------------------------------------------------------------
    public long getPhloggID() {
        return(phloggID);
    }
//-------------------------------------------------------------------------------------------------
    public String getTitle() {
        return(title);
    }
//-------------------------------------------------------------------------------------------------
    public String getDescription() {
        return(description);
    }
//-------------------------------------------------------------------------------------------------
    public String getPhotoUri() {
        return(photoUri);
    }
//-------------------------------------------------------------------------------------------------
    public String getTime() {
        return(time);
    }
//-------------------------------------------------------------------------------------------------
    public double getLatitude() {
        return(latitude);
    }
//-------------------------------------------------------------------------------------------------
    public double getLongitude() {
        return(longitude);
    }
//-------------------------------------------------------------------------------------------------
    public void setId(int newId) { _id = newId; }
//-------------------------------------------------------------------------------------------------
    public void setPhloggID(long newId) { phloggID = newId; }
//-------------------------------------------------------------------------------------------------
    public void setTitle(String newTitle) { title = newTitle; }
//-------------------------------------------------------------------------------------------------
    public void setDescription(String newDescription) { description = newDescription; }
//-------------------------------------------------------------------------------------------------
    public void setPhotoUri(String newPhotoUri) { photoUri = newPhotoUri; }
//-------------------------------------------------------------------------------------------------
    public void setTime(String newTime) { time = newTime; }
//-------------------------------------------------------------------------------------------------
    public void setLatitude(double newLatitude) { latitude = newLatitude; }
//-------------------------------------------------------------------------------------------------
    public void setLongitude(double newLongitude) { longitude = newLongitude; }
//-------------------------------------------------------------------------------------------------
}
//=================================================================================================