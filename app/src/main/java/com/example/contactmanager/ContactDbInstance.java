package com.example.contactmanager;

import android.content.Context;

import androidx.room.Room;

public class ContactDbInstance {
    private static ContactDatabase database;

    public static ContactDatabase getDatabase(Context context) {
        if (database == null) {
            database = Room.databaseBuilder(context,
                            ContactDatabase.class, "app_database")
                    .allowMainThreadQueries()
                    .build();
        }
        return database;
    }
}
