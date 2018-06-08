package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.myvisitorslist;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.NammaApartmentVisitor;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/5/2018
 */
public class VisitorsListAdapter extends RecyclerView.Adapter<VisitorsListAdapter.VisitorViewHolder> implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    /* ------------------------------------------------------------- *
     * Public Members
     * ------------------------------------------------------------- */

    public static int count = 5;
    private final Context mCtx;
    private final BaseActivity baseActivity;
    private View rescheduleDialog;
    private AlertDialog dialog;
    private List<NammaApartmentVisitor> nammaApartmentVisitorList;
    private EditText editPickDate;
    private EditText editPickTime;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    public VisitorsListAdapter(List<NammaApartmentVisitor> nammaApartmentVisitorList, Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentVisitorList = nammaApartmentVisitorList;
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
        //Creating an instance of NammaApartmentVisitor class and retrieving the values from Firebase
        NammaApartmentVisitor nammaApartmentVisitor = nammaApartmentVisitorList.get(position);
        String dateAndTime = nammaApartmentVisitor.getDateAndTimeOfVisit();
        String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
        holder.textVisitorNameValue.setText(nammaApartmentVisitor.getFullName());
        holder.textInvitationDateOrServiceRatingValue.setText(separatedDateAndTime[0]);
        holder.textInvitationTimeValue.setText(separatedDateAndTime[1]);
        holder.textInvitedByOrNumberOfFlatsValue.setText(((NammaApartmentsGlobal) mCtx.getApplicationContext()).getNammaApartmentUser().getFullName());
    }

    @Override
    public int getItemCount() {
        return nammaApartmentVisitorList.size();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editPickDate:
                baseActivity.pickDate(mCtx, this);
                break;
            case R.id.editPickTime:
                baseActivity.pickTime(mCtx, this);
                break;
            case R.id.buttonReschedule:
                //TODO: On click of Reschedule button the rescheduled date and time should go to firebase.
                dialog.cancel();
                break;
            case R.id.buttonCancel:
                dialog.cancel();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnDateSet & OnTimeSet Listener
     * ------------------------------------------------------------- */

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        if (view.isShown()) {
            String selectedDate = new DateFormatSymbols().getMonths()[month].substring(0, 3) + " " + dayOfMonth + ", " + year;
            editPickDate.setText(selectedDate);
        }
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            editPickTime.setText(selectedTime);
        }
    }

    /*-------------------------------------------------------------------------------
     *Private Methods
     *-----------------------------------------------------------------------------*/

    /**
     * This method is invoked when user clicks on reschedule icon.
     */
    private void openRescheduleDialog(String existingDate, String existingTime) {
        rescheduleDialog = View.inflate(mCtx, R.layout.layout_dialog_reschedule, null);

        /*Getting Id's for all the views*/
        editPickDate = rescheduleDialog.findViewById(R.id.editPickDate);
        editPickTime = rescheduleDialog.findViewById(R.id.editPickTime);
        TextView textPickDate = rescheduleDialog.findViewById(R.id.textPickDate);
        TextView textPickTime = rescheduleDialog.findViewById(R.id.textPickTime);
        TextView buttonReschedule = rescheduleDialog.findViewById(R.id.buttonReschedule);
        TextView buttonCancel = rescheduleDialog.findViewById(R.id.buttonCancel);

        /*Setting Fonts for all the views*/
        textPickDate.setTypeface(Constants.setLatoRegularFont(mCtx));
        textPickTime.setTypeface(Constants.setLatoRegularFont(mCtx));
        buttonReschedule.setTypeface(Constants.setLatoRegularFont(mCtx));
        buttonCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

        /*Setting existing values*/
        editPickDate.setText(existingDate);
        editPickTime.setText(existingTime);

        /*We don't want the keyboard to be displayed when user clicks on the pick date and time edit fields*/
        editPickDate.setInputType(InputType.TYPE_NULL);
        editPickTime.setInputType(InputType.TYPE_NULL);

        /*This method is used to create reschedule dialog */
        createRescheduleDialog();

        /*Setting OnClick Listeners to the views*/
        editPickDate.setOnClickListener(this);
        editPickTime.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonReschedule.setOnClickListener(this);
    }

    /**
     * This method is invoked to create a reschedule dialog.
     */
    private void createRescheduleDialog() {
        AlertDialog.Builder alertRescheduleDialog = new AlertDialog.Builder(mCtx);
        alertRescheduleDialog.setView(rescheduleDialog);
        dialog = alertRescheduleDialog.create();

        new Dialog(mCtx);
        dialog.show();
    }

    /* ------------------------------------------------------------- *
     * Visitor View Holder class
     * ------------------------------------------------------------- */
    class VisitorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

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

            //Setting Fonts for all the views on cardview
            textVisitorName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVisitorType.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitationDateOrServiceRating.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitationTime.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitedByOrNumberOfFlats.setTypeface(Constants.setLatoRegularFont(mCtx));
            textVisitorNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textVisitorTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitationDateOrServiceRatingValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitationTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitedByOrNumberOfFlatsValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textCall.setTypeface(Constants.setLatoRegularFont(mCtx));
            textMessage.setTypeface(Constants.setLatoRegularFont(mCtx));
            textReschedule.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

            //Setting events for items in card view
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textReschedule.setOnClickListener(this);
            textCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NammaApartmentVisitor nammaApartmentVisitor = nammaApartmentVisitorList.get(position);
            switch (v.getId()) {
                case R.id.textCall:
                    baseActivity.makePhoneCall(nammaApartmentVisitor.getMobileNumber());
                    break;
                case R.id.textMessage:
                    baseActivity.sendTextMessage(nammaApartmentVisitor.getMobileNumber());
                    break;
                case R.id.textRescheduleOrEdit:
                    openRescheduleDialog(textInvitationDateOrServiceRatingValue.getText().toString(), textInvitationTimeValue.getText().toString());
                    break;
                case R.id.textCancel:
                    nammaApartmentVisitorList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, nammaApartmentVisitorList.size());
                    break;
            }
        }
    }

}
