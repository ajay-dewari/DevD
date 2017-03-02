package com.ajaysinghdewari.devd.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Ajay on 01-03-2017.
 */

public class MyDB {

    private static final String TAG = MyDB.class.getSimpleName();
    private static final String DATABASE_NAME = "Song";
    private static final int DATABASE_VERSION = 1;
    private DBHelper mHelper;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    public MyDB(Context context){
        mHelper=new DBHelper(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    public void open(){
        mDatabase=mHelper.getWritableDatabase();
    }

    public void addSongs(ArrayList<Map<String, String>> notificationList) {

        try {
            int i = 0;
            mDatabase.beginTransaction();
            mDatabase.delete(DBHelper.DATABASE_TABLE_SONGS, null, null);
            while (i < notificationList.size()) {
                ContentValues contentValues = new ContentValues();
                Map<String, String> notificationMap = notificationList.get(i);
                Iterator<String> iterator = notificationMap.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = iterator.next().toString();
                    contentValues.put(key, notificationMap.get(key));
                }
                try {
                    mDatabase.insert(DBHelper.DATABASE_TABLE_SONGS, null, contentValues);
                } catch (Exception ex) {
                    Log.d(TAG,"Insert into database exception caught===== "+ ex.getMessage());
                }
                i++;
            }
            mDatabase.setTransactionSuccessful();
        } finally {
            mDatabase.endTransaction();
        }
    }
    public ArrayList<Map<String, String>> getSongs() {
        // method to get the cart items in form of cursor
        Cursor c = null;
        ArrayList<Map<String, String>> songsArrayList = new ArrayList<>();
        c = mDatabase.query(DBHelper.DATABASE_TABLE_SONGS, null, null, null, null, null, null, null);

        try {
            c.moveToPosition(0);
            while (!c.isAfterLast()) {
                Map<String, String> map = new HashMap<>();
                for (int i = 0; i < c.getColumnCount(); i++) {
                    map.put(c.getColumnName(i), c.getString(i));
                }
                songsArrayList.add(map);
                c.moveToNext();
            }
            return songsArrayList;
        } finally {
            c.close();
        }

    }

    public void updateFavourate(String productId, String isFavorite){

        ContentValues cv = new ContentValues();
        cv.put(DBHelper.KEY_IS_FAVOURATE, isFavorite);
        mDatabase.update(DBHelper.DATABASE_TABLE_SONGS, cv, DBHelper.KEY_ID+"=?",new String[]{productId});

    }

    public ArrayList<String> getSongNames(){

        ArrayList<String> al=new ArrayList<String>();
        Cursor c=null;

        c = mDatabase.query(DBHelper.DATABASE_TABLE_SONGS, new String[]{DBHelper.KEY_TITLE}, null, null, null, null, null, null);

        try {
            c.moveToPosition(0);
            while (!c.isAfterLast()) {
                al.add(c.getString(c.getColumnIndex(DBHelper.KEY_TITLE)));
                c.moveToNext();
            }
            return al;
        } finally {
            c.close();
        }
    }

    public void clearDB() {
        try {
            mDatabase.delete(DBHelper.DATABASE_TABLE_SONGS, null, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        mDatabase.close();
    }

}
