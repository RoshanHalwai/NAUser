package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.FirebaseDatabase;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.Constants;
import com.kirtanlabs.nammaapartments.NammaApartmentsGlobal;
import com.kirtanlabs.nammaapartments.R;

import java.util.List;

public class DailyServicesHomeAdapter extends RecyclerView.Adapter<DailyServicesHomeAdapter.DailyServicesHolder> {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    /* ------------------------------------------------------------- *
     * Public Members
     * ------------------------------------------------------------- */
    private List<NammaApartmentDailyService> nammaApartmentDailyServiceList;
    private String service_name_value;
    private String service_inTime_value;
    private String service_type_value;

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
        holder.textInvitationDateOrServiceRating.setText(R.string.rating);
        holder.textInvitedByOrNumberOfFlats.setText(R.string.flats);

        //Creating an instance of NammaApartmentDailyService class and retrieving the values from Firebase
        NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
        holder.textServiceNameValue.setText(nammaApartmentDailyService.getfullName());
        holder.textServiceTypeValue.setText(nammaApartmentDailyService.getDailyServiceType());
        holder.textInvitationDateOrServiceRatingValue.setText(String.valueOf(nammaApartmentDailyService.getRating()));
        holder.textInvitedByOrNumberOfFlatsValue.setText("3");

        holder.textEdit.setText(R.string.edit);
        holder.textCancel.setText(R.string.remove);

        /*Here we are changing edit icon*/
        holder.textEdit.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.edit, 0, 0);

        service_name_value = holder.textServiceNameValue.getText().toString();
        service_inTime_value = holder.textInvitationTimeValue.getText().toString();
        service_type_value = holder.textServiceTypeValue.getText().toString();

    }

    @Override
    public int getItemCount() {
        return nammaApartmentDailyServiceList.size();
    }


    /* ------------------------------------------------------------- *
     * Private Methods
     * ------------------------------------------------------------- */

    /**
     * This method is invoked when user clicks on Edit icon in the list and passes all daily service details in EditMy daily Services Details
     */
    private void editMyServiceDetails(String name, String inTime, String serviceType) {
        Intent EditIntent = new Intent(mCtx, EditDailyServicesAndFamilyMemberDetails.class);
        EditIntent.putExtra(Constants.SCREEN_TITLE, R.string.my_daily_services);
        EditIntent.putExtra(Constants.NAME, name);
        EditIntent.putExtra(Constants.MOBILE_NUMBER, "7895185103");    //TODO :  To change the mobile number here
        EditIntent.putExtra(Constants.IN_TIME, inTime);
        EditIntent.putExtra(Constants.SERVICE_TYPE, serviceType);
        mCtx.startActivity(EditIntent);
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
        final TextView textInvitationTime;
        final TextView textInvitationTimeValue;
        final TextView textInvitedByOrNumberOfFlats;
        final TextView textInvitedByOrNumberOfFlatsValue;
        final TextView textCall;
        final TextView textMessage;
        final TextView textEdit;
        final TextView textCancel;

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
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textInvitedByOrNumberOfFlats = itemView.findViewById(R.id.textInvitedByOrNumberOfFlats);

            textServiceNameValue = itemView.findViewById(R.id.textVisitorOrServiceNameValue);
            textServiceTypeValue = itemView.findViewById(R.id.textVisitorOrServiceTypeValue);
            textInvitationDateOrServiceRatingValue = itemView.findViewById(R.id.textInvitationDateOrServiceRatingValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textInvitedByOrNumberOfFlatsValue = itemView.findViewById(R.id.textInvitedByOrNumberOfFlatsValue);

            textCall = itemView.findViewById(R.id.textCall);
            textMessage = itemView.findViewById(R.id.textMessage);
            textEdit = itemView.findViewById(R.id.textRescheduleOrEdit);
            textCancel = itemView.findViewById(R.id.textCancel);

            textServiceName.setTypeface(Constants.setLatoRegularFont(mCtx));
            textServiceType.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitationDateOrServiceRating.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitationTime.setTypeface(Constants.setLatoRegularFont(mCtx));
            textInvitedByOrNumberOfFlats.setTypeface(Constants.setLatoRegularFont(mCtx));

            textServiceNameValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textServiceTypeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitationDateOrServiceRatingValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitationTimeValue.setTypeface(Constants.setLatoBoldFont(mCtx));
            textInvitedByOrNumberOfFlatsValue.setTypeface(Constants.setLatoBoldFont(mCtx));

            textCall.setTypeface(Constants.setLatoRegularFont(mCtx));
            textMessage.setTypeface(Constants.setLatoRegularFont(mCtx));
            textEdit.setTypeface(Constants.setLatoRegularFont(mCtx));
            textCancel.setTypeface(Constants.setLatoRegularFont(mCtx));

            //Setting events for items in card view
            textCall.setOnClickListener(this);
            textMessage.setOnClickListener(this);
            textEdit.setOnClickListener(this);
            textCancel.setOnClickListener(this);
        }

        @SuppressLint("RestrictedApi")
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
                    editMyServiceDetails(service_name_value, service_inTime_value, service_type_value);
                    break;
                case R.id.textCancel:
                    nammaApartmentDailyServiceList.remove(position);
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, nammaApartmentDailyServiceList.size());
                    String dailyServiceType = "my" + nammaApartmentDailyService.getDailyServiceType();
                    FirebaseDatabase.getInstance().getReference(Constants.FIREBASE_CHILD_USERS)
                            .child(Constants.FIREBASE_CHILD_PRIVATE)
                            .child(((NammaApartmentsGlobal) mCtx.getApplicationContext()).getNammaApartmentUser().getUID())
                            .child(Constants.FIREBASE_CHILD_MYDAILYSERVICES)
                            .child(dailyServiceType)
                            .child(nammaApartmentDailyService.getUID())
                            .removeValue();
                    break;
            }
        }
    }

}
