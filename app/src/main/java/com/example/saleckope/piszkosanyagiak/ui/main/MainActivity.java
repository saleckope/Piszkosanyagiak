package com.example.saleckope.piszkosanyagiak.ui.main;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.saleckope.piszkosanyagiak.R;
import com.example.saleckope.piszkosanyagiak.model.MoneyItem;
import com.example.saleckope.piszkosanyagiak.sevice.NotifyService;
import com.example.saleckope.piszkosanyagiak.ui.settings.SettingsActivity;

import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewMoneyItemDialogFragment.INewMoneyItemDialogListener {
    private RecyclerView recyclerView;
    private MoneyAdapter adapter;
    public static TextView sumMoneyTextView;
    public static TextView spentTextView;
    public static TextView haveTextView;
    public static View spentSeparator;
    public static View haveSeparator;
    public static int budget = 40000;
    public static int spent = 0;
    public static int haveLeft = 40000;
    public SharedPreferences settings;
    PendingIntent pendingIntent;
    private boolean notified = false;
    public static CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Set view
        this.setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Set floating action button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Clicking on floating action button
                //can makes a new item in list
                new NewMoneyItemDialogFragment().show(getSupportFragmentManager(), NewMoneyItemDialogFragment.TAG);
            }
        });

        //Get saved budget
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        budget = settings.getInt("budget", 40000);
        notified = settings.getBoolean("notified", false);

        //Set coordinatorLayout element
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.coordLayout);

        //Create the header of hom page
        createHeader();

        //Initializing the RecyclerView
        initRecyclerView();

        //Calculate the actual budget
        if (adapter != null) {
            adapter.haveLeft();
        }

        //Creates notification only one time
        if (!notified) {
            alarmEveryDay();
            //Set notified true -> there will be only one notification on a day
            notified = true;
        }
    }

    //Shows snackbar with todays budget
    public static void showDailyBudget() {
        //Create Snackbar
        Snackbar snackbar = Snackbar
                .make(coordinatorLayout, calculateTodayBudget(), Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    //Onclick -> dismiss Snackbar
                    @Override
                    public void onClick(View v) {
                    }
                });
        //Snackbar is dismissed only if clicking "OK"
        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
        //"OK" Color is black
        snackbar.setActionTextColor(Color.BLACK);
        //Create the view of snackbar
        View snackbarView = snackbar.getView();
        //With orange background
        snackbarView.setBackgroundColor(Color.rgb(255, 57, 22));
        //Set Textview
        TextView textView = (TextView) snackbarView.findViewById(android.support.design.R.id.snackbar_text);
        //Text color is white
        textView.setTextColor(Color.WHITE);
        //Show snackbar
        snackbar.show();
    }

    //This method calculates the budget for the actual day
    //returns with the text of the snackbar
    //"You can spend ... Ft today." or "You are over budget"
    public static String calculateTodayBudget() {
        String spendToday;
        int daysLeft;
        //Get today's date
        Calendar calendar = Calendar.getInstance();
        //Number of days in the actual month
        int daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        //today's date
        int today = calendar.get(Calendar.DAY_OF_MONTH);

        //Calculate how many days left in month
        daysLeft = daysInMonth - today + 1;

        //If the user still have money to spend
        if (haveLeft / daysLeft > 0) {
            spendToday = "You can spend " + Integer.toString(haveLeft / daysLeft) + " Ft today.";
        } else {
            spendToday = "You are over budget";
        }

        return spendToday;
    }

    //Creates the notification
    //and set alarm for every day
    public void alarmEveryDay() {
        Calendar calendar = Calendar.getInstance();

        //Send intent to NotifyService
        Intent myIntent = new Intent(this, NotifyService.class);
        pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);

        //at midnight
        calendar.set(Calendar.HOUR_OF_DAY, 00);
        calendar.set(Calendar.MINUTE, 00);
        calendar.set(Calendar.SECOND, 00);

        //Set repeat of notification to every day at midnight
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
    }

    //Creates the header of home page
    //Initialize the variables of textviews and separators
    public void createHeader() {
        sumMoneyTextView = (TextView) findViewById(R.id.SumMoney);
        spentTextView = (TextView) findViewById(R.id.spentTextView);
        haveTextView = (TextView) findViewById(R.id.haveTextView);
        spentSeparator = findViewById(R.id.separatorSpent);
        haveSeparator = findViewById(R.id.separatorHave);
    }

    //Creates menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    //If MainActivity is paused
    //saves the amount of budget
    @Override
    protected void onPause() {
        super.onPause();

        //Use SharedPreferences for saving budget
        //Get access
        settings = PreferenceManager.getDefaultSharedPreferences(this);
        //Enable editing
        SharedPreferences.Editor editor = settings.edit();
        //Update saved budget with the current budget
        editor.putInt("budget", budget);
        editor.putBoolean("notified", notified);
        //Commit changes
        editor.commit();
    }

    //Method for selected options item
    //starts SettingsActivity
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //Selected Settings option
        if (id == R.id.action_settings) {
            //Send intent to SettingsActivity
            Intent showDetailsIntent = new Intent(this, SettingsActivity.class);
            //Start activity
            this.startActivity(showDetailsIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //Method for initializing RecyclerView
    //Do it in Async task
    private void initRecyclerView() {
        recyclerView = (RecyclerView) findViewById(R.id.MainRecyclerView);
        //Create MoneyAdapter for handling RecyclerView
        adapter = new MoneyAdapter();
        //Load items form database in Async Task
        loadItemsInBackground();
        //set manager
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //Set adapter to created MoneyAdapter
        recyclerView.setAdapter(adapter);
    }

    //Method for loading items in background to RecyclerView
    private void loadItemsInBackground() {
        //Do loading in Async Task
        //So it won't block the Main task
        new AsyncTask<Void, Void, List<MoneyItem>>() {
            @Override
            protected List<MoneyItem> doInBackground(Void... voids) {
                return MoneyItem.listAll(MoneyItem.class);
            }

            //Loaded the items from database
            //Then update adapter with data
            @Override
            protected void onPostExecute(List<MoneyItem> moneyItems) {
                super.onPostExecute(moneyItems);
                //Load data to adapter
                adapter.update(moneyItems);
            }
        }.execute();
    }

    //Method for new MoneyItem added
    //adapter need to add item to list
    @Override
    public void onMoneyItemCreated(MoneyItem newItem) {
        adapter.addItem(newItem);
    }
}
