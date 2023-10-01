package com.example.contactmanager;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Contact.class}, version = 1)
@TypeConverters(BitmapTypeConverter.class) // Allows for the bitmap to be converted to a byte[]

public abstract class ContactDatabase extends RoomDatabase {
    public abstract ContactDao contactDao();

}
