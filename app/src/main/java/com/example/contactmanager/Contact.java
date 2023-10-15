package com.example.contactmanager;

import android.graphics.Bitmap;

import androidx.lifecycle.ViewModel;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity(tableName = "contacts")
public class Contact {
    // Database for contacts
    @PrimaryKey(autoGenerate = true)
    private long id;
    @ColumnInfo(name = "first_name")
    private String firstName;

    @ColumnInfo(name = "last_name")
    private String lastName;

    @ColumnInfo(name = "phone_number")
    private String phoneNumber;

    @ColumnInfo(name = "email")
    private String email;


    @ColumnInfo(name = "favourite")
    private int favourite;

    @ColumnInfo(name = "notes")
    private String notes;

    @ColumnInfo(name = "image")
    private Bitmap image;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        this.firstName = name;
    }


    public String getLastName () {return lastName;}

    public void setLastName(String lastName) { this.lastName = lastName;}

    public String getPhoneNumber() {
        return phoneNumber;
    }
    public void setPhoneNumber(String value) {
        this.phoneNumber = value;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String value) {
        this.email = value;
    }

    public int getFavourite() {
        return favourite;
    }
    public void setFavourite(int value) {
        this.favourite = value;
    }

    public String getNotes() { return  notes;}

    public void setNotes(String value) {
        this.notes = value;
    }

    public Bitmap getImage() {
        return image;
    }
    public void setImage(Bitmap value) {
        this.image = value;
    }
}
