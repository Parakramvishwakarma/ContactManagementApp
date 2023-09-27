package com.example.contactmanager;

import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


public class SelectContactModel extends ViewModel {

    public MutableLiveData<Long> contactId;


    public SelectContactModel(){
        contactId = new MediatorLiveData<Long>();
        contactId.setValue(null);
    }

    public Long getContactId() {
        return contactId.getValue();
    }


    public void setContactId(Long value) {
        contactId.setValue(value);
    }

}
