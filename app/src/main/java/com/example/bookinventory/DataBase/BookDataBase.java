package com.example.bookinventory.DataBase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = BookEntry.class,version = 1,exportSchema = false)
public abstract class BookDataBase extends RoomDatabase {

    private static final String DATABASE_NAME = "book_databsae";
    private static final Object LOCK = new Object();
    public static BookDataBase sInstance;

    public static BookDataBase getDataBase(Context context) {
        if(sInstance==null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context,BookDataBase.class,DATABASE_NAME).allowMainThreadQueries().build();
            }
        }
        return sInstance;
    }
    public abstract BookDAO bookDAO();
}
