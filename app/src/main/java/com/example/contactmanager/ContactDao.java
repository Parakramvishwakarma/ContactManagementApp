package com.example.contactmanager;

import android.graphics.Bitmap;

import androidx.room.Dao;
import androidx.room.Database;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {
    // Dao for contacts

    @Insert
    void insert(Contact... contact);

    @Update
    void update(Contact... contact);

    @Delete
    void delete(Contact... contact);

    @Query("SELECT * FROM contacts")
    List<Contact> getAllContacts();


    @Query("SELECT * FROM contacts WHERE first_name = :firstName")
    Contact getContactsByFirstName(String firstName);

    @Query("SELECT * FROM contacts WHERE last_name = :lastName")
    Contact getContactsByLastName(String lastName);

    @Query("SELECT * FROM contacts WHERE phone_number = :phoneNumber")
    Contact getContactsByPhoneNumber(String phoneNumber);

    @Query("SELECT * FROM contacts WHERE email = :email")
    Contact getContactsByEmail(String email);

    @Query("SELECT * FROM contacts WHERE id = :contactId")
    Contact getContactByID(long contactId);


    @Query("UPDATE contacts SET first_name = :firstName WHERE id = :contactId")
    void updateFirstName(long contactId, String firstName);
    @Query("UPDATE contacts SET last_name = :lastName WHERE id = :contactId")
    void updateLastName(long contactId, String lastName);
    @Query("UPDATE contacts SET phone_number = :phoneNumber WHERE id = :contactId")
    void updatePhoneNumber(long contactId, String phoneNumber);
    @Query("UPDATE contacts SET email = :email WHERE id = :contactId")
    void updateEmail(long contactId, String email);
    @Query("UPDATE contacts SET image = :contactIcon WHERE id = :contactId")
    void updateContactIcon(long contactId, Bitmap contactIcon);
    @Query("DELETE FROM contacts WHERE id = :contactId")
    void deleteContact(long contactId);


}
