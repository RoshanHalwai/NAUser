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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.invitevisitors.NammaApartmentVisitor;
import com.kirtanlabs.nammaapartments.userpojo.NammaApartmentUser;

import java.text.DateFormatSymbols;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DATEANDTIMEOFVISIT;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_VISITORS;
import static com.kirtanlabs.nammaapartments.Constants.PREAPPROVED_VISITORS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.PRIVATE_USERS_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

/**
 * KirtanLabs Pvt. Ltd.
 * Created by Roshan Halwai on 5/5/2018
 */
public class VisitorsListAdapter extends RecyclerView.Adapter<VisitorsListAdapter.VisitorViewHolder> implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

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

    VisitorsListAdapter(List<NammaApartmentVisitor> nammaApartmentVisitorList, Context mCtx) {
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

        //Since we need inviters name we get the details by inviter UID
        DatabaseReference userPrivateReference = PRIVATE_USERS_REFERENCE.child(nammaApartmentVisitor.getInviterUID());
        userPrivateReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        NammaApartmentUser nammaApartmentUser = dataSnapshot.getValue(NammaApartmentUser.class);
                        String dateAndTime = nammaApartmentVisitor.getDateAndTimeOfVisit();
                        String separatedDateAndTime[] = TextUtils.split(dateAndTime, "\t\t ");
                        holder.textVisitorNameValue.setText(nammaApartmentVisitor.getFullName());
                        holder.textInvitationDateOrServiceRatingValue.setText(separatedDateAndTime[0]);
                        holder.textInvitationTimeValue.setText(separatedDateAndTime[1]);
                        holder.textInvitedByOrNumberOfFlatsValue.setText(Objects.requireNonNull(nammaApartmentUser).getPersonalDetails().getFullName());
                        baseActivity.showProgressIndicator();
                        Glide.with(mCtx).load(nammaApartmentVisitor.getProfilePhoto())
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        baseActivity.hideProgressIndicator();
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        baseActivity.hideProgressIndicator();
                                        return false;
                                    }
                                })
                                .into(holder.visitorOrDailyServiceProfilePic);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
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
            updateVisitorDataInFirebase(position);
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
    private void updateVisitorDataInFirebase(int position) {
        NammaApartmentVisitor nammaApartmentVisitor = nammaApartmentVisitorList.get(position);
        String updatedDateAndTime = editPickDate.getText().toString() + "\t\t " + editPickTime.getText().toString();
        nammaApartmentVisitor.setDateAndTimeOfVisit(updatedDateAndTime);
        notifyItemChanged(position);
        PREAPPROVED_VISITORS_REFERENCE.child(nammaApartmentVisitor.getUid())
                .child(FIREBASE_CHILD_DATEANDTIMEOFVISIT).setValue(updatedDateAndTime);
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
        private final de.hdodenhof.circleimageview.CircleImageView visitorOrDailyServiceProfilePic;

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
            visitorOrDailyServiceProfilePic = itemView.findViewById(R.id.visitorOrDailyServiceProfilePic);

            //Setting Fonts for all the views on cardview
            textVisitorName.setTypeface(setLatoRegularFont(mCtx));
            textVisitorType.setTypeface(setLatoRegularFont(mCtx));
            textInvitationDateOrServiceRating.setTypeface(setLatoRegularFont(mCtx));
            textInvitationTime.setTypeface(setLatoRegularFont(mCtx));
            textInvitedByOrNumberOfFlats.setTypeface(setLatoRegularFont(mCtx));
            textVisitorNameValue.setTypeface(setLatoBoldFont(mCtx));
            textVisitorTypeValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationDateOrServiceRatingValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationTimeValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitedByOrNumberOfFlatsValue.setTypeface(setLatoBoldFont(mCtx));
            textCall.setTypeface(setLatoRegularFont(mCtx));
            textMessage.setTypeface(setLatoRegularFont(mCtx));
            textReschedule.setTypeface(setLatoRegularFont(mCtx));
            textCancel.setTypeface(setLatoRegularFont(mCtx));

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
                    openRescheduleDialog(textInvitationDateOrServiceRatingValue.getText().toString(), textInvitationTimeValue.getText().toString(), position);
                    break;
                case R.id.textCancel:
                    nammaApartmentVisitorList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, nammaApartmentVisitorList.size());
                    String inviterUID = nammaApartmentVisitor.getInviterUID();
                    String visitorUID = nammaApartmentVisitor.getUid();
                    if (inviterUID.equals(NammaApartmentsGlobal.userUID)) {
                        ((NammaApartmentsGlobal) mCtx.getApplicationContext()).getUserDataReference().child(FIREBASE_CHILD_VISITORS)
                                .child(NammaApartmentsGlobal.userUID).child(visitorUID).removeValue();
                    } else {
                        //TODO: Show Dialog box indicating that they cannot delete this visitor since they haven't invited them
                        Log.d("NammaApartment TAG", "User cannot delete this record since they haven't invited them");
                    }
                    break;
            }
        }
    }

}
