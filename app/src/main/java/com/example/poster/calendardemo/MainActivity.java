package com.example.poster.calendardemo;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private FragmentManager fragmentManager;
    private CalendarMainFragment calendarMainFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (calendarMainFragment == null){
            calendarMainFragment = new CalendarMainFragment();
        }
        if (fragmentManager == null){
            fragmentManager = getSupportFragmentManager();
        }
        fragmentManager.beginTransaction().replace(R.id.container, calendarMainFragment, null).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        calendarMainFragment = null;
        super.onDestroy();
    }
}
