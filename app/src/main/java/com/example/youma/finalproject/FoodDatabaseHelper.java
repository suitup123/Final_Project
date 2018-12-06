package com.example.youma.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*Reference: https://www.youtube.com/watch?v=SK98ayjhk1E  */

/**
 * this class is extends SQLiteOpenHelper to create the database that will store information
 */
public class FoodDatabaseHelper extends SQLiteOpenHelper {

    protected static final String ACTIVITY_NAME = "Favourite_list";
    public static final String DATABASE_NAME="mylist.db";
    public static final String TABLE_NAME="mylist_data";
    public static final String KEY_ID="ID";
    public static final String COL2="ITEM1";
    public FoodDatabaseHelper(Context context){super(context,DATABASE_NAME,null,3);}

    /**
     * This object of SQLiteDatabase to create the table of database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
       // String createTable = "CREATE TABLE "+TABLE_NAME + "(" +COL1+" INTEGER PRIMARY KEY AUTOINCREMENT,"+COL2+" STRING);";
        String createTable = "CREATE TABLE "
                + TABLE_NAME
                + "( "
                + KEY_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2
                + " TEXT "
                + ");";
        db.execSQL(createTable);
    }

    /**
     * @author: Chi Mien Huynh
     * it uses object of SQLDatabase to drop old table and create new table
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    /**
     * addData do the database
     * check that everything enter correctly
     * @param item1
     * @return
     */
    public boolean addData(String item1){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2,item1);
        //db.insert(TABLE_NAME,null,contentValues);
       // db.close();
        long result =db.insert(TABLE_NAME,null,contentValues);

        if (result ==-1){
            return false;
        }else {return true;}
        //return true;

    }
    /**get the contents of database
      using select
    * */
    /**
     * get the contents of database
     using select
     * @return data
     */
    public Cursor getListContents () {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM "+TABLE_NAME,null);
        return data;
    }
}
