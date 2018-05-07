package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors;


import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.=
 */
public class DatePicker extends DialogFragment {

    private int day;
    private int month;
    private int year;
    public DatePicker datepickercallback;

    public DatePicker() {
        // Required empty public constructor
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = null;

        Calendar mycalendar = Calendar.getInstance();
        day = mycalendar.get(Calendar.DAY_OF_MONTH);
        month = mycalendar.get(Calendar.MONTH);
        year = mycalendar.get(Calendar.YEAR);
        /*DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(android.widget.DatePicker view, int year, int month, int dayOfMonth) {
                String selecteddate = dayOfMonth + "-" + (month + 1) + "-" + year;
                datepickercallback.updateDateValue(selecteddate);
            }
        }, year, month, day);*/
        return super.onCreateDialog(savedInstanceState);
    }

}
