package com.example.saleckope.piszkosanyagiak.ui.statistics;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.saleckope.piszkosanyagiak.R;

public class StatisticsActivity extends AppCompatActivity {
    private static final String TAG = "StatisticsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set layout
        setContentView(R.layout.activity_statistics);
        //Set title to Statistics
        getSupportActionBar().setTitle(R.string.statistics);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Set ViewPager
        ViewPager mainViewPager = (ViewPager) findViewById(R.id.mainViewPager);
        //Create PagerAdapter for handling paging
        StatisticsPagerAdapter statisticsPagerAdapter = new StatisticsPagerAdapter(getSupportFragmentManager(), this);
        //Set adapter
        mainViewPager.setAdapter(statisticsPagerAdapter);
    }

    //Method for option item selected
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //If back arror pressed
        if (item.getItemId() == android.R.id.home) {
            //finish activity -> go back to MainActivity
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

