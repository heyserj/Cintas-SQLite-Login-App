package com.example.loginsqliteactivity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;

import androidx.appcompat.app.AppCompatActivity;

import com.example.databaselibrary.DBmain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    final Calendar myCalendar = Calendar.getInstance();
    static boolean startedFlag;
    public static final String myPrefs = "MyPrefs";
    NewService mService;
    boolean mBound = false;
    DBmain dBmain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dBmain = new DBmain(this);
        loadFragments();
        if (!startedFlag) {
            insertExitRecord();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mBound) {
            unbindService(connection);
            mBound = false;
            updateTimeStamps();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, NewService.class);
        bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!isChangingConfigurations()) {
            startedFlag = false;
            insertExitRecord();
            updateTimeStamps();
        }
    }

    private ServiceConnection connection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className, IBinder service) {
            NewService.LocalBinder binder = (NewService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            if (!startedFlag) {
                mService.insertStartupRecord();
                startedFlag = true;
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
            updateTimeStamps();
        }
    };

    private void loadFragments() {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        SharedPreferences sharedPref = MainActivity.this.getSharedPreferences(myPrefs, Context.MODE_PRIVATE);
        if (!sharedPref.getBoolean("appOpened", false)) {
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("appOpened", true);
            editor.apply();
            FirstFragment firstFrag = new FirstFragment();
            ft.add(R.id.fragment, firstFrag, "one");
            ft.commit();
        } else {
            SecondFragment secondFrag = new SecondFragment();
            ft.add(R.id.fragment, secondFrag, "two");
            ft.commit();
        }
    }

    private void updateTimeStamps() {
        SharedPreferences sharedPref = getSharedPreferences(myPrefs, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        String format = "M/d/yy h:mm:ss a";
        SimpleDateFormat dateFormat = new SimpleDateFormat(format, Locale.US);
        String currentTime = dateFormat.format(myCalendar.getTime());
        editor.putString("currentTime", currentTime);
        editor.apply();
    }

    private void insertExitRecord() {
        SharedPreferences sharedPref = getSharedPreferences(myPrefs, MODE_PRIVATE);
        if (sharedPref.getString("currentTime", null) != null) {
            dBmain.insertAppDetails(sharedPref.getString("currentTime", null),
                    "App Closed");
        }
    }
}