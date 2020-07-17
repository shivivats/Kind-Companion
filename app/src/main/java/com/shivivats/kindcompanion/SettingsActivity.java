package com.shivivats.kindcompanion;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.InputFilter;
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
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;
import androidx.preference.SwitchPreferenceCompat;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity implements PendingIntent.OnFinished {

    Toolbar settingsTopBar;
    public static Calendar userCalendar;
    SettingsFragment settingsFragment;
    AlarmManager alarmManager;
    public static Calendar currentCalendar;
    PendingIntent alarmPendingIntent;
    Intent alarmIntent = new Intent();
    EditTextPreference vaultPinPreference;
    boolean reminderSwitchValue;
    private long reminderInterval;

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

        if (userCalendar == null) {
            userCalendar = Calendar.getInstance();
            userCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        if (currentCalendar == null) {
            currentCalendar = Calendar.getInstance();
            currentCalendar.setTimeInMillis(System.currentTimeMillis());
        }

        SwitchPreferenceCompat reminderSwitch = settingsFragment.findPreference("reminderSwitch");

        if (reminderSwitch == null) {
            Log.d("SETTINGS_TAG", "reminder switch is null");
        }

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        reminderSwitchValue = sharedPreferences.getBoolean("reminderSwitch", false);

        reminderSwitch.setOnPreferenceChangeListener((preference, newValue) -> {

            if ((boolean) newValue == true) {
                currentCalendar = userCalendar;
                createReminder(userCalendar.getTimeInMillis());
                enableReceiver();
                reminderSwitchValue = true;
                Toast.makeText(getApplicationContext(), "Reminder set", Toast.LENGTH_SHORT).show();
                //Log.d("SETTINGS_TAG", "reminder value is true");
            } else {
                // a way to disable the reminder
                cancelReminder();
                disableReceiver();
                reminderSwitchValue = false;
                //Log.d("SETTINGS_TAG", "reminder value is false");
            }
            return true;
        });


        if (reminderSwitch != null) {
            reminderSwitch.setSummaryProvider(preference -> {
                if (reminderSwitchValue) {
                    java.text.DateFormat formatter = java.text.DateFormat.getDateTimeInstance();
                    //SimpleDateFormat formatter = new SimpleDateFormat("E, dd MMM yyyy HH:mm");
                    Date calendarDate = currentCalendar.getTime();


                    return "Reminder set for " + formatter.format(calendarDate);
                } else
                    return "Reminder not set.";
            });
        }


        Preference reminderTimePicker, reminderDatePicker;
        reminderDatePicker = settingsFragment.findPreference("reminderDatePicker");
        reminderTimePicker = settingsFragment.findPreference("reminderTimePicker");

        if (reminderTimePicker != null) {
            reminderTimePicker.setOnPreferenceClickListener(preference -> {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getSupportFragmentManager(), "timePicker");
                return true;
            });

            if (reminderDatePicker != null) {
                reminderDatePicker.setOnPreferenceClickListener(preference -> {
                    DialogFragment newFragment = new DatePickerFragment();
                    newFragment.show(getSupportFragmentManager(), "datePicker");
                    return true;
                });
            }
        }


        switch (sharedPreferences.getString("reminderFrequencyPicker", "day")) {
            case "fifteen_minutes":
                reminderInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                break;
            case "half_hour":
                reminderInterval = AlarmManager.INTERVAL_HALF_HOUR;
                break;
            case "hour":
                reminderInterval = AlarmManager.INTERVAL_HOUR;
                break;
            case "half_day":
                reminderInterval = AlarmManager.INTERVAL_HALF_DAY;
                break;
            case "day":
                reminderInterval = AlarmManager.INTERVAL_DAY;
                break;
            default:
                break;
        }
        ListPreference reminderFrequency = settingsFragment.findPreference("reminderFrequencyPicker");
        reminderFrequency.setOnPreferenceChangeListener((preference, newValue) -> {
            String value = (String) newValue;
            switch (value) {
                case "fifteen_minutes":
                    reminderInterval = AlarmManager.INTERVAL_FIFTEEN_MINUTES;
                    break;
                case "half_hour":
                    reminderInterval = AlarmManager.INTERVAL_HALF_HOUR;
                    break;
                case "hour":
                    reminderInterval = AlarmManager.INTERVAL_HOUR;
                    break;
                case "half_day":
                    reminderInterval = AlarmManager.INTERVAL_HALF_DAY;
                    break;
                case "day":
                    reminderInterval = AlarmManager.INTERVAL_DAY;
                    break;
                default:
                    reminderInterval = AlarmManager.INTERVAL_DAY;
                    break;
            }
            return true;
        });


        vaultPinPreference = settingsFragment.findPreference("vaultPin");
        vaultPinPreference.setSummaryProvider(preference -> {
            EditTextPreference pref = (EditTextPreference) preference;
            return "Current pin is: " + pref.getText();
        });

        vaultPinPreference.setOnBindEditTextListener(editText -> {
            editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            editText.setFilters(new InputFilter[]{new InputFilter.LengthFilter(4)});
        });


    }

    public void createReminder(long time) {
        alarmIntent = new Intent(this, NotifyService.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmPendingIntent = PendingIntent.getService(this, 0, alarmIntent, 0);
        alarmManager.set(AlarmManager.RTC_WAKEUP, time, alarmPendingIntent);
    }

    public void enableReceiver() {
        ComponentName receiver = new ComponentName(getApplicationContext(), DeviceBootReceiver.class);
        PackageManager pm = getApplicationContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelReminder() {
        AlarmManager alarmManager =
                (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        PendingIntent pendingIntent =
                PendingIntent.getService(this, 0, alarmIntent,
                        PendingIntent.FLAG_NO_CREATE);
        if (pendingIntent != null && alarmManager != null) {
            alarmManager.cancel(pendingIntent);
            Toast.makeText(getApplicationContext(), "Reminder removed.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getApplicationContext(), "Reminder couldn't be removed.", Toast.LENGTH_LONG).show();
        }
    }

    public void disableReceiver() {
        ComponentName receiver = new ComponentName(getApplicationContext(), DeviceBootReceiver.class);
        PackageManager pm = getApplicationContext().getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onSendFinished(PendingIntent pendingIntent, Intent intent, int resultCode, String resultData, Bundle resultExtras) {
        if (pendingIntent == alarmPendingIntent && intent == alarmIntent && resultCode == RESULT_OK) {
            currentCalendar = Calendar.getInstance();
            currentCalendar.setTimeInMillis(System.currentTimeMillis() + reminderInterval);
            createReminder(System.currentTimeMillis() + reminderInterval);
            Log.d("SETTINGS_TAG", "pending intent finished");
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

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        @NotNull
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

    public static class DeviceBootReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
                // Set the alarm here.

                // lets figure this out after we've figured out the repeating alarm thing
            }
        }
    }
}