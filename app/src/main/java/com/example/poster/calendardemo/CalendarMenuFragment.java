package com.example.poster.calendardemo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by POSTER on 29.03.2017.
 */

public class CalendarMenuFragment extends Fragment{
    private final String LOG_TAG = "myLogs";
    private ListView listView;
    private ArrayList<TimeModel> arrayList = null;
    private int idCurItemList = 0;
    private CustomListItem adapter;
    private DBHelper helper;
    private SQLiteDatabase db;
    private TimeModel timeModel;
    private ContentValues cv;
    private Cursor c;
    private String day;
    private String month;
    private String year;
    private Button add;
    private int hour;
    private int min;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar_menu, container, false);
        innitWidgets(view);
        getArgsFromBundle();
        innitDB();
        new LoadData().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                innitDB();
                c = db.query("events", null, "id=?",
                        new String[]{String.valueOf(arrayList.get(position).getIdModel())}, null, null, null);
                if (c.moveToFirst()){
                    idCurItemList = c.getInt(c.getColumnIndex("id"));
                }

                helper.close();
                c.close();
                alertDialog(position);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                innitDB();
                alertDialog(0);
            }
        });
        return view;
    }

    private void innitWidgets(View view) {
        listView = (ListView)view.findViewById(R.id.time_list);
        add = (Button)view.findViewById(R.id.addListItemBtn);
    }

    private void getArgsFromBundle(){
        Bundle bundle = getArguments();
        day = bundle.getString("day");
        month = bundle.getString("month");
        year = bundle.getString("year");
    }

    private void alertDialog(final int position){
        View style = getLayoutInflater(getArguments()).inflate(R.layout.style_of_alert, null, false);
        final EditText description = (EditText)style.findViewById(R.id.alertDescr);
        final CheckBox checkNotif = (CheckBox)style.findViewById(R.id.alertCheckbox);
        final TimePicker timePicker = (TimePicker)style.findViewById(R.id.timePicker);

        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                hour = hourOfDay;
                min = minute;
            }
        });
        if (idCurItemList != 0){
            description.setText(arrayList.get(position).getDescription());
            if (arrayList.get(position).getIsNotif() == 0){
                checkNotif.setChecked(false);
            }else {
                checkNotif.setChecked(true);
            }
            timePicker.setCurrentHour(arrayList.get(position).getCurrentTime() / 60);
            timePicker.setCurrentMinute(arrayList.get(position).getCurrentTime() % 60);
        }else{
            timePicker.setCurrentHour(0);
            timePicker.setCurrentMinute(0);
        }
        AlertDialog alertDialog = new AlertDialog.Builder(getContext())
                .setTitle("Set alarm clock settings")
                .setIcon(R.drawable.ic_add_alert_black_24dp)
                .setView(style)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        innitDB();
                        if (idCurItemList == 0){
                            cv.put("description", description.getText().toString());
                            if (checkNotif.isChecked()){
                                cv.put("is_notify", 1);
                            }else {
                                cv.put("is_notify", 0);
                            }
                            cv.put("year", year);
                            cv.put("month", month);
                            cv.put("day_of_month", day);
                            cv.put("event_time", timeToMinutes(hour, min));
                            db.insert("events", null, cv);
                        }else {
                            cv.put("description", description.getText().toString());
                            if (checkNotif.isChecked()){
                                cv.put("is_notify", 1);
                            }else {
                                cv.put("is_notify", 0);
                            }
                            cv.put("event_time", timeToMinutes(hour, min));
                            db.update("events", cv, "id=?", new String[]{Integer.toString(idCurItemList)});
                            idCurItemList = 0;
                        }
                        helper.close();
                        c.close();
                        new LoadData().execute();
                        setAlarmManager(getContext());
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        idCurItemList = 0;
                    }
                }).create();
        alertDialog.show();
    }

    private void innitDB() {
        helper = new DBHelper(getContext());
        db = helper.getReadableDatabase();
        cv = new ContentValues();
    }

    private class LoadData extends AsyncTask<Void, String, String>{

        @Override
        protected void onPreExecute() {
            innitDB();
            arrayList = new ArrayList<>();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... params) {
            c = db.query("events", null, "year=? and month=? and day_of_month=?", new String[]{year, month, day}, null, null, null);
            if (c.moveToFirst()){
                int idColIndex = c.getColumnIndex("id");
                int descriptionColIndex = c.getColumnIndex("description");
                int is_notifyColIndex = c.getColumnIndex("is_notify");
                int event_timeColIndex = c.getColumnIndex("event_time");
                do {
                    timeModel = new TimeModel();
                    timeModel.setIdModel(c.getInt(idColIndex));
                    timeModel.setDescription(c.getString(descriptionColIndex));
                    timeModel.setCurrentTime(c.getInt(event_timeColIndex));
                    timeModel.setNotif(c.getInt(is_notifyColIndex));
                    arrayList.add(timeModel);
                }while (c.moveToNext());
            }else {
                Log.d(LOG_TAG, "0 rows");
            }
            return "Loaded";
        }

        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getContext(), result, Toast.LENGTH_SHORT).show();
            adapter = new CustomListItem(arrayList, getContext());
            listView.setAdapter(adapter);
            c.close();
            helper.close();
        }
    }
    private Integer timeToMinutes(int hours, int minutes){
        return Integer.valueOf(hours * 60 + minutes);
    }
    private void setAlarmManager(Context context){
        Calendar calendar = Calendar.getInstance();
        PendingIntent pi = PendingIntent.getBroadcast(context, 101, new Intent("wakeup"),
                PendingIntent.FLAG_ONE_SHOT);
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.MILLISECOND, (int)((timeToSeconds(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day), hour, min)
                - calendar.getTimeInMillis())) / 1000);

        long time = calendar.getTimeInMillis();
        System.out.println("set alarm" + time);
        manager.setRepeating(AlarmManager.RTC_WAKEUP, time, AlarmManager.INTERVAL_HOUR, pi);
    }

    private long timeToSeconds(Integer year, Integer month, Integer day, int hour, int min){
        Calendar feature_cal = Calendar.getInstance();
        feature_cal.set(year, month, day, hour, min);
        return feature_cal.getTimeInMillis();
    }
}
