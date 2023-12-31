package com.example.contactmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    /* -----------------------------------------------------------------------------------------
            Function: Initialise View models + Elements
            Author: Parakram + Ryan
     ---------------------------------------------------------------------------------------- */
    NavigationData navigationData;
    NavBarFragment navBarFragment = new NavBarFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //initiate the model for navigation between pages
        navigationData = new ViewModelProvider(this).get(NavigationData.class);
        loadNavBar(); //always load the load the navigation bar on creating hte view
        selectViewFragment(); //this function decides which page to render
    }


    /* -----------------------------------------------------------------------------------------
                  Author: Parakram
                  Description: Populates the nav bar fragment
           ---------------------------------------------------------------------------------------- */
    private void selectViewFragment(){
        navigationData.clickedValue.observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                switch(integer) {
                    case 0:
                        loadContactViewFrag();
                        break;
                    case 1:
                        loadContactFrag();
                        break;
                    case 2:
                        loadDisplayFrag();
                }
            }
        });
    }

    /* -----------------------------------------------------------------------------------------
               Author: Parakram
               Description: Loads the contact view fragment
        ---------------------------------------------------------------------------------------- */
    private void loadDisplayFrag() {
        DisplayFragment displayFragment = new DisplayFragment();
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.body_container);
        if (frag!= null) {
            fm.beginTransaction().replace(R.id.body_container, displayFragment, "displayFragment").commit();
        }
        else {
            fm.beginTransaction().add(R.id.body_container, displayFragment, "displayFragment").commit();
        }
    }

    /* -----------------------------------------------------------------------------------------
                  Author: Parakram
                  Description: Loads the contact view fragment
           ---------------------------------------------------------------------------------------- */
    private void loadContactViewFrag() {
        ViewContactsFragment contactsFragment = new ViewContactsFragment();
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.body_container);
        if (frag!= null) {
            fm.beginTransaction().replace(R.id.body_container, contactsFragment, "contactsFragment").commit();
        }
        else {
            fm.beginTransaction().add(R.id.body_container, contactsFragment, "contactsFragment").commit();
        }
    }

    /* -----------------------------------------------------------------------------------------
                  Author: Ryan
                  Description: Loads the contact fragment
           ---------------------------------------------------------------------------------------- */
    private void loadContactFrag() {
        ContactFragment contactFragment = new ContactFragment();
        FragmentManager fm = getSupportFragmentManager();
        Fragment frag = fm.findFragmentById(R.id.body_container);
        if (frag!= null) {
            fm.beginTransaction().replace(R.id.body_container, contactFragment, "contactFragment").commit();
        }
        else {
            fm.beginTransaction().add(R.id.body_container, contactFragment, "contactFragment").commit();
        }
    }

    /* -----------------------------------------------------------------------------------------
                 Author: Parakram
                 Description: Populates the nav bar fragment
          ---------------------------------------------------------------------------------------- */
    private void loadNavBar() {
        FragmentManager fm = getSupportFragmentManager();
        fm.beginTransaction().add(R.id.navBar_container, navBarFragment, "navBarFragment").commit();
    }
}