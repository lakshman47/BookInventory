package com.example.bookinventory.DataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;

public class EditViewModel extends AndroidViewModel {
    BookDataBase mDB;
    public EditViewModel(@NonNull Application application) {
        super(application);
        mDB = BookDataBase.getDataBase(application.getApplicationContext());
    }

    public void insertBook(BookEntry bookEntry){
        mDB.bookDAO().insertBook(bookEntry);
    }

    public void updateBook(BookEntry bookEntry){
        mDB.bookDAO().updateBook(bookEntry);
    }

    public void deleteBook(BookEntry bookEntry){
        mDB.bookDAO().deleteBook(bookEntry);
    }

    public BookEntry getBookById(int Id){
        return mDB.bookDAO().getBookById(Id);
    }

}
