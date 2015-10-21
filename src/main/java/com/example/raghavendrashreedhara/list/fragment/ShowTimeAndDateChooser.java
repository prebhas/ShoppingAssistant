package com.example.raghavendrashreedhara.list.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.example.raghavendrashreedhara.list.R;
import com.example.raghavendrashreedhara.list.utility.Constants;
import com.example.raghavendrashreedhara.list.utility.DateAndTimeUtil;
import com.example.raghavendrashreedhara.list.utility.ShoppingList;
import com.example.raghavendrashreedhara.list.utility.Utils;

import java.util.Calendar;

/**
 * Created by root on 9/3/15.
 */
public class ShowTimeAndDateChooser extends DialogFragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    public EditText mReminderDate;
    public EditText mReminderTime;
    private Spinner mRepeatEvent;
    // the spinner options
    private String[] mSpinnerOptions = {"Does not repeat", "Daily", "Weekly", "Monthly", "Yearly"};
    private int mPos;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        AlertDialog.Builder b = new AlertDialog.Builder(getActivity())
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                if (getArguments() != null) {
                                    ShoppingList list = getArguments().getParcelable(Constants.GET_PARCELABLE_SHOPPING_LIST);
                                    if (list != null) {
                                        setReminderAlarm(list.getListDate(), list.getListname());
                                    }
                                }
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                );
        if (getActivity() == null)
            return null;

        View v = getActivity().getLayoutInflater().inflate(R.layout.show_time_and_date_dialog_fragment, null);
        mReminderDate = (EditText) v.findViewById(R.id.reminder_date);
        mReminderDate.setOnClickListener(this);
        Calendar c = Calendar.getInstance();
        mReminderDate.setText(Utils.returnDate((c.getTime())));

        mReminderTime = (EditText) v.findViewById(R.id.reminder_time);
        mReminderTime.setOnClickListener(this);

        mReminderTime.setText(Utils.returnCurrentTime(System.currentTimeMillis()));

        mRepeatEvent = (Spinner) v.findViewById(R.id.repeat_event);
        mRepeatEvent.setOnItemSelectedListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, mSpinnerOptions);
        mRepeatEvent.setAdapter(adapter); // this will set list of values to spinner
        b.setView(v);
        return b.create();
    }

    private void setReminderAlarm(long listCreationDate, String listname) {
        Calendar cal = Calendar.getInstance();
        cal.set(DateAndTimeUtil.getYear(), DateAndTimeUtil.getMonth(), DateAndTimeUtil.getDay(), DateAndTimeUtil.getHour(), DateAndTimeUtil.getMinute());

        Utils.setReminder(getActivity(), cal, mPos, listCreationDate, listname);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.reminder_date:
                showDatePickerFrag();
                break;
            case R.id.reminder_time:
                showTimePickerFrag();
                break;
        }
    }

    private void showTimePickerFrag() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        TimePickerFragment frag = new TimePickerFragment();
        frag.show(fm, "fragment_show_time");
    }

    private void showDatePickerFrag() {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        DatePickerFragment frag = new DatePickerFragment();
        frag.show(fm, "fragment_show_date");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String repeatOptionSelected = mSpinnerOptions[position];
        mPos = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {
        public Calendar mCal = Calendar.getInstance();

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            int hour = mCal.get(Calendar.HOUR_OF_DAY);
            int minute = mCal.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    false);
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Do something with the time chosen by the user

            mCal = Calendar.getInstance();
            mCal.set(0, 0, 0, hourOfDay, minute, 0);
            // Set the edit text with the time chosen by the user
            mReminderTime.setText(Utils.returnCurrentTime(mCal.getTimeInMillis()));
            DateAndTimeUtil.setHour(hourOfDay);
            DateAndTimeUtil.setMinute(minute);


        }
    }


    public class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {
        public Calendar mCalendar = Calendar.getInstance();
        ;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            int year = mCalendar.get(Calendar.YEAR);
            int month = mCalendar.get(Calendar.MONTH);
            int day = mCalendar.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            // Do something with the date chosen by the user
            mCalendar.set(year, month, day,
                    0, 0, 0);
            DateAndTimeUtil.setDay(day);
            DateAndTimeUtil.setYear(year);
            DateAndTimeUtil.setMonth(month);
            mReminderDate.setText(Utils.returnDate(mCalendar.getTime()));
        }


    }
}

