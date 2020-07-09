package com.shivivats.kindcompanion;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.preference.EditTextPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity {

    Toolbar settingsTopBar;
    public static Calendar userCalendar = Calendar.getInstance();
    SettingsFragment settingsFragment;
    AlarmManager alarmManager;
    PendingIntent alarmIntent;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        settingsFragment = new SettingsFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings, settingsFragment)
                .commit();

        settingsTopBar = findViewById(R.id.settingsTopBar);
        setSupportActionBar(settingsTopBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("Settings");
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        SwitchPreferenceCompat reminderSwitch = settingsFragment.findPreference("reminder");

        if (reminderSwitch == null) {
            Log.d("SETTINGS_TAG", "reminder switch is null");
        }
        reminderSwitch.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {

                if ((boolean) newValue == true) {
                    createReminder();
                } else {
                    // a way to disable the reminder
                    cancelReminder();

                }
                return true;
            }
        });


        Preference reminderTimePicker, reminderDatePicker;
        reminderDatePicker = settingsFragment.findPreference("reminderDatePicker");
        reminderTimePicker = settingsFragment.findPreference("reminderTimePicker");

        if (reminderTimePicker != null) {
            reminderTimePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    DialogFragment newFragment = new TimePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "timePicker");
                    return true;
                }
            });

            if (reminderDatePicker != null) {
                reminderDatePicker.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                    @Override
                    public boolean onPreferenceClick(Preference preference) {
                        DialogFragment newFragment = new DatePickerFragment();
                        newFragment.show(getSupportFragmentManager(), "datePicker");
                        return true;
                    }
                });
            }
        }
    }

    public void createReminder() {
        intent = new Intent(this, NotifyService.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmIntent = PendingIntent.getService(this, 0, intent, 0);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, userCalendar.getTimeInMillis(), 1000 * 60 * 5, alarmIntent);
        Toast.makeText(getApplicationContext(), "Reminder set", Toast.LENGTH_SHORT).show();
    }

    public void cancelReminder() {
        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(this, 0, intent,
                        PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(getApplicationContext(), "Reminder removed.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Reminder couldn't be removed.", Toast.LENGTH_LONG).show();
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_settings, rootKey);
            EditTextPreference numberPreference = findPreference("pin");

            if (numberPreference != null) {
                numberPreference.setOnBindEditTextListener(
                        editText -> editText.setInputType(InputType.TYPE_CLASS_NUMBER));
            }
        }

    }

    public static class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));

        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user
            userCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            userCalendar.set(Calendar.MINUTE, minute);
            Toast.makeText(getContext(), "Time set for " + hourOfDay + " " + minute + ", kindly set the reminder again.", Toast.LENGTH_LONG).show();

        }
    }

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            userCalendar.set(Calendar.YEAR, year);
            userCalendar.set(Calendar.MONTH, month);
            userCalendar.set(Calendar.DAY_OF_MONTH, day);
            Toast.makeText(getContext(), "Day set for " + day + " " + month + " " + year + ", kindly set the reminder again.", Toast.LENGTH_LONG).show();
        }
    }


}