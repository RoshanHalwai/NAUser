package com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.notifydigitalgate;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DatabaseReference;
import com.kirtanlabs.nammaapartments.BaseActivity;
import com.kirtanlabs.nammaapartments.R;
import com.kirtanlabs.nammaapartments.nammaapartmentsservices.digitalgate.mydailyservices.NammaApartmentDailyService;

import java.util.List;

import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_HANDED_THINGS;
import static com.kirtanlabs.nammaapartments.Constants.FIREBASE_CHILD_HANDED_THINGS_DESCRIPTION;
import static com.kirtanlabs.nammaapartments.Constants.PUBLIC_DAILYSERVICES_REFERENCE;
import static com.kirtanlabs.nammaapartments.Constants.setLatoBoldFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoLightFont;
import static com.kirtanlabs.nammaapartments.Constants.setLatoRegularFont;

public class HandedThingsToDailyServiceAdapter extends RecyclerView.Adapter<HandedThingsToDailyServiceAdapter.DailyServiceViewHolder> implements View.OnClickListener {

    /* ------------------------------------------------------------- *
     * Private Members
     * ------------------------------------------------------------- */

    private final Context mCtx;
    private final BaseActivity baseActivity;
    private final List<NammaApartmentDailyService> nammaApartmentDailyServiceList;

    /* ------------------------------------------------------------- *
     * Constructor
     * ------------------------------------------------------------- */

    HandedThingsToDailyServiceAdapter(List<NammaApartmentDailyService> nammaApartmentDailyServiceList, Context mCtx) {
        this.mCtx = mCtx;
        baseActivity = (BaseActivity) mCtx;
        this.nammaApartmentDailyServiceList = nammaApartmentDailyServiceList;
    }

    /* ------------------------------------------------------------- *
     * Overriding RecyclerView.Adapter Objects
     * ------------------------------------------------------------- */

