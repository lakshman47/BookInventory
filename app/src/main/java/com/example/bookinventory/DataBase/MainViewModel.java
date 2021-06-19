package com.example.bookinventory.DataBase;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.bookinventory.R;

import java.util.ArrayList;
import java.util.List;

public class MainViewModel extends AndroidViewModel {

    BookDataBase mDB;

    public MainViewModel(@NonNull Application application) {
        super(application);
        mDB = BookDataBase.getDataBase(application.getApplicationContext());
    }

    public LiveData<List<BookEntry>> getAllBooks(){
        return mDB.bookDAO().getAllBooks();
    }
    public void insertBook(BookEntry bookEntry){
        mDB.bookDAO().insertBook(bookEntry);
    }
    public void deleteBook(BookEntry bookEntry){
        mDB.bookDAO().deleteBook(bookEntry);
    }
    public BookEntry getBookById(int Id){
        return mDB.bookDAO().getBookById(Id);
    }
    public void updateBook(BookEntry bookEntry){
        mDB.bookDAO().updateBook(bookEntry);
    }
    public LiveData<List<BookEntry>> getAllBooks(String value){
        if(value.equals(getApplication().getString(R.string.pref_order_by_title_label))){
            return mDB.bookDAO().getAllBooksByTitle();
        }
        else if(value.equals(getApplication().getString(R.string.pref_order_by_author_label))){
            return mDB.bookDAO().getAllBooksByAuthor();
        }
        else if(value.equals(getApplication().getString(R.string.pref_order_by_price_label))){
            return mDB.bookDAO().getAllBooksByPrice();
        }
        else{
            return mDB.bookDAO().getAllBooksByQuantity();
        }
    }
    public void deleteAllBooks(){
        mDB.bookDAO().deleteAllBooks();
    }
}
