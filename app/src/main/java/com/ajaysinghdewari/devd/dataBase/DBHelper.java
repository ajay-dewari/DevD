package com.ajaysinghdewari.devd.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Ajay on 01-03-2017.
 */

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_TABLE_SONGS = "Songs";

    public static final String KEY_ID = "_id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_ARTIST = "artist";
    public static final String KEY_ALBUM = "album";
    public static final String KEY_ALBUM_ID = "album_id";
    public static final String KEY_IS_FAVOURATE= "favourate";


    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(" CREATE TABLE " + DATABASE_TABLE_SONGS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY, " +
                KEY_TITLE + " TEXT NOT NULL, " +
                KEY_ARTIST + " TEXT NOT NULL, " +
                KEY_ALBUM + " TEXT NOT NULL, " +
                KEY_ALBUM_ID + " INTEGER NOT NULL, " +
                KEY_IS_FAVOURATE + " INTEGER NOT NULL);"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + DATABASE_TABLE_SONGS);
        onCreate(db);
    }
}
