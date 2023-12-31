package com.example.contactmanager;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ContactData extends ViewModel {

    // This ViewModel is used to store data for editing a contact data

    public MutableLiveData<String> firstName; // this is the new username
    public MutableLiveData<String> lastName; // this is the new username
    public MutableLiveData<Integer> contactIcon; // this is the userIcon
    public MutableLiveData<Long> phoneNumber; // this is the userIcon
    public MutableLiveData<String> email; // this is the new username
    public MutableLiveData<Long> contactId; // permanent userID
    public MutableLiveData<Long> deleteContactId; // the userID of the user we decide to delete
    public MutableLiveData<Integer> deleteContactPosition; //the position of hte user in the RecycleView that we decide to delete
    public MutableLiveData<Integer> contactCount; //The amount of users


    public ContactData(){
        firstName = new MediatorLiveData<String>();
        firstName.setValue("");

        contactIcon = new MediatorLiveData<Integer>();
        contactIcon.setValue(0);

        phoneNumber = new MediatorLiveData<Long>();
        phoneNumber.setValue(0L);

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

    public int getContactIcon() {
        return contactIcon.getValue();
    }
    public void setContactIcon(int value) {
        contactIcon.setValue(value);
    }

    public long getPhoneNumber() {
        return phoneNumber.getValue();
    }
    public void setPhoneNumber(long value) {
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
