package com.example.contactmanager;

import android.graphics.Bitmap;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditContact extends ViewModel {

    // This ViewModel is used to store data for editing a contact data

    public MutableLiveData<String> firstName; // this is the new username
    public MutableLiveData<String> lastName; // this is the new username
    public MutableLiveData<Bitmap> contactIcon; // this is the userIcon
    public MutableLiveData<String> phoneNumber; // this is the userIcon
    public MutableLiveData<String> email; // this is the contact email
    public MutableLiveData<Long> contactId; // permanent userID
    public MutableLiveData<Long> deleteContactId; // the userID of the user we decide to delete
    public MutableLiveData<Integer> deleteContactPosition; //the position of hte user in the RecycleView that we decide to delete
    public MutableLiveData<Integer> contactCount; //The amount of users


    public EditContact(){
        firstName = new MediatorLiveData<String>();
        firstName.setValue("");

        lastName = new MediatorLiveData<String>();
        lastName.setValue("");

        contactIcon = new MediatorLiveData<Bitmap>();
        contactIcon.setValue(null);

        phoneNumber = new MediatorLiveData<String>();
        phoneNumber.setValue("");

        email = new MediatorLiveData<String>();
        email.setValue("");

        contactId = new MediatorLiveData<Long>();
        contactId.setValue(0L);

        deleteContactId = new MediatorLiveData<Long>();
        deleteContactId.setValue(0L);

        deleteContactPosition = new MediatorLiveData<Integer>();
        deleteContactPosition.setValue(0);

        contactCount = new MediatorLiveData<Integer>();
        contactCount.setValue(0);
    }

    public void resetEditContact() {
        firstName.setValue("");
        lastName.setValue("");
        contactIcon.setValue(null);
        phoneNumber.setValue("");
        email.setValue("");
        contactId.setValue(0L);
    }

    public String getFirstName() {
        return firstName.getValue();
    }
    public void setFirstName(String value) {
        firstName.setValue(value);
    }

    public String getLastName() {
        return lastName.getValue();
    }
    public void setLastName(String value) {
        lastName.setValue(value);
    }

    public Bitmap getContactIcon() {
        return contactIcon.getValue();
    }
    public void setContactIcon(Bitmap value) {
        contactIcon.setValue(value);
    }

    public String getPhoneNumber() {
        return phoneNumber.getValue();
    }
    public void setPhoneNumber(String value) {
        phoneNumber.setValue(value);
    }

    public String getEmail() {
        return email.getValue();
    }
    public void setEmail(String value) {
        email.setValue(value);
    }

    public long getContactId() {
        return contactId.getValue();
    }
    public void setContactId(long value) {
        contactId.setValue(value);
    }

    public long getDeleteContactId() {
        return deleteContactId.getValue();
    }
    public void setDeleteContactId(long value) {
        deleteContactId.setValue(value);
    }

    public int getDeleteContactPosition() {
        return deleteContactPosition.getValue();
    }
    public void setDeleteContactPosition(int value) {
        deleteContactPosition.setValue(value);
    }

    public int getContactCount() {
        return contactCount.getValue();
    }
    public void setContactCount(int value) { contactCount.setValue(value);
    }


}
