package com.example.poster.calendardemo;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class CalendarMainFragment extends Fragment {
    private CalendarView calendarView;
    private CalendarMenuFragment calendarMenuFragment = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_main, container, false);
        calendarView = (CalendarView)view.findViewById(R.id.calendar_main);
        if (calendarMenuFragment == null){
            calendarMenuFragment = new CalendarMenuFragment();
        }

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if (checkCalendarData(year, month, dayOfMonth)){
                    Bundle bundle = new Bundle();
                    bundle.putString("year", year + "");
                    bundle.putString("month", (month + 1) + "");
                    bundle.putString("day", dayOfMonth + "");
                    calendarMenuFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.container, calendarMenuFragment).commit();
                    getActivity().startService(new Intent(getContext(), NotificationService.class));
                }else {
                    Toast.makeText(getContext(), "WTF?", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }
    private boolean checkCalendarData(int year, int month, int dayOfMonth){
        DateFormat df = new SimpleDateFormat("yyyy" + "M" + "d");
        int calendarData = Integer.valueOf(year + "" + (month + 1) + "" + dayOfMonth);
        int currentDate = Integer.valueOf(df.format(Calendar.getInstance().getTime()));
        if (calendarData < currentDate){
            return false;
        }else{
            return true;
        }
    }

    @Override
    public void onDestroy() {
        calendarMenuFragment = null;
        super.onDestroy();
    }
}
