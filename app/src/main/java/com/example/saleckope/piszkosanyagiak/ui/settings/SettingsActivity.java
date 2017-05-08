package com.example.saleckope.piszkosanyagiak.ui.settings;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.saleckope.piszkosanyagiak.R;

public class SettingsActivity extends AppCompatActivity {

    Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set layout
        setContentView(R.layout.activity_settings);
        //Set title
        getSupportActionBar().setTitle(R.string.settings);
        //Show back arrow
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Get fragment manager
        FragmentManager fm = getSupportFragmentManager();
        //Start SettingsFragment
        fragment = fm.findFragmentByTag("SettingsFragment");
        if (fragment == null) {
            FragmentTransaction ft = fm.beginTransaction();
            fragment = new SettingsFragment();
            ft.add(android.R.id.content, fragment, "SettingsFragment");
            ft.commit();
        }
    }

    //Clicked on option item
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //If clicked item is the back arrow
        if (item.getItemId() == android.R.id.home) {
            //finish settings -> go back to MainActivity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
