package com.example.bookinventory.DataBase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface BookDAO {
    @Insert
    void insertBook(BookEntry bookEntry);

    @Delete
    void deleteBook(BookEntry bookEntry);

    @Update
    void updateBook(BookEntry bookEntry);

    @Query("SELECT * FROM `book-table`")
    LiveData<List<BookEntry>> getAllBooks();

    @Query("SELECT * FROM `book-table` WHERE id==:Id" )
    BookEntry getBookById(int Id);

    @Query("SELECT * FROM `book-table` ORDER BY Title")
    LiveData<List<BookEntry>> getAllBooksByTitle();

    @Query("SELECT * FROM `book-table` ORDER BY Price")
    LiveData<List<BookEntry>> getAllBooksByPrice();

    @Query("SELECT * FROM `book-table` ORDER BY Author")
    LiveData<List<BookEntry>> getAllBooksByAuthor();

    @Query("SELECT * FROM `book-table` ORDER BY Quantity")
    LiveData<List<BookEntry>> getAllBooksByQuantity();

    @Query("DELETE FROM `book-table` ")
    void deleteAllBooks();

}