    @NonNull
    @Override
    public HandedThingsToDailyServiceAdapter.DailyServiceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //inflating and returning our view holder
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.layout_handed_things_to_daily_services, parent, false);
        return new HandedThingsToDailyServiceAdapter.DailyServiceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HandedThingsToDailyServiceAdapter.DailyServiceViewHolder holder, int position) {
        //Creating an instance of NammaApartmentVisitor class and retrieving the values from Firebase
        NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
        holder.textInvitationTimeValue.setText(nammaApartmentDailyService.getTimeOfVisit());
        holder.textDailyServiceNameValue.setText(nammaApartmentDailyService.getfullName());
        holder.textDailyServiceTypeValue.setText(nammaApartmentDailyService.getDailyServiceType());
        holder.textDailyServiceRatingValue.setText(String.valueOf(nammaApartmentDailyService.getRating()));
        holder.textDailyServiceNoOfFlatsValue.setText(String.valueOf(nammaApartmentDailyService.getOwnersUID().size()));
        Glide.with(mCtx.getApplicationContext()).load(nammaApartmentDailyService.getProfilePhoto())
                .into(holder.profileImage);
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

    }

    class DailyServiceViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        /* ------------------------------------------------------------- *
         * Private Members
         * ------------------------------------------------------------- */

        private final TextView textDailyServiceName;
        private final TextView textDailyServiceType;
        private final TextView textDailyServiceRating;
        private final TextView textInvitationTime;
        private final TextView textDailyServiceNoOfFlats;
        private final TextView textDailyServiceNameValue;
        private final TextView textDailyServiceTypeValue;
        private final TextView textDailyServiceRatingValue;
        private final TextView textInvitationTimeValue;
        private final TextView textDailyServiceNoOfFlatsValue;
        private final TextView textGivenThings;
        private final TextView textDescription;
        private final EditText editDescription;

        private final Button buttonYes;
        private final Button buttonNo;
        private final Button buttonNotifyGate;

        private final de.hdodenhof.circleimageview.CircleImageView profileImage;

        /* ------------------------------------------------------------- *
         * Constructor
         * ------------------------------------------------------------- */

        DailyServiceViewHolder(View itemView) {
            super(itemView);
            textDailyServiceName = itemView.findViewById(R.id.textDailyServiceName);
            textDailyServiceType = itemView.findViewById(R.id.textDailyServiceType);
            textDailyServiceRating = itemView.findViewById(R.id.textRating);
            textInvitationTime = itemView.findViewById(R.id.textInvitationTime);
            textDailyServiceNoOfFlats = itemView.findViewById(R.id.textDailyServiceNoOfFlats);
            textDailyServiceNameValue = itemView.findViewById(R.id.textDailyServiceNameValue);
            textDailyServiceTypeValue = itemView.findViewById(R.id.textDailyServiceTypeValue);
            textDailyServiceRatingValue = itemView.findViewById(R.id.textRatingValue);
            textInvitationTimeValue = itemView.findViewById(R.id.textInvitationTimeValue);
            textDailyServiceNoOfFlatsValue = itemView.findViewById(R.id.textNoOfFlatValue);
            textGivenThings = itemView.findViewById(R.id.textGivenThings);
            textDescription = itemView.findViewById(R.id.textDescription);
            editDescription = itemView.findViewById(R.id.editDescription);
            buttonYes = itemView.findViewById(R.id.buttonYes);
            buttonNo = itemView.findViewById(R.id.buttonNo);
            buttonNotifyGate = itemView.findViewById(R.id.buttonNotifyGate);
            profileImage = itemView.findViewById(R.id.profileImage);

            //Setting Fonts for all the views on cardview
            textDailyServiceName.setTypeface(setLatoRegularFont(mCtx));
            textDailyServiceType.setTypeface(setLatoRegularFont(mCtx));
            textDailyServiceRating.setTypeface(setLatoRegularFont(mCtx));
            textInvitationTime.setTypeface(setLatoRegularFont(mCtx));
            textDailyServiceNoOfFlats.setTypeface(setLatoRegularFont(mCtx));
            textGivenThings.setTypeface(setLatoBoldFont(mCtx));
            textDescription.setTypeface(setLatoBoldFont(mCtx));
            editDescription.setTypeface(setLatoRegularFont(mCtx));
            buttonYes.setTypeface(setLatoRegularFont(mCtx));
            buttonNo.setTypeface(setLatoRegularFont(mCtx));
            buttonNotifyGate.setTypeface(setLatoLightFont(mCtx));
            textDailyServiceNameValue.setTypeface(setLatoBoldFont(mCtx));
            textDailyServiceTypeValue.setTypeface(setLatoBoldFont(mCtx));
            textDailyServiceRatingValue.setTypeface(setLatoBoldFont(mCtx));
            textInvitationTimeValue.setTypeface(setLatoBoldFont(mCtx));
            textDailyServiceNoOfFlatsValue.setTypeface(setLatoBoldFont(mCtx));

            buttonYes.setOnClickListener(this);
            buttonNo.setOnClickListener(this);
            buttonNotifyGate.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getLayoutPosition();
            NammaApartmentDailyService nammaApartmentDailyService = nammaApartmentDailyServiceList.get(position);
            switch (v.getId()) {
                case R.id.buttonYes:
                    textDescription.setVisibility(View.VISIBLE);
                    editDescription.setVisibility(View.VISIBLE);
                    buttonNotifyGate.setVisibility(View.VISIBLE);
                    editDescription.requestFocus();
                    buttonYes.setBackgroundResource(R.drawable.button_guest_selected);
                    buttonNo.setBackgroundResource(R.drawable.button_guest_not_selected);
                    buttonYes.setTextColor(Color.WHITE);
                    buttonNo.setTextColor(Color.BLACK);
                    break;

                case R.id.buttonNo:
                    textDescription.setVisibility(View.GONE);
                    editDescription.setVisibility(View.GONE);
                    buttonNotifyGate.setVisibility(View.GONE);
                    buttonYes.setBackgroundResource(R.drawable.button_guest_not_selected);
                    buttonNo.setBackgroundResource(R.drawable.button_guest_selected);
                    buttonYes.setTextColor(Color.BLACK);
                    buttonNo.setTextColor(Color.WHITE);
                    break;

                case R.id.buttonNotifyGate:
                    String handedThingsDescription = editDescription.getText().toString();
                    nammaApartmentDailyService.setDailyServiceHandedThingsDescription(handedThingsDescription);
                    String dailyServiceType = nammaApartmentDailyService.getDailyServiceType();
                    String dailyServiceTypeValue = dailyServiceType.substring(0, 1).toLowerCase() + dailyServiceType.substring(1);
                    DatabaseReference DailyServicesReference = PUBLIC_DAILYSERVICES_REFERENCE
                            .child(dailyServiceTypeValue)
                            .child(nammaApartmentDailyService.getUID());
                    DailyServicesReference.child(FIREBASE_CHILD_HANDED_THINGS)
                            .child(FIREBASE_CHILD_HANDED_THINGS_DESCRIPTION)
                            .setValue(handedThingsDescription);
                    baseActivity.showSuccessDialog(mCtx.getResources().getString(R.string.handed_things_success_title),
                            mCtx.getResources().getString(R.string.handed_things_success_message),
                            null);
                    break;
            }
        }
    }

}
