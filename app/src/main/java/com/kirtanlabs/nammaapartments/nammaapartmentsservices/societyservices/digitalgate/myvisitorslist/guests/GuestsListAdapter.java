package com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.myvisitorslist.guests;

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

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.societyservices.digitalgate.invitevisitors.NammaApartmentGuest;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DATEANDTIMEOFVISIT;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartments.Constants.NOT_ENTERED;
import static com.kirtanlabs.nammaapartments.Constants.PREAPPROVED_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/5/2018
 */
public class GuestsListAdapter extends RecyclerView.Adapter<GuestsListAdapter.GuestViewHolder> implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private final List<NammaApartmentGuest> nammaApartmentGuestList;
    private View rescheduleDialog;
    private AlertDialog dialog;
    private EditText editPickDate;
    private EditText editPickTime;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    GuestsListAdapter(List<NammaApartmentGuest> nammaApartmentGuestList, Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentGuestList = nammaApartmentGuestList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public GuestViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitors_and_my_daily_services_list, parent, false);
        return new GuestViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GuestViewHolder holder, int position) {
        //Creating an instance of NammaApartmentGuest class and retrieving the values from Firebase
        NammaApartmentGuest nammaApartmentGuest = nammaApartmentGuestList.get(position);

        /*If Guest has not arrived we change cancel text with appropriate text */
        if (nammaApartmentGuest.getStatus().equals(NOT_ENTERED)) {
            holder.textCancel.setText(mCtx.getString(R.string.cancel));
        }

        String dateAndTime = nammaApartmentGuest.getDateAndTimeOfVisit();
        String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
        holder.textGuestNameValue.setText(nammaApartmentGuest.getFullName());
        holder.textGuestStatusValue.setText(nammaApartmentGuest.getStatus());
        holder.textInvitationDateValue.setText(separatedDateAndTime[0]);
        holder.textInvitationTimeOrStatusValue.setText(separatedDateAndTime[1]);
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentGuest.getProfilePhoto())
                .into(holder.visitorOrDailyServiceProfilePic);

        /*We check if the inviters UID is equal to current UID if it is then we don't have to check in
        firebase since we now know that the inviter is current user.*/
        if (nammaApartmentGuest.getInviterUID().equals(NammaApartmentsGlobal.userUID)) {
            holder.textInvitedByValue.setText(
                    ((NammaApartmentsGlobal) mCtx.getApplicationContext())
                            .getNammaApartmentUser()
                            .getPersonalDetails()
                            .getFullName());
        } else {
            /*Guest has been invited by some other family member; We check in firebase and get the name
             * of that family member*/
            DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentGuest.getInviterUID());
            userPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                    holder.textInvitedByValue.setText(Objects.requireNonNull(nammaApartmentUser).getPersonalDetails().getFullName());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return nammaApartmentGuestList.size();
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
    private void openRescheduleDialog(String existingDate, String existingTime, int position) {
        rescheduleDialog = View.inflate(mCtx, R.layout.layout_dialog_reschedule, null);

        /*Getting Id's for all the views*/
        editPickDate = rescheduleDialog.findViewById(R.id.editPickDate);
        editPickTime = rescheduleDialog.findViewById(R.id.editPickTime);
        TextView textPickDate = rescheduleDialog.findViewById(R.id.textPickDate);
        TextView textPickTime = rescheduleDialog.findViewById(R.id.textPickTime);
        TextView buttonReschedule = rescheduleDialog.findViewById(R.id.buttonReschedule);
        TextView buttonCancel = rescheduleDialog.findViewById(R.id.buttonCancel);

        /*Setting Fonts for all the views*/
        textPickDate.setTypeface(setLatoRegularFont(mCtx));
        textPickTime.setTypeface(setLatoRegularFont(mCtx));
        buttonReschedule.setTypeface(setLatoRegularFont(mCtx));
        buttonCancel.setTypeface(setLatoRegularFont(mCtx));

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
        buttonReschedule.setOnClickListener(v -> {
            updateGuestDataInFirebase(position);
            dialog.cancel();
        });
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

    /**
     * Based on the position the date and time is updated in both UI and Firebase
     *
     * @param position of card view for which date and time has been manipulated
     */
    private void updateGuestDataInFirebase(int position) {
        NammaApartmentGuest nammaApartmentGuest = nammaApartmentGuestList.get(position);
        String updatedDateAndTime = editPickDate.getText().toString() + "\t\t " + editPickTime.getText().toString();
        nammaApartmentGuest.setDateAndTimeOfVisit(updatedDateAndTime);
        notifyItemChanged(position);
        PREAPPROVED_VISITORS_REFERENCE.child(nammaApartmentGuest.getUid())
                .child(FIREBASE_CHILD_DATEANDTIMEOFVISIT).setValue(updatedDateAndTime);
    }

    /**
     * Based on the position the guest data is removed from List in UI and Firebase
     *
     * @param position            of card view whose guest details need to be removed
     * @param nammaApartmentGuest whose data needs to be removed
     */
    private void removeVisitor(int position, NammaApartmentGuest nammaApartmentGuest) {
        String inviterUID = nammaApartmentGuest.getInviterUID();
        if (inviterUID.equals(NammaApartmentsGlobal.userUID)) {

            /*Runnable Interface which gets invoked once user presses OK button in Confirmation Dialog*/
            Runnable removeVisitor = () -> {
                String visitorUID = nammaApartmentGuest.getUid();
                nammaApartmentGuestList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, nammaApartmentGuestList.size());
                DatabaseReference userDataReference = ((NammaApartmentsGlobal) mCtx.getApplicationContext()).getUserDataReference();
                userDataReference.child(FIREBASE_CHILD_VISITORS).child(NammaApartmentsGlobal.userUID).child(visitorUID).setValue(false);
            };
            String confirmDialogTitle = mCtx.getString(R.string.remove_guest_title);
            String confirmDialogMessage = mCtx.getString(R.string.remove_guest_message);

            /*If Guest has not yet arrived, indicates User wants to cancel the invitation,
             * setting title and message appropriately*/
            if (nammaApartmentGuest.getStatus().equals(NOT_ENTERED)) {
                confirmDialogTitle = mCtx.getString(R.string.cancel_invitation_title);
                confirmDialogMessage = mCtx.getString(R.string.cancel_invitation_message);
            }
            baseActivity.showConfirmDialog(confirmDialogTitle, confirmDialogMessage, removeVisitor);
        } else {
            baseActivity.showNotificationDialog(mCtx.getResources().getString(R.string.non_admin_remove_title_message),
                    mCtx.getResources().getString(R.string.non_inviter_remove_message), null);
        }
    }

    /* ------------------------------------------------------------- *
     * Guest View Holder class
     * ------------------------------------------------------------- */

    class GuestViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textGuestName;
        private final TextView textGuestNameValue;
        private final TextView textGuestStatus;
        private final TextView textGuestStatusValue;
        private final TextView textInvitationDate;
        private final TextView textInvitationDateValue;
        private final TextView textInvitationTimeOrStatus;
        private final TextView textInvitationTimeOrStatusValue;
        private final TextView textInvitedBy;
        private final TextView textInvitedByValue;
        private final TextView textCall;
        private final TextView textMessage;
        private final TextView textReschedule;
        private final TextView textCancel;
        private final de.hdodenhof.circleimageview.CircleImageView visitorOrDailyServiceProfilePic;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        GuestViewHolder(View itemView) {
            super(itemView);
            textGuestName = itemView.findViewById(R.id.textVisitorOrServiceName);
            textGuestStatus = itemView.findViewById(R.id.textVisitorOrServiceType);
            textInvitationDate = itemView.findViewById(R.id.textInvitationDateOrServiceRating);
            textInvitationTimeOrStatus = itemView.findViewById(R.id.textInvitationTimeOrStatus);
            textInvitedBy = itemView.findViewById(R.id.textInvitedByOrNumberOfFlats);
            textGuestNameValue = itemView.findViewById(R.id.textVisitorOrServiceNameValue);
            textGuestStatusValue = itemView.findViewById(R.id.textVisitorOrServiceTypeValue);
            textInvitationDateValue = itemView.findViewById(R.id.textInvitationDateOrServiceRatingValue);
            textInvitationTimeOrStatusValue = itemView.findViewById(R.id.textInvitationTimeOrStatusValue);
            textInvitedByValue = itemView.findViewById(R.id.textInvitedByOrNumberOfFlatsValue);
            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textReschedule = itemView.findViewById(R.id.textRescheduleOrEdit);
            textCancel = itemView.findViewById(R.id.textCancel);
            visitorOrDailyServiceProfilePic = itemView.findViewById(R.id.visitorOrDailyServiceProfilePic);

            //Setting Fonts for all the views on cardview
            textGuestName.setTypeface(setLatoRegularFont(mCtx));
            textGuestStatus.setTypeface(setLatoRegularFont(mCtx));
            textInvitationDate.setTypeface(setLatoRegularFont(mCtx));
            textInvitationTimeOrStatus.setTypeface(setLatoRegularFont(mCtx));
            textInvitedBy.setTypeface(setLatoRegularFont(mCtx));
            textGuestNameValue.setTypeface(setLatoBoldFont(mCtx));
            textGuestStatusValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationDateValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationTimeOrStatusValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitedByValue.setTypeface(setLatoBoldFont(mCtx));

            textCall.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textReschedule.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textCancel.setTypeface(Constants.setLatoBoldItalicFont(mCtx));

            /*Since this is Guests list we would want to show Status instead of Type*/
            textGuestStatus.setText(mCtx.getString(R.string.status));

            //Setting events for items in card view
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textReschedule.setOnClickListener(this);
            textCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NammaApartmentGuest nammaApartmentGuest = nammaApartmentGuestList.get(position);
            switch (v.getId()) {
                case R.id.textCall:
                    baseActivity.makePhoneCall(nammaApartmentGuest.getMobileNumber());
                    break;
                case R.id.textMessage:
                    baseActivity.sendTextMessage(nammaApartmentGuest.getMobileNumber());
                    break;
                case R.id.textRescheduleOrEdit:
                    openRescheduleDialog(textInvitationDateValue.getText().toString(), textInvitationTimeOrStatusValue.getText().toString(), position);
                    break;
                case R.id.textCancel:
                    removeVisitor(position, nammaApartmentGuest);
                    break;
            }
        }
    }

}
