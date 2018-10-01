package com.kirtanlabs.nammaapartments.services.societyservices.digigate.mydailyservices;


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
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.utilities.Constants;

import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_DAILYSERVICES;
import static com.kirtanlabs.nammaapartments.utilities.Constants.FIREBASE_CHILD_TIMEOFVISIT;
import static com.kirtanlabs.nammaapartments.utilities.Constants.PUBLIC_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.utilities.Constants.setLatoRegularFont;

public class DailyServicesHomeAdapter extends RecyclerView.Adapter<DailyServicesHomeAdapter.DailyServicesHolder> implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private final List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private RatingBar ratingBarSocietyService;
    private EditText editPickTime;

    /* ------------------------------------------------------------- *
     * Public Members
     * ------------------------------------------------------------- */
    private Dialog dialog;

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
        holder.textInvitedByOrNumberOfFlatsValue.setText(String.valueOf(DailyServicesHome.numberOfFlats.get(nammaApartmentDailyService.getUID())));
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

    /**
     * This method is invoked when User presses on Submit button in Rate Daily Service dialog
     *
     * @param position                    of card view for which time has to be manipulated
     * @param nammaApartmentDailyService  of whose rating needs to be updated
     * @param dailyServiceRatingReference where rating value has toi be set
     */
    private void submitRating(int position, NammaApartmentDailyService nammaApartmentDailyService, DatabaseReference dailyServiceRatingReference) {
        float rating = ratingBarSocietyService.getRating();
        int rateInt = Math.round(rating);
        nammaApartmentDailyService.setRating(rateInt);
        notifyItemChanged(position);
        dailyServiceRatingReference.setValue(rating);
        //TODO: Calculate, Store, and Retrieve Average Rating
    }

    /**
     * This method is invoked when the User presses on 'Rate' option. The rating is then stored in firebase
     *
     * @param position                   of card view for which time has to be manipulated
     * @param nammaApartmentDailyService of whose rating needs to be updated
     */
    private void openRateDailyServiceDialog(int position, NammaApartmentDailyService nammaApartmentDailyService) {
        View rateServiceDialog = View.inflate(mCtx, R.layout.layout_rate_society_service, null);
        /*Getting Id's for all the views*/
        TextView textRateExperience = rateServiceDialog.findViewById(R.id.textRateExperience);
        TextView textRecentSocietyService = rateServiceDialog.findViewById(R.id.textRecentSocietyService);
        CircleImageView imageRecentSocietyService = rateServiceDialog.findViewById(R.id.imageRecentSocietyService);
        ratingBarSocietyService = rateServiceDialog.findViewById(R.id.ratingBarSocietyService);
        Button buttonSubmit = rateServiceDialog.findViewById(R.id.buttonSubmit);
        /*Setting font for all the views*/
        textRateExperience.setTypeface(setLatoBoldFont(mCtx));
        buttonSubmit.setTypeface(setLatoRegularFont(mCtx));

        /*Setting dialog message*/
        textRateExperience.setText(R.string.rate_service_message);
        imageRecentSocietyService.setVisibility(View.GONE);
        textRecentSocietyService.setVisibility(View.GONE);

        /*Setting dialog*/
        android.support.v7.app.AlertDialog.Builder alertRateServiceDialog = new android.support.v7.app.AlertDialog.Builder(mCtx);
        alertRateServiceDialog.setView(rateServiceDialog);
        android.support.v7.app.AlertDialog dialog = alertRateServiceDialog.create();
        dialog.setCancelable(true);
        new Dialog(mCtx);
        dialog.show();

        /*Setting onClickListener for view*/
        buttonSubmit.setOnClickListener(v -> {
            String dailyServiceUID = nammaApartmentDailyService.getUID();
            String dailyServiceType = nammaApartmentDailyService.getDailyServiceType();
            /*Setting the rating given by the user in Daily Services data in firebase*/
            DatabaseReference dailyServiceRatingReference = PUBLIC_DAILYSERVICES_REFERENCE
                    .child(DailyServiceType.getKeyByValue(dailyServiceType)).child(dailyServiceUID).child(NammaApartmentsGlobal.userUID)
                    .child(Constants.RATING);
            submitRating(position, nammaApartmentDailyService, dailyServiceRatingReference);
            dialog.cancel();
        });
    }

    /**
     * @param position                   of card view whose daily service details needs to be removed.
     * @param nammaApartmentDailyService whose data needs to be removed.
     */
    private void removeDailyServiceDialog(int position, NammaApartmentDailyService nammaApartmentDailyService) {
        /*Runnable Interface which gets invoked once user presses OK button in Confirmation Dialog*/
        Runnable removeDailyServiceDialog = () ->
        {
            nammaApartmentDailyServiceList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, nammaApartmentDailyServiceList.size());
            String dailyServiceType = nammaApartmentDailyService.getDailyServiceType();
            DatabaseReference userDailyServiceReference = ((NammaApartmentsGlobal) mCtx.getApplicationContext()).getUserDataReference()
                    .child(FIREBASE_CHILD_DAILYSERVICES);
            userDailyServiceReference.child(DailyServiceType.getKeyByValue(dailyServiceType))
                    .child(nammaApartmentDailyService.getUID())
                    .setValue(false);

            /*This is to ensure when user deletes the last item in the list a blank screen is not shown
             * instead feature unavailable layout is shown*/
            if (nammaApartmentDailyServiceList.isEmpty()) {
                baseActivity.showFeatureUnavailableLayout(R.string.daily_service_unavailable_message);
            }
        };
        String confirmDialogTitle = mCtx.getString(R.string.remove_daily_service_title);
        String confirmDialogMessage = mCtx.getString(R.string.daily_service_cancel);
        baseActivity.showConfirmDialog(confirmDialogTitle, confirmDialogMessage, removeDailyServiceDialog);
    }
    /* ------------------------------------------------------------- *
     * Daily Service Holder class
     * ------------------------------------------------------------- */

    class DailyServicesHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        final LinearLayout layoutExtras;
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
        final TextView textRate;
        final de.hdodenhof.circleimageview.CircleImageView visitorOrDailyServiceProfilePic;


        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        DailyServicesHolder(View itemView) {
            super(itemView);

            /*Getting id's for all the views*/
            layoutExtras = itemView.findViewById(R.id.layoutExtras);
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
            textRate = itemView.findViewById(R.id.textRate);

            /*Setting fonts for all the views*/
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

            /*Ensuring 'Rate' option is shown only in the Daily Service list, since the Guest list also uses the same layout*/
            layoutExtras.setWeightSum(5);
            textRate.setVisibility(View.VISIBLE);

            textCall.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textMessage.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textEdit.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textCancel.setTypeface(Constants.setLatoBoldItalicFont(mCtx));
            textRate.setTypeface(Constants.setLatoBoldItalicFont(mCtx));

            //Setting events for items in card view
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textEdit.setOnClickListener(this);
            textRate.setOnClickListener(this);
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
                case R.id.textRate:
                    openRateDailyServiceDialog(position, nammaApartmentDailyService);
                    break;
                case R.id.textCancel:
                    //Create a Remove Dialog in which user can remove their daily services.
                    removeDailyServiceDialog(position, nammaApartmentDailyService);
                    break;
            }
        }
    }

}