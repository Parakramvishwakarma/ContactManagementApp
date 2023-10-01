package com.example.contactmanager;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import java.util.LinkedList;

public class NavigationData extends ViewModel {

    public MutableLiveData<Integer> clickedValue; // Primary navigation data
    public MutableLiveData<Integer> historicalClickedValue; // Secondary Navigation data if required to traverse back to previous page
    public MutableLiveData<Integer> settingsHistoricalValue; //Mutable value to stpre hre the settings button was clicked to go back to original page

    public NavigationData(){
        clickedValue = new MediatorLiveData<Integer>();
        clickedValue.setValue(0);

        historicalClickedValue = new MediatorLiveData<Integer>();
        historicalClickedValue.setValue(0);

        settingsHistoricalValue = new MediatorLiveData<Integer>();
        settingsHistoricalValue.setValue(0);

    }
    public int getClickedValue(){
        return clickedValue.getValue();
    }
    public void setClickedValue(int value){
        clickedValue.setValue(value);
    }

    public int getHistoricalClickedValue() { return historicalClickedValue.getValue();}

    public void setHistoricalClickedValue(int value) { historicalClickedValue.setValue(value);}

    public int getSettingsHistoricalValue() { return settingsHistoricalValue.getValue();}

    public void setSettingsHistoricalValue(int value) { settingsHistoricalValue.setValue(value);}

}
