package edu.miami.cs.enzo_carvalho.timedtextminiapp15;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

//==============================================================================================================================================================
public class DataSQLiteDB {
    public static final String DATABASE_NAME = "Thoughts.db";
    private static final int DATABASE_VERSION = 1;

    private static final String THOUGHTS_TABLE_NAME = "Thoughts";
    private static final String CREATE_THOUGHTS_TABLE = "CREATE TABLE IF NOT EXISTS " + THOUGHTS_TABLE_NAME + "(_id INTEGER PRIMARY KEY AUTOINCREMENT," +
             "thought_name TEXT," + "date TEXT);";

    private DatabaseHelper dbHelper;
    private SQLiteDatabase theDB;
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public DataSQLiteDB(Context context) {
        dbHelper = new DatabaseHelper(context);
        theDB = dbHelper.getWritableDatabase();
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public void close() {
        dbHelper.close();
        theDB.close();
    }
///--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public boolean addThought(ContentValues thoughtData) {
        return (theDB.insert(THOUGHTS_TABLE_NAME, null, thoughtData) >= 0);
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//    public boolean deleteThought(long thoughtID) {
//        return (theDB.delete(THOUGHTS_TABLE_NAME, "_id =" + thoughtID, null) > 0);
//    }
////--------------------------------------------------------------------------------------------------------------------------------------------------------------
//    public boolean updateThought(long thoughtID, ContentValues thoughtData) {
//        return (theDB.update(THOUGHTS_TABLE_NAME, thoughtData, "_id =" + thoughtID, null) > 0);
//    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    public Cursor fetchAllData() {
        String[] fieldNames = {"_id", "thought_name", "date"};

        return (theDB.query(THOUGHTS_TABLE_NAME, fieldNames, null, null, null, null, "_id"));
    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//    public ContentValues getThoughtById(long thoughtID) {
//
//        Cursor cursor;
//        ContentValues thoughtData;
//
//        cursor = theDB.query(CREATE_THOUGHTS_TABLE,null,"_id = \"" + thoughtID + "\"",null,null,null,
//                null);
//        thoughtData = thoughtDataFromCursor(cursor);
//        cursor.close();
//        return(thoughtData);
//}
////--------------------------------------------------------------------------------------------------------------------------------------------------------------
//    public ContentValues getSongByAudioMediaId(long thoughtID) {
//
//        Cursor cursor;
//        ContentValues thoughtData;
//
//        cursor = theDB.query(THOUGHTS_TABLE_NAME,null,"thought_id = " + thoughtID,null, null,null,
//                null);
//        thoughtData = thoughtDataFromCursor(cursor);
//        cursor.close();
//        return(thoughtData);
//    }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
//    private ContentValues thoughtDataFromCursor(Cursor cursor) {
//
//        String[] fieldNames;
//        int index;
//        ContentValues thoughtData;
//
//        if (cursor != null && cursor.moveToFirst()) {
//            fieldNames = cursor.getColumnNames();
//            thoughtData = new ContentValues();
//            for (index = 0; index < fieldNames.length; index++) {
//                if (fieldNames[index].equals("_id")) {
//                    thoughtData.put("_id", cursor.getInt(index));
//                } else if (fieldNames[index].equals("thought_id")) {
//                    thoughtData.put("thought_id", cursor.getInt(index));
//                } else if (fieldNames[index].equals("thought_name")) {
//                    thoughtData.put("thought_name", cursor.getString(index));
//                } else if (fieldNames[index].equals("date")) {
//                    thoughtData.put("date", cursor.getString(index));
//                }
//            }
//            return (thoughtData);
//        } else
//            return (null);
//    }
//==============================================================================================================================================================
    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context theContext) {
            super(theContext,DATABASE_NAME,null,DATABASE_VERSION);
        }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_THOUGHTS_TABLE);
        }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
        }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + THOUGHTS_TABLE_NAME);
            onCreate(db);
        }
//--------------------------------------------------------------------------------------------------------------------------------------------------------------
    }
//==============================================================================================================================================================
}
//==============================================================================================================================================================