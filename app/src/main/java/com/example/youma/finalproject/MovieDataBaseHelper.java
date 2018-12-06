package com.example.youma.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * Create a Database to store movies searched.
 * @author Nguyen Gia Nguyen
 */
public class MovieDataBaseHelper extends SQLiteOpenHelper {

    public static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "movies";
    public static final String KEY_ID = "id";
    public static final String MOVIE_TITLE = "movie_title";
    public static final String MOVIE_DESCRIPTION = "movie_description";
    public static final String MOVIE_POSTER = "movie_poster";
    public static final String MOVIE_RATING = "movie_rating";
    public static final String MOVIE_YEAR = "movie_year";
    public static final String MOVIE_LENGTH = "movie_length";
    public static final String MOVIE_GENRE = "movie_genre";
    public static final String MOVIE_ACTORS = "movie_actors";
    public static final String TAG = "MovieDataBaseHelper";
    public static final String CREATE_TABLE = " CREATE TABLE " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            MOVIE_TITLE + " TEXT," +
            MOVIE_DESCRIPTION + " TEXT," +
            MOVIE_POSTER + " TEXT," +
            MOVIE_RATING + " TEXT," +
            MOVIE_YEAR + " TEXT," +
            MOVIE_LENGTH + " TEXT," +
            MOVIE_GENRE + " TEXT," +
            MOVIE_ACTORS + " TEXT" +
            ");";

    /**
     * Get table name and version number.
     * @param context
     */
    public MovieDataBaseHelper(Context context) {
        super(context, TABLE_NAME, null, VERSION_NUM);
    }

    /**
     * Execute database
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "Calling onCreate");
        db.execSQL(CREATE_TABLE);

    }

    /**
     * Drop and recreate new table.
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
        Log.i(TAG, "Calling onUpgrade, oldVersion=" + oldVersion + "newVersion=" + newVersion);
    }

    /**
     *
     * @author Nguyen Gia Nguyen, Chi Mien Huynh
     *
     * @return
     */
    public boolean addMovie(String title, String description, String rating, String year, String length, String genre, String actor) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MOVIE_TITLE, title);
        contentValues.put(MOVIE_DESCRIPTION, description);
        contentValues.put(MOVIE_RATING, rating);
        contentValues.put(MOVIE_YEAR, year);
        contentValues.put(MOVIE_LENGTH, length);
        contentValues.put(MOVIE_GENRE, genre);
        contentValues.put(MOVIE_ACTORS, actor);
        long result = db.insert(TABLE_NAME, null, contentValues);
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getListContents(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor data = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        return data;
    }
}
