package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.R;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/5/2018
 */
public class VisitorsListAdapter extends RecyclerView.Adapter<VisitorsListAdapter.VisitorViewHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private EditText editPickDateTime;
    private String concatenatedDateAndTime = "";
    private String selectedDate = "";
    private String selectedTime = "";

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    VisitorsListAdapter(Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public VisitorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitors_and_my_daily_services_list, parent, false);
        return new VisitorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VisitorViewHolder holder, int position) {
        holder.textVisitorName.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textVisitorType.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textInvitationDateOrServiceRating.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textInvitationTime.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textInvitedByOrNumberOfFlats.setTypeface(Constants.setLatoRegularFont(mCtx));

        holder.textVisitorNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textVisitorTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationDateOrServiceRatingValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitationTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
        holder.textInvitedByOrNumberOfFlatsValue.setTypeface(Constants.setLatoBoldFont(mCtx));

        holder.textCall.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textMessage.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textReschedule.setTypeface(Constants.setLatoRegularFont(mCtx));
        holder.textCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

        holder.textCall.setOnClickListener(this);
        holder.textMessage.setOnClickListener(this);
        holder.textReschedule.setOnClickListener(this);
        holder.textCancel.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        //TODO: To change the get item count here
        return 5;
    }

    /* ------------------------------------------------------------- *
     * Visitor View Holder class
     * ------------------------------------------------------------- */
    class VisitorViewHolder extends RecyclerView.ViewHolder {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textVisitorName;
        private final TextView textVisitorNameValue;
        private final TextView textVisitorType;
        private final TextView textVisitorTypeValue;
        private final TextView textInvitationDateOrServiceRating;
        private final TextView textInvitationDateOrServiceRatingValue;
        private final TextView textInvitationTime;
        private final TextView textInvitationTimeValue;
        private final TextView textInvitedByOrNumberOfFlats;
        private final TextView textInvitedByOrNumberOfFlatsValue;

        private final TextView textCall;
        private final TextView textMessage;
        private final TextView textReschedule;
        private final TextView textCancel;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        VisitorViewHolder(View itemView) {
            super(itemView);
            textVisitorName = itemView.findViewById(R.id.textVisitorOrServiceName);
            textVisitorType = itemView.findViewById(R.id.textVisitorOrServiceType);
            textInvitationDateOrServiceRating = itemView.findViewById(R.id.textInvitationDateOrServiceRating);
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textInvitedByOrNumberOfFlats = itemView.findViewById(R.id.textInvitedByOrNumberOfFlats);

            textVisitorNameValue = itemView.findViewById(R.id.textVisitorOrServiceNameValue);
            textVisitorTypeValue = itemView.findViewById(R.id.textVisitorOrServiceTypeValue);
            textInvitationDateOrServiceRatingValue = itemView.findViewById(R.id.textInvitationDateOrServiceRatingValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textInvitedByOrNumberOfFlatsValue = itemView.findViewById(R.id.textInvitedByOrNumberOfFlatsValue);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textReschedule = itemView.findViewById(R.id.textRescheduleOrEdit);
            textCancel = itemView.findViewById(R.id.textCancel);
        }

    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick and OnFocusChange Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.textCall:
                //TODO: Change Mobile Number here
                baseActivity.makePhoneCall("9885665744");
                break;
            case R.id.textMessage:
                //TODO: Change Mobile Number here
                baseActivity.sendTextMessage("9885665744");
                break;
            case R.id.textRescheduleOrEdit:
                openRescheduleDialog();
                break;
            case R.id.textCancel:
                openCancelDialog();
                break;
            case R.id.editPickDateTime:
                displayDateAndTime();
                break;
            case R.id.buttonReschedule:
                Intent intent = new Intent(mCtx, VisitorsList.class);
                mCtx.startActivity(intent);
                break;
        }
    }
    /*-------------------------------------------------------------------------------
     *Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method is invoked when user clicks on reschedule icon.
     */
    private void openRescheduleDialog() {
        AlertDialog.Builder alertRescheduleDialog = new AlertDialog.Builder(mCtx);
        View rescheduleDialog = View.inflate(mCtx, R.layout.layout_dialog_reschedule, null);
        editPickDateTime = rescheduleDialog.findViewById(R.id.editPickDateTime);
        Button buttonReschedule = rescheduleDialog.findViewById(R.id.buttonReschedule);
        //String scheduledDateTime = Date + "\t\t" + Time;
        editPickDateTime.setText(" ");

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit field*/
        editPickDateTime.setInputType(InputType.TYPE_NULL);
        alertRescheduleDialog.setView(rescheduleDialog);
        AlertDialog dialog = alertRescheduleDialog.create();

        new Dialog(mCtx);
        dialog.show();

        editPickDateTime.setOnClickListener(this);
        buttonReschedule.setOnClickListener(this);
        //editPickDateTime.setOnFocusChangeListener(this);
    }

    /**
     * This method is invoked when user clicks on cancel icon.
     */
    private void openCancelDialog() {
        AlertDialog.Builder alertCancelDialog = new AlertDialog.Builder(mCtx);
        View cancelDialog = View.inflate(mCtx, R.layout.layout_dialog_cancel, null);
        // Setting Custom Dialog Buttons
        alertCancelDialog.setTitle("Delete");
        alertCancelDialog.setPositiveButton("Yes", (dialog, which) -> notifyItemRemoved(getItemViewType(1)));
        alertCancelDialog.setNegativeButton("No", (dialog, which) -> dialog.cancel());

        alertCancelDialog.setView(cancelDialog);
        Dialog dialog1 = alertCancelDialog.create();
        new Dialog(mCtx);
        dialog1.show();
    }

    /**
     * This method is invoked when user clicks on pick date and time icon.
     */
    private void displayDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        // Date Picker Dialog
        datePickerDialog = new DatePickerDialog(mCtx,
                (DatePicker view, int year, int month, int dayOfMonth) -> {
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    selectedDate = "";
                    selectedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
                    datePickerDialog.cancel();
                    timePickerDialog.show();
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();

        // Time Picker Dialog
        timePickerDialog = new TimePickerDialog(mCtx,
                (view, hourOfDay, minute) -> {
                    Calendar datetime = Calendar.getInstance();
                    datetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                    datetime.set(Calendar.MINUTE, minute);
                    if (selectedDate.equals(new DateFormatSymbols().getMonths()[mMonth].substring(0, 3) + " " + mDay + ", " + mYear) &&
                            datetime.getTimeInMillis() < calendar.getTimeInMillis()) {
                        Toast.makeText(mCtx, R.string.select_future_time, Toast.LENGTH_LONG).show();
                        editPickDateTime.setText("");
                    } else {
                        selectedTime = "";
                        selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
                        timePickerDialog.cancel();
                        concatenatedDateAndTime = selectedDate + "\t\t" + " " + selectedTime;
                        editPickDateTime.setText(concatenatedDateAndTime);
                    }
                }, mHour, mMinute, true);
    }
}
