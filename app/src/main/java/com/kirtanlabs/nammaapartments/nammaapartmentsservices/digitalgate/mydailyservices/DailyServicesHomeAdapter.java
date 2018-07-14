package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;

import java.util.List;
import java.util.Locale;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_DAILYSERVICES;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_TIMEOFVISIT;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class DailyServicesHomeAdapter extends RecyclerView.Adapter<DailyServicesHomeAdapter.DailyServicesHolder> implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private EditText editPickTime;
    private Dialog dialog;

    /* ------------------------------------------------------------- *
     * Public Members
     * ------------------------------------------------------------- */

    private final List<NammaApartmentDailyService> nammaApartmentDailyServiceList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    DailyServicesHomeAdapter(List<NammaApartmentDailyService> nammaApartmentDailyServiceList, Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentDailyServiceList = nammaApartmentDailyServiceList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView Adapter object
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public DailyServicesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_visitors_and_my_daily_services_list, parent, false);
        return new DailyServicesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyServicesHolder holder, int position) {
        /*Since we are reusing the layouts we need to modify the Layout weight for Title
         * and values, since in Daily Services Home the title take less space than Visitors
         * list*/
        LinearLayout.LayoutParams layoutTitleParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1.2f
        );
        LinearLayout.LayoutParams layoutTitleValuesParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                0.8f
        );

        holder.layoutTitle.setLayoutParams(layoutTitleParams);
        holder.layoutTitleValues.setLayoutParams(layoutTitleValuesParams);
        String stringServiceName = mCtx.getResources().getString(R.string.name) + ":";
        holder.textServiceName.setText(stringServiceName);
        holder.textInvitationTimeOrStatus.setText(R.string.status);
        holder.textInvitationDateOrServiceRating.setText(R.string.rating);
        holder.textInvitedByOrNumberOfFlats.setText(R.string.flats);

        //Creating an instance of NammaApartmentDailyService class and retrieving the values from Firebase
        NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
        holder.textServiceNameValue.setText(nammaApartmentDailyService.getFullName());
        holder.textServiceTypeValue.setText(nammaApartmentDailyService.getDailyServiceType());
        holder.textInvitationDateOrServiceRatingValue.setText(String.valueOf(nammaApartmentDailyService.getRating()));
        holder.textInvitationTimeOrStatusValue.setText(nammaApartmentDailyService.getStatus());
        holder.textInvitedByOrNumberOfFlatsValue.setText(String.valueOf(nammaApartmentDailyService.getNumberOfFlats()));
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentDailyService.getProfilePhoto())
                .into(holder.visitorOrDailyServiceProfilePic);

        holder.textEdit.setText(R.string.edit);
        holder.textCancel.setText(R.string.remove);

        /*Here we are changing edit icon*/
        holder.textEdit.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.edit_new, 0, 0);
    }

    @Override
    public int getItemCount() {
        return nammaApartmentDailyServiceList.size();
    }

    /* ------------------------------------------------------------- *
     * Overriding OnClick Listeners
     * ------------------------------------------------------------- */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.editPickTime:
                baseActivity.pickTime(mCtx, this);
                break;
            case R.id.buttonCancel:
                dialog.cancel();
                break;
        }
    }

    /* ------------------------------------------------------------- *
     * Overriding OnTimeSet Listener
     * ------------------------------------------------------------- */
    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (view.isShown()) {
            String selectedTime = String.format(Locale.getDefault(), "%02d:%02d", hourOfDay, minute);
            editPickTime.setText(selectedTime);
        }
    }

    /**
     * This method gets invoked to open time dialog so that user can update time of dailyService.
     *
     * @param existingTime contains string value of the existing time value in cardView.
     * @param position     of card view for which time has to be manipulated.
     */
    private void openTimeDialog(String existingTime, int position) {
        AlertDialog.Builder alertTimeDialog = new AlertDialog.Builder(mCtx);
        View timeDialog = View.inflate(mCtx, R.layout.layout_dialog_reschedule, null);

        /*Getting Id's for all the views*/
        TextView textPickTime = timeDialog.findViewById(R.id.textPickTime);
        editPickTime = timeDialog.findViewById(R.id.editPickTime);
        TextView buttonReschedule = timeDialog.findViewById(R.id.buttonReschedule);
        TextView buttonCancel = timeDialog.findViewById(R.id.buttonCancel);
        RelativeLayout relativeLayoutDate = timeDialog.findViewById(R.id.relativeLayoutDate);

        /*Setting Fonts for all the views*/
        textPickTime.setTypeface(setLatoRegularFont(mCtx));
        buttonReschedule.setTypeface(setLatoRegularFont(mCtx));
        buttonCancel.setTypeface(setLatoRegularFont(mCtx));

        /*Hiding the appropriate views according to the appropriate dialog box*/
        relativeLayoutDate.setVisibility(View.GONE);

        /*Setting existing values*/
        editPickTime.setText(existingTime);

        /*We don't want the keyboard to be displayed when user clicks on the time edit field*/
        editPickTime.setInputType(InputType.TYPE_NULL);

        alertTimeDialog.setView(timeDialog);
        dialog = alertTimeDialog.create();
        new Dialog(mCtx);
        dialog.show();

        /*Setting OnClick Listeners to the views*/
        editPickTime.setOnClickListener(this);
        buttonCancel.setOnClickListener(this);
        buttonReschedule.setOnClickListener(v -> {
            updateDailyServiceDetails(position);
            dialog.cancel();
        });

    }

    /**
     * Based on the position the time should be updated in both UI and Firebase
     *
     * @param position of card view for which time has to be manipulated
     */
    private void updateDailyServiceDetails(int position) {
        NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
        String dailyServiceType = nammaApartmentDailyService.getDailyServiceType();
        String updatedTime = editPickTime.getText().toString();
        nammaApartmentDailyService.setTimeOfVisit(updatedTime);
        notifyItemChanged(position);
        DatabaseReference updatedTimeReference = PUBLIC_DAILYSERVICES_REFERENCE
                .child(DailyServiceType.getKeyByValue(dailyServiceType))
                .child(nammaApartmentDailyService.getUID())
                .child(NammaApartmentsGlobal.userUID)
                .child(FIREBASE_CHILD_TIMEOFVISIT);
        updatedTimeReference.setValue(updatedTime);
    }
    /* ------------------------------------------------------------- *
     * Daily Service Holder class
     * ------------------------------------------------------------- */

    class DailyServicesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        final LinearLayout layoutTitle;
        final LinearLayout layoutTitleValues;
        final TextView textServiceName;
        final TextView textServiceNameValue;
        final TextView textServiceType;
        final TextView textServiceTypeValue;
        final TextView textInvitationDateOrServiceRating;
        final TextView textInvitationDateOrServiceRatingValue;
        final TextView textInvitationTimeOrStatus;
        final TextView textInvitationTimeOrStatusValue;
        final TextView textInvitedByOrNumberOfFlats;
        final TextView textInvitedByOrNumberOfFlatsValue;
        final TextView textCall;
        final TextView textMessage;
        final TextView textEdit;
        final TextView textCancel;
        final de.hdodenhof.circleimageview.CircleImageView visitorOrDailyServiceProfilePic;


        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        DailyServicesHolder(View itemView) {
            super(itemView);
            layoutTitle = itemView.findViewById(R.id.layoutTitle);
            layoutTitleValues = itemView.findViewById(R.id.layoutTitleValues);

            textServiceName = itemView.findViewById(R.id.textVisitorOrServiceName);
            textServiceType = itemView.findViewById(R.id.textVisitorOrServiceType);
            textInvitationDateOrServiceRating = itemView.findViewById(R.id.textInvitationDateOrServiceRating);
            textInvitationTimeOrStatus = itemView.findViewById(R.id.textInvitationTimeOrStatus);
            textInvitedByOrNumberOfFlats = itemView.findViewById(R.id.textInvitedByOrNumberOfFlats);

            textServiceNameValue = itemView.findViewById(R.id.textVisitorOrServiceNameValue);
            textServiceTypeValue = itemView.findViewById(R.id.textVisitorOrServiceTypeValue);
            textInvitationDateOrServiceRatingValue = itemView.findViewById(R.id.textInvitationDateOrServiceRatingValue);
            textInvitationTimeOrStatusValue = itemView.findViewById(R.id.textInvitationTimeOrStatusValue);
            textInvitedByOrNumberOfFlatsValue = itemView.findViewById(R.id.textInvitedByOrNumberOfFlatsValue);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEdit = itemView.findViewById(R.id.textRescheduleOrEdit);
            textCancel = itemView.findViewById(R.id.textCancel);
            visitorOrDailyServiceProfilePic = itemView.findViewById(R.id.visitorOrDailyServiceProfilePic);

            textServiceName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textServiceType.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitationDateOrServiceRating.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitationTimeOrStatus.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitedByOrNumberOfFlats.setTypeface(Constants.setLatoRegularFont(mCtx));

            textServiceNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textServiceTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitationDateOrServiceRatingValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitationTimeOrStatusValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitedByOrNumberOfFlatsValue.setTypeface(Constants.setLatoBoldFont(mCtx));

            textCall.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textEdit.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textCancel.setTypeface(Constants.setLatoBoldItalicFont(mCtx));

            //Setting events for items in card view
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textEdit.setOnClickListener(this);
            textCancel.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
            switch (v.getId()) {
                case R.id.textCall:
                    baseActivity.makePhoneCall(nammaApartmentDailyService.getPhoneNumber());
                    break;
                case R.id.textMessage:
                    baseActivity.sendTextMessage(nammaApartmentDailyService.getPhoneNumber());
                    break;
                case R.id.textRescheduleOrEdit:
                    //Create a Time Dialog in which user can change time of their daily services.
                    openTimeDialog(nammaApartmentDailyService.getTimeOfVisit(), position);
                    break;
                case R.id.textCancel:
                    nammaApartmentDailyServiceList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, nammaApartmentDailyServiceList.size());
                    String dailyServiceType = nammaApartmentDailyService.getDailyServiceType();
                    DatabaseReference userDailyServiceReference = ((NammaApartmentsGlobal) mCtx.getApplicationContext()).getUserDataReference()
                            .child(FIREBASE_CHILD_DAILYSERVICES);
                    userDailyServiceReference.child(DailyServiceType.getKeyByValue(dailyServiceType))
                            .child(nammaApartmentDailyService.getUID())
                            .removeValue();
                    break;
            }
        }
    }

}