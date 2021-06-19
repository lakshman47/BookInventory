package com.example.bookinventory.DataBase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "book-table")
public class BookEntry{
    @PrimaryKey(autoGenerate = true)
    int Id;
    @NonNull
    String Title;
    String Author;

    int Price;
    int Quantity;

    int SupplierId;

    public BookEntry(@NonNull String title, String author, int price, int quantity, int supplierId) {
        Title = title;
        Author = author;
        Price = price;
        Quantity = quantity;
        SupplierId = supplierId;
    }

    public BookEntry(int id, @NonNull String title, String author, int price, int quantity, int supplierId) {
        Id = id;
        Title = title;
        Author = author;
        Price = price;
        Quantity = quantity;
        SupplierId = supplierId;
    }

    public BookEntry() {
    }

    public int getSupplierId() {
        return SupplierId;
    }

    public void setSupplierId(int supplierId) {
        SupplierId = supplierId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    @NonNull
    public String getTitle() {
        return Title;
    }

    public void setTitle(@NonNull String title) {
        Title = title;
    }

    public String getAuthor() {
        return Author;
    }

    public void setAuthor(String author) {
        Author = author;
    }

    public int getPrice() {
        return Price;
    }

    public void setPrice(int price) {
        Price = price;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }
}
